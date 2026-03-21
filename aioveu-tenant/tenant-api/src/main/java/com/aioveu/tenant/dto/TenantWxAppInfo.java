package com.aioveu.tenant.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName: TenantWxAppInfo
 * @Description TODO 客户端ID租户信息
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/20 10:55
 * @Version 1.0
 **/
@Data
@Builder
public class TenantWxAppInfo {

    private String wxAppid;      // 微信小程序appid
    private Long tenantId;     // 租户ID
    private String clientId;     // 客户端ID（查询条件）
    private String tenantName;   // 租户名称（可选）
}
