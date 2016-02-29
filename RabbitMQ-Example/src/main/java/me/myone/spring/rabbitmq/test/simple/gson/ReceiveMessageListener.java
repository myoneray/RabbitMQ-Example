package me.myone.spring.rabbitmq.test.simple.gson;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * Title:ReceiveMessageListener.java <br>
 * Description: 实现一个消息监听器ReceiveMessageListener.java
 *
 * @author Wangming
 * @since 2016年2月18日
 */
public class ReceiveMessageListener implements MessageListener {

    public void onMessage(Message msg) {
        System.out.println(msg);
    }

}
