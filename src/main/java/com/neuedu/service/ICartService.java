package com.neuedu.service;

import com.neuedu.common.ServerResponse;

public interface ICartService {
    //前台-购物车列表
    ServerResponse list(Integer userId);
    //购物车添加商品
    ServerResponse add(Integer userId, Integer productId, Integer count);
}
