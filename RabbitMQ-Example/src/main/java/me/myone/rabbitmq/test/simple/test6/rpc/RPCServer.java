package me.myone.rabbitmq.test.simple.test6.rpc;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * Title:RPCServer.java <br>
 * Description:RPCServer
 *
 * @author Wangming
 * @since 2016年3月1日
 */
public class RPCServer {

    public static final String RPCNAME = "rpc_queue";

    public static String hello(String name) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
        String now = "&服务器处理时间:" + dateFormat.format(new Date());
        return "你好" + name + now;
    }

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
        // 为channel定义queue的属性
        channel.queueDeclare(RPCNAME, false, false, false, null);
        // 限制：每次最多给一个消费者发送1条消息
        channel.basicQos(1);
        // 为rpc_queue队列创建消费者，用于处理请求
        QueueingConsumer consumer = new QueueingConsumer(channel);
        // 消费者与队列关联
        channel.basicConsume(RPCNAME, false, consumer);
        while (true) {
            System.out.println("等待消息.....");
            // 获取消息，如果没有消息，这一步将会一直阻塞
            Delivery delivery = consumer.nextDelivery();
            // 获取请求中的属性值
            BasicProperties deliveryProperties = delivery.getProperties();
            // 获取请求数据
            String msg = new String(delivery.getBody(), "UTF-8");
            System.out.println("收到消息:[" + msg + "]!!!");
            // 获取处理结果
            String remsg = hello(msg);
            // 新建BasicProperties,并将获取请求中的correlationId属性值设置到结果消息的correlationId属性中
            BasicProperties basicProperties = new BasicProperties().builder()
                    .correlationId(deliveryProperties.getCorrelationId()).build();
            // 先发送回调结果,返回queue消息 ,此处使用默认队列("");
            channel.basicPublish("", deliveryProperties.getReplyTo(), basicProperties, remsg.getBytes());
            // 后手动发送消息反馈
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            System.out.println("服务器消息返回完成!");
        }
    }
}
