package com.neuedu.pojo.vo;

import lombok.Data;

@Data
public class CategoryVo {
    private Integer id;

    private Integer parentId;

    private String name;

    private Boolean status;

    private Integer sortOrder;

    private String createTime;

    private String updateTime;
}
