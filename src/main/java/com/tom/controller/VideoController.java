package com.tom.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.tom.pojo.Video;
import com.tom.pojo.vo.QueryVideo;
import com.tom.pojo.vo.UploadVideo;
import com.tom.service.BlogService;
import com.tom.service.MessageService;
import com.tom.service.VideoService;
import com.tom.utils.aliUtils.ConstantPropertiesUtils;
import com.tom.utils.aliUtils.ConstantVodUtils;
import com.tom.utils.aliUtils.InitObjectVodClient;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * @author: Tom
 * @create: 2020-11-04 19:14
 * @description: 用于操作视频的上传和查询所用
 **/
@Controller
public class VideoController {

    // 博客总数
    private static int blogSize;
    // 留言总数
    private static int messageSize;
    // 访问总数
    private static int totalView;


    @Autowired
    private VideoService videoService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private BlogService blogService;


    // 去往视频管理页面
    @GetMapping("/admin/toVideo")
    public String toVideo(Model model) {

        // 先把页脚三兄弟发送了再说
        blogSize = blogService.queryAll().size();
        totalView = blogService.getTotalView();
        messageSize = messageService.queryAll().size();
        model.addAttribute("blogSize", blogSize);
        model.addAttribute("totalView", totalView);
        model.addAttribute("messageSize", messageSize);

        List<QueryVideo> queryVideoList = videoService.queryAllVideo();
        if(queryVideoList.size() == 0) { // 之前没上传过视频，现在数据库还是空的
            // 直接不显示了，不然会报错的，它会循环失败
            model.addAttribute("show", false);
            model.addAttribute("size", 0); // 控制下面循环的条件，直接不循环了
            model.addAttribute("queryVideoList", queryVideoList); // 现在他是空的，但是还是传递一下，仅仅是为了前端不报错
            return "admin/videoInfo";
        }
        // 往下走，说明数据库有内容，需要重新传递
        model.addAttribute("show", true);
        model.addAttribute("size", queryVideoList.size() - 1); // 这样前端统一操作，不需要前端再去 - 1
        model.addAttribute("queryVideoList", queryVideoList);
        return "admin/videoInfo";
    }


    /**
     * 暂时不知道怎么从底层写，就直接引入从上往下写，先写文件的上传，然后再规范数据库的方法吧
     * @param files 现在还不确定，但是总体来说，应该包含两个，一个是封面图（下标为0），一个是视频文件（下标为1）
     * @param r 请求剩下的信息---标题
     * @return 返回到列表页面----这样写会麻烦一点
     */
    @PostMapping("/admin/uploadFile")
    @Transactional
    public String uploadFile(MultipartFile[] files, HttpServletRequest r) {

        // 先把前端获取到的图片上传到OSS服务中
        MultipartFile pictureFile = files[0];

        // 工具类获取值
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        // 事先定义好返回的URL参数
        String url = null;

        try {
            // 创建OSS实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            //获取上传文件输入流
            InputStream inputStream = pictureFile.getInputStream();

            // 获取文件名称
            String filename = pictureFile.getOriginalFilename();

            // 在文件名称里需要添加一个随机值----并且吧UUID中的-替换为“”，也就是说，祛除了UUID中的-，使整个名称更加通顺一点
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            filename = uuid + filename;

            // 规范一下上传到哪个文件中----就是在他前面，给他加上一层目录结构
            filename = "videoPicture/" + filename;

            // 调用OSS方法实现上传
            // 第一个参数  Bucket名称
            // 第二个参数 上传到OSS文件路径和文件名称
            // 第三个参数：上传文件输入流
            ossClient.putObject(bucketName, filename, inputStream);

            // 关闭OSSClient
            ossClient.shutdown();

            // 返回上传文件之后的路径
            // 需要把上传到阿里云OSS路径手动拼接出来
            url = "https://" + bucketName + "." + endpoint + "/" + filename;
        }catch(Exception e) {
            e.printStackTrace();
        }

        // 再进行上传视频的操作
        MultipartFile videoFile = files[1];
        String videoId = null; // 事先定义好，否则的话，无法set进去
        String playAuth = null; // 事先定义好，否则的话，无法set进去
        try {
            //accessKeyId, accessKeySecret
            //fileName：上传文件原始名称
            String fileName = videoFile.getOriginalFilename();
            //title：上传之后显示名称
            String title = fileName.substring(0, fileName.lastIndexOf("."));
            //inputStream：上传文件输入流
            InputStream inputStream = videoFile.getInputStream();
            UploadStreamRequest request = new UploadStreamRequest(ConstantVodUtils.ACCESS_KEY_ID,ConstantVodUtils.ACCESS_KEY_SECRET, title, fileName, inputStream);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);


            if (response.isSuccess()) {
                videoId = response.getVideoId();
            } else { // 直接给劳资报错就完事了，别BB
                return "500";
            }

            // 创建初始化对象
            DefaultAcsClient client =
                    InitObjectVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            // 创建获取凭证的request和response对象
            GetVideoPlayAuthRequest req = new GetVideoPlayAuthRequest();

            // 给request设置视频的id
            req.setVideoId(videoId);

            // 调用方法得到凭证
            GetVideoPlayAuthResponse res = client.getAcsResponse(req);
            // 获取到凭证----至此，所有上传操作完毕
            playAuth = res.getPlayAuth();

            // 判断一下是否上传成功了，都上传成功了，则写入到数据库中，否则的话，啥都玩蛋了
            if(StringUtils.isEmpty(playAuth)) {
                return "404";
            }
        }catch(Exception e) {
            e.printStackTrace();
        }

