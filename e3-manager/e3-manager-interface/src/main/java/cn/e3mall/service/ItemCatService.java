package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyUITreeNode;

import java.util.List;

/**
 * Created by Administrator on 2017/7/26.
 */
public interface ItemCatService {

    //根据parentId去查节点列表
    List<EasyUITreeNode> getItemCatList(long parentId);
}
