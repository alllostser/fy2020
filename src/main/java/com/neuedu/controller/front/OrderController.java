package com.neuedu.controller.front;

import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.pojo.User;
import com.neuedu.service.IOrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    private IOrderService service;
    /*  前台模块   */


    /*
     *创建订单
     * */
    @RequestMapping(value = "/create.do",method = RequestMethod.GET)
    public ServerResponse create(Integer shippingId, HttpSession session){
        User user =(User) session.getAttribute(StatusEnum.LOGIN_USER);
        //step1:判断用户是否登录
        if (user == null){
            //未登录
            return ServerResponse.serverResponseByFail(StatusEnum.USER_NOT_LOGIN.getStatus(),StatusEnum.USER_NOT_LOGIN.getDesc());
        }
        ServerResponse sr = service.createOrder(user.getId(),shippingId);
        return sr;
    }
}
