package me.myone.spring.rabbitmq.test.simple.rpc;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Title:Server.java <br>
 * Description:启动服务端代码
 *
 * @author Wangming
 * @since 2016年2月18日
 */

public class Server {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("RpcConfig/applicationContext-rabbitmq-rpc-server.xml");
    }
}