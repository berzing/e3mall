package cn.e3mall.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2017/8/4.
 */
public class MessageConsumer {

    @Test
    public void msgConsumer() throws Exception{
        //初始化spring容器
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:/spring/applicationContext-activemq.xml");
        //等待
        System.in.read();
    }
}
