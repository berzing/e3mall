package cn.e3mall.cart.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;

import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */
public interface CartService {
    //添加购物车
    E3Result addCart(long userId,long itemId,int num);
    //合并购物车
    E3Result mergeCart(long userId, List<TbItem> itemList);
    //取购物车列表
    List<TbItem> getCartList(long userId);
    //更新购物车数量
    E3Result updateCartNum(long userId,long itemId,int num);
    //删除购物车
    E3Result deleteCartItem(long userId,long itemId);
    //清空购物车
    E3Result clearCartItem(long userId);
}
