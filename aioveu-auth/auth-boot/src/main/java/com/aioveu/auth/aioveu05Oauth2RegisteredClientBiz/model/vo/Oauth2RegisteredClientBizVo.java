package com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.model.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: Oauth2RegisteredClientBizVo
 * @Description TODO OAuth2 客户端业务状态（auth 服务本地校验用）视图对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/11 17:32
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "OAuth2 客户端业务状态（auth 服务本地校验用）视图对象")
public class Oauth2RegisteredClientBizVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "OAuth2 客户端UUID")
    private String clientUUId;
    @Schema(description = "OAuth2 客户端ID")
    private String clientId;
    @Schema(description = "租户ID")
    private Long tenantId;
    @Schema(description = "是否启用：1-启用 0-禁用")
    private Integer enabled;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
