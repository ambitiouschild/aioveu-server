package com.aioveu.pay.aioveu03PayChannelConfig.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: PayChannelConfigForm
 * @Description TODO 支付渠道配置表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 19:53
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "支付渠道配置表单对象")
public class PayChannelConfigForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "渠道编码：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联")
    @NotBlank(message = "渠道编码：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联不能为空")
    @Size(max=20, message="渠道编码：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联长度不能超过20个字符")
    private String channelCode;

    @Schema(description = "渠道名称")
    @NotBlank(message = "渠道名称不能为空")
    @Size(max=50, message="渠道名称长度不能超过50个字符")
    private String channelName;

    @Schema(description = "渠道类型：ONLINE-线上 OFFLINE-线下")
    @NotBlank(message = "渠道类型：ONLINE-线上 OFFLINE-线下不能为空")
    @Size(max=20, message="渠道类型：ONLINE-线上 OFFLINE-线下长度不能超过20个字符")
    private String channelType;

    @Schema(description = "配置键")
    @NotBlank(message = "配置键不能为空")
    @Size(max=50, message="配置键长度不能超过50个字符")
    private String configKey;

    @Schema(description = "配置值")
    @NotBlank(message = "配置值不能为空")
    @Size(max=65535, message="配置值长度不能超过65535个字符")
    private String configValue;

    @Schema(description = "配置类型：CERT-证书 KEY-密钥 URL-地址")
    @Size(max=20, message="配置类型：CERT-证书 KEY-密钥 URL-地址长度不能超过20个字符")
    private String configType;

    @Schema(description = "配置描述")
    @Size(max=200, message="配置描述长度不能超过200个字符")
    private String configDesc;

    @Schema(description = "是否启用：0-禁用 1-启用")
    @NotNull(message = "是否启用：0-禁用 1-启用不能为空")
    private Integer isEnabled;

    @Schema(description = "是否默认：0-否 1-是")
    private Integer isDefault;

    @Schema(description = "优先级，数值越大优先级越高")
    private Integer priority;

    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    @NotNull(message = "逻辑删除：0-未删除 1-已删除不能为空")
    private Integer isDeleted;

    @Schema(description = "创建人")
    @Size(max=64, message="创建人长度不能超过64个字符")
    private String createBy;

    @Schema(description = "更新人")
    @Size(max=64, message="更新人长度不能超过64个字符")
    private String updateBy;

    @Schema(description = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
