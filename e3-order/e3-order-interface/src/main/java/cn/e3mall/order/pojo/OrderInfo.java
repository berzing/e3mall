package cn.e3mall.order.pojo;

import cn.e3mall.pojo.TbOrder;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/15.
 */

/*
我们怎么接收表单order-cart.jsp中的数据呢？
我们可以使用pojo  但是简单的pojo还不行  需要使用包装的pojo
orderShipping,orderItems,,
我们接收表单的数据就是为了插到与订单相关的数据库中(tb_order_shipping,tb_order_item,tb_order)
因此我们可以将这3个表对应的pojo来拼装一个能接收表单中的所有数据的pojo！！
因此我们需要jsp中name属性能与3个订单表相关pojo中的属性能对应
首先TbOrder中能对应的属性(payment,paymentType,postFee)   但是我们还需要接收一个List和收货地址  因此我们需要"扩展TbOrder"
(最好的扩展方式应该是继承这个TbOrder然后添加我想要的属性)
那么这个扩展的pojo应该放在哪里呢？
放到e3-common中行吗？不行！！
因为你要继承TbOrder  所以e3-common就要依赖e3-manager-pojo  但是e3-maanger-pojo又是依赖e3-common的  形成循环依赖
所以这个"扩展的pojo"是不能放到e3-common中的！！！！
我们可以将这个扩展的pojo放到e3-order-interface中！！！
(因为e3-order-service与e3-order-web都依赖于e3-order-interface  这样这个pojo就可以被这两个工程所共用！)
*/

//当前的pojo需要从表现层传递给服务层  需要网络传输  因而需要实现序列化接口
public class OrderInfo extends TbOrder implements Serializable{

    private List<TbOrderItem> orderItems;
    private TbOrderShipping orderShipping;

    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }
}
