package com.neuedu.common;

public class Consts {
    public enum RoleEnum {
        ADMIN(1,"管理员"),
        USER(0,"普通用户")
        ;
        private int role; //权限 ：0为普通用户 1为管理员
        private String msg;//管理员或普通用户
        RoleEnum(int role, String msg) {
            this.role=role;
            this.msg=msg;
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
        PARAM_IS_NULL(201,"查询的ID不存在"),

        //操作失败
        ERROR(202,"操作失败"),
        PRODUCT_INSTER_FAIL(202,"未知错误，商品添加失败"),
        PRODUCT_UPDATE_FAIL(202,"未知错误，商品更新失败"),

        RETURN_IS_NULL(202,"该分类无子分类"),

        PRODUCT_NOT_HAVE(203,"没有找到该商品"),
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

    public enum CategoryStatusEnum {
        //参数为空
        CATEGORY_ID_NULL(301,"类别ID异常，不能为空"),
        CATEGORY_NAME_NULL(301,"类别名称不能为空"),

        //操作失败
        ERROR(302,"操作失败"),
        CATEGORY_INSTER_FAIL(302,"未知错误，类别添加失败"),
        CATEGORY_ALREADY_EXIST(302,"该类别已经存在"),
        //类别不存在
        CATEGORY_INEXISTENCE(303,"类别不存在"),
        NO_CHILDREN_CATEGORY(303,"该品类无子分类")
        ;
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
        CHECKED(1,"已选中"),
        UNCHECKED(0,"未选中"),

        //错误

        ADD_CART_FAILED(401,"添加购物车失败"),
        UPDATE_CART_FAILED(401,"更新购物车商品失败"),
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
}
