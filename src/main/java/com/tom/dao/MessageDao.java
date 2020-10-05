package com.tom.dao;

import com.tom.pojo.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MessageDao {


    // 查询全部的留言内容
    public List<Message> queryAll();

    // 增加一条留言
    public int addMessage(Message message);

    // 分页查询
    public List<Message> queryByPage(@Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize);


}
