package cn.e3mall.controller;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 商品分类选择Controller
 * Created by Administrator on 2017/7/26.
 */


@Controller
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping("/item/cat/list")
    @ResponseBody
    //EasyUI的tree控件包含3个属性：id,text,state  当前参数parentId要与其一一对应  因而我们使用注解@RequestParam来给parentId器别名为id  并且给其一个初始值0
    public List<EasyUITreeNode> getItemCatList(@RequestParam(name = "id",defaultValue = "0")Long parentId){
        //调用服务查询节点列表
        List<EasyUITreeNode> list=itemCatService.getItemCatList(parentId);
        return list;
    }
}
