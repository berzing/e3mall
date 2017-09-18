package cn.e3mall.fast;

import cn.e3mall.common.utils.FastDFSClient;
import org.csource.fastdfs.*;
import org.junit.Test;

/**
 * Created by Administrator on 2017/7/27.
 */
public class FastDfsTest {

    //@Test
    public void testUpload() throws Exception{
        //创建一个配置文件。文件名任意。内容就是tracker服务器的地址
        //使用全局对象加载配置文件
        ClientGlobal.init("D:\\IDEA\\e3-manager-web\\src\\main\\resources\\conf\\client.conf");
        //创建一个TrackerClient对象
        TrackerClient trackClient=new TrackerClient();
        //通过TrackerClient获得一个TrackerServer对象
        TrackerServer trackerServer=trackClient.getConnection();
        //创建一个StorageServer的引用  可以是null
        StorageServer storageServer=null;
        //创建一个StorageClient  参数需要TrackerServer和StorageServer
        StorageClient storageClient=new StorageClient(trackerServer,storageServer);
        //为了得到storageClient我们前面写了n步  其实我们可以将前面的步骤写到工具类FastDFSClient中(我们将他放到了e3-common工程中)


        //使用StorageClient上传文件
        String[] strings=storageClient.upload_file("E:\\图片\\图片\\8.jpg","jpg",null);
        for(String string:strings){
            System.out.println(string);
        }
    }


    //测试e3-common中的"工具类"FastDFSClient(对上边方法的一种简化)[推荐!!!]
    //@Test
    public void testFastDFSClient() throws Exception{
        FastDFSClient fastDFSClient=new FastDFSClient("D:\\IDEA\\e3-manager-web\\src\\main\\resources\\conf\\client.conf");
        String string=fastDFSClient.uploadFile("E:\\图片\\图片\\2.jpg");
        System.out.println(string);
    }
}