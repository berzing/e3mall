package cn.e3mall.search.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理器
 * Created by Administrator on 2017/8/4.
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver{

    //推荐使用slf4j中的Logger
    private static final Logger logger= LoggerFactory.getLogger(GlobalExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //打印控制台
        ex.printStackTrace();
        //写日志
        logger.debug("测试输出的日志....");
        logger.info("系统发生异常了....");
        logger.error("系统发生异常....",ex);
        //发邮件、发短信
        //使用jMail工具包(发短信使用第三方的webservice)
        //显示错误页面
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("error/exception");
        return modelAndView;
    }
}
