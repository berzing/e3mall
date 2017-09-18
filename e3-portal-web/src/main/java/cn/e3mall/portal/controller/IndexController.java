package cn.e3mall.portal.controller;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 展示首页轮播图Controller
 * Created by Administrator on 2017/7/29.
 *
 * 一般来说  有什么请求先是到Controller  然后由Controller把这个请求转发到jsp
 * jsp需要什么数据  我们可以在Controller中准备好  并不是直接显示的jsp
 */


/*
e3-content前台后台都可以调用  但是展示首页轮播图属于前台的服务  因而我们应该在工程e3-portal-web中写对应的Controller!!!
*/


/*
注意：
Controller指的是类IndexController   而Handler指的是方法showIndex()


此处我们这样写(省略了.html后缀)：@RequestMapping("/index")  因此输入：localhost:8082/index.html我们就可以访问到首页
但是我们想实现输入localhost:8082就可以访问首页  应该怎么做？
1.我们把web.xml中的欢迎页<welcome-file>只一个留index.html
2.输入localhost:8082后会先到webapp中找欢迎页  但是webapp中并没有index.html
3.此时就会去servlet中找  发现web.xml中的欢迎页<welcome-file>中的index.html恰好是.html后缀
该后缀又被前端控制器拦截   因而就会进入控制器IndexController中  进而可以返回一个逻辑视图index.jsp
*/
@Controller
public class IndexController {

    @Value("${CONTENT_LUNBO_ID}")
    private Long CONTENT_LUNBO_ID;//CONTENT_LUNBO_ID与表tb_content_category中id相对应

    @Autowired
    private ContentService contentService;

    @RequestMapping("/index")
    public String showIndex(Model model){
        //查询内容列表
        List<TbContent> ad1List=contentService.getContentListByCid(CONTENT_LUNBO_ID);
        //ad1List与index.jsp中42行对应

        //把结果传递给页面
        model.addAttribute("ad1List",ad1List);
        return "index";
    }
}
