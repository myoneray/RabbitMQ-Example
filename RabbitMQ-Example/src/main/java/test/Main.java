package test;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class Main {
    public Main() throws Exception {

        QueueConsumer consumer = new QueueConsumer("queue");
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        Producer producer = new Producer("queue");

        for (int i = 0; i < 10; i++) {
            HashMap<String, Integer> message = new HashMap<String, Integer>();
            message.put("msg", i);
            producer.sendMessage(message);
            System.out.println( i + " 发送");
        }
    }

    /**
     * @param args
     * @throws SQLException
     * @throws IOException
     */
    public static void main(String[] args) throws Exception {
        new Main();
    }

}