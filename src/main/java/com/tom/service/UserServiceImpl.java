package com.tom.service;

import com.tom.dao.UserDao;
import com.tom.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public User checkLogin(User user) {
        return userDao.checkLogin(user);
    }
}
