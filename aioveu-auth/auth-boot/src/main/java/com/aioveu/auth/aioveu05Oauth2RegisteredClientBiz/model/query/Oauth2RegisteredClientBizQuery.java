package com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.model.query;


import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: Oauth2RegisteredClientBizQuery
 * @Description TODO OAuth2 客户端业务状态（auth 服务本地校验用）分页查询对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/11 17:32
 * @Version 1.0
 **/
@Schema(description ="OAuth2 客户端业务状态（auth 服务本地校验用）查询对象")
@Getter
@Setter
public class Oauth2RegisteredClientBizQuery extends BasePageQuery {

    @Schema(description = "OAuth2 客户端ID")
    private String clientId;
    @Schema(description = "租户ID")
    private Long tenantId;
    @Schema(description = "是否启用：1-启用 0-禁用")
    private Integer enabled;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
}
