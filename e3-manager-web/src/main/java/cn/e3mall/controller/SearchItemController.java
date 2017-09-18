package cn.e3mall.controller;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 导入商品数据到索引库
 * Created by Administrator on 2017/8/2.
 */

@Controller
public class SearchItemController {

    @Autowired
    private SearchItemService searchItemService;

    @RequestMapping("/index/item/import")//参见index-item.jsp
    @ResponseBody
    public E3Result importItemList(){
        E3Result e3Result=searchItemService.importAllItems();
        return e3Result;
    }

}
