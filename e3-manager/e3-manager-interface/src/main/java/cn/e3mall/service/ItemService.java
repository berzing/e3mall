package cn.e3mall.service;

/**
 * Created by fanghan on 2017/7/11.
 */

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemService {
    //根据商品id取商品信息
    TbItem getItemById(long itemId);
    EasyUIDataGridResult getItemList(int page,int rows);
    E3Result addItem(TbItem item,String desc);
    //根据商品信息取商品描述
    TbItemDesc getItemDescById(long itemId);
}
