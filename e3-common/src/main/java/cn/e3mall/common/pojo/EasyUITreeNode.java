package cn.e3mall.common.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/26.
 */


/*
展示商品分类列表，使用EasyUI的tree控件展示
返回值：json。数据格式
[{
    "id": 1,
    "text": "Node 1",
    "state": "closed"
},{
    "id": 2,
    "text": "Node 2",
    "state": "closed"
}]
(state：如果节点下有子节点“closed”，如果没有子节点“open”)
创建一个pojo来描述tree的节点信息，包含三个属性id、text、state。因为多个工程需要用到当前pojo，所以我们将其放到e3-common工程中
*/


//用当前的类可以表示Tree中的一个节点
//实现序列化接口
public class EasyUITreeNode implements Serializable{

    private long id;
    private String text;
    private String state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
