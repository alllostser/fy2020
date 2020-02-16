package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    User selectByPrimaryKey(Integer id);
    /**
     * 用户注册
     * */
    ServerResponse register(User user);
}
