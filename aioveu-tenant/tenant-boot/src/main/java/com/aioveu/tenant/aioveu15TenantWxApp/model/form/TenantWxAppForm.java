package com.aioveu.tenant.aioveu15TenantWxApp.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: TenantWxAppForm
 * @Description TODO 租户与微信小程序关联表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/19 17:01
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "租户与微信小程序关联表单对象")
public class TenantWxAppForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "租户ID")
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @Schema(description = "微信小程序ID")
    @NotBlank(message = "微信小程序ID不能为空")
    @Size(max=255, message="微信小程序ID长度不能超过255个字符")
    private String wxAppid;

    /**
     * 微信小程序appname
     */
    private String wxAppname;

    /**
     * 微信小程序注册邮箱
     */
    private String registeredEmail;

    @Schema(description = "是否为默认小程序")
    private Integer isDefault;

    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
}
