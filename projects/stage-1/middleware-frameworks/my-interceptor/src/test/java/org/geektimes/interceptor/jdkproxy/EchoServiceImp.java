package org.geektimes.interceptor.jdkproxy;

public class EchoServiceImp implements EchoService {

	@Override
	public String echo(String message) {
		return "[ECHO] : " + message;
	}
}
