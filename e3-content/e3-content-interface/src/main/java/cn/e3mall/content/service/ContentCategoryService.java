package cn.e3mall.content.service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;

import java.util.List;

/**
 * 内容分类列表展示
 * Created by Administrator on 2017/7/29.
 */


public interface ContentCategoryService {

    List<EasyUITreeNode> getContentCatList(long parentId);

    //因为页面content-category.jsp中就提交了两个参数(30行)  一个parentId一个name  因而形参写作parentId,name
    E3Result addContentCategory(long parentId,String name);
}
