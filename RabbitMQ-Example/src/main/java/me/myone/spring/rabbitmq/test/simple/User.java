package me.myone.spring.rabbitmq.test.simple;

/**
 * Title:User.java <br>
 * Description: entity
 *
 * @author Wangming
 * @since 2016年2月18日
 */
public class User {
    private String name;
    private String data;

    /**
     * 获取data
     * 
     * @return data
     */
    public String getData() {
        return data;
    }

    /**
     * 设置data
     * 
     * @param data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * 获取name
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置name
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

}
