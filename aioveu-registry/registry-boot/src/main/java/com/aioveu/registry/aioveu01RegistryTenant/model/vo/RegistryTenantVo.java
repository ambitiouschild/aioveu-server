package com.aioveu.registry.aioveu01RegistryTenant.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: RegistryTenantVo
 * @Description TODO 租户注册小程序基本信息视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 16:30
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "租户注册小程序基本信息视图对象")
public class RegistryTenantVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @Schema(description = "租户ID")
    private Long tenantId;
    @Schema(description = "租户唯一编码")
    private String tenantCode;
    @Schema(description = "主体类型：1-企业，2-个体工商户，3-政府/媒体，4-其他组织，5-个人")
    private String tenantType;
    @Schema(description = "行业类别/小程序类目")
    private String businessType;
    @Schema(description = "注册国家/地区")
    private String countryRegion;
    @Schema(description = "租户注册状态：0-未注册，1-已注册，2-已认证，3-已备案，4-已禁用")
    private Integer tenantRegistryStatus;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
