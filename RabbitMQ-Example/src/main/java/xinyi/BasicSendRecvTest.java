package xinyi;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
/**
 * 基本接收 
 * @author MYONERAY
 *
 */
public class BasicSendRecvTest {
    public static void main(String[] args) throws Exception {
        BasicSendRecvTest.test();
    }

    public static void test() throws Exception {
        new Thread() {
            @Override
            public void run() {
                try {
                    send();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
        Thread.currentThread().sleep(1000);
        new Thread() {
            @Override
            public void run() {
                try {
                    recieve();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    private static void send() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost("upsmart");
        factory.setUsername("xinyi");
        factory.setPassword("516185423");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("HELLO", false, false, false, null);
        String message = "Hello World!";
        channel.basicPublish("", "HELLO", null, message.getBytes());
        System.out.println(" [基本发送] ＞" + message);
        channel.close();
        connection.close();
    }

    private static void recieve() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost("upsmart");
        factory.setUsername("xinyi");
        factory.setPassword("516185423");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("HELLO", false, false, false, null);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [基本接收] ＞" + message);
            }
        };
        channel.basicConsume("HELLO", true, consumer);
        // channel.close();
        // connection.close();
    }

}