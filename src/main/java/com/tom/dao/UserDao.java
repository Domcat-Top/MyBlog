package com.tom.dao;

import com.tom.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDao {

    // 登录校验
    User checkLogin(User user);


}
