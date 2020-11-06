package com.tom.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author: Tom
 * @create: 2020-11-04 19:19
 * @description: 封装需要写入到数据库的内容
 **/
public class UploadVideo {

    private String name; // 视频的名字
    private String videoId; // 视频的播放ID
    private String playAuth; // 视频的播放凭证
    private String cover; // 封面图

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getPlayAuth() {
        return playAuth;
    }

    public void setPlayAuth(String playAuth) {
        this.playAuth = playAuth;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
