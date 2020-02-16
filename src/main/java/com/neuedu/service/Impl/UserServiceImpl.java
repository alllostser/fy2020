package com.neuedu.service.Impl;

import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
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

    /**
     * 用户注册
     * */
    @Override
    public ServerResponse register(User user) {
        //参数非空判断
        if (user==null){
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(),StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        //用户名非空判断
        if (user.getUsername()==null || "".equals(user.getUsername())){
            return ServerResponse.serverResponseByFail(StatusEnum.USERNAME_NOT_EMPTY.getStatus(),StatusEnum.USERNAME_NOT_EMPTY.getDesc());
        }
        //密码非空判断
        if (user.getPassword() == null || "".equals(user.getPassword())){
            return ServerResponse.serverResponseByFail(StatusEnum.PASSWORD_NOT_EMPTY.getStatus(),StatusEnum.PASSWORD_NOT_EMPTY.getDesc());
        }
        //邮箱非空判断
        if (user.getEmail() == null || "".equals(user.getEmail())){
            return ServerResponse.serverResponseByFail(StatusEnum.EMAIL_NOT_EMPTY.getStatus(),StatusEnum.EMAIL_NOT_EMPTY.getDesc());
        }
        //手机号非空判断
        if (user.getPhone() == null || "".equals(user.getPhone())){
            return ServerResponse.serverResponseByFail(StatusEnum.PHONE_NOT_EMPTY.getStatus(),StatusEnum.PHONE_NOT_EMPTY.getDesc());
        }
        //密保问题非空判断
        if (user.getQuestion() == null || "".equals(user.getQuestion())){
            return ServerResponse.serverResponseByFail(StatusEnum.QUESTION_NOT_EMPTY.getStatus(),StatusEnum.QUESTION_NOT_EMPTY.getDesc());
        }
        //密保答案非空判断
        if (user.getAnwser() == null || "".equals(user.getAnwser())){
            return ServerResponse.serverResponseByFail(StatusEnum.ANSER_NOT_EMPTY.getStatus(),StatusEnum.ANSER_NOT_EMPTY.getDesc());
        }
        //检查用户名是否存在
        int i = dao.selectByUsernameOrEmailOrPhone("username", user.getUsername());
        if (i>0){
            return ServerResponse.serverResponseByFail(StatusEnum.USERNAME_EXISTS.getStatus(),StatusEnum.USERNAME_EXISTS.getDesc());
        }
        int insert = dao.insert(user);
        if (insert<=0){
            return ServerResponse.serverResponseByFail(StatusEnum.ERROR.getStatus(),StatusEnum.ERROR.getDesc());
        }
        return ServerResponse.serverResponseBySucess("注册成功");
    }
}
