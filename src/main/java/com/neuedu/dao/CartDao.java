package com.neuedu.dao;

import com.neuedu.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartDao {
    int deleteByPrimaryKey(Integer id);



    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);




    //根据用户id获取购物车信息
    List<Cart> findCartByUserId(Integer userId);
    //修改该商品在购物车中数量，根据商品id
    int updateByProductId(@Param("userId") Integer userId,@Param("productId") Integer productId, @Param("quantity") Integer quantity);
    //统计购物车中未选中的商品数量
    int totalCountByUnchecked(Integer userId);
    //根据用户id和商品id查询购物车中是否包含此商品
    Cart findCartByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);
    //添加购物车
    int insert(Cart record);
    //更新购物车商品数量
    int updateByPrimaryKey(Cart record);
    //查询购物车中用户已经选中的商品
    List<Cart> findCartByUserIdAndChecked(Integer userId);
    //清除购物车中已下单的商品
    int deleteBach(@Param("cartList") List<Cart> cartList);
}