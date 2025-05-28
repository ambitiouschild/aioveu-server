package com.aioveu.vo;

import lombok.Data;

@Data
public class CategoryVo extends IdNameVO {
    private Long parentId;

    private String name;

    private String parentName;

    private String cover;

    private Integer priority;

    private String code;

    private Integer status;
}
