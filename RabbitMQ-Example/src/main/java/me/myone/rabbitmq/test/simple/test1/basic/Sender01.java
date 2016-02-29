package me.myone.rabbitmq.test.simple.test1.basic;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Title:Sender01.java <br>
 * Description:
 *
 * @author Wangming
 * @since 2016年2月29日
 */
public class Sender01 {
    public static void main(String[] args) throws IOException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String queueName = "queueName1";
        channel.queueDeclare(queueName, false, false, false, null);
        channel.basicPublish("", queueName, null, "Maizi1".getBytes());
        channel.close();
        connection.close();
    }
}
