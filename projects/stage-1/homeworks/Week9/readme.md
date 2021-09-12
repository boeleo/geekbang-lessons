# 作业

实现分布式事件，基于 Zookeeper 或者 JMS 来实现

## 作答

此处使用 ActiveMQ， 基于 JMS，实现对于 topic 的消息生产和消费。

1）在工程中添加ActiveMQ的依赖

```text
<dependency>
    <groupId>org.apache.activemq</groupId>
    <artifactId>activemq-all</artifactId>
</dependency>
```

2）实现topic的消息生产和消费

- 生产者

```java
package com.hebaojia;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicProducer {
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER; // 默认用户名
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD; // 默认密码
    private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL; // 默认连接地址
    private static final String name = "queue_message";

    public static void main(String[] args) throws JMSException {
        // 1.创建连接工场
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEURL);
        // 2.创建连接
        Connection connection = connectionFactory.createConnection();
        // 3.启动连接
        connection.start();
        // 4.创建会话
        Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
        // 5.创建一个目标
        Destination destination = session.createTopic(name);
        // 6.创建生产者
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        for (int i = 0; i < 100; i++) {
            // 7.创建消息
            TextMessage textMessage = session.createTextMessage("activeMQ" + i);
            producer.send(textMessage);
        }
        session.commit();
        System.out.print("所有消息已经全部发送完了");
        connection.close();
    }
}
```

- 消费者

```java
package com.hebaojia;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicConsumer {
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER; // 默认用户名
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD; // 默认密码
    private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL; // 默认连接地址
    private static final String name = "queue_message";

    public static void main(String[] args) throws JMSException {
        // 1.创建连接工场
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEURL);
        // 2.创建连接
        Connection connection = connectionFactory.createConnection();
        // 3.启动连接
        connection.start();
        // 4.创建会话
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 5.创建一个目标
        Destination destination = session.createTopic(name);
        // 6.创建消费者
        MessageConsumer consumer = session.createConsumer(destination);
        // 7.创建一个监听器
        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("接收消息:" + textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        connection.close();
    }
}
```

- 比较

  . Quenue模式：在点对点的传输方式中，消息数据被持久化，每条消息都能被消费，没有监听QUEUE地址也能被消费，数据不会丢失，一对一的发布接受策略，保证数据完整。
  . Topic模式：在发布订阅消息方式中，消息是无状态的，不保证每条消息被消费，只有监听该TOPIC地址才能收到消息并消费，否则该消息将会丢失。一对多的发布接受策略，可以同时消费多个消息。
