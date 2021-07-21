### 作业：

- 通过 `MicroProfile REST Client` 实现 `POST` 接口去请求项目中的 `ShutdownEndpoint`，URI： http://127.0.0.1:8080/actuator/shutdown
- 可选：完善 `my-rest-client` 框架 `POST` 方法，实现 `org.geektimes.rest.client.DefaultInvocationBuilder#buildPost` 方法

### 作答：
(这道题做到一半发现如果测试想跑通，"可选"部分也需要实现， :) 以下题解包含选做部分)  
题目中提到的URI`http://127.0.0.1:8080/actuator/shutdown` 可以通过 "Spring Boot Actuator" 来实现：  
1) 在工程中添加"Spring Boot Actuator"的依赖  
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
由于本机8080端口被占用，在此使用9001端口。  
默认情况下像"shutdown"这样比较敏感的API是disabled并且不可见的状态，需要在`application.properties`中enable以及暴露出来:  
```
management.server.port: 9001
management.server.address: 127.0.0.1
management.endpoints.enabled-by-default=false
management.endpoint.shutdown.enabled=true
management.endpoints.web.exposure.include=shutdown
```
这样配置之后启动Spring Boot Application，在浏览器中访问 http://localhost:9001/actuator/ 就可以看到 "http://localhost:9001/actuator/shutdown"  
(工程如 `geekbang-lessons\projects\stage-1\homeworks\Week3\actuator-service`)

2) (必做部分) 参考 `org.geektimes.microprofile.rest.DefaultRestClientBuilderTest` 专门为 `ShutdownEndpoint` 实现一个`DefaultRestClientBuilderPostTest` 去请求上一步开放的 shutdown 接口  
```
/**
 * 
 * Test {@link DefaultRestClientBuilder} with POST
 *
 */
public class DefaultRestClientBuilderPostTest {
    public static void main(String[] args) throws MalformedURLException {
        ShutDownService shutdownService = RestClientBuilder.newBuilder()
                .baseUrl(new URL("http://localhost:9001"))
                .build(ShutDownService.class);

        System.out.println(shutdownService.shutdown());
    }
}

/**
 * ShutDown Service
 *
 */
interface ShutDownService {

    @POST
    @Path("/actuator/shutdown")
    @Timeout(500)
    String shutdown();
}
```
P.S.：此时，`DefaultRestClientBuilderPostTest` 会报Exception，因为 `org.geektimes.rest.client.DefaultInvocationBuilder` 里的 `buildPost` 返回的是 null 。。。

3) (可选部分) 实现 `org.geektimes.rest.client.DefaultInvocationBuilder#buildPost` 方法，此处偷懒，复制了`org.geektimes.rest.client.HttpGetInvocation` 的实现，然后把`GET` 改成 `POST` 来实现 `org.geektimes.rest.client.HttpPostInvocation` :P  
```
/**
 * HTTP POST Method {@link Invocation}
 */
class HttpPostInvocation implements Invocation {

    private final URL url;

    private final MultivaluedMap<String, Object> headers;

    HttpPostInvocation(URI uri, MultivaluedMap<String, Object> headers) {
        this.headers = headers;
        try {
            this.url = uri.toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Response invoke() {
        HttpURLConnection connection = null;
        try {
            // 建立连接并发送POST请求
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HttpMethod.POST);
            setRequestHeaders(connection);
            int statusCode = connection.getResponseCode();
            // 返回回复信息
            DefaultResponse response = new DefaultResponse();
            response.setConnection(connection);
            response.setStatus(statusCode);
            return response;
        } catch (IOException e) {
        }
        return null;
    }

    private void setRequestHeaders(HttpURLConnection connection) {
        for (Map.Entry<String, List<Object>> entry : headers.entrySet()) {
            String headerName = entry.getKey();
            for (Object headerValue : entry.getValue()) {
                connection.setRequestProperty(headerName, headerValue.toString());
            }
        }
    }

    // ....
```
在`org.geektimes.rest.client.DefaultInvocationBuilder#buildPost` 中使用 `HttpPostInvocation` 来处理 POST 请求   
```
@Override
    public Invocation buildPost(Entity<?> entity) {
        return new HttpPostInvocation(uriBuilder.build(), headers);
    }
```

4) 右键 `org.geektimes.microprofile.rest.DefaultRestClientBuilderPostTest`，"Run As" -> "Java Application"， 可以看到 shutdown 回复的信息如下   
```
{"message":"Shutting down, bye..."}
```