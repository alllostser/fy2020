package com.neuedu.dao;

import com.neuedu.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDao {
    //根据商品id获取商品详情
    Product selectByIdForDetail(@Param("id") Integer id,
                                @Param("is_new")Integer is_new,
                                @Param("is_hot")Integer is_hot,
                                @Param("is_banner")Integer is_banner);
    //添加商品
    int insert(Product product);
    //查询商品是否存在
    Product selectById(Integer pid);
    //更新商品
    int updateProductBySelective(Product product);
    //按照商品名称和categoryId进行检索
    List<Product> findProductsByCategoryIdAndKeyword(
            @Param("categoryIds") List<Integer> categoryIds,
            @Param("keyword") String keyword);
}