package cn.e3mall.controller;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 内容管理Controller
 * Created by Administrator on 2017/7/29.
 */

@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;

    //与content-add.jsp中69行中的"/content/save"相对应
    @RequestMapping(value = "/content/save",method = RequestMethod.POST)
    @ResponseBody
    public E3Result addContent(TbContent content){//使用pojo接收表单中的数据
        //调用服务把内容数据保存到数据库
        E3Result e3Result=contentService.addContent(content);
        return e3Result;
    }
}
