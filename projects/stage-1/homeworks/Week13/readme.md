# 作业

将项目的中 Zipkin 客户端（ServiceClientApplication 和 ServiceProviderApplication）与 Zipkin 服务端整合起来。

可选：

1. 写出 Zipkin 客户端和服务端之间数据传输的过程以及细节
2. 了解 Zipkin 如何实现分布式数据采集和存储

## 作答

1. 使用下面的 docker 命令启动 Zipkin 服务器

    ```shell
    docker run -d -p 9411:9411 openzipkin/zipkin
    ```

    当 log 中出现 `Serving HTTP at /0.0.0.0:9411 - http://127.0.0.1:9411/` 则服务器启动成功。

2. 在项目 sc-sm-shop 中添加 Sleuth Zipkin 的依赖

    ```xml
    <!-- Spring Cloud Sleuth zipkin -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-sleuth-zipkin</artifactId>
    </dependency>
    ```

3. 将下面的配置添加到 `service-provider.yaml` 中

    ```yaml
    spring:
    zipkin:
        base-url: http://localhost:9411/
        sleuth:
        sampler:
            percentage: 1.0 # sleuth采样率，默认为0.1，1表示采集服务的全部追踪数据。值越大收集越及时，但性能影响也越大
    ```

    启动 ServiceClientApplication 和 ServiceProviderApplication，此时分别访问 `http://localhost:8080/` 和 `http://localhost:9090/`，可以看到 Zipkin GUI `http://localhost:9411` 只跟踪到 8080 端口的 `service-provider get /` 访问。

4. 将上述配置也添加到 `service-client.yaml` 中之后，再次访问 9090 端口，就可以看到 Zipkin GUI 抓到了 `service-client get /` 的访问跟踪：

    ```text
    SERVICE-CLIENT  get / [150.811ms]
        |- SERVICE-PROVIDER get /hello/world [8.864ms]
                |- SERVICE-PROVIDER echo [145µs]
    ```
