package me.myone.rabbitmq.test.simple.test2.ack;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Title:Sender01.java <br>
 * Description:设置持久化消息
 *
 * @author Wangming
 * @since 2016年2月29日
 */
public class Sender03 {
    public static void main(String[] args) throws IOException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String queueName = "queueName3";
        /**
         * 注意： 但是这样还是不够的，如果rabbitMQ-Server突然挂掉了，那么还没有被读取的消息还是会丢失 ，<br>
         * 所以我们可以让消息持久化。<br>
         * 只需要在定义Queue时，设置持久化消息就可以了，方法如下
         */
        boolean durable = false;// 设置持久化消息
        /**
         * 这样设置之后，服务器收到消息后就会立刻将消息写入到硬盘，就可以防止突然服务器挂掉，而引起的数据丢失了。<br>
         * 但是服务器如果刚收到消息，还没来得及写入到硬盘，就挂掉了，这样还是无法避免消息的丢失。
         */
        channel.queueDeclare(queueName, durable, false, false, null);
        channel.basicPublish("", queueName, null, "Maizi".getBytes());
        channel.close();
        connection.close();
    }
}
