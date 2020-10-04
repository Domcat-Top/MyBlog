package com.tom.pojo;


import lombok.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {

    private int id;
    private String nickName;
    private String userName;
    private String email;
    private String content;
    private Date create_time;

    public Message() {
    }

    public Message(String nickName, String email, String content) {
        this.nickName = nickName;
        this.email = email;
        this.content = content;
    }

    public Message(int id, String nickName, String userName, String email, String content, Date create_time) {
        this.id = id;
        this.nickName = nickName;
        this.userName = userName;
        this.email = email;
        this.content = content;
        this.create_time = create_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", content='" + content + '\'' +
                ", create_time=" + create_time +
                '}';
    }

    public String formatCreateTime() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(create_time);
    }



}
