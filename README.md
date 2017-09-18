## 宜立方购物商城
### 项目简介
系统架构：基于SOA架构，分布式系统架构

开发环境： idea+maven+svn

软件架构： mysql+spring+springmvc+mybatis+nginx+redis+solrJ

项目描述：该商城是一个综合性的B2C平台，类似京东商城。商城采用基于SOA的分布式系统架构，子系统之间都是调用服务来实现系统之间的通信，使用http协议传递json
数据方式实现。降低了系统之间的耦合度，提高了系统的扩展性。为了提高系统的性能使用redis做系统缓存，并使用redis实现session共享。为了保证redis的性能使用redis的集群。搜索功能使用solrCloud做搜索引擎。

系统主要包括以下模块：

后台管理系统：管理商品、订单、类目、商品规格属性、用户管理以及内容发布等功能。

前台系统：用户可以在前台系统中进行注册、登录、浏览商品、首页、下单等操作。

搜索系统：提供商品的搜索功能。

单点登录系统：为多个系统之间提供用户登录凭证以及查询登录用户的信息。

购物车系统：未登录状态也可以添加商品到购物车

### 工程简介
e3-cart-web,e3-cart 购物车系统

e3-common 工具类工程

e3-content 商城首页内容管理系统

e3-item-web 商品详情页面

e3-manager-web,e3-manager 后台管理系统

e3-order-web,e3-order 订单系统

e3-parent 父工程

e3-portal-web 商城首页系统

e3-search-web,e3-search 搜索系统

e3-sso-web,e3-sso 单点登录系统

fastdfs_client 图片上传服务器

generatorSqlmapCustom mybatis逆向工程

pagehelper mybatis分页插件
