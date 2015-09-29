package xinyi;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Random;

/**
 * 模式：routingKey定向发布<br />
 * 指定routing key来决定消息流向哪里<br />
 * Queue可以绑定多个routingKey 同样的routingKey绑定到多个queue, 则类似fanout。
 * 
 * @author MYONERAY
 *
 */
public class RoutingSample {

    /**
     * 本次测试的目的是： routing key 生产者在将消息发送给Exchange的时候，一般会指定一个routing
     * key，来指定这个消息的路由规则， <br />
     * 而这个routing key需要与Exchange Type及binding key联合使用才能最终生效。 <br />
     * 在Exchange Type与binding key固定的情况下（在正常使用时一般这些内容都是固定配置好的），<br />
     * 我们的生产者就可以在发送消息给Exchange时，通过指定routing key来决定消息流向哪里。<br />
     */
    public static void main(String[] args) throws Exception {
        new RoutingSample().test();
    }

    private static final String EXCHANGE_NAME = "direct_logs";

    public void test() throws Exception {
        new Thread() {
            public void run() {
                try {
                    receiveLogsWithRoutingKey();
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
                    emitLogDirect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();

    }

    private void emitLogDirect() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost("upsmart");
        factory.setUsername("xinyi");
        factory.setPassword("516185423");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        // 创建一个路由key数组
        String[] severitys = new String[] { "A", "B", "C", "D", "E", "F", "G" };
        Random rand = new Random();
        for (int i = 0; i < severitys.length - 1; i++) {
            int severityInd = rand.nextInt(3);
            String routingKeyName = severitys[severityInd];
            String message = " at: " + System.currentTimeMillis();
            /**
             * 随机取出一个值作为一个消息流的routingKey <br />
             * 通过routingKey来绝对决定消息流向哪里
             */
            channel.basicPublish(EXCHANGE_NAME, routingKeyName, null, message.getBytes());
            System.out.println(" 发送： '" + routingKeyName + "':'" + message + "'");
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignore) {
            }
        }

        channel.close();
        connection.close();
    }

    private void receiveLogsWithRoutingKey() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost("upsmart");
        factory.setUsername("xinyi");
        factory.setPassword("516185423");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();
        /**
         * 將routingKeyName为 "B", "C"的消息接收到此处
         */
        String[] severitys = new String[] { "B", "C" };

        for (String severity : severitys) {
            channel.queueBind(queueName, EXCHANGE_NAME, severity);
        }
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [接收消息] '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}
