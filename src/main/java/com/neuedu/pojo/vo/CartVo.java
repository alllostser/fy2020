package com.neuedu.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartVo {
    private boolean allChecked;//是否全选

    private BigDecimal cartTotalPrice;//购物车总价格

    private List<CartProductVo> productVoList;


}