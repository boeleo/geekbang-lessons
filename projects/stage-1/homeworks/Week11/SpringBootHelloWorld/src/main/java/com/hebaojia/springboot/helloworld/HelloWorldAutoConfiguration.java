package com.hebaojia.springboot.helloworld;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnNotWebApplication /* 此句设置装配条件为非 Web 应用 */
public class HelloWorldAutoConfiguration {
    @Bean
    public ApplicationRunner applicationRunner() {
        // 返回一个输出 HelloWorld 的 ApplicationRunner 对象
        return new HelloWorldRunner();
    }
}
