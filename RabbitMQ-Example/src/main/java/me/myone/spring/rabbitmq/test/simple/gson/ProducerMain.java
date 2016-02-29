package me.myone.spring.rabbitmq.test.simple.gson;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Title:ProducerMain.java <br>
 * Description: 生产者启动类ProducerMain.java
 *
 * @author Wangming
 * @since 2016年2月18日
 */
public class ProducerMain {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("gsonConfig/Producer.xml");
        AmqpTemplate amqpTemplate = context.getBean(RabbitTemplate.class);

        User user = new User();
        user.setName("TestName");
        amqpTemplate.convertAndSend(user);

    }
}
