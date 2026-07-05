package com.aioveu.auth.model;


import lombok.Data;

/**
 * @ClassName: TenantClientInitDTO
 * @Description TODO 租户客户端初始化表单
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/5 14:10
 * @Version 1.0
 **/
@Data
public class TenantClientInitDTO {

    private Long tenantId;
    private String tenantCode;
    private String tenantName;
    private String adminUsername;
}
