package com.tom.service;

import com.tom.pojo.User;

public interface UserService {


    // 登录校验
    User checkLogin(User user);
}
