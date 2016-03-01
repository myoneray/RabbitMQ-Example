package me.myone.rabbitmq.test.simple.test5.topic;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Title:Sender06.java <br>
 * Description:
 *
 * @author Wangming
 * @since 2016年3月1日
 */
public class Sender06 {
    public static void main(String[] args) throws IOException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String exchange = "exchangeName006";
        channel.exchangeDeclare(exchange, "topic");
        // 以".type01"結尾
        String msgtype = "123123.type01";
        channel.basicPublish(exchange, msgtype, null, "maizi".getBytes());
        System.out.println("send message success!");
        channel.close();
        connection.close();
    }
}
