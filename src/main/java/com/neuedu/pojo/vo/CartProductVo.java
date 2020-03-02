package com.neuedu.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class CartProductVo {
    private Integer id;

    private Integer userId;

    private Integer productId;

    private Integer quantity;

    private String productName;

    private String productSubtitle;

    private String productMainImage;

    private BigDecimal productPrice;//购物车商品单价

    private BigDecimal productTotalPrice;//购物车商品总价格

    private Integer productStock;

    private Integer productChecked;

    private String limitQuantity;//库存是否充足
}
