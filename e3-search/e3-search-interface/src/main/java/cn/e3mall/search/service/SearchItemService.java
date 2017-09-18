package cn.e3mall.search.service;

import cn.e3mall.common.utils.E3Result;

/**
 * Created by Administrator on 2017/8/1.
 */

/*
导入商品数据：1.把商品数据查出来  2.遍历商品列表  一条一条把他加到索引库中去  3.返回添加成功
现在查询数据我们可以通过调用mapper  但是怎样将数据写到索引库中去呢？？
我们可以使用solrj将数据写入索引库
*/
public interface SearchItemService {
    //只需要返回成功或者不成功  我们可以用E3Result做返回值
    E3Result importAllItems();
}
