package me.myone.rabbitmq.test.simple.test5.topic;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * Title:Recv06_01.java <br>
 * Description:
 *
 * @author Wangming
 * @since 2016年3月1日
 */
public class Recv06_01 {
    public static void main(String[] args) throws IOException, ShutdownSignalException, ConsumerCancelledException,
            InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "exchangeName006";
        // topic 模糊匹配消息类型。
        channel.exchangeDeclare(exchangeName, "topic");
        String queueName = channel.queueDeclare().getQueue();
        // 模糊匹配中的 *代表一个 #代表零个或1个
        channel.queueBind(queueName, exchangeName, "#.type01");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        while (true) {
            Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println(msg);

        }
    }
}
