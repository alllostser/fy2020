package com.neuedu.controller.front;


import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.pojo.User;
import com.neuedu.pojo.vo.UsersVo;
import com.neuedu.service.UserService;
import com.neuedu.utils.TimeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


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


    /**
     * 用户登录
     * */
    @RequestMapping(value = "/login.do",method = RequestMethod.POST)
    public ServerResponse login(String username, String password, HttpSession session){
        ServerResponse sr = service.login(username, password);
        User user = (User) sr.getData();
        UsersVo usersVo = new UsersVo();
        session.setAttribute(StatusEnum.LOGIN_USER, user);
        //将user对象转换为vo对象返回
        if (sr.getData() != null) {
            usersVo.setId(user.getId());
            usersVo.setUsername(user.getUsername());
            usersVo.setNickname(user.getNickname());
            usersVo.setEmail(user.getEmail());
            usersVo.setPhone(user.getPhone());
            usersVo.setCreateDate(TimeUtils.dateToStr(user.getCreateDate()));
            usersVo.setUpdateDate(TimeUtils.dateToStr(user.getUpdateDate()));
            usersVo.setLoginNumber(user.getLoginNumber());
            sr.setData(usersVo);
            return sr;
        }
        return sr;
    }

    /**
     * 检查参数是否有效
     *
     * @param value
     * @param type
     * @return
     */
    @RequestMapping(value = "/check_valid.do", method = RequestMethod.GET)
    public ServerResponse checkValid(String type, String value) {
        ServerResponse rs = service.checkValid(type, value);
        return rs;
    }

    /**
     * 获取登录用户信息
     *
     * */
    @RequestMapping(value = "/get_user_info.do", method = RequestMethod.GET)
    public ServerResponse getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(StatusEnum.LOGIN_USER);
        if (user == null) {
            return ServerResponse.serverResponseByFail(StatusEnum.USER_NOT_LOGIN.getStatus(),StatusEnum.USER_NOT_LOGIN.getDesc());
        }
        UsersVo usersVo = new UsersVo();
        usersVo.setId(user.getId());
        usersVo.setUsername(user.getUsername());
        usersVo.setNickname(user.getNickname());
        usersVo.setEmail(user.getEmail());
        usersVo.setPhone(user.getPhone());
        usersVo.setCreateDate(TimeUtils.dateToStr(user.getCreateDate()));
        usersVo.setUpdateDate(TimeUtils.dateToStr(user.getUpdateDate()));
        usersVo.setLoginNumber(user.getLoginNumber());
        return ServerResponse.serverResponseBySucess(usersVo);
    }
    /**
     * 获取当前登录用户的详细信息
     * */
    @RequestMapping(value = "/get_inforamtion.do", method = RequestMethod.GET)
    public ServerResponse getInforamtion(HttpSession session) {
        User users = (User) session.getAttribute(StatusEnum.LOGIN_USER);
        if (users == null) {
            return ServerResponse.serverResponseByFail(StatusEnum.USER_NOT_LOGIN.getStatus(),StatusEnum.USER_NOT_LOGIN.getDesc());
        }
        ServerResponse sr = service.selectById(users.getId());
        return sr;
    }

    /**
     * 忘记密码
     * */
    @RequestMapping(value = "/forget_get_question.do", method = RequestMethod.GET)
    public ServerResponse forgetGetQuestion(String username) {
        service.forgetGetQuestion(username);
        return ServerResponse.serverResponseBySucess("注销成功");
    }

    /**
     * 提交问题答案
     * */
    @RequestMapping(value = "/forget_check_answer.do", method = RequestMethod.GET)
    public ServerResponse forgetCheckAnswer(String username, String question, String answer) {
        ServerResponse sr = service.forgetCheckAnswer(username, question, answer);
        return sr;
    }
    /**
     * 忘记密码的重设密码
     * */
    @RequestMapping(value = "/forget_reset_password.do", method = RequestMethod.GET)
    public ServerResponse forgetResetPassword(String username,String passwordNew,String forgetToken) {
        ServerResponse sr = service.forgetResetPassword(username,passwordNew,forgetToken);
        return sr;
    }
    /**
     * 登录状态中重置密码
     * */
    @RequestMapping(value = "/reset_password.do", method = RequestMethod.GET)
    public ServerResponse resetPassword(String passwordOld,String passwordNew,HttpSession session) {
        User user = (User) session.getAttribute(StatusEnum.LOGIN_USER);
        if (user == null){
            return ServerResponse.serverResponseByFail(StatusEnum.USER_NOT_LOGIN.getStatus(),StatusEnum.USER_NOT_LOGIN.getDesc());
        }
        ServerResponse sr = service.resetPassword(user.getId(),passwordOld,passwordNew);
        return sr;
    }

    /**
     * 登录状态更新个人信息
     * */
    @RequestMapping(value = "/update_information.do", method = RequestMethod.GET)
    public ServerResponse updateInformation(HttpSession session,User users) {
        User u = (User) session.getAttribute(StatusEnum.LOGIN_USER);
        if (u == null) {
            return ServerResponse.serverResponseByFail(StatusEnum.USER_NOT_LOGIN.getStatus(),StatusEnum.USER_NOT_LOGIN.getDesc());
        }
        users.setId(u.getId());
        users.setUsername(u.getUsername());
        ServerResponse rs = service.updateInformation(users);
        return rs;
    }


    /**
     * 退出登录
     * */
    @RequestMapping(value = "/logout.do", method = RequestMethod.GET)
    public ServerResponse logOut(HttpSession session) {
        session.removeAttribute(StatusEnum.LOGIN_USER);
        return ServerResponse.serverResponseBySucess("注销成功");
    }
}
