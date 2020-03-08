package com.neuedu.dao;

import com.neuedu.pojo.Order;

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
}