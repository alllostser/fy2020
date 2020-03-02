package com.neuedu.controller.front;

import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.pojo.User;
import com.neuedu.service.ICartService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Resource
    private ICartService service;

    /**
     * 购物车列表
     * @param session
     * @return
     */
    @RequestMapping("/list.do")
    public ServerResponse list(HttpSession session){
        //step1:判断用户是否登录
        User u = (User) session.getAttribute(StatusEnum.LOGIN_USER);
        if (u == null) {
            return ServerResponse.serverResponseByFail(StatusEnum.USER_NOT_LOGIN.getStatus(),StatusEnum.USER_NOT_LOGIN.getDesc());
        }
        ServerResponse sr = service.list(u.getId());
        return sr;
    }

    /**
     *购物车添加商品
     * @param productId
     * @param count
     * @param session
     * @return
     */
    @RequestMapping("/add.do")
    public ServerResponse add(Integer productId,Integer count,HttpSession session){
        //step1:判断用户是否登录
        User u = (User) session.getAttribute(StatusEnum.LOGIN_USER);
        if (u == null) {
            return ServerResponse.serverResponseByFail(StatusEnum.USER_NOT_LOGIN.getStatus(),StatusEnum.USER_NOT_LOGIN.getDesc());
        }
        ServerResponse sr = service.add(u.getId(),productId,count);
        return sr;
    }

}
