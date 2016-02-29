package me.myone.spring.rabbitmq.test.simple;

import org.springframework.amqp.core.Message;

/**
 * Title:MessageListener.java <br>
 * Description:消息监听器<br>
 * Listener interface to receive asynchronous delivery of Amqp Messages.
 *
 * @author Wangming
 * @since 2016年2月18日
 */
public interface MessageListener {
    void onMessage(Message message);
}
