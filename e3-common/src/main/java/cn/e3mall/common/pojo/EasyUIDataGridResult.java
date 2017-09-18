package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/26.
 */


/*
* Easyui中datagrid控件要求的数据格式为：{total:”2”,rows:[{“id”:”1”,”name”:”张三”},{“id”:”2”,”name”:”李四”}]}
* 现在描述商品信息的pojo已由逆向工程生成   但是我们并没有包含total,rows的pojo
* 所以要手工写一个包含total,rows的pojo
* 又因为工程e3-manager,e3-manager-web都会用到此pojo  因而我们应该将此pojo放到工程e3-common中
* */

//因为要进行网络传输(要从服务层传到表现层)  所以需要实现序列化接口！！
public class EasyUIDataGridResult implements Serializable{

    private long total;
    //List既可能放商品列表又可能用户列表  所以就不加泛型  这样加啥都行
    private List rows;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
