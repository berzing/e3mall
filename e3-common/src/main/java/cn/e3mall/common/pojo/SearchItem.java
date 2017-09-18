package cn.e3mall.common.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/31.
 */


/*
我们应该用一个pojo来接收查询出来的数据  没有现成的pojo  我们自己可以写一个pojo(SearchItem)

那么这个pojo(SearchItem)应该放在哪里？？
注意  我们是从商品表中把所有数据都取出来后就将其放到索引库中  将来我们从索引库中查完数据后还是用的当前pojo来接收数据
而且表现层也会调用商品服务 查询商品列表  因而这个pojo表现层也会用到
因此  我们应该将这个pojo放到e3-common工程中去
*/
//要想支持表现层与服务层之间的通信  我们要实现序列化接口
public class SearchItem implements Serializable{
    private String id;
    private String title;
    private String sell_point;
    private long price;
    private String image;
    private String category_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSell_point() {
        return sell_point;
    }

    public void setSell_point(String sell_point) {
        this.sell_point = sell_point;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }


    /*
    现在我们搜索出了我们添加的商品  但是我们看不到商品图片  为什么呢？？
    f12  我们可以看到链接是用逗号分割的图片列表  我们把所有图片都取出来了  并且我们把它们全都放到了同一个链接中  这样商品图片肯定查看不了！！
    解决：取一张图片就可以了
    只取一张图片思路：
    1.从数据库往索引库中导数据的时候  只取一张图片
    2.从索引库中查出来  封装到商品对应的pojo(SearchItem)里面时  只取一张图片
    3.在页面(search.jsp)中  我们只取一张图片
    */

    /*
    编写方法getImages()后  就相当于创建了一个images属性
    当我访问属性images时默认会调用getImages()方法
    而调用该方法会返回一个数组  这时候我只取一张图片就行！！
    所以  我们看到页面(search.jsp)第53行会这样写：${item.images[0] }
    */
    public String[] getImages(){
        //有图片就返回一个数组  没图片就返回null
        if(image!=null && !"".equals(image)){
            String[] strings=image.split(",");
            return strings;
        }
        return null;
    }
}
