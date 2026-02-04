package com.aioveu.pay.aioveu03PayChannelConfig.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: PayChannelConfigVO
 * @Description TODO 支付渠道配置视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 19:55
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "支付渠道配置视图对象")
public class PayChannelConfigVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "渠道编码：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联")
    private String channelCode;
    @Schema(description = "渠道名称")
    private String channelName;
    @Schema(description = "渠道类型：ONLINE-线上 OFFLINE-线下")
    private String channelType;
    @Schema(description = "配置键")
    private String configKey;
    @Schema(description = "配置值")
    private String configValue;
    @Schema(description = "配置类型：CERT-证书 KEY-密钥 URL-地址")
    private String configType;
    @Schema(description = "配置描述")
    private String configDesc;
    @Schema(description = "是否启用：0-禁用 1-启用")
    private Integer isEnabled;
    @Schema(description = "是否默认：0-否 1-是")
    private Integer isDefault;
    @Schema(description = "优先级，数值越大优先级越高")
    private Integer priority;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建人")
    private String createBy;
    @Schema(description = "更新人")
    private String updateBy;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
