package com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.model.form;


import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.enums.RegisteredClientBizStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: Oauth2RegisteredClientBizForm
 * @Description TODO OAuth2 客户端业务状态（auth 服务本地校验用）表单对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/11 17:31
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "OAuth2 客户端业务状态（auth 服务本地校验用）表单对象")
public class Oauth2RegisteredClientBizForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "OAuth2 客户端UUID")
    @NotBlank(message = "OAuth2 客户端UUID不能为空")
    @Size(max=64, message="OAuth2 客户端UUID长度不能超过64个字符")
    private String clientUUId;

    @Schema(description = "OAuth2 客户端ID")
    @NotBlank(message = "OAuth2 客户端ID不能为空")
    @Size(max=64, message="OAuth2 客户端ID长度不能超过64个字符")
    private String clientId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "是否启用：1-启用 0-禁用")
    @NotNull(message = "是否启用：1-启用 0-禁用不能为空")
    private RegisteredClientBizStatusEnum enabled;

    @Schema(description = "备注")
    @Size(max=255, message="备注长度不能超过255个字符")
    private String remark;
}
