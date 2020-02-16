package com.neuedu.common;

public enum StatusEnum {
    //参数非空判断
    PARAM_NOT_EMPTY(101,"该参数不能为空"),
    USERNAME_NOT_EMPTY(101,"用户名不能为空"),
    PASSWORD_NOT_EMPTY(101,"密码不能为空"),
    EMAIL_NOT_EMPTY(101,"邮箱不能为空"),
    PHONE_NOT_EMPTY(101,"联系方式不能为空"),
    QUESTION_NOT_EMPTY(101,"密保问题不能为空"),
    ANSER_NOT_EMPTY(101,"密保答案不能为空"),

    //判断参数是否已存在
    USERNAME_EXISTS(102,"用户名已存在"),


    //未知错误失败
    ERROR(100,"操作失败");
    ;

    private int status; //状态码值
    private String desc;//对状态码描述

    StatusEnum(int status, String desc) {
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
