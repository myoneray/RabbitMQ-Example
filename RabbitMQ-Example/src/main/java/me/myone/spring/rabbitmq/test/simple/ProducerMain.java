package me.myone.spring.rabbitmq.test.simple;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new ClassPathXmlApplicationContext("config/Producer.xml");
        AmqpTemplate amqpTemplate = context.getBean(RabbitTemplate.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 1; i <= 1; i++) {
            User user = new User();
            user.setName("TestNO" + i);
            user.setData(dateFormat.format(new Date()));
            amqpTemplate.convertAndSend(user);
            Thread.sleep(1000);
        }
    }

}
