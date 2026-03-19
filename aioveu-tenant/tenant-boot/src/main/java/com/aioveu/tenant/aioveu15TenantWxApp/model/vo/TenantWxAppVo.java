package com.aioveu.tenant.aioveu15TenantWxApp.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @ClassName: TenantWxAppVo
 * @Description TODO 租户与微信小程序关联视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/19 17:02
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "租户与微信小程序关联视图对象")
public class TenantWxAppVo {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @Schema(description = "租户ID")
    private Long tenantId;
    @Schema(description = "微信小程序ID")
    private String wxAppid;
    /**
     * 微信小程序appname
     */
    private String wxAppName;

    /**
     * 微信小程序注册邮箱
     */
    private String registeredEmail;


    @Schema(description = "是否为默认小程序")
    private Integer isDefault;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
