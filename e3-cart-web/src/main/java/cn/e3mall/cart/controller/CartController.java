package cn.e3mall.cart.controller;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车处理Controller
 * Created by Administrator on 2017/8/14.
 */


@Controller
public class CartController {

    @Autowired
    private ItemService itemService;
    @Value("${COOKIE_CART_EXPIRE}")
    private Integer COOKIE_CART_EXPIRE;
    @Autowired
    private CartService cartService;

    @RequestMapping("/cart/add/{itemId}")
    //点击"加入购物车"会显示"添加成功"的页面！！  因此返回值就应该是String(返回一个逻辑视图)
    public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1")Integer num,
                          HttpServletRequest request, HttpServletResponse response){

        //itemId:商品编号  num:商品数量  request:从cookie中取  response:往cookie中写

        //判断用户是否登录
        TbUser user= (TbUser) request.getAttribute("user");
        //如果是登录状态  把购物车写入redis
        if(user!=null){
            //保存到服务端
            cartService.addCart(user.getId(),itemId,num);
            //返回逻辑视图
            return "cartSuccess";
        }
        //如果未登录使用cookie
        //从cookie中取购物车列表
        //因为添加商品与展示商品都要取购物车列表  所以我们应该将此功能单独提出来  即我们后边实现的getCartListFromCookie()！！
        List<TbItem> cartList=getCartListFromCookie(request);
        //判断商品在商品列表中是否存在(也就是判断id是否相等  如果id相等则表明存在)
        boolean flag=false;//定义标志位  flag=true表示找到了商品   flag=false表示没有找到商品
        for (TbItem tbItem : cartList) {
            /*
            因为itemId的类型是Long  是一个对象  而getId()返回也是对象Long
            两个对象相比较   比较的是内存地址  并不是他们的值
            因此itemId要写成itemId.longValue()  [longValue()返回的是简单数据类型long]
            (Ctrl+Q  查看返回值类型)
            */
            if (tbItem.getId() == itemId.longValue()) {
                flag=true;
                //找到商品  数量相加
                tbItem.setNum(tbItem.getNum()+num);
                //跳出循环
                break;
            }
        }
        //如果不存在
        if(!flag){
            //根据商品id查询商品信息  得到一个TbItem对象
            TbItem tbItem=itemService.getItemById(itemId);
            //设置商品数量
            tbItem.setNum(num);
            //取一张图片
            String image=tbItem.getImage();
            if(StringUtils.isNotBlank(image)){
                tbItem.setImage(image.split(",")[0]);
            }
            //把商品添加到商品列表
            cartList.add(tbItem);
        }
        //写入cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);
        //返回添加成功页面
        return "cartSuccess";
    }


    //从cookie中取购物车列表的处理
    private List<TbItem> getCartListFromCookie(HttpServletRequest request){
        String json= CookieUtils.getCookieValue(request,"cart",true);
        //判断json是否为空
        if(StringUtils.isBlank(json)){
            //如果json为空我们就返回一个List(此时List中没有元素)
            //也就是说  不管json为空还是不为空  都会返回一个List对象
            return new ArrayList<>();
        }
        //把json转换成商品列表
        List<TbItem> list= JsonUtils.jsonToList(json,TbItem.class);
        return list;
    }


    //展示购物车列表
    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request,HttpServletResponse response){
        //从cookie中取购物车列表
        List<TbItem> cartList=getCartListFromCookie(request);
        //判断用户是否为登录状态
        TbUser user= (TbUser) request.getAttribute("user");
        //如果是登录状态
        if(user!=null){
            //从cookie中取购物车列表
            //如果不为空  把cookie中的购物车商品和服务端购物车商品合并
            cartService.mergeCart(user.getId(),cartList);
            //把cookie中的购物车删除
            CookieUtils.deleteCookie(request,response,"cart");
            //从服务端取购物车列表
            cartList=cartService.getCartList(user.getId());
        }
        //把列表传递给页面
        request.setAttribute("cartList",cartList);
        //返回逻辑视图
        return "cart";
    }


    //更新购物车商品数量
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody  //ajax请求  所以响应一个json数据
    public E3Result updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,
                                  HttpServletRequest request,HttpServletResponse response){

        //itemId:商品编号  num:商品数量  request:从cookie中取  response:往cookie中写

        //判断用户是否为登录状态
        TbUser user= (TbUser) request.getAttribute("user");
        if(user!=null){
            cartService.updateCartNum(user.getId(),itemId,num);
            return E3Result.ok();
        }
        //从cookie中取购物车列表
        List<TbItem> cartList=getCartListFromCookie(request);
        //遍历商品列表找到对应的商品
        for(TbItem tbItem:cartList){
            if (tbItem.getId().longValue()==itemId){
                //更新数量
                tbItem.setNum(num);
                break;
            }
        }
        //把购物车列表写回cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);
        //返回成功
        return E3Result.ok();
    }


    //删除购物车商品
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request,
                                 HttpServletResponse response){
        //判断用户是否为登录状态
        TbUser user= (TbUser) request.getAttribute("user");
        if(user!=null){
            cartService.deleteCartItem(user.getId(),itemId);
            return "redirect:/cart/cart.html";
        }

        //从cookie中取购物车列表
        List<TbItem> cartList=getCartListFromCookie(request);
        //遍历列表  找到要删除的商品
        for (TbItem tbItem:cartList) {
            if (tbItem.getId().longValue()==itemId){
                //删除商品
                cartList.remove(tbItem);
                //跳出循环
                break;
            }
        }
        //把购物车列表写入cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);
        //返回逻辑视图
        return "redirect:/cart/cart.html";
    }
}
