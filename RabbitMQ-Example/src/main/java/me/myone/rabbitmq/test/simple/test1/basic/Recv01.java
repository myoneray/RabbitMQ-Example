package me.myone.rabbitmq.test.simple.test1.basic;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

public class Recv01 {
    public static void main(String[] args) throws IOException, ShutdownSignalException, ConsumerCancelledException,
            InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String queueName = "queueName1";
        channel.queueDeclare(queueName, false, false, false, null);// Declare:申报; 宣布; 声明，声称; [法]供述
        // All top the same to sender

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
        // 循环获取消息
        while (true) {
            // 获取消息，如果没有消息，这一步将会一直阻塞
            Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("接收到消息:" + msg);
        }
    }
}
