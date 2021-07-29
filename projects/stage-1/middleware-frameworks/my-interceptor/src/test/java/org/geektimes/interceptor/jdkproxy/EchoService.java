package org.geektimes.interceptor.jdkproxy;

import java.util.concurrent.Future;

import org.eclipse.microprofile.faulttolerance.*;

@Bulkhead(value = 1)
public interface EchoService {

	@Timeout
	public void echo(String message);

	@Asynchronous
    public Future<Void> echo(Object message);
    
	@Retry(maxRetries = 3,
            delay = 0, maxDuration = 0, jitter = 0,
            retryOn = UnsupportedOperationException.class)
    @Fallback(fallbackMethod = "fallback")
    public String echo(Long value) throws UnsupportedOperationException;

    public String fallback(Long value);
}
