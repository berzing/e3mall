package cn.e3mall.service.impl;


import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;

/**
 * Created by fanghan on 2017/7/11.
 */

/*
* 商品管理service
* */

@Service
public class ItemServiceImpl implements ItemService {

    //注入mapper代理对象
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;//方法addItem要注入的类
    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource//根据id注入(与applicationContext-activemq.xml中id为topicDestination的bean对应！！)
    private Destination topicDestination;
    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_ITEM_PRE}")
    private String REDIS_ITEM_PRE;
    @Value("${ITEM_CACHE_EXPIRE}")
    private Integer ITEM_CACHE_EXPIRE;



    @Override
    public TbItem getItemById(long itemId) {
        //查询缓存
        try{
            String json=jedisClient.get(REDIS_ITEM_PRE+":"+itemId+":BASE");
            //如果查询出来的数据不为空串的话  我们就将它转为TbItem对象
            if(StringUtils.isNotBlank(json)){
                TbItem tbItem=JsonUtils.jsonToPojo(json,TbItem.class);
                return tbItem;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //缓存没有  查询数据库
        //根据主键查询
        //TbItem tbItem=itemMapper.selectByPrimaryKey(itemId);
        TbItemExample example=new TbItemExample();
        TbItemExample.Criteria criteria=example.createCriteria();
        //设置条件查询
        criteria.andIdEqualTo(itemId);
        //执行查询
        List<TbItem> list=itemMapper.selectByExample(example);
        if(list!=null&&list.size()>0){
            //把结果添加到缓存
            try{
                //key-value
                //JsonUtils.objectToJson(list.get(0))  将TbItem对象转换成json串存到缓存中！！
                jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":BASE", JsonUtils.objectToJson(list.get(0)));
                //设置过期时间
                jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":BASE",ITEM_CACHE_EXPIRE);
            }catch (Exception e){
                e.printStackTrace();
            }
            return list.get(0);
        }
        return null;
    }


    //商品列表查询-服务层
    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page,rows);
        //执行查询
        TbItemExample example=new TbItemExample();
        List<TbItem> list=itemMapper.selectByExample(example);
        //创建一个返回值对象
        EasyUIDataGridResult result=new EasyUIDataGridResult();
        result.setRows(list);
        //取分页结果
        PageInfo<TbItem> pageInfo=new PageInfo<>(list);
        //取总记录数
        long total=pageInfo.getTotal();
        result.setTotal(total);
        return result;
    }

    //添加商品
    /*
    由item-add.jsp的商品添加表单itemAddForm知  该表单会提交这些字段到数据库：cid,title,sellPoint,price,num,barcode,image,desc
    其中cid,title,sellPoint,price,num,barcode,image我们可以在表tb_item中找到对应字段   而desc我们可以在表tb_item_desc找到对应字段item_desc
    因而  每添加一个商品我们需要向表tb_item  表tb_item_desc中添加记录

    通常  我们推荐使用pojo来接收表单传递过来的数据

    因而我们只需用一个pojo(即TbItem)和自定义的desc这两个参数做形参就可以接收到表单传过来的所有数据
    [其中image字段不用管(之前专门用了图片服务器处理)]
    */
    @Override
    public E3Result addItem(TbItem item, String desc) {
        //生成商品id(使用工具类IDUtils)
        final long itemId= IDUtils.genItemId();
        //"补全"item的属性(除cid,title,sellPoint,price,num,barcode,image之外的字段)
        item.setId(itemId);
        //1-正常,2-下架,3-删除
        item.setStatus((byte) 1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        //向商品表插入数据
        itemMapper.insert(item);

        //创建一个商品描述表对应的pojo对象
        TbItemDesc itemDesc=new TbItemDesc();
        //补全属性
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        //向商品描述表插入数据
        itemDescMapper.insert(itemDesc);

        //发送商品添加消息(注意事先要注入topicDestination)
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage=session.createTextMessage(itemId+"");
                return textMessage;
            }
        });
        /*
        注意  消息的发送是很快  可能事务还没提交消息就被发送出去了
        此时肯定是搜索不到商品的
        解决1：在ItemAddMessageListener.onMessage()中等待事务提交[Thread.sleep(1000)]
        解决2：在表现层e3-manager-web/ItemController.addItem()中发送消息
        (因为事务是在服务层中  因此当前执行到表现层时事务肯定就被提交)
        */


        //返回成功
        return E3Result.ok();
    }

    @Override
    public TbItemDesc getItemDescById(long itemId) {
        //查询缓存
        try{
            String json=jedisClient.get(REDIS_ITEM_PRE+":"+itemId+":DESC");
            if(StringUtils.isNotBlank(json)){
                TbItemDesc tbItemDesc=JsonUtils.jsonToPojo(json,TbItemDesc.class);
                return tbItemDesc;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        TbItemDesc itemDesc=itemDescMapper.selectByPrimaryKey(itemId);

        //把结果添加到缓存
        try{
            jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":DESC", JsonUtils.objectToJson(itemDesc));
            //设置过期时间
            jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":DESC",ITEM_CACHE_EXPIRE);
        }catch (Exception e){
            e.printStackTrace();
        }

        return itemDesc;
    }
}
