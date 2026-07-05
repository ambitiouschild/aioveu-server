package com.aioveu.auth.aioveu02Oauth2Authorization.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import com.aioveu.common.base.BasePageQuery;
/**
 * @ClassName: Oauth2AuthorizationQuery
 * @Description TODO OAuth2授权信息，存储所有的授权记录、令牌和状态信息分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 13:56
 * @Version 1.0
 **/
@Schema(description ="OAuth2授权信息，存储所有的授权记录、令牌和状态信息查询对象")
@Getter
@Setter
public class Oauth2AuthorizationQuery extends BasePageQuery {

    @Schema(description = "授权记录的唯一标识符，通常是UUID")
    private String id;
    @Schema(description = "关联的注册客户端ID，指向oauth2_registered_client表")
    private String registeredClientId;
    @Schema(description = "用户主体名称，通常是用户名或用户标识")
    private String principalName;
    @Schema(description = "授权类型，如authorization_code、password、client_credentials、refresh_token等")
    private String authorizationGrantType;
}
