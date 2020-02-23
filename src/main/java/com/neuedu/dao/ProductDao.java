package com.neuedu.dao;

import com.neuedu.pojo.Product;
import org.apache.ibatis.annotations.Param;

public interface ProductDao {
    //根据商品id获取商品详情
    Product selectByIdForDetail(@Param("id") Integer id,
                                @Param("is_new")Integer is_new,
                                @Param("is_hot")Integer is_hot,
                                @Param("is_banner")Integer is_banner);
}