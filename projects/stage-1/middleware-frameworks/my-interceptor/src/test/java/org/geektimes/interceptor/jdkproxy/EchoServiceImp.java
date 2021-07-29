package org.geektimes.interceptor.jdkproxy;

import static java.lang.String.format;

import java.util.concurrent.Future;
import java.util.logging.Logger;

public class EchoServiceImp implements EchoService {
	
	
	private final Logger logger = Logger.getLogger(getClass().getName());

	@Override
	public void echo(String message) {
		echo((Object) message);
	}

	@Override
	public Future<Void> echo(Object message) {
		logger.info(format("[%s] - echo : %s", Thread.currentThread().getName(), message));
		return null;
	}

	@Override
	public String echo(Long value) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String fallback(Long value) {
		return String.valueOf(value);
	}

}
