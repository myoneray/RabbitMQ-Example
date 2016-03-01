package me.myone.rabbitmq.test.simple.test4.route;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Title:Sender05.java <br>
 * Description: 发送消息
 *
 * @author Wangming
 * @since 2016年2月29日
 */
public class Sender05 {
    public static void main(String[] args) throws IOException {

        ConnectionFactory connFac = new ConnectionFactory();
        connFac.setHost("127.0.0.1");
        Connection conn = connFac.newConnection();
        Channel channel = conn.createChannel();
        String exchangeName = "exchange02";
        // Exchange的类型有direct, topic, headers 、 fanout四种
        channel.exchangeDeclare(exchangeName, "direct");
        /**
         * 生产者会生产出很多消息 ， 但是不同的消费者可能会有不同的需求，只需要接收指定的消息， <br>
         * 其他的消息需要被过滤掉。 这时候就可以对消息进行过滤了。 在消费者端设置好需要接收的消息类型。 <br>
         * 
         * 如果不使用默认的Exchange发送消息，而是使用我们自定定义的Exchange发送消息， <br>
         * 那么下面这个方法的第二个参数就不是QueueName了，而是消息的类型。 <br>
         * 
         * channel.basicPublish( exchangeName , messageType , null ,
         * msg.getBytes());
         */
        // 发送消息
        String messageType = "type02";
        channel.basicPublish(exchangeName, messageType, null, "Maizi".getBytes());
        System.out.println("send success!");
        channel.close();
        conn.close();

    }
}
