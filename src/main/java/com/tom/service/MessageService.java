package com.tom.service;

import com.tom.pojo.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageService {

    // 查询全部的留言内容
    public List<Message> queryAll();

    // 增加一条留言
    public int addMessage(Message message);

    // 分页查询
    public List<Message> queryByPage(@Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize);
}
