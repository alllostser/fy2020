package com.neuedu.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class OrderVo {

    private Long orderNo;

    private Integer shippingId;

    private BigDecimal payment;

    private Integer paymentType;

    private Integer postage;

    private String status;

    private String paymentTime;

    private String sendTime;

    private String endTime;

    private String closeTime;

    private String createTime;

    private ShippingVo shippingVo;

    private List<OrderItemVo> orderItemVoList;
}
