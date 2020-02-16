package com.neuedu.controller;


import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.User;
import com.neuedu.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService service;
    /**
     * 用户注册
     * */
    @RequestMapping(value = "/register.do",method = RequestMethod.POST)
    public ServerResponse register(User user){
        ServerResponse sr = service.register(user);
        return sr;
    }

}
