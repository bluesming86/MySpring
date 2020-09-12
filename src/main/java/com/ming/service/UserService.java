package com.ming.service;

import com.ming.po.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    List<User> queryUsers(Map param);
}
