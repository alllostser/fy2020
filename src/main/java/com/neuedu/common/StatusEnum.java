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
    TOKEN_NOT_EMPTY(101,"非法的令牌参数或令牌已失效"),

    //判断参数是否已存在
    USERNAME_EXISTS(102,"用户名已存在"),
    EMAIL_EXISTS(102,"邮箱已存在"),
    PHONE_EXISTS(102,"手机号已被使用"),

    //参数不存在
    LOGIN_PARAM_ERROR(103,"用户名或密码错误"),
    QUESTION_ANSWER_MISMATCHING(103,"问题答案不匹配"),
    TOKEN_MISMATCHING(103,"令牌不匹配"),
    USERNAME_NOT_EXISTS(103,"用户名不存在"),
    //用户未登录
    USER_NOT_LOGIN(104,"用户未登录"),

    //账号状态信息
    USER_DISABLED(105,"用户被禁用"),
    USER_LIMITED_AUTHORITY(105,"用户无权限"),
    //更新信息
    UPDATE_SUCCESS(0,"更新成功"),
    UPDATA_FAILED(-1,"更新失败"),
    //未知错误失败
    ERROR(100,"操作失败或未知错误");
    ;
    public static final String LOGIN_USER = "session_user";
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
