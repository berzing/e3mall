package cn.e3mall.search.service;

import cn.e3mall.common.pojo.SearchResult;

/**
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
public interface SearchService {
    /*
    SearchResult就是我们自己定义的pojo
    (参数  查询条件--keyword,分页条件--page(页码),rows(每页显示的记录数))
    因为是服务层  所以每页显示的记录数不应该写死  应该作为参数出现
    因此  start=(page-1)*rows
    */
    SearchResult search(String keyword,int page,int rows) throws Exception;
}
