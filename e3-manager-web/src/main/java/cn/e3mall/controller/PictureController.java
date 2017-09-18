package cn.e3mall.controller;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片上传Controller
 * Created by Administrator on 2017/7/28.
 */

@Controller
public class PictureController {

    //取出配置文件resource.properties中IMAGE_SERVER_URL的值
    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;


    @RequestMapping(value="/pic/upload",produces= MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")//与common.js中第28行对应
    @ResponseBody
    //ResponseBody的作用：直接响应浏览器  不走逻辑视图  相当于使用response对象去调用他的writer方法往浏览器写内容
    //并且他还有一个默认行为：当你返回一个"对象"时  ResponseBody会先将当前对象转成"json"再返回(当你返回的不是对象时就不会转成json)
    //为什么会有这个默认行为：因为浏览器不能直接解析对象！(浏览器可以直接识别字符串！)
    public String uploadFile(MultipartFile uploadFile){//为了使图片上传插件能更好兼容所有浏览器  我们将返回值类型由Map变为String
        try{
            //把图片上传到图片服务器
            FastDFSClient fastDFSClient=new FastDFSClient("classpath:conf/client.conf");
            //取文件扩展名
            String originalFilename=uploadFile.getOriginalFilename();
            String extName=originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            //得到一个图片地址和文件名
            String url=fastDFSClient.uploadFile(uploadFile.getBytes(),extName);

            //补充为完整的url
            url=IMAGE_SERVER_URL+url;
            //图片服务器的地址不应写死  为方便以后的修改 我们将其写到resource.properties配置文件中
            //然后再将该配置文件加载到springmvc.xml中  之后再用@Value注解取出相应值即可

            //封装到map中返回
            Map result=new HashMap<>();
            result.put("error",0);
            result.put("url",url);
            return JsonUtils.objectToJson(result);//使用工具类JsonUtils手工将对象转化为json
        }catch(Exception e){
            e.printStackTrace();
            Map result=new HashMap<>();
            result.put("error",1);
            result.put("message","图片上传失败");
            return JsonUtils.objectToJson(result);
        }
    }
}
