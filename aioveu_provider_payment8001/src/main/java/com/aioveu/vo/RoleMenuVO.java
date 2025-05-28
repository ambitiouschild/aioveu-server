package com.aioveu.vo;

import lombok.Data;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/29 0029 21:41
 */
@Data
public class RoleMenuVO extends IdNameVO {

    private Long roleMenuId;

    private String parentCode;

    private String code;

    /**
     * 选中状态 1选中 0 未选中 -1半选中
     */
    private int checked;

    private List<RoleMenuVO> children;

}
