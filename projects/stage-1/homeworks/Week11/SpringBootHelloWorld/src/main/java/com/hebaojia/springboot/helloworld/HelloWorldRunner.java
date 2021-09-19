package com.hebaojia.springboot.helloworld;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * An Application runner to print "Hello World"
 *
 */
public class HelloWorldRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Hello, World!");
    }

}
