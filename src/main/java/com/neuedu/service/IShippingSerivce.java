package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;

public interface IShippingSerivce {
    //添加用户收货地址
    ServerResponse add(Shipping shipping);
}
