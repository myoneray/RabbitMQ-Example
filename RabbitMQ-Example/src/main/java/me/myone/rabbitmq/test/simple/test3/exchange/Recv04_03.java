package me.myone.rabbitmq.test.simple.test3.exchange;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * Title:Recv04_01.java <br>
 * Description:接收消息
 *
 * @author Wangming
 * @since 2016年2月29日
 */
public class Recv04_03 {
    public static void main(String[] args) throws IOException, ShutdownSignalException, ConsumerCancelledException,
            InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String exchangeName = "exchangeName";
        channel.exchangeDeclare(exchangeName, "fanout");
        // 以上与发送者相同

        /**
         * 
         * 使用channel.queueDeclare()方法创建了一个Queue，<br>
         * 该Queue有系统创建， 并分配了一个随机的名称。<br>
         * 然后将该Queue与与Exchange绑定在一起。<br>
         * 该Queue就能从Exchange中后取消息了。
         */

        // 这里使用没有参数的queueDeclare方法创建Queue并获取QueueName
        String queueName = channel.queueDeclare().getQueue();
        // 将queue绑定到Exchange中
        channel.queueBind(queueName, exchangeName, "");

        // 配置好获取消息的方式
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
        // 循环获取消息
        while (true) {
            Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("reclive-03-:in ["+dateFormat.format(new Date())+"]" + msg);
        }

    }
}
