package rpc;

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
public class RPCServer {

    public static final String RPC_QUEUE_NAME_TESTA = "RPCServer_queue_testA";
    public static final String RPC_QUEUE_NAME_TESTB = "RPCServer_queue_testB";

    public static void main(String[] args) throws Exception {
        // 生成服務A
        new RPCServer().ServerTest(RPC_QUEUE_NAME_TESTB);
    }

    public static String TestA(String name) {
        return "RPCServer_TestA " + name;
    }

    public static String TestB(String name) {
        return "RPCServer_TestB " + name;
    }

    public void ServerTest(final String QUEUE_NAME) throws Exception {

        ConnectionFactory connFac = new ConnectionFactory();
        connFac.setHost("localhost");
        connFac.setVirtualHost("upsmart");
        connFac.setUsername("xinyi");
        connFac.setPassword("516185423");
        Connection conn = connFac.newConnection();
        Channel channel = conn.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME, false, consumer);
        while (true) {
            System.out.println("服务端等待接收消息..");
            Delivery deliver = consumer.nextDelivery();
            System.out.println("服务端成功收到消息..");
            BasicProperties props = deliver.getProperties();
            String message = new String(deliver.getBody(), "UTF-8");
            // 控制调用的方法
            String responseMessage = null;
            if (QUEUE_NAME == RPC_QUEUE_NAME_TESTA) {
                responseMessage = TestA(message);
            } else if (QUEUE_NAME == RPC_QUEUE_NAME_TESTB) {
                responseMessage = TestB(message);
            } else {
                System.out.println("無可以匹配的方法!");
            }
            BasicProperties responseProps = new BasicProperties.Builder().correlationId(props.getCorrelationId())
                    .build();
            // 将结果返回到客户端Queue
            channel.basicPublish("", props.getReplyTo(), responseProps, responseMessage.getBytes("UTF-8"));
            // 向客户端确认消息
            channel.basicAck(deliver.getEnvelope().getDeliveryTag(), false);
            System.out.println("服务端返回消息完成..");
        }

    }
}
