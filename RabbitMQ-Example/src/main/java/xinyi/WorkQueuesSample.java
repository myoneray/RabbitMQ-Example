package xinyi;

import java.io.IOException;

import com.rabbitmq.client.*;

/**
 * 分发模式
 * 
 * @author MYONERAY
 *
 */
public class WorkQueuesSample {
    private int temp = 10;

    public static void main(String[] args) throws Exception {
        new WorkQueuesSample().test();
    }

    public void test() throws Exception {
        new Thread() {
            @Override
            public void run() {
                try {
                    send();
                    System.out.println("----------------------------------------------------");
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
                    worker("分发模式线程A");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    worker("分发模式线程B");
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
        channel.queueDelete("task_queue");
        channel.queueDeclare("task_queue", true, false, false, null);
        for (int i = 0; i < temp; i++) {
            String m = i + ">TestMessage";
            channel.basicPublish("", "task_queue", MessageProperties.PERSISTENT_TEXT_PLAIN, m.getBytes());
            System.out.println(" [分发模式发送消息] ： '" + m + "'");
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
        channel.queueDeclare("task_queue", true, false, false, null);
        /**
         * 意思为，最多为当前接收方发送5条消息。<br />
         * 如果接收方还未处理完毕消息，还没有回发确认，就不要再给他分配消息了，<br />
         * 应该把当前消息分配给其它空闲接收方。
         */
        channel.basicQos(5);

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [" + name + "] 接收：" + message + "'");
            }
        };
        // 自动删除消息
        // channel.basicConsume("task_queue", true, consumer);
        // 需要接受方发送ack回执,删除消息
        channel.basicConsume("task_queue", false, consumer);
    }

}
