package cn.e3mall.service.impl;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/26.
 */



//商品分类管理
@Service   //注意当前的Service注解是spring的注解  不是alibaba的注解！！
public class ItemCatServiceImpl implements ItemCatService{

    //因为是单表查询  所以我们可以直接使用逆向工程生成的mapper
    @Autowired
    private TbItemCatMapper itemCatMapper;


    @Override
    public List<EasyUITreeNode> getItemCatList(long parentId) {
        //根据parentId查询子节点列表
        TbItemCatExample example=new TbItemCatExample();
        TbItemCatExample.Criteria criteria=example.createCriteria();
        //设置查询条件
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbItemCat> list=itemCatMapper.selectByExample(example);
        //因为parentId不是主键  所以我们用的selectByExample()  而不是selectByPrimaryKey()

        //创建返回结果List
        List<EasyUITreeNode> resultList=new ArrayList<>();
        //把列表转换成EasyUITreeNode列表
        for(TbItemCat tbItemCat:list){
            EasyUITreeNode node=new EasyUITreeNode();
            //设置属性
            node.setId(tbItemCat.getId());
            node.setText(tbItemCat.getName());
            //如果节点下面有子节点，state=closed，没有子节点state=open
            node.setState(tbItemCat.getIsParent()?"closed":"open");
            //添加到结果列表
            resultList.add(node);
        }
        //返回结果
        return resultList;
    }
}
