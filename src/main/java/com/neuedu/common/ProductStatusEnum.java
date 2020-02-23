package com.neuedu.common;



public enum ProductStatusEnum {
    PARAM_IS_NULL(201,"查询的ID不存在"),


    RETURN_IS_NULL(202,"该分类无子分类"),

    PRODUCT_RETURN_STATUS(203,"该商品已被下架或删除"),

    ;
    private int status; //状态码值
    private String desc;//对状态码描述

    ProductStatusEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

