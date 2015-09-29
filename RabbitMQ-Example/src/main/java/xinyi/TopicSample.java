package xinyi;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Random;

/**
 * Exchange Type: topic<br />
 * Routingkey 格式：*.*.* <br />
 * 表示一个单词。＃表示0到多个单词。<br />
 * ＃形似的routingKey表现类似fanout<br />
 * routingKey中无通配符，则和direct exchange表现相同。<br />
 * 
 * @author upsmart
 *
 */
public class TopicSample {

    public static void main(String[] args) throws Exception {
        new TopicSample().test();
    }

    private static final String EXCHANGE_NAME = "topic_logs";

    public void test() throws Exception {
        new Thread() {
            public void run() {
                try {
                    receiveLogTopic();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignore) {
        }

        new Thread() {
            public void run() {
                try {
                    emitLogTopic();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();

    }

    private void emitLogTopic() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost("upsmart");
        factory.setUsername("xinyi");
        factory.setPassword("516185423");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String[] keys = new String[] { "aa.bb.cc", "xx.yy.zz", "ss.mm" };

        Random rand = new Random();
        for (int i = 0; i < 15; i++) {
            int keyInd = rand.nextInt(3);
            String key = keys[keyInd];
            String message = "message generated at: " + System.currentTimeMillis();
            channel.basicPublish(EXCHANGE_NAME, key, null, message.getBytes());
            System.out.println(" 发送： '" + key + "':'" + message + "'");
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignore) {
            }
        }

        // channel.close();
        connection.close();
    }

    private void receiveLogTopic() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost("upsmart");
        factory.setUsername("xinyi");
        factory.setPassword("516185423");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String queueName = channel.queueDeclare().getQueue();

        String[] bindingkeys = new String[] { "aa.#", "*.yy.*" };

        for (String key : bindingkeys) {
            channel.queueBind(queueName, EXCHANGE_NAME, key);
        }
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" 接收：" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

}
