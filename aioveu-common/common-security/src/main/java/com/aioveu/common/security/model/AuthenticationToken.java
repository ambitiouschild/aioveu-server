package com.aioveu.common.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @ClassName: AuthenticationToken
 * @Description TODO  认证令牌响应对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/14 14:12
 * @Version 1.0
 **/

@Schema(description = "认证令牌响应对象")
@Data
@Builder
public class AuthenticationToken {

    @Schema(description = "令牌类型", example = "Bearer")
    private String tokenType;

    @Schema(description = "访问令牌")
    private String accessToken;

    @Schema(description = "刷新令牌")
    private String refreshToken;

    @Schema(description = "过期时间(单位：秒)")
    private Integer expiresIn;

}