        String name = r.getParameter("name");
        // 接下来，把所有的信息绑定到一起，写入到数据库中即可
        UploadVideo uploadVideo = new UploadVideo();
        uploadVideo.setCover(url);
        uploadVideo.setName(name);
        uploadVideo.setPlayAuth(playAuth);
        uploadVideo.setVideoId(videoId);
        // 封装数据完毕，写入到数据库
        int result = videoService.addInfoAboutVideo(uploadVideo);
        // 最后再次判断一次，如果成功了，则返回，否则的话，数据回滚，不给他写入
        if(result >= 1) { // 跳转到列表页面
            return "redirect:/admin/toVideo";
        } else {
            // 抛出异常，再次完蛋
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
                return "500";
            }
        }
    }

    /**
     *
     * 通过前端获取到的参数，来删除视频，这个ID对应的是阿里云视频点播服务的某个视频的ID
     *
     * @return ： 删除完毕后返回到列表页面，继续管理
     */
    @Transactional // 上个保险---虽然功能性不强
    @RequestMapping("/admin/deleteVideo")
    public String deleteVideo(HttpServletRequest r) throws Exception {

        String videoId = r.getParameter("videoId"); // 获取到前端传递的参数

        // 先通过videoId查询出整个数据---包含封面图和videoId
        Video video = videoService.queryByVideoId(videoId);

        // 先删除视频
        try {
            // 初始化对象
            DefaultAcsClient client = InitObjectVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            // 创建删除视频的request对象
            DeleteVideoRequest request = new DeleteVideoRequest();

            // 向request设置视频的ID
            request.setVideoIds(videoId);
            // 调用初始化对象的方法，实现删除操作
            client.getAcsResponse(request);

        } catch(Exception e) {
            throw new Exception();
        }

        // 再来删除封面图
        String coverUrl = video.getCover();
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ConstantPropertiesUtils.END_POINT;
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;
        // Object所在存储空间的完整名称，即包含文件后缀在内的完整路径，例如example/test.jpg。
        // 获取到封面图的名称-----利用Java内置的字符串裁剪工具类
        String objectName = coverUrl.substring(52);
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 删除指定版本的Object。
        ossClient.deleteObject(bucketName, objectName);
        // 关闭OSSClient。
        ossClient.shutdown();

        // 最后删除数据库中的数据
        int i = videoService.deleteByVideoId(videoId);
        if(i >= 1) { // 删除成功---跳转到列表页面
            return "redirect:/admin/toVideo";
        } else { // 给劳资报错500吧
            return "500";
        }
    }
}



















