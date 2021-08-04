package org.geektimes.interceptor.jdkproxy;

import static org.geektimes.interceptor.AnnotatedInterceptor.loadInterceptors;
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
}
