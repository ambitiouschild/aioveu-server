package com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.model.entity;


import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.enums.RegisteredClientBizStatusEnum;
import com.aioveu.common.base.BaseEntity;
import com.aioveu.common.base.BaseEntityWithTenantId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: Oauth2RegisteredClientBiz
 * @Description TODO OAuth2 客户端业务状态（auth 服务本地校验用）实体对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/11 17:30
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("oauth2_registered_client_biz")
public class Oauth2RegisteredClientBiz extends BaseEntityWithTenantId {

    private static final long serialVersionUID = 1L;

    /**
     * OAuth2 客户端UUID
     * 显式指定数据库字段（最稳妥）
     */
    @TableField("client_uuid")
    private String clientUUId;

    /**
     * OAuth2 客户端ID
     */
    private String clientId;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 是否启用：1-启用 0-禁用
     */
    private RegisteredClientBizStatusEnum enabled;
    /**
     * 备注
     */
    private String remark;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
