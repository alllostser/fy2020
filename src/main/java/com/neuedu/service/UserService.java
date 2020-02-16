package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.User;

import java.util.List;

public interface UserService {
      //用户注册
    ServerResponse register(User user);
    //用户登录
    ServerResponse login(String username, String password);
    //检查参数是否有效
    ServerResponse checkValid(String type, String value);
    //获取当前登录用户的详细信息
    ServerResponse selectById(Integer id);
    //登录状态更新个人信息
    ServerResponse updateInformation(User users);
    //忘记密码
    ServerResponse forgetGetQuestion(String username);
    //提交问题答案
    ServerResponse forgetCheckAnswer(String username, String question, String answer);
    //忘记密码的重设密码
    ServerResponse forgetResetPassword(String username, String passwordNew, String forgetToken);
    //登录状态中重置密码
    ServerResponse resetPassword(String username, String passwordOld, String passwordNew);
}
