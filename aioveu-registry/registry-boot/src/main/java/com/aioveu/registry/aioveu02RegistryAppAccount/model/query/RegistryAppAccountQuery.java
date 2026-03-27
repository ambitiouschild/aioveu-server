package com.aioveu.registry.aioveu02RegistryAppAccount.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RegistryAppAccountQuery
 * @Description TODO 小程序账号分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:03
 * @Version 1.0
 **/
@Schema(description ="小程序账号查询对象")
@Getter
@Setter
public class RegistryAppAccountQuery extends BasePageQuery {

    @Schema(description = "小程序AppID")
    private Long appId;
    @Schema(description = "所属租户ID")
    private Long tenantId;
    @Schema(description = "小程序名称")
    private String accountName;
    @Schema(description = "账号类型：0-未注册，1-普通小程序，2-游戏小程序")
    private Integer accountType;
    @Schema(description = "注册邮箱")
    private String email;
}
