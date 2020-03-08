package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;

public interface IProductService {
    //前台-获取商品分类信息
    ServerResponse topCategory(Integer categoryId);
    //前台-获取商品详情
    ServerResponse detail(Integer id, Integer isNew, Integer isHot, Integer isBanner);
    //前台-商品搜索+动态排序
    ServerResponse productList(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);
    //后台-新增OR更新产品
    ServerResponse addOrUpdate(Product product);


    //订单引用商品扣库存
    ServerResponse reduceStock(Integer productId,Integer quantity);
}
