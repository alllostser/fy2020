package com.neuedu.service;

import com.neuedu.common.ServerResponse;

public interface ProductService {
    //获取商品分类信息
    ServerResponse topCategory(Integer categoryId);
    //获取商品详情
    ServerResponse detail(Integer id, Integer isNew, Integer isHot, Integer isBanner);
}
