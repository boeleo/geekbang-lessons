package com.hebaojia.dubbofilter;

import java.time.Duration;
import java.util.function.Supplier;

import org.apache.dubbo.common.extension.Activate;

import com.alibaba.dubbo.rpc.*;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.vavr.control.Try;

import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;

@SuppressWarnings("deprecation")
@Activate(group = {PROVIDER})
public class Resilience4jBulkheadDubboFilter implements Filter {

	private BulkheadConfig bulkheadConfig;
	private BulkheadRegistry bulkheadRegistry;
	
	public Resilience4jBulkheadDubboFilter(
			int maxConcurrentCalls,
			int maxWaitDurationOfMillis) {

		// Create a custom configuration for a Bulkhead
		this.bulkheadConfig = BulkheadConfig.custom()
		        .maxConcurrentCalls(maxConcurrentCalls)
		        .maxWaitDuration(Duration.ofMillis(maxWaitDurationOfMillis))
		        .build();

		// Create a BulkheadRegistry with a custom global configuration
		this.bulkheadRegistry = BulkheadRegistry.of(bulkheadConfig);
	}

	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

		Bulkhead bulkhead = bulkheadRegistry
				  .bulkhead(invocation.getServiceName() + ": " + invocation.getMethodName());

		Supplier<Result> decoratedSupplier = Bulkhead
			    .decorateSupplier(bulkhead, () -> invoker.invoke(invocation));

		Result result = Try.ofSupplier(decoratedSupplier).get();

		return result;
	}

}
