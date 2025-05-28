package com.aioveu.vo;

import lombok.Data;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/29 0029 21:41
 */
@Data
public class RoleMenuPermissionVO extends IdNameVO {

    private Long roleMenuPermissionId;

    private String url;

    private String menuCode;


}
