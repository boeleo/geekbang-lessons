package org.geektimes.interceptor.jdkproxy;

import org.geektimes.interceptor.Logging;

public interface EchoService {

	@Logging
    public String echo(String message);
}
