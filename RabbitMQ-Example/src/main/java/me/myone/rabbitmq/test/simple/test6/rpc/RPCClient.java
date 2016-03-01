package me.myone.rabbitmq.test.simple.test6.rpc;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * 客户端向服务端定义好的Queue发送消息，<br />
 * 其中携带的消息就应该是服务端将要调用的方法的参数 <br />
 * 并使用Propertis告诉服务端将结果返回到指定的Queue。
 * 
 * @author MYONERAY
 *
 */
public class RPCClient {

    public static final String RPCNAME = "rpc_queue";

    public static void main(String[] args) throws IOException, ShutdownSignalException, ConsumerCancelledException,
            InterruptedException {
        // 创建一个连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置MabbitMQ所在主机ip或者主机名
        connectionFactory.setHost("localhost");
        // 创建一个连接
        Connection connection = connectionFactory.newConnection();
        // 创建一个队列
        Channel channel = connection.createChannel();
        // 为每一个客户端获取一个随机的回调队列
        String queueName = channel.queueDeclare().getQueue();
        // UUID
        String uuid = UUID.randomUUID().toString();
        // 设置basicProperties的replyTo和correlationId属性值
        BasicProperties basicProperties = new BasicProperties().builder().replyTo(queueName).correlationId(uuid)
                .build();
        // 发送消息到rpc_queue队列 ,此处使用默认队列("");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
        String now = "[Wangming]客户端发送时间:" + dateFormat.format(new Date());
        channel.basicPublish("", RPCNAME, basicProperties, now.getBytes("UTF-8"));
        // 为每一个客户端创建一个消费者（用于监听回调队列，获取结果）
        QueueingConsumer consumer = new QueueingConsumer(channel);
        // 消费者与队列关联
        channel.basicConsume(queueName, consumer);
        while (true) {
            // 获取消息，如果没有消息，这一步将会一直阻塞
            Delivery delivery = consumer.nextDelivery();
            // 检查每一个响应消息中correlationId值，是否是它要寻找的。如果是，它会保存这响应。
            // 最终，我们把响应返回给用户。
            if (delivery.getProperties().getCorrelationId().equals(uuid)) {
                System.out.println("RPC请求成功:" + new String(delivery.getBody()));
            }
        }
    }
}
