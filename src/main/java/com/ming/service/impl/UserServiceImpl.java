package com.ming.service.impl;

import com.ming.dao.UserDao;
import com.ming.po.User;
import com.ming.service.UserService;

import java.util.List;
import java.util.Map;

/**
 * @Author ming
 * @time 2020/9/8 16:09
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }

    public List<User> queryUsers(Map param) {
        System.out.println("queryUser");
        return userDao.queryUsers(param);
    }
}
