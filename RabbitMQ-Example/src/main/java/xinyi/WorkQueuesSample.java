package xinyi;

import java.io.IOException;

import com.rabbitmq.client.*;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
/**
 * 分发模式
 * @author upsmart
 *
 */
public class WorkQueuesSample {

    public static void main(String[] args) throws Exception {
        new WorkQueuesSample().test();
    }

    private static final String TASK_QUEUE_NAME = "task_queue";

    public void test() throws Exception {
        new Thread() {
            @Override
            public void run() {
                System.out.println("sending message...");
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
                System.out.println("recieving message...");
                try {
                    worker("W1");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                System.out.println("recieving message...");
                try {
                    worker("W2");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    private void send() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost("upsmart");
        factory.setUsername("xinyi");
        factory.setPassword("516185423");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // queueDeclare(queue, durable, exclusive, autoDelete,
        // Map<String,Object> args)
        // Declare a queue
        channel.queueDeclare(TASK_QUEUE_NAME, true/* durable */, false, false, null);

        for (int i = 0; i < 4; i++) {
            String m = "message-" + i + "-" + System.currentTimeMillis();
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, m.getBytes());
            System.out.println(" 发送： '" + m + "'");

        }

        channel.close();
        connection.close();
    }

    private void worker(final String name) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost("upsmart");
        factory.setUsername("xinyi");
        factory.setPassword("516185423");

        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        // basicQos(int prefetchCount)
        channel.basicQos(2);

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [" + name + "] 接收：" + message + "'");
            }
        };
        channel.basicConsume(TASK_QUEUE_NAME, false/* autoAck */, consumer);
    }

}
