### 作业：

将 `my-interceptor` 工程代码增加 JDK 动态代理，将 `@BulkHead` 等注解标注在接口上，实现方法拦截。  
步骤：
 - 通过 JDK 动态代理实现类似 InterceptorEnhancer 的代码
 - 实现 JDK 动态代理方法的 InvocationContext


### 作答：
（之前没明白题目意思，胡乱写了一遍作业，测试一直跑不过。后来听马老师周四的讲解之后，把作业重新捋了一下，于是repository里的commits颇乱，请见谅 :D ）  
作业中要求添加JDK动态代理，实现接口的方法拦截。根据要求，在 `my-interceptor` 工程的基础上添加了 `org.geektimes.interceptor.jdkproxy` 包，用于存放 JDK 动态代理的实现。   
1）实现 JDK 动态代理方法的 InvocationContext。偷懒一下，题目中第二部提到的 JDK 动态代理方法的 InvocationContext 可以借用马老师已经实现好的 `ChainableInvocationContext` 和 `ReflectiveMethodInvocationContext`。   
2）通过 JDK 动态代理实现类似 InterceptorEnhancer 的代码。代码位于 `org.geektimes.interceptor.jdkproxy.JdkProxyInterceptorEnhancer`，其中在 `InvocationHandler` 中 invoke 的实现里使用了 `ChainableInvocationContext` 和 `ReflectiveMethodInvocationContext` 如下：  
```
public class JdkProxyInterceptorEnhancer {

  @SuppressWarnings("unchecked")
  public <T> T enhance(T source, Object... interceptors) {

    // classLoader, 目标类的类加载器
    ClassLoader classLoader = source.getClass().getClassLoader();

    // interfaces, 目标类的接口
    Class<?>[] interfaces = source.getClass().getInterfaces();

    return (T) Proxy.newProxyInstance(classLoader, interfaces, new InvocationHandler() {

      /**
       * Object proxy: 代理对象
       * Method method: 当前正在被代理的方法对象
       * Object[] args: 方法的参数
       */
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        InvocationContext delegateContext = new ReflectiveMethodInvocationContext(source, method, args);
        ChainableInvocationContext context = new ChainableInvocationContext(delegateContext, interceptors);
        return context.proceed();
      }
    });
  }
}
```

这样，就可以利用工程中定义好的 `AsynchronousInterceptor`, `TimeoutInterceptor`, `RetryInterceptor`, `FallbackInterceptor` 等对标记有 `@Asynchronous`, `@Timeout`, `@Retry`, `@Fallback` 等的接口方法进行拦截和增强。   

3）实现单元测试。（测试包位于 `src\test\java\org\geektimes\interceptor\jdkproxy` 中 ）  
- 借用马老师测试包中 `org.geektimes.interceptor.microprofile.faulttolerance.EchoService` 的定义，抽象出接口 `org.geektimes.interceptor.jdkproxy.EchoService` 如下:   
```
@Bulkhead(value = 1)
public interface EchoService {

  @Timeout
  public void echo(String message);

  @Asynchronous
  public Future<Void> echo(Object message);
    
  @Retry(maxRetries = 3,
          delay = 0, maxDuration = 0, jitter = 0,
          retryOn = UnsupportedOperationException.class)
  @Fallback(fallbackMethod = "fallback")
  public String echo(Long value) throws UnsupportedOperationException;
  
  @Fallback(fallbackMethod = "fallback")
  public String echo() throws Exception;
}
```

- 实现接口类 `org.geektimes.interceptor.jdkproxy.EchoServiceImp` 如下:  
```
public class EchoServiceImp implements EchoService {

  @Override
  public void echo(String message) {
    echo((Object) message);
  }

  @Override
  public Future<Void> echo(Object message) {
    System.out.println(format("[%s] - echo : %s", Thread.currentThread().getName(), message));
    return null;
  }

  @Override
  public String echo(Long value) throws UnsupportedOperationException {
    System.out.println(format("[%s] - echo : %s", Thread.currentThread().getName(), value));
    throw new UnsupportedOperationException();
  }

  public String fallback() {
    // fallback 函数的定义应该与被拦截函数一致
    System.out.println(format("[%s] - echo : fallbackTask", Thread.currentThread().getName()));
    return "fallbackTask";
  }

  @Override
  public String echo() throws Exception {
    throw new UnsupportedOperationException();
  }

}
```

- 实现测试类 `org.geektimes.interceptor.jdkproxy.JdkProxyInterceptorEnhancerTest` 如下：  
```
public class JdkProxyInterceptorEnhancerTest {
    
    private JdkProxyInterceptorEnhancer enhancer = new JdkProxyInterceptorEnhancer();
    private EchoService echoService = new EchoServiceImp();
    
    @Before
    public void init() {
        // 获取 JDK Proxy,
        // loadInterceptors() 高优先值的拦截优先级别高
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
        // 此处由于 @Retry 的优先级别比 @Fallback 高，
        // 执行的是 @Retry
        echoService.echo(Long.valueOf(1L));
    }
    
    @Test
    public void testFallbackEcho() throws Throwable {
        // 测试 Fallback 拦截
        assertEquals(echoService.echo(), "fallbackTask");
    }
}
```

- 运行单元测试之后，打印结果如下：   
```
[ForkJoinPool.commonPool-worker-3] - echo : Hello, Echo
[ForkJoinPool.commonPool-worker-3] - echo : Hello, Asynchronous
[main] - echo : fallbackTask
[main] - echo : 1
[main] - echo : 1
[main] - echo : 1
[main] - echo : 1
```