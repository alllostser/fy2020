package com.neuedu.dao;

import com.neuedu.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UsersDao {
    List<User> getUsers();
    int deleteByPrimaryKey(Integer id);
    //用户注册
    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);
    //登录状态更新个人信息
    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //判断参数是否存在
    int selectByUsernameOrEmailOrPhone(@Param("type") String type,@Param("value") String value);
    //用户登录判断
    User selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
    //忘记密码
    String selectByUsername(String username);
    //提交问题答案
    int selectByUsernameAndQuestionAndAnswer(@Param("username")String username, @Param("question")String question,@Param("answer")String answer);
    //根据用户名重设密码
    int updateByUsernameAndPasswordNew(@Param("username")String username, @Param("passwordNew")String passwordNew);
}