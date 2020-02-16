package com.neuedu.pojo.vo;

import lombok.Data;

@Data
public class UsersVo {
    private Integer id;

    private String username;

    private String nickname;

    private String phone;

    private String email;

    private String createDate;

    private String updateDate;

    private Integer loginNumber;
}
