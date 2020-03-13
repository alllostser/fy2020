package com.neuedu.controller.front;

import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.pojo.Shipping;
import com.neuedu.pojo.User;
import com.neuedu.service.IShippingSerivce;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/shipping")
public class ShippingController {
    @Resource
    private IShippingSerivce shippingSerivce;

    /**
     * 添加用户收货地址
     * @param shipping
     * @param session
     * @return
     */
    @RequestMapping("add.do")
    public ServerResponse add(Shipping shipping, HttpSession session){
        //判断用户是否登录
        User user = (User) session.getAttribute(StatusEnum.LOGIN_USER);
        if (user == null){
            return ServerResponse.serverResponseByFail(StatusEnum.USER_NOT_LOGIN.getStatus(),StatusEnum.USER_NOT_LOGIN.getDesc());
        }
        Integer userId = user.getId();
        shipping.setUserId(userId);
        ServerResponse serverResponse = shippingSerivce.add(shipping);
        return serverResponse;
    }
}
