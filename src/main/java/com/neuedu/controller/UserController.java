package com.neuedu.controller;

import com.neuedu.pojo.User;
import com.neuedu.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService service;
    @RequestMapping("/a.do")
    public List<User> geta(){
            return service.getUsers();
        }
    @RequestMapping("/b.do")
    public User getUserById(String id){
        System.out.println(id);
        return service.selectByPrimaryKey(Integer.parseInt(id));
    }
//    @Value("${user.username}")
    private String username;
    @RequestMapping("/c.do")
    public String getUser(){
        System.out.println(username);
        return username;
    }
}
