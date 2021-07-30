package org.geektimes.interceptor.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import javax.interceptor.InvocationContext;

import org.geektimes.interceptor.ChainableInvocationContext;
import org.geektimes.interceptor.ReflectiveMethodInvocationContext;

public class JdkProxyInterceptorEnhancer {

	@SuppressWarnings("unchecked")
	public <T> T enhance(T source, Object... interceptors) {

		// classLoader, 目标类的类加载器
		ClassLoader classLoader = source.getClass().getClassLoader();

		// interfaces, 目标类的接口
		Class<?>[] interfaces = source.getClass().getInterfaces();

		return (T) Proxy.newProxyInstance(classLoader, interfaces, new InvocationHandler() {

			/**
			 * Object proxy: 代理对象
			 * Method method: 当前正在被代理的方法对象
			 * Object[] args: 方法的参数
			 */
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				InvocationContext delegateContext = new ReflectiveMethodInvocationContext(source, method, args);
				ChainableInvocationContext context = new ChainableInvocationContext(delegateContext, interceptors);
				return context.proceed();
			}
		});
	}
}
