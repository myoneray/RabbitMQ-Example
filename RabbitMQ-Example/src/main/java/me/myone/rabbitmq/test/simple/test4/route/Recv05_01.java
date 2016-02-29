package me.myone.rabbitmq.test.simple.test4.route;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * Title:Recv05_01.java <br>
 * Description:启动Recv05_01.java 然后启动Sender05.java ，消费者端就会收到消息。
 * 然后将Sender05.java 中的messageType分别改为type02 type03<br>
 * 然后发送消息 ， 可以看到消费者端能接收到type02的消息，但是不能接收到type03的消息。
 *
 * @author Wangming
 * @since 2016年2月29日
 */
public class Recv05_01 {

    public static void main(String[] args) throws IOException, ShutdownSignalException, ConsumerCancelledException,
            InterruptedException {
        ConnectionFactory connFac = new ConnectionFactory();
        connFac.setHost("127.0.0.1");
        Connection conn = connFac.newConnection();
        Channel channel = conn.createChannel();

        String exchangeName = "exchange02";
        channel.exchangeDeclare(exchangeName, "direct");
        String queueName = channel.queueDeclare().getQueue();
        // 第三个参数就是type，这里表示只接收type01类型的消息。
        channel.queueBind(queueName, exchangeName, "type01");

        // 也可以选择接收多种类型的消息。只需要再下面再绑定一次就可以了
        channel.queueBind(queueName, exchangeName, "type02");
        // 配置好获取消息的方式
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
        // 循环获取消息
        while (true) {
            // 获取消息，如果没有消息，这一步将会一直阻塞
            Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("received message[" + msg + "] from " + exchangeName);
        }

    }
}
