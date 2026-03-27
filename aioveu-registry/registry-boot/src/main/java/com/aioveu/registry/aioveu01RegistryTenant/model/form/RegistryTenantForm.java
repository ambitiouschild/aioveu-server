package com.aioveu.registry.aioveu01RegistryTenant.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: RegistryTenantForm
 * @Description TODO 租户注册小程序基本信息表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 16:28
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "租户注册小程序基本信息表单对象")
public class RegistryTenantForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "租户ID")
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @Schema(description = "租户唯一编码")
    @NotBlank(message = "租户唯一编码不能为空")
    @Size(max=100, message="租户唯一编码长度不能超过100个字符")
    private String tenantCode;

    @Schema(description = "主体类型：1-企业，2-个体工商户，3-政府/媒体，4-其他组织，5-个人")
    @Size(max=100, message="主体类型：1-企业，2-个体工商户，3-政府/媒体，4-其他组织，5-个人长度不能超过100个字符")
    private String tenantType;

    @Schema(description = "行业类别/小程序类目")
    @Size(max=255, message="行业类别/小程序类目长度不能超过255个字符")
    private String businessType;

    @Schema(description = "注册国家/地区")
    @Size(max=100, message="注册国家/地区长度不能超过100个字符")
    private String countryRegion;

    @Schema(description = "租户注册状态：0-未注册，1-已注册，2-已认证，3-已备案，4-已禁用")
    @NotNull(message = "租户注册状态：0-未注册，1-已注册，2-已认证，3-已备案，4-已禁用不能为空")
    private Integer tenantRegistryStatus;
}
