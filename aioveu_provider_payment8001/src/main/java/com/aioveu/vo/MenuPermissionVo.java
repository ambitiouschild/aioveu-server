package com.aioveu.vo;

import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: 2024/11/26 13:48
 */
@Data
public class MenuPermissionVo extends BaseItemVO {

    private Long menuPermissionId;

    private String url;

    private String method;

    private Boolean needToken;
}
