# 作业

利用 Spring Boot 自动装配特性，编写一个自定义 Starter，
规则如下：

* 利用 @EnableAutoConfiguration 加载一个自定义 Configration 类
* Configuration 类装配条件需要它非 Web 应用
  * WebApplicationType = NONE
* Configuration 类中存在一个 @Bean 返回一个输出 HelloWorld ApplicationRunner 对象

## 作答

实现工程如 `Week11/SpringBootHelloWorld` 所示。其中：

1. `SpringBootHelloWorldApplication` 为程序入口，`@SpringBootApplication` 已包含 `@EnableAutoConfiguration`， 因此会自动加载工程中标注 `@Configuration` 的配置类。其中使用下面的代码来启动一个非 Web 的应用。

    ```java
    new SpringApplicationBuilder(SpringBootHelloWorldApplication.class)
        // 下面一句配置配置 Spring Boot 应用以非 web 模式启动
        // 若不加此句，HelloWorldAutoConfiguration 将不被加载
        .web(WebApplicationType.NONE)
        .run(args);
    ```

2. `HelloWorldAutoConfiguration` 为上述自定义的 Configration 类，其中利用 `@ConditionalOnNotWebApplication` 来标注此配置类只在非 Web 应用上装配。

3. 实现了一个 `HelloWorldRunner` 来提供 `HelloWorldAutoConfiguration` 返回输出 HelloWorld 的 ApplicationRunner 对象。

4. 最终运行 `SpringBootHelloWorldApplication` 之后可以看到下面的 console log。

    ```text

    .   ____          _            __ _ _
    /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
    ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
    \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
    '  |____| .__|_| |_|_| |_\__, | / / / /
    =========|_|==============|___/=/_/_/_/
    :: Spring Boot ::                (v2.5.4)

    2021-09-19 19:16:27.847  INFO 91762 --- [           main] c.h.s.h.SpringBootHelloWorldApplication  : Starting SpringBootHelloWorldApplication using Java 15.0.2 on hebaojiadeMacBook-Pro.local with PID 91762 (/Users/beatrice/eclipse-workspace/20210811/SpringBootHelloWorld/target/classes started by beatrice in /Users/beatrice/eclipse-workspace/20210811/SpringBootHelloWorld)
    2021-09-19 19:16:27.849  INFO 91762 --- [           main] c.h.s.h.SpringBootHelloWorldApplication  : No active profile set, falling back to default profiles: default
    2021-09-19 19:16:28.218  INFO 91762 --- [           main] c.h.s.h.SpringBootHelloWorldApplication  : Started SpringBootHelloWorldApplication in 0.548 seconds (JVM running for 1.088)
    Hello, World!
    ```
