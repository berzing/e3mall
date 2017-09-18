package cn.e3mall.content.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbContent;

import java.util.List;

/**
 * Created by Administrator on 2017/7/29.
 */
public interface ContentService {

    E3Result addContent(TbContent content);


    //动态展示首页轮播图
    /*
    动态展示首页轮播图：根据分类id(category_id)查询内容列表(只需要查表tb_content就行  因而dao层可以使用逆向工程生成的代码)
    参数：分类id(category_id)(cid)  List<TbContent>

    注意首页轮播图指的就是"大广告"  由表tb_content_category我们知道对应的id为89(即是表tb_content中字段category_id)
    */
    List<TbContent> getContentListByCid(long cid);
}
