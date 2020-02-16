package com.neuedu.service.Impl;

import com.alibaba.druid.util.StringUtils;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.common.TokenCache;
import com.neuedu.dao.UsersDao;
import com.neuedu.pojo.User;
import com.neuedu.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

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
    /**
     * 用户登录
     * */
    @Override
    public ServerResponse login(String username, String password) {
        /**
         * 参数非空判断
         * */
        if (username == null || "".equals(username)){
            return ServerResponse.serverResponseByFail(StatusEnum.USERNAME_NOT_EMPTY.getStatus(),StatusEnum.USERNAME_NOT_EMPTY.getDesc());
        }
        if (password == null || "".equals(password)){
            return ServerResponse.serverResponseByFail(StatusEnum.PASSWORD_NOT_EMPTY.getStatus(),StatusEnum.PASSWORD_NOT_EMPTY.getDesc());
        }
        User user = dao.selectByUsernameAndPassword(username, password);
        if(user == null){
            return ServerResponse.serverResponseByFail(StatusEnum.LOGIN_PARAM_ERROR.getStatus(),StatusEnum.LOGIN_PARAM_ERROR.getDesc());
        }
        if (user .getStatus() == 0){
            return ServerResponse.serverResponseByFail(StatusEnum.USER_DISABLED.getStatus(),StatusEnum.USER_DISABLED.getDesc());
        }
        return ServerResponse.serverResponseBySucess(user);
    }

    /***
     * 检查参数是否有效
     */
    @Override
    public ServerResponse checkValid(String type, String value) {
        //非空判断
        if (value == null || "".equals(value)){
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(),StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        if (type == null || "".equals(type)){
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(),StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        int i = dao.selectByUsernameOrEmailOrPhone(type, value);
        if (i>0 && "username".equals(type)){
            return ServerResponse.serverResponseByFail(StatusEnum.USERNAME_EXISTS.getStatus(),StatusEnum.USERNAME_EXISTS.getDesc());
        }
        //检验是否已存在
        if (i>0 && "email".equals(type)){
            return ServerResponse.serverResponseByFail(StatusEnum.EMAIL_EXISTS.getStatus(),StatusEnum.EMAIL_EXISTS.getDesc());
        }
        if (i>0 && "phone".equals(type)){
            return ServerResponse.serverResponseByFail(StatusEnum.PHONE_EXISTS.getStatus(),StatusEnum.PHONE_EXISTS.getDesc());
        }
        return ServerResponse.serverResponseBySucess("可以使用");
    }
    /**
     * 获取当前登录用户的详细信息
     * */
    @Override
    public ServerResponse selectById(Integer id) {
        //非空判断
        if (id == null || "".equals(id)){
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(),StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        User users = dao.selectByPrimaryKey(id);
        if (users.getStatus() == 0){
            return ServerResponse.serverResponseByFail(StatusEnum.USER_DISABLED.getStatus(),StatusEnum.USER_DISABLED.getDesc());
        }
        users.setPassword(null);
        return ServerResponse.serverResponseBySucess(users);
    }
    /**
     * 登录状态更新个人信息
     * */
    @Override
    public ServerResponse updateInformation(User users) {
        int i = dao.updateByPrimaryKeySelective(users);
        if (i<=0){
            return ServerResponse.serverResponseByFail(StatusEnum.UPDATA_FAILED.getStatus(),StatusEnum.UPDATA_FAILED.getDesc());
        }
        return ServerResponse.serverResponseBySucess(StatusEnum.UPDATE_SUCCESS.getDesc());
    }
    /**
     * 忘记密码
     * */
    @Override
    public ServerResponse forgetGetQuestion(String username) {
        //非空判断
        if (username == null || "".equals(username)){
            return ServerResponse.serverResponseByFail(StatusEnum.USERNAME_NOT_EMPTY.getStatus(),StatusEnum.USERNAME_NOT_EMPTY.getDesc());
        }
        int i = dao.selectByUsernameOrEmailOrPhone("username", username);
        if (i<=0){
            return ServerResponse.serverResponseByFail(StatusEnum.USERNAME_NOT_EXISTS.getStatus(),StatusEnum.USERNAME_NOT_EXISTS.getDesc());
        }
        String s = dao.selectByUsername(username);
        if (StringUtils.isEmpty(s)){
            return ServerResponse.serverResponseBySucess("用户没有设置密码问题");
        }
        return ServerResponse.serverResponseBySucess(s);
    }
    /**
     * 提交问题答案
     * */
    @Override
    public ServerResponse forgetCheckAnswer(String username, String question, String answer) {
        //非空判断
        if (username == null || "".equals(username)){
            return ServerResponse.serverResponseByFail(StatusEnum.USERNAME_NOT_EMPTY.getStatus(),StatusEnum.USERNAME_NOT_EMPTY.getDesc());
        }
        if (question == null || "".equals(question)){
            return ServerResponse.serverResponseByFail(StatusEnum.QUESTION_NOT_EMPTY.getStatus(),StatusEnum.QUESTION_NOT_EMPTY.getDesc());
        }
        if (answer == null || "".equals(answer)){
            return ServerResponse.serverResponseByFail(StatusEnum.ANSER_NOT_EMPTY.getStatus(),StatusEnum.ANSER_NOT_EMPTY.getDesc());
        }
        //数据库查询
        int i = dao.selectByUsernameAndQuestionAndAnswer(username,question,answer);
        //判断问题答案是否匹配
        if(i<=0){
            return ServerResponse.serverResponseByFail(StatusEnum.QUESTION_ANSWER_MISMATCHING.getStatus(),StatusEnum.QUESTION_ANSWER_MISMATCHING.getDesc());
        }
        //产生随机字符令牌
        String token = UUID.randomUUID().toString();
        //把令牌放入缓存中，这里使用的是google的guava缓存后期会使用Redis
        TokenCache.setKey("token_"+username,token);
        return ServerResponse.serverResponseBySucess(token);
    }
    /**
     * 忘记密码的重设密码
     * */
    @Transactional
    @Override
    public ServerResponse forgetResetPassword(String username, String passwordNew, String forgetToken) {
        //非空判断
        if (username == null || "".equals(username)){
            return ServerResponse.serverResponseByFail(StatusEnum.USERNAME_NOT_EMPTY.getStatus(),StatusEnum.USERNAME_NOT_EMPTY.getDesc());
        }
        if (passwordNew == null || "".equals(passwordNew)){
            return ServerResponse.serverResponseByFail(StatusEnum.PASSWORD_NOT_EMPTY.getStatus(),StatusEnum.PASSWORD_NOT_EMPTY.getDesc());
        }
        if (forgetToken == null || "".equals(forgetToken)){
            return ServerResponse.serverResponseByFail(StatusEnum.TOKEN_NOT_EMPTY.getStatus(),StatusEnum.TOKEN_NOT_EMPTY.getDesc());
        }
        //获取缓存中的token
        String token = TokenCache.getKey("token_" + username);
        //判断token是否失效
        if (token == null || "".equals(token)){
            return ServerResponse.serverResponseByFail(StatusEnum.TOKEN_NOT_EMPTY.getStatus(),StatusEnum.TOKEN_NOT_EMPTY.getDesc());
        }
        if (!forgetToken.equals(token)){
            return ServerResponse.serverResponseByFail(StatusEnum.TOKEN_MISMATCHING.getStatus(),StatusEnum.TOKEN_MISMATCHING.getDesc());
        }
        //数据库操作
        int i = dao.updateByUsernameAndPasswordNew(username,passwordNew);
        if (i<=0){
            //进行事务手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.serverResponseByFail(StatusEnum.UPDATA_FAILED.getStatus(),StatusEnum.UPDATA_FAILED.getDesc());
        }
        return ServerResponse.serverResponseBySucess(StatusEnum.UPDATE_SUCCESS.getDesc(),i);
    }
    /**
     * 登录状态中重置密码
     * */
    @Override
    public ServerResponse resetPassword(Integer id, String passwordOld, String passwordNew) {
        return null;
    }
}
