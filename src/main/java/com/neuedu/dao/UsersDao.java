package com.neuedu.dao;

import com.neuedu.pojo.User;

import java.util.List;

public interface UsersDao {
    List<User> getUsers();
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}