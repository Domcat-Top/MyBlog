package com.tom.pojo.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Tom
 * @create: 2020-11-05 09:04
 * @description: 用于封装查询所有的视频时属性
 **/
public class QueryVideo {

    private int id;
    private String name;
    private int view;
    private String videoId;
    private String playAuth;
    private String cover;
    private Date updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public QueryVideo(int id, String name, int view, String videoId, String playAuth, String cover, Date updateTime) {
        this.id = id;
        this.name = name;
        this.view = view;
        this.videoId = videoId;
        this.playAuth = playAuth;
        this.cover = cover;
        this.updateTime = updateTime;
    }

    public QueryVideo() {
    }

    public String formatUpdateTime() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(updateTime);
    }
}

























