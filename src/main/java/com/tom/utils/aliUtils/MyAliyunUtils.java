package com.tom.utils.aliUtils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.tom.pojo.Video;
import com.tom.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @author: Tom
 * @create: 2020-11-06 07:43
 * @description: 由于视频的凭证会过期，所以这里自己写一个方法，来获取视频的凭证----这里数据库设计的缺陷就出现了，没考虑到凭证会过期，所以，那一条字段是没用的
 **/
public class MyAliyunUtils {


    // 根据视频的ID获取到视频的凭证---这个凭证会过期
    public static String getAliyunPlayAuth(String videoId) throws ClientException {

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
        String playAuth = res.getPlayAuth();

        // 判断一下是否获取成功了，成功返回，失败返回null
        if(StringUtils.isEmpty(playAuth)) {
            return null;
        } else {
            return playAuth;
        }

    }
}
