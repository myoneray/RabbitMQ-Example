package test;
import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 我们首先写一个类，将产生产者和消费者统一为 EndPoint类型的队列。<br/>
 * 不管是生产者还是消费者， 连接队列的代码都是一样的，这样可以通用一些。
 * 
 * @author MYONERAY
 *
 */
public abstract class EndPoint {
    // channel 主要进行相关定义，发送消息，获取消息，事务处理等。
    protected Channel channel;
    // Connection是RabbitMQ的socket链接，它封装了socket协议相关部分逻辑。
    protected Connection connection;
    protected String endPointName;

    public EndPoint(String endpointName) throws IOException {
        this.endPointName = endpointName;
        // ConnectionFactory为Connection的制造工厂。
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();
        // Channel是我们与RabbitMQ打交道的最重要的一个接口，
        // 我们大部分的业务操作是在Channel这个接口中完成的，
        // 包括定义Queue、定义Exchange、绑定Queue与Exchange、发布消息等。
        channel.queueDeclare(endpointName, false, false, false, null);
    }

    /**
     * 关闭channel和connection。并非必须，因为隐含是自动调用的。
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        this.channel.close();
        this.connection.close();
    }
}