package me.myone.rabbitmq.test.simple.test3.exchange;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Title:Sender04.java <br>
 * Description:发送消息
 *
 * @author Wangming
 * @since 2016年2月29日
 */
public class Sender04 {
    /**
     * 
     * 前面都是一条消息只会被一个消费者处理。<br>
     * 
     * 如果要每个消费者都处理同一个消息，rabbitMq也提供了相应的方法。<br>
     * 
     * 在以前的程序中，不管是生产者端还是消费者端都<br>
     * 必须知道一个指定的QueueName才能发送、获取消息。 <br>
     * 而rabbitMQ消息模型的核心思想是生产者不会将消息直接发送给队列。
     * 
     * 因为，生产者通常不会知道消息将会被哪些消费者接收。<br>
     * 
     * 生产者的消息虽然不是直接发送给Queue，但是消息会交给Exchange，<br>
     * 所以需要定义Exchange的消息分发模式 ，之前的程序中，<br>
     * 
     * 有如下一行代码： channel.basicPublish("", queueName , null , msg.getBytes());
     * 第一个参数为空字符串，其实第一个参数就是ExchangeName，<br>
     * 这里用空字符串，就表示消息会交给默认的Exchange。
     */
    public static void main(String[] args) throws IOException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        /**
         * 下面我们将自己定义Exchange的属性。 <br>
         * 定义ExchangeName,<br>
         * 第二个参数是Exchange的类型， fanout表示消息将会分列发送给多账户
         */
        String exchangeName = "exchangeName";
        channel.exchangeDeclare(exchangeName, "fanout");
        /**
         * 发送消息，这里与前面的不同，<br>
         * 第一个参数不再是字符串， 而是ExchangeName ，<br>
         * 第二个参数也不再是queueName，而是空字符串
         */
        channel.basicPublish(exchangeName, "", null, ">>>maiziExchange".getBytes());
        channel.close();
        connection.close();
    }
}
