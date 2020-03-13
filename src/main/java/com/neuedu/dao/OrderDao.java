package com.neuedu.dao;

import com.neuedu.pojo.Order;
import com.neuedu.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface OrderDao {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    /**
     * 插入订单
     * */
    int insert(Order record);

    /**
     * 根据订单号查询订单是否存在
     * */
    Order findOrderByOrderNo(Long orderNo);

    /**
     *获取订单列表（用户）
     *  */
    List<Order> orderListByUserId(Integer userId);

    /**
     * 修改订单状态
     * */
    int updateOrderStatus(@Param("orderNo") Long orderNo,
                          @Param("payTime") Date payTime,
                          @Param("status") Integer status);
}