package cn.e3mall.jedis;

import cn.e3mall.common.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2017/7/31.
 */
public class JedisClientTest {

    @Test
    public void testJedisClient() throws Exception{
        //初始化spring容器
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        //从容器中获得JedisClient对象
        JedisClient jedisClient=applicationContext.getBean(JedisClient.class);
        jedisClient.set("mytest","jedisClient");
        String string=jedisClient.get("mytest");
        System.out.println(string);
    }
}
