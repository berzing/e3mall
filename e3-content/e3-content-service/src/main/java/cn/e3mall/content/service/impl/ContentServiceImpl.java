package cn.e3mall.content.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 内容管理Service
 * Created by Administrator on 2017/7/29.
 */

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${CONTENT_LIST}")
    private String CONTENT_LIST;

    @Override
    public E3Result addContent(TbContent content) {
        //将内容数据插入到内容表
        content.setCreated(new Date());
        content.setUpdated(new Date());
        //插入到数据库
        contentMapper.insert(content);
        /*
        怎样同步缓存？
        对内容信息做增删改操作后只需要把对应缓存删除即可！！

        注意!!!  删除缓存时应该精确删除  应该将缓存对应的fields(此处的fields指的就是cid)删除  而不能将整个hash都删了！！
        */
        //CONTENT_LIST为hash的key,content.getCategoryId().toString()就是cid  也就是也就是hash中的fields
        //hash做缓存  [key-(fields-values)]
        jedisClient.hdel(CONTENT_LIST,content.getCategoryId().toString());
        return E3Result.ok();
    }


    //动态展示首页轮播图
    //根据内容分类id查询内容列表
    /*
    应该在表现层加redis缓存还是在服务层加呢
    应该在服务层加  这样所有调用该服务的工程就都会用到当前服务层的缓存！！

    此处我们使用hash做缓存  [key-(fields-values)](相当于一个key对应一个map  map中还有key-value  此处的fields就相当于map中的key!!)
    由89行可知  此处key为CONTENT_LIST,fields为cid+"", values为JsonUtils.objectToJson(list)
    */
    @Override
    public List<TbContent> getContentListByCid(long cid) {
        //查询缓存
        /*添加缓存不应该影响正常的业务逻辑  因而我们应该使用一个try...catch捕获添加缓存时的异常*/
        try{
            //如果缓存中有  直接响应结果
            String json=jedisClient.hget(CONTENT_LIST,cid+"");
            //若取出来的json不为空  就将其转化为List类型
            if(StringUtils.isNotBlank(json)){
                List<TbContent> list=JsonUtils.jsonToList(json,TbContent.class);
                return list;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //如果没有查询数据库
        TbContentExample example=new TbContentExample();
        TbContentExample.Criteria criteria=example.createCriteria();
        //设置查询条件
        criteria.andCategoryIdEqualTo(cid);
        //执行查询
        List<TbContent> list=contentMapper.selectByExampleWithBLOBs(example);
        //为啥选择方法selectByExampleWithBLOBs()  可见TbContentMapper.xml

        //返回结果之前应该先把结果添加到缓存(这样下次就可以直接走缓存)
        try{
            //redis中只能存放字符串  因而要将cid转为String(cid+"")  也要将list转成String(JsonUtils.objectToJson(list))
            jedisClient.hset(CONTENT_LIST,cid+"", JsonUtils.objectToJson(list));
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }
}
