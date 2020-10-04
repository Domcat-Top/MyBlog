package com.tom.service;

import com.tom.pojo.Message;

import java.util.List;

public interface MessageService {

    // 查询全部的留言内容
    public List<Message> queryAll();

    // 增加一条留言
    public int addMessage(Message message);
}
