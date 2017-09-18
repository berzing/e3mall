package cn.e3mall.search.mapper;

import cn.e3mall.common.pojo.SearchItem;

import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */

/*
把商品数据导入到索引库中:

思路：要从数据库中将商品数据查询出来  再将其导入到索引库
商品数据由两个表组成  商品表(tb_item)与商品分类表(tb_item_cat)
因此需要做表连接  因此逆向工程生成的pojo就用不了了  需要自己写mapper

那么自己写的mapper应该放哪？
为什么不能放在e3-manager-dao？
因为其实工程e3-manager-dao和工程e3-search-service就像两个团队  一个做商品一个做搜索  做搜索的团队不应该将文件添加到做商品的团队中
因此  搜索团队可以使用商品团队的提供的服务  但是当我想要的功能别的团队提供不了的时候  我就在"自己的工程"里面写出想要的功能
因而我们将自己写的mapper放到工程e3-search-service下
(service层不能包含dao??注意e3-search-service是service工程  并不是纯粹的service层  因而是可以包含dao的！！)



我们应该用一个pojo来接收查询出来的数据  没有现成的pojo  我们自己可以写一个pojo(SearchItem)

那么这个pojo(SearchItem)应该放在哪里？？
注意  我们是从商品表中把所有数据都取出来后就将其放到索引库中  将来我们从索引库中查完数据后还是用的当前pojo来接收数据
而且表现层也会调用商品服务 查询商品列表  因而这个pojo表现层也会用到
因此  我们应该将这个pojo放到e3-common工程中去
*/

public interface ItemMapper {


    List<SearchItem> getItemList();

    //添加商品并同步到索引库
    /*
    添加一个商品  你把商品id告诉我就行  我就可以通过商品id将新添加的商品同步到索引库
    也就是说消息中只需要传递一个商品id就行！！
    e3-manager-service中的ItemServiceImpl.addItem()发出消息后
    我们应该在e3-search-service中的MyMessageListener.onMessage()监听消息  并取出商品id
    现在我们就可以用取出的商品id去数据库中查这个商品  可以使用逆向工程生成的代码吗？？
    不能！！因为导入索引库的mapper(ItemMapper.xml)查询时会涉及到两个表
    */
    SearchItem getItemById(long itemId);
}
