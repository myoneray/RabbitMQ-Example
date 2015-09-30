package rpc;

import java.util.UUID;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

/**
 * 客户端向服务端定义好的Queue发送消息，<br />
 * 其中携带的消息就应该是服务端将要调用的方法的参数 <br />
 * 并使用Propertis告诉服务端将结果返回到指定的Queue。
 * 
 * @author MYONERAY
 *
 */
public class RPCClient {

    public static final String RPC_QUEUE_NAME_TESTA = "RPCServer_queue_testA";
    public static final String RPC_QUEUE_NAME_TESTB = "RPCServer_queue_testB";

    // 测试客戶端A
    public static void main(String[] args) throws Exception {
        new RPCClient().ClientTest(RPC_QUEUE_NAME_TESTB);
    }

    public void ClientTest(final String QUEUE_NAME) throws Exception {
        ConnectionFactory connFac = new ConnectionFactory();
        connFac.setHost("localhost");
        connFac.setVirtualHost("upsmart");
        connFac.setUsername("xinyi");
        connFac.setPassword("516185423");
        Connection conn = connFac.newConnection();
        Channel channel = conn.createChannel();

        // 响应QueueName ，服务端将会把要返回的信息发送到该Queue
        String responseQueue = channel.queueDeclare().getQueue();
        String correlationId = UUID.randomUUID().toString();
        BasicProperties props = new BasicProperties.Builder().replyTo(responseQueue).correlationId(correlationId)
                .build();
        String message = "_RPCClient";
        channel.basicPublish("", QUEUE_NAME, props, message.getBytes("UTF-8"));
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(responseQueue, consumer);

        while (true) {
            Delivery delivery = consumer.nextDelivery();
            if (delivery.getProperties().getCorrelationId().equals(correlationId)) {
                String result = new String(delivery.getBody());
                System.out.println(result);
            }

        }
    }

}
