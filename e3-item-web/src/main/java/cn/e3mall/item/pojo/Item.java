package cn.e3mall.item.pojo;

import cn.e3mall.pojo.TbItem;

/**
 * Created by Administrator on 2017/8/5.
 */
public class Item extends TbItem{

    //将父类中的内容复制到子类中去
    /*
    子类是包含父类的(子类涵盖内容比父类大)  因此子类可以强转为父类  但是父类不能强转为子类！！
    并且子类又不能直接访问父类的成员变量  除非父类定义了公有的成员方法(父类中有这样的公有方法)
    因此  此处我们就可以通过调用父类共有的成员方法实现"将父类内容复制到子类"这个目的！！
    */
    public Item(TbItem tbItem){
        this.setId(tbItem.getId());
        this.setTitle(tbItem.getTitle());
        this.setSellPoint(tbItem.getSellPoint());
        this.setPrice(tbItem.getPrice());
        this.setNum(tbItem.getNum());
        this.setBarcode(tbItem.getBarcode());
        this.setImage(tbItem.getImage());
        this.setCid(tbItem.getCid());
        this.setStatus(tbItem.getStatus());
        this.setCreated(tbItem.getCreated());
        this.setUpdated(tbItem.getUpdated());
    }

    //添加images属性
    public String[] getImages(){
        String image2=this.getImage();
        if(image2!=null&&!"".equals(image2)){
            String[] strings=image2.split(",");
            return strings;
        }
        return null;
    }
}
