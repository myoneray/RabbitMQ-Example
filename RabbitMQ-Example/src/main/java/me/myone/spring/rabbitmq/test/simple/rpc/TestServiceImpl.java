package me.myone.spring.rabbitmq.test.simple.rpc;

/**
 * Title:TestServiceImpl.java <br>
 * Description:测试服务接口实现
 *
 * @author Wangming
 * @since 2016年2月18日
 */
public class TestServiceImpl implements TestService {

    public String say(String msg) {
        return "HELLO " + msg;
    }

}
