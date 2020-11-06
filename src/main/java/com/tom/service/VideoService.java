package com.tom.service;

import com.tom.pojo.Video;
import com.tom.pojo.vo.DescVideo;
import com.tom.pojo.vo.PlayVideo;
import com.tom.pojo.vo.QueryVideo;
import com.tom.pojo.vo.UploadVideo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: Tom
 * @create: 2020-11-04 19:38
 * @description:
 **/
public interface VideoService {

    // 上传视频到阿里云，最后写入到数据库，所以这里这样创建了
    int addInfoAboutVideo(UploadVideo uploadVideo);

    // 查询所有的视频信息----我的想法一共是上传四个视频，显示在首页上面
    List<QueryVideo> queryAllVideo();

    // 删除视频
    int deleteByVideoId(@Param("videoId") String videoId);

    // 通过VideoId查询出这一条数据-----因为阿里云视频储存中已经给确定好了，只会有唯一的ID，所以这里不用担心不是主键会冲突
    Video queryByVideoId(@Param("videoId") String videoId);

    // 倒序查询上传的电-------主要就是放置在首页上
    List<DescVideo> queryIndexVideo();

    // 通过cover查询出需要的数据
    PlayVideo queryByCover(String cover);




    // 然后如果有机会的话，写一个爬虫
    // 整合其他盗版网站的搜索功能和播放功能，用我自己的服务器来转播和播放





}
