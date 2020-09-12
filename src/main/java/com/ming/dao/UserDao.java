package com.ming.dao;

import com.ming.po.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    List<User> queryUsers(Map param);
}
