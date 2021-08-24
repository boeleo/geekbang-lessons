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

(TBA)
