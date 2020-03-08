package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Cart;

import java.util.List;

public interface ICartService {
    //前台-购物车列表
    ServerResponse list(Integer userId);
    //购物车添加商品
    ServerResponse add(Integer userId, Integer productId, Integer count);
    //查询购物车中用户已经选中的商品
    ServerResponse findCartByUserIdAndChecked(Integer userId);
    //清除购物车中已下单的商品
    ServerResponse deleteBatchByIds(List<Cart> cartList);
}
