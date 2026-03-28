package com.aioveu.pay.aioveu04PayConfigDummy.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: PayConfigDummyVo
 * @Description TODO 模拟支付配置视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:29
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "模拟支付配置视图对象")
public class PayConfigDummyVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "支付配置主表ID")
    private Long configId;
    @Schema(description = "所属租户ID")
    private Long tenantId;
    @Schema(description = "支付成功率(0-100)")
    private Integer successRate;
    @Schema(description = "模拟延迟(毫秒)")
    private Integer mockDelay;
    @Schema(description = "模拟回调地址")
    private String callbackUrl;
    @Schema(description = "回调延迟(毫秒)")
    private Integer callbackDelay;
    @Schema(description = "成功响应模板")
    private String successResponse;
    @Schema(description = "失败响应模板")
    private String failResponse;
    @Schema(description = "是否模拟异常: 0-否, 1-是")
    private Integer simulateError;
    @Schema(description = "模拟错误码")
    private String errorCode;
    @Schema(description = "模拟错误信息")
    private String errorMsg;
    @Schema(description = "是否自动退款: 0-否, 1-是")
    private Integer autoRefund;
    @Schema(description = "退款延迟(毫秒)")
    private Integer refundDelay;
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
