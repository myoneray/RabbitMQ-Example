package me.myone.rabbitmq.test.simple.test2.ack;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * Title:Recv03.java <br>
 * Description:消息确认&公平调度
 *
 * @author Wangming
 * @since 2016年2月29日
 */
public class Recv03 {
    /**
     * 消息确认
     * <p>
     * 为了确保消息一定被消费者处理，rabbitMQ提供了消息确认功能，<br>
     * 就是在消费者处理完任务之后，就给服务器一个回馈，服务器就会将该消息删除，<br>
     * 如果消费者超时不回馈，那么服务器将就将该消息重新发送给其他消费者
     * <p>
     * 默认是开启的，在消费者端通过下面的方式开启消息确认,<br>
     * 首先将autoAck自动确认关闭，等我们的任务执行完成之后，手动的去确认，<br>
     * 类似JDBC的autocommit一样
     * <p>
     * QueueingConsumer consumer = new QueueingConsumer(channel); <br>
     * boolean autoAck = false; <br>
     * channel.basicConsume("hello", autoAck, consumer);<br>
     * 在前面的例子中使用的是channel.basicConsume(channelName, true, consumer) ;
     * 在接收到消息后，就会自动反馈一个消息给服务器。
     */
    static String queueName = "queueName3";

    public static void main(String[] args) throws IOException, ShutdownSignalException, ConsumerCancelledException,
            InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(queueName, false, false, false, null);
        QueueingConsumer consumer = new QueueingConsumer(channel);
        /**
         * 注意：一旦将autoAck关闭之后，一定要记得处理完消息之后，向服务器确认消息。<br>
         * 否则服务器将会一直转发该消息<br>
         * 如果将 <br>
         * channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);注释掉， <br>
         * Sender03.java只需要运行一次 ， <br>
         * Recv03.java每次运行将都会收到HelloWorld消息
         */
        boolean autoAck = false; // 取消 autoAck 后channel.basicAck需要確認
        channel.basicConsume(queueName, autoAck, consumer);
        /**
         * 公平调度 :上一个例子能够实现发送一个Message与接收一个Message<br>
         * 从上一个Recv01中可以看出，必须处理完一个消息，才会去接收下一个消息。<br>
         * 如果生产者众多，那么一个消费者肯定是忙不过来的。
         * 此时就可以用多个消费者来对同一个Channel的消息进行处理，并且要公平的分配任务给多个消费者。<br>
         * 不能部分很忙 部分总是空闲, 实现公平调度的方式就是让每个消费者在同一时刻会分配一个任务。
         */
        channel.basicQos(1);// 实现公平调度的方式就是让每个消费者在同一时刻会分配一个任务。
        while (true) {
            Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);// 确认消息，已经收到
            System.out.println("received message[" + msg + "] from " + queueName);
        }
    }
}
