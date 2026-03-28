package com.aioveu.pay.aioveu02PayConfig.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: PayConfigVo
 * @Description TODO 支付配置主表视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:04
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "支付配置主表视图对象")
public class PayConfigVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "租户ID")
    private Long tenantId;
    @Schema(description = "配置编码（唯一标识）")
    private String configCode;
    @Schema(description = "配置名称")
    private String configName;
    @Schema(description = "支付平台类型: WECHAT-微信支付, ALIPAY-支付宝, DUMMY-模拟支付")
    private String platformType;
    @Schema(description = "支付类型: APP-APP支付, JSAPI-公众号支付, NATIVE-扫码支付, MWEB-H5支付, MINIPROGRAM-小程序支付, FACE-刷脸支付")
    private String payType;
    @Schema(description = "是否启用: 0-禁用, 1-启用")
    private Integer enabled;
    @Schema(description = "是否默认配置: 0-否, 1-是")
    private Integer isDefault;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    @Schema(description = "创建人ID")
    private Long createBy;
    @Schema(description = "更新人ID")
    private Long updateBy;
}
