package org.geektimes.interceptor.jdkproxy;

import static org.geektimes.interceptor.AnnotatedInterceptor.loadInterceptors;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;

public class JdkProxyInterceptorEnhancerTest {
    
    private JdkProxyInterceptorEnhancer enhancer = new JdkProxyInterceptorEnhancer();
    private EchoService echoService = new EchoServiceImp();
    
    @Before
    public void init() {
    	// 获取 JDK Proxy,
    	// loadInterceptors() 高优先值的拦截优先级别高
		echoService = enhancer.enhance(echoService, loadInterceptors());
    }
    
	@Test
    public void testEcho() {
		echoService.echo("Hello, Echo");
    }
	
	@Test
    public void testAsynchronousEcho() throws Throwable {
		// 测试 Asynchronous 拦截
		Future<?> future = (Future<?>) echoService.echo((Object) "Hello, Asynchronous");
        future.get();
    }
	
	@Test
	public void testRetryEcho() {
		// 此处由于 @Retry 的优先级别比 @Fallback 高，
		// 执行的是 @Retry
		echoService.echo(Long.valueOf(1L));
	}
	
	@Test
	public void testFallbackEcho() throws Throwable {
		// 测试 Fallback 拦截
		assertEquals(echoService.echo(), "fallbackTask");
	}
}
