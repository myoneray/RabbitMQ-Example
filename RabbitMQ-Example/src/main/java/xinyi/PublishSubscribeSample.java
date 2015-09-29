package xinyi;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * 发布订阅模式： <br />
 * 消息先到exchange, 然后到绑定的queue<br />
 * Exchange的类型：fanout, 忽略routingKey <br />
 * Queue和exchange绑定。
 * 
 * @author MYONERAY
 *
 */
public class PublishSubscribeSample {

    public static void main(String[] args) throws Exception {
        new PublishSubscribeSample().test();
    }

    private static final String EXCHANGE_NAME = "exchange_test_name";

    public void test() throws Exception {
        new Thread(new Runnable() {
            public void run() {
                try {
                    recieveLogs();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
        Thread.currentThread().sleep(1000);
        new Thread(new Runnable() {
            public void run() {
                try {
                    emitLog();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private static String getMessage(String[] strings) {
        if (strings.length < 1)
            return "Hello World!";
        return joinStrings(strings, " ");
    }

    private static String joinStrings(String[] strings, String delimiter) {
        int length = strings.length;
        if (length == 0)
            return "";
        StringBuilder words = new StringBuilder(strings[0]);
        for (int i = 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }

    private void emitLog() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost("upsmart");
        factory.setUsername("xinyi");
        factory.setPassword("516185423");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        for (int i = 0; i < 5; i++) {
            String message = getMessage(new String[] { i + "MEG", "ts: ", "" + System.currentTimeMillis() });
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
            System.out.println(" 发送： '" + message + "'");
        }
        channel.close();
        connection.close();
    }

    private void recieveLogs() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost("upsmart");
        factory.setUsername("xinyi");
        factory.setPassword("516185423");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // exchange类型为fanout
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //  创建匿名Queue
        String queueNameA = channel.queueDeclare().getQueue();
        String queueNameB = channel.queueDeclare().getQueue();
        System.out.println(" 创建匿名Queue:A: " + queueNameA);
        System.out.println(" 创建匿名Queue:B: " + queueNameB);
        channel.queueBind(queueNameA, EXCHANGE_NAME, "");
        channel.queueBind(queueNameB, EXCHANGE_NAME, "");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [Consumer-A] 接收：" + message + "'");
            }
        };
        Consumer consumer2 = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [Consumer-B] 接收：" + message + "'");
            }
        };
        channel.basicConsume(queueNameA, true, consumer);
        channel.basicConsume(queueNameB, true, consumer2);
    }

}
