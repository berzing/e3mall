package cn.e3mall.order.interceptor;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 订单系统用户登录拦截器
 * Created by Administrator on 2017/8/15.
 */
public class LoginInterceptor implements HandlerInterceptor{
    @Value("${SSO_URL}")
    private String SSO_URL;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CartService cartService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从cookie中取token
        String token=CookieUtils.getCookieValue(request,"token");
        //判断token是否存在
        if(StringUtils.isBlank(token)){
            //如果token不存在  未登录状态  跳转到sso系统的登录界面  用户登录成功后  跳转当前请求的url
            //此处只能用redirect  使用不了forward(因为sso系统与订单系统属于两个不同的页面)
            response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL());
            //拦截
            return false;
        }
        //如果token存在  需要调用sso系统的服务  根据token取用户信息
        E3Result e3Result=tokenService.getUserByToken(token);
        //如果取不到  用户登录已经过期  需要登录
        if(e3Result.getStatus()!=200){
            //如果token不存在  未登录状态  跳转到sso系统的登录界面  用户登录成功后  跳转当前请求的url
            response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL());
            //拦截
            return false;
        }
        //如果取到用户信息  是登录状态  需要把用户信息写入request
        TbUser user= (TbUser) e3Result.getData();
        request.setAttribute("user",user);
        //判断cookie中是否有购物车数据  如果有就会合并到服务器
        String jsonCartList=CookieUtils.getCookieValue(request,"cart",true);
        if(StringUtils.isNotBlank(jsonCartList)){
            //合并购物车
            cartService.mergeCart(user.getId(), JsonUtils.jsonToList(jsonCartList, TbItem.class));
        }
        //放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
