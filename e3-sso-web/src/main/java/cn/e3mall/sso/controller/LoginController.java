package cn.e3mall.sso.controller;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录处理
 * Created by Administrator on 2017/8/13.
 */

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;


    //该方法仅仅用来展示登录页面(不能确认用户是否登录)
    /*
    拦截器LoginInterceptor配置好以后进行测试  发现用户没登录时点击"去结算"就会被拦截并且会跳转到"用户登录页面"
    但是我们发现用户登录以后  就直接跳转到了首页
    这样很不合理  用户登录以后我们应该跳转到"订单确认页面"！！！
    */

    /*
    那么我们怎么确定用户是否登录成功呢？
    我们可以在页面login.jsp中确认
    由方法doLogin可知  redirectUrl不为空时  登录后会跳转到redirectUrl代表的页面  如果redirectUrl为空则就会跳转到首页
    所以我们可以在LoginController中给页面login.jsp传递一个redirectUrl即可(可以使用Model)
    这样我们就可以决定用户登录后需要跳转的页面(此处用户登录成功后我们将用户跳转到订单确认页面  注意不是商城首页！)
    */
    @RequestMapping("/page/login")
    public String showLogin(String redirect, Model model){
        model.addAttribute("redirect",redirect);//login.jsp(119行  136行)
        return "login";
    }


    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    @ResponseBody
    public E3Result login(String username, String password,
                          HttpServletRequest request, HttpServletResponse response){
        E3Result e3Result=loginService.userLogin(username,password);
        //判断是否登录成功
        if(e3Result.getStatus()==200){
            String token=e3Result.getData().toString();
            //如果登录成功需要把token写入cookie(需要将资料中的工具类CookieUtils加入e3-common)
            CookieUtils.setCookie(request,response,TOKEN_KEY,token);
        }
        //返回结果
        return e3Result;
    }
}
