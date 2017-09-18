package cn.e3mall.search.controller;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 商品搜索Controller(参见search.jsp)
 * Created by Administrator on 2017/8/2.
 */


/*
搜索的结果页面的展示要单独分出来----搜索工程(e3-search-web)
商品搜索的结果我们应该封装到一个对象中去(可以使用我们在e3-common中定义的pojo--SearchItem)
因此  我们的返回值就可以是List<SearchItem>


另外  由search.jsp可知  我们还需要totalPages(总页数),page(当前页),recourdCount(总记录数),itemList(商品列表)
现在我们可以取到recourdCount(总记录数),itemList(商品列表)
其中  总页数(totalPages)=总记录数(recourdCount)/单页记录数
然后每页显示的记录数可以由我们自己定(service层需要传参数  表现层我们可以自定义一个数)
因此  我们要响应的结果应该包含3个属性：总页数(totalPages),总记录数(recourdCount),商品列表(itemList)
所以我们应该将这些结果封装到pojo(SearchResult)中去


又因为服务层与表现层都会用到该pojo(SearchResult)  所以我们将这个pojo放到e3-common中会比较合适
*/
@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;
    @Value("${SEARCH_RESULT_ROWS}")
    private Integer SEARCH_RESULT_ROWS;

    @RequestMapping("/search")
    /*
    "/search"怎么得到的？？我们进入商城首页  然后点击搜索  就会跳转到一个新页面  这个新页面的url就是"/search.html;keyword=手机"
    又web.xml中的拦截形式是"*.html"  因而此处的url就写做"/search"  然后方法的参数中至少要包含一个keyword
    */
    public String searchItemList(String keyword,
                                 @RequestParam(defaultValue = "1") Integer page, Model model) throws Exception {
        //keyword(关键字),page(当前页面页码),model(相当于request)

        //解决乱码问题
        keyword=new String(keyword.getBytes("iso-8859-1"),"utf-8");
        //查询商品列表
        SearchResult searchResult=searchService.search(keyword,page,SEARCH_RESULT_ROWS);
        //把结果传递给页面(参考search.jsp)
        model.addAttribute("query",keyword);
        model.addAttribute("totalPages",searchResult.getTotalPages());
        model.addAttribute("page",page);
        model.addAttribute("recordCount",searchResult.getRecordCount());
        model.addAttribute("itemList",searchResult.getItemList());
        //异常测试
        //int a=1/0;
        //返回逻辑视图(也正是因为要返回逻辑视图  所以我们的返回值类型就是String)
        return "search";
    }
}
