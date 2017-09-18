package cn.e3mall.search.message;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 监听商品添加消息  接收消息后  "将对应的商品信息同步到索引库!!!!!!!!"
 * Created by Administrator on 2017/8/5.
 */


/*
添加一个商品  你把商品id告诉我就行  我就可以通过商品id将新添加的商品同步到索引库
也就是说消息中只需要传递一个商品id就行！！
e3-manager-service中的ItemServiceImpl.addItem()发出消息后
我们应该在e3-search-service中的MyMessageListener.onMessage()监听消息  并取出商品id
现在我们就可以用取出的商品id去数据库中查这个商品  可以使用逆向工程生成的代码吗？？
不能！！因为导入索引库的mapper(ItemMapper.xml)查询时会涉及到两个表
*/
public class ItemAddMessageListener implements MessageListener{

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrServer solrServer;

    @Override
    public void onMessage(Message message) {
        try{
            //从消息中取商品id
            TextMessage textMessage= (TextMessage) message;
            String text=textMessage.getText();
            Long itemId=new Long(text);
            //等待事务提交
            Thread.sleep(1000);
            //根据商品id查询商品信息
            SearchItem searchItem=itemMapper.getItemById(itemId);
            //创建一个文档对象
            SolrInputDocument document=new SolrInputDocument();
            //向文档对象中添加域
            document.addField("id",searchItem.getId());
            document.addField("item_title",searchItem.getTitle());
            document.addField("item_sell_point",searchItem.getSell_point());
            document.addField("item_price",searchItem.getPrice());
            document.addField("item_image",searchItem.getImage());
            document.addField("item_category_name",searchItem.getCategory_name());
            //把文档写入索引库
            solrServer.add(document);
            //提交
            solrServer.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
