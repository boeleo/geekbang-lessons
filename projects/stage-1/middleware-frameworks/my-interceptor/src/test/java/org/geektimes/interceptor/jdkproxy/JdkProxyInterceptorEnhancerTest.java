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
        // loadInterceptors() 高优先值的拦截优先处理。
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
        // 由之后的打印结果可以发现, 此处 @Retry 先于 @Fallback 处理了 Exception, 
        // 导致 Fallback 没有抓到Exception而被处理, 因此 fallback(Long value) 没有执行
        System.err.println(echoService.echo(Long.valueOf(1L)));
    }
    
    @Test
    public void testFallbackEcho() throws Throwable {
        // 测试 Fallback 拦截
        assertEquals(echoService.echo(), "fallbackTask");
    }
}
