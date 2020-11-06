package com.tom.pojo.vo;

/**
 * @author: Tom
 * @create: 2020-11-05 21:00
 * @description:
 **/
public class PlayVideo {

    private String videoId;
    private String playAuth;
    private String cover;

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
