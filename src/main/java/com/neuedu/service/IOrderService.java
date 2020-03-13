package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.User;

import java.util.Map;

public interface IOrderService {

    /**
     * 创建订单
     * */
    ServerResponse createOrder(Integer userId, Integer shippingId);

    /**
     * 取消订单
     * */
    ServerResponse cancelOrder(Long orderNo);

    /**
     *获取订单列表（用户）
     * */
    ServerResponse orderList(Integer userId, Integer pageNum, Integer pageSize);

    /**
     * 用户支付
     * */
    ServerResponse orderPay(Integer userId, Long orderNo);

    /**
     * 支付宝回调
     * */
    String alipayCallback(Map<String,String> signMap);
}
