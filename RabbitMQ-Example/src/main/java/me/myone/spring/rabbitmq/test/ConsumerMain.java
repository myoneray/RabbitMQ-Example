package me.myone.spring.rabbitmq.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Title:ConsumerMain.java <br>
 * Description: 消费者启动类ConsumerMain.java
 *
 * @author Wangming
 * @since 2016年2月18日
 */
public class ConsumerMain {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("config/context.xml");
    }
}
