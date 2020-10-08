package com.tom.pojo;


import lombok.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Blog implements Serializable {

    public int id;
    public String blogName;
    public String label;
    public String introduction;
    public String blogContent;
    public int view;
    public int version;
    public int deleted;
    public Date create_time;
    public Date update_time;
    public String url;
    public String comeForm;

    public Blog() {

    }

    public Blog(String blogName, String label, String introduction, String blogContent, String url, String comeForm) {
        this.blogName = blogName;
        this.label = label;
        this.introduction = introduction;
        this.blogContent = blogContent;
        this.url = url;
        this.comeForm = comeForm;
    }

    // 修改博客用到的构造器
    public Blog(int id, String blogName, String label, String introduction, String blogContent, String url, String comeForm) {
        this.id = id;
        this.blogName = blogName;
        this.label = label;
        this.introduction = introduction;
        this.blogContent = blogContent;
        this.url = url;
        this.comeForm = comeForm;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Blog(String blogName, String label, String introduction, String blogContent) {
        this.blogName = blogName;
        this.label = label;
        this.introduction = introduction;
        this.blogContent = blogContent;
    }

    public int getId() {
        return id;
    }

    public String getBlogContent() {
        return blogContent;
    }

    public void setBlogContent(String blogContent) {
        this.blogContent = blogContent;
    }

    public String formatCreateTime() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(create_time);
    }
    public String formatUpdateTime() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(update_time);
    }



}
