package com.neuedu.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProductVo {
    //图片服务器地址
    private String imageHost;

    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private String subImages;

    private String detail;

    private BigDecimal price;

    private Integer stock;

    private Integer status;

    private String createTime;

    private String updateTime;
}
