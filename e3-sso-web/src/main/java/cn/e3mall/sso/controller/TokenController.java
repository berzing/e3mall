package cn.e3mall.sso.controller;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 根据token查询用户信息Controller
 * Created by Administrator on 2017/8/13.
 */

@Controller
public class TokenController {

    @Autowired
    private TokenService tokenService;

    /*  跨域处理方案1
    @RequestMapping(value = "/user/token/{token}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getUserByToken(@PathVariable String token,String callback){
        E3Result result=tokenService.getUserByToken(token);
        //响应结果之前  判断是否为json请求
        if(StringUtils.isNotBlank(callback)){
            //把结果封装成一个js语句响应
            return callback+"("+ JsonUtils.objectToJson(result)+");";
        }
        return JsonUtils.objectToJson(result);
    }
    */


    //跨域处理方案2(推荐)
    @RequestMapping(value = "/user/token/{token}")
    @ResponseBody
    public Object getUserByToken(@PathVariable String token,String callback){
        //因为响应的都是对象  所以返回类型为Object


        E3Result result=tokenService.getUserByToken(token);
        //响应结果之前  判断是否为"jsonp"请求(如果callback不为空则说明当前请求是一个"jsonp"请求)
        if(StringUtils.isNotBlank(callback)){
            //如果是jsonp请求   我们就应该把结果封装成一个js语句响应
            MappingJacksonValue mappingJacksonValue=new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
        return result;
    }
}
