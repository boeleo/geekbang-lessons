package com.hebaojia.springboot.helloworld;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication /* 此注解中已包含 @EnableAutoConfiguration */
public class SpringBootHelloWorldApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringBootHelloWorldApplication.class)
            // 下面一句配置配置 Spring Boot 应用以非 web 模式启动
            // 若不加此句，HelloWorldAutoConfiguration 将不被加载
            .web(WebApplicationType.NONE)
            .run(args);
    }

}
