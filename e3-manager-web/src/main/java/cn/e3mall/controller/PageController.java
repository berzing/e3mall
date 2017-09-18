package cn.e3mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转Controller
 * Created by Administrator on 2017/7/25.
 */

@Controller
public class PageController {

    //展示后台首页
    @RequestMapping("/")
    public String showIndex(){
        return "index";
    }

    /*
    * {page}表示你请求那个我就取对应的jsp
    * 新增商品就取item-add.jsp  查询商品就取item-list.jsp  规格参数就取item-param-list.jsp
    * */
    @RequestMapping("/{page}")
    public String showPage(@PathVariable String page){
        return page;
    }
}
