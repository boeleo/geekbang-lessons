package org.geektimes.interceptor.jdkproxy;

import static org.geektimes.interceptor.AnnotatedInterceptor.loadInterceptors;

import org.junit.Test;

public class JdkProxyInterceptorEnhancerTest {
    
    private JdkProxyInterceptorEnhancer enhancer = new JdkProxyInterceptorEnhancer();
    
	@Test
    public void testEcho() {
		EchoService echoService = new EchoServiceImp();
		echoService = (EchoService) enhancer.enhance(echoService, EchoService.class, loadInterceptors());
		echoService.echo("Hello, Echo");
    }
}
