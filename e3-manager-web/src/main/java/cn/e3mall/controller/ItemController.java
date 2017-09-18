package cn.e3mall.controller;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by fanghan on 2017/7/11.
 */

/*
* 商品管理Controller
* */

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable long itemId){
        TbItem tbItem=itemService.getItemById(itemId);
        return tbItem;
    }

    //商品列表查询-表现层
    @RequestMapping("/item/list")
    @ResponseBody//有了@ResponseBody注解后  返回的对象会自动转换成json
    public EasyUIDataGridResult getItemList(Integer page,Integer rows){
        //调用服务查询商品列表
        EasyUIDataGridResult result=itemService.getItemList(page,rows);
        return result;
    }


    /*商品添加功能*/
    @RequestMapping(value = "/item/save",method = RequestMethod.POST)
    @ResponseBody
    public E3Result addItem(TbItem item,String desc){
        E3Result result=itemService.addItem(item, desc);
        return result;
    }
}
