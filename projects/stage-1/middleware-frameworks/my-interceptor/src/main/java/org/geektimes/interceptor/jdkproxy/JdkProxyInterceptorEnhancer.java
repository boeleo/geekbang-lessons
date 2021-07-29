package org.geektimes.interceptor.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import javax.interceptor.InvocationContext;

import org.geektimes.interceptor.ChainableInvocationContext;
import org.geektimes.interceptor.ReflectiveMethodInvocationContext;

public class JdkProxyInterceptorEnhancer {

    @SuppressWarnings("unchecked")
	public <T> T enhance(T source, Class<? super T> type, Object... interceptors) {

        Object proxy = null;
        
        // classLoader, 目标类的类加载器
        ClassLoader classLoader = getClassLoader(type);
        
        proxy = Proxy.newProxyInstance(classLoader, new Class[]{type}, new InvocationHandler() {

			/**
             *  Object proxy: 代理对象
             *  Method method: 当前正在被代理的方法对象
             *  Object[] args: 方法的参数
             */
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				InvocationContext delegateContext = new ReflectiveMethodInvocationContext(source, method, proxy, args);
		        ChainableInvocationContext context = new ChainableInvocationContext(delegateContext, interceptors);
		        return context.proceed();
			}
        	
        });
        
		return (T) proxy;
    }

    private ClassLoader getClassLoader(Class<?> clazz) {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = clazz.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }

        return cl;
    }
 
}
