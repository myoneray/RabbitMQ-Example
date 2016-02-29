package me.myone.spring.rabbitmq.test.simple.rpc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Title:Client.java <br>
 * Description:客户端调用代码
 *
 * @author Wangming
 * @since 2016年2月18日
 */

public class Client {
    // 先启动服务端，再运行客户端调用
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "RpcConfig/applicationContext-rabbitmq-rpc-client.xml");
        TestService testService = (TestService) context.getBean("testService");
        System.out.println(testService.say(" Tom"));
    }
}
