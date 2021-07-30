package org.geektimes.interceptor.jdkproxy;

import static java.lang.String.format;

import java.util.concurrent.Future;

public class EchoServiceImp implements EchoService {

	@Override
	public void echo(String message) {
		echo((Object) message);
	}

	@Override
	public Future<Void> echo(Object message) {
		System.out.println(format("[%s] - echo : %s", Thread.currentThread().getName(), message));
		return null;
	}

	@Override
	public String echo(Long value) throws UnsupportedOperationException {
		System.out.println(format("[%s] - echo : %s", Thread.currentThread().getName(), value));
		throw new UnsupportedOperationException();
	}

	public String fallback(Long value) {
		// fallback 函数的定义应该与被拦截函数一致
		System.out.println(format("[%s] - echo : fallback(Long value)", Thread.currentThread().getName()));
		return String.valueOf(value);
	}

	@Override
	public String echo() {
		throw new UnsupportedOperationException();
	}

	public String fallback() {
		// fallback 函数的定义应该与被拦截函数一致
		System.out.println(format("[%s] - echo : fallback()", Thread.currentThread().getName()));
		return "fallbackTask";
	}

}
