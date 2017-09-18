package cn.e3mall.sso.controller;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 注册功能Controller
 * Created by Administrator on 2017/8/7.
 */

@Controller
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @RequestMapping("/page/register")
    public String showRegister(){
        return "register";
    }


    @RequestMapping("/user/check/{param}/{type}")//与register.jsp对应
    @ResponseBody
    //两个参数param,type都需要从路径中取  因而需要使用注解"@PathVariable"
    public E3Result checkData(@PathVariable String param,@PathVariable Integer type){
        E3Result e3Result=registerService.checkData(param,type);
        return e3Result;
    }


    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    @ResponseBody
    public E3Result register(TbUser user){
        E3Result e3Result=registerService.register(user);
        return e3Result;
    }
}
