package com.neuedu.common;

public class Consts {
    public enum RoleEnum {
        ADMIN(1, "管理员"),
        USER(0, "普通用户");
        private int role; //权限 ：0为普通用户 1为管理员
        private String msg;//管理员或普通用户

        RoleEnum(int role, String msg) {
            this.role = role;
            this.msg = msg;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public enum ProductStatusEnum {
        //商品状态
        ON_SALE(1, "在售"),
        SOLD_OUT(2, "下架"),
        BE_DELETED(3, "删除"),
        PARAM_IS_NULL(201, "查询的ID不存在"),

        //操作失败
        ERROR(202, "操作失败"),
        PRODUCT_INSTER_FAIL(202, "未知错误，商品添加失败"),
        PRODUCT_UPDATE_FAIL(202, "未知错误，商品更新失败"),

        RETURN_IS_NULL(202, "该分类无子分类"),

        PRODUCT_NOT_HAVE(203, "没有找到该商品"),
        PRODUCT_RETURN_STATUS(203, "该商品已被下架或删除"),

        PRODUCT_UNDERSTOCK(204, "该商品库存不足"),

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

    public enum CategoryStatusEnum {
        //参数为空
        CATEGORY_ID_NULL(301, "类别ID异常，不能为空"),
        CATEGORY_NAME_NULL(301, "类别名称不能为空"),

        //操作失败
        ERROR(302, "操作失败"),
        CATEGORY_INSTER_FAIL(302, "未知错误，类别添加失败"),
        CATEGORY_ALREADY_EXIST(302, "该类别已经存在"),
        //类别不存在
        CATEGORY_INEXISTENCE(303, "类别不存在"),
        NO_CHILDREN_CATEGORY(303, "该品类无子分类");
        private int status; //状态码值
        private String desc;//对状态码描述

        CategoryStatusEnum(int status, String desc) {
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

    public enum CartProductEnum {
        //是否选中
        CHECKED(1, "已选中"),
        UNCHECKED(0, "未选中"),

        //错误

        ADD_CART_FAILED(401, "添加购物车失败"),
        UPDATE_CART_FAILED(401, "更新购物车商品失败"),
        CLEAN_CART_FAILED(401,"清除购物车商品失败或未选择购买任何商品"),

        THIS_PRODUCT_NOT_STOCK(402,"该商品没有库存")
        ;
        private int checkCode; //状态码值
        private String desc;//对状态码描述

        CartProductEnum(int checkCode, String desc) {
            this.checkCode = checkCode;
            this.desc = desc;
        }

        public int getStatus() {
            return checkCode;
        }

        public void setStatus(int checkCode) {
            this.checkCode = checkCode;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public enum OrderStatusEnum {
        //订单状态
        CANCELED(0, "已取消"),
        UNPAY(10, "未付款"),
        PAYED(20,"已付款"),
        SEND(40,"已发货"),
        SUCCESS(50,"交易成功"),
        CLOSE(60,"交易关闭"),

        //错误
        CREATE_ORDER_FAILED(501, "创建订单失败"),
        CREATE_ORDERITEM_FAILED(501, "创建订单明细失败"),
        REDUCESTOCK_FAILED(501,"商品扣库存失败"),
        ORDER_CANCEL_FAILED(501,"订单取消失败"),
        //参数为空
        ADDRESS_ISEMPTY(502, "收货地址id不能为空"),
        //购物车为空
        USER_CART_ISEMPTY(503, "用户购物车为空"),

        //订单查询错误
        ORDER_NOT_EXISTS(504,"订单不存在"),

        //取消订单
        ORDER_IS_INDEFEASIBLE(505,"已付款，无法取消订单"),

        //重复操作
        ORDER_ALREADY_CANCELLED(506,"该订单已经被取消"),
        ;
        private int status; //状态码值
        private String desc;//对状态码描述

        OrderStatusEnum(int status, String desc) {
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

    public enum shippingStatusEnum {
        ADDRESS_ADD_FAIL(600,"地址添加失败"),

        RECEIVERNAME_NOT_BE_EMPTY(601,"收货人姓名不能为空"),
        RECEIVERPHONE_NOT_BE_EMPTY(601,"收货人电话不能为空"),
        RECEIVERADDRESS_NOT_BE_EMPTY(601,"收货人地址不能为空"),
        ;
        private int status; //状态码值
        private String desc;//对状态码描述

        shippingStatusEnum(int status, String desc) {
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
    public enum PayInfoEnum {
        //支付方式
        ZHIFUBAO(1,"支付宝支付"),
        //是否选中

        ;
        private int checkCode; //状态码值
        private String desc;//对状态码描述

        PayInfoEnum(int checkCode, String desc) {
            this.checkCode = checkCode;
            this.desc = desc;
        }

        public int getStatus() {
            return checkCode;
        }

        public void setStatus(int checkCode) {
            this.checkCode = checkCode;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
