package com.neuedu.service.Impl;

import com.neuedu.dao.UsersDao;
import com.neuedu.pojo.User;
import com.neuedu.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UsersDao dao;

    @Override
    public List<User> getUsers() {
        return dao.getUsers();
    }

    @Override
    public User selectByPrimaryKey(Integer id) {
        return dao.selectByPrimaryKey(id);
    }
}
