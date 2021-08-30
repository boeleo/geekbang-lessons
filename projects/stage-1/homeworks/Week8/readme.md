# 作业

利用 Reactor Mono API
配合 Reactive Streams Publisher 实现，让 Subscriber 实现能够获取到数据，可以参考以下代码：

```java
SimplePublisher();
Mono.from(publisher)
    .subscribe(new BusinessSubscriber(5));
for (int i = 0; i < 5; i++) {
    publisher.publish(i);
}
```

## 作答

Mono 是一个发出(emit) 0-1 个元素的 `Publisher<T>`，可以被onComplete信号或者onError信号所终止。

```java
public class LearnMonoDemo {
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void main(String[] args) {
        /**
         * SimplePublisher 基于 org.reactivestreams.Publisher<T> 实现，其中实现了
         * org.reactivestreams.Publisher.subscribe(Subscriber<? super T>) 方法，
         * 和 publish(T data) 方法，当收到 Subscription 请求时，向 Subscriber 发送
         * 数据通知。
         */
        SimplePublisher publisher = new SimplePublisher();
        Mono.from(publisher)
            .subscribe(new BusinessSubscriber(5));

        for (int i = 0; i < 5; i++) {
            publisher.publish(i);
        }

        Supplier<String> supplier = () -> "Hello,World";
        Mono.fromSupplier(supplier)
            .subscribe(data -> {
                System.out.printf("[Thread : %s] %s\n",
                    Thread.currentThread().getName(), data);
            });
    }
}
```
