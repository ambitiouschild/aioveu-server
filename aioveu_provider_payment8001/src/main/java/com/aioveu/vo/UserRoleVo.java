package com.aioveu.vo;

import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: 2024/12/5 0027 6:20
 */
@Data
public class UserRoleVo extends BaseItemVO {

    private String storeName;

    private String companyName;

    private String roleCode;

    private Integer roleType;

}
