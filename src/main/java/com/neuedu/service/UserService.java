package com.neuedu.service;

import com.neuedu.pojo.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    User selectByPrimaryKey(Integer id);
}
