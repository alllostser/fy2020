package com.neuedu.utils;

import com.neuedu.pojo.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public class BigDecimalUtil {

  public static BigDecimal add(String value1,String value2){

      BigDecimal bigDecimal1=new BigDecimal(value1);
      BigDecimal bigDecimal2=new BigDecimal(value2);
       return bigDecimal1.add(bigDecimal2);
  }

    public static BigDecimal multi(String value1,String value2){

        BigDecimal bigDecimal1=new BigDecimal(value1);
        BigDecimal bigDecimal2=new BigDecimal(value2);
        return bigDecimal1.multiply(bigDecimal2);
    }

    /**
     * 计算订单总金额
     * */
    public static BigDecimal getOrderTotalPrice(List<OrderItem> orderItems){
        BigDecimal orderTotalPrice = new BigDecimal("0");
        for (OrderItem orderItem : orderItems) {
            orderTotalPrice = BigDecimalUtil.add(String.valueOf(orderTotalPrice), String.valueOf(orderItem.getCurrentUnitPrice()));
        }
        return orderTotalPrice;
    }

}
