package com.aioveu.auth.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysRolePermissionVO {

    //权限ID
    private Long permissionId;

    //权限url
    private String url;

    private String method;

    //权限名称
    private String permissionName;

    private List<String> roles;

    private String roleCodeStr;
}
