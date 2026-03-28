package com.aioveu.pay.aioveu04PayConfigDummy.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: PayConfigDummyForm
 * @Description TODO 模拟支付配置表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:28
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "模拟支付配置表单对象")
public class PayConfigDummyForm implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "支付配置主表ID")
    @NotNull(message = "支付配置主表ID不能为空")
    private Long configId;

    @Schema(description = "所属租户ID")
    @NotNull(message = "所属租户ID不能为空")
    private Long tenantId;

    @Schema(description = "支付成功率(0-100)")
    @NotNull(message = "支付成功率(0-100)不能为空")
    private Integer successRate;

    @Schema(description = "模拟延迟(毫秒)")
    private Integer mockDelay;

    @Schema(description = "模拟回调地址")
    @Size(max=500, message="模拟回调地址长度不能超过500个字符")
    private String callbackUrl;

    @Schema(description = "回调延迟(毫秒)")
    private Integer callbackDelay;

    @Schema(description = "成功响应模板")
    @Size(max=65535, message="成功响应模板长度不能超过65535个字符")
    private String successResponse;

    @Schema(description = "失败响应模板")
    @Size(max=65535, message="失败响应模板长度不能超过65535个字符")
    private String failResponse;

    @Schema(description = "是否模拟异常: 0-否, 1-是")
    @NotNull(message = "是否模拟异常: 0-否, 1-是不能为空")
    private Integer simulateError;

    @Schema(description = "模拟错误码")
    @Size(max=50, message="模拟错误码长度不能超过50个字符")
    private String errorCode;

    @Schema(description = "模拟错误信息")
    @Size(max=200, message="模拟错误信息长度不能超过200个字符")
    private String errorMsg;

    @Schema(description = "是否自动退款: 0-否, 1-是")
    private Integer autoRefund;

    @Schema(description = "退款延迟(毫秒)")
    private Integer refundDelay;

    @Schema(description = "创建人ID")
    private Long createBy;

    @Schema(description = "更新人ID")
    private Long updateBy;
}
