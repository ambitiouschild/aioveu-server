package com.aioveu.pay.aioveu08PayAccount.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: PayAccountForm
 * @Description TODO 支付账户表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:08
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "支付账户表单对象")
public class PayAccountForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "账户编号")
    @NotBlank(message = "账户编号不能为空")
    @Size(max=32, message="账户编号长度不能超过32个字符")
    private String accountNo;

    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "账户类型：USER-用户账户 MERCHANT-商户账户 PLATFORM-平台账户")
    @NotBlank(message = "账户类型：USER-用户账户 MERCHANT-商户账户 PLATFORM-平台账户不能为空")
    @Size(max=20, message="账户类型：USER-用户账户 MERCHANT-商户账户 PLATFORM-平台账户长度不能超过20个字符")
    private String accountType;

    @Schema(description = "账户余额")
    @NotNull(message = "账户余额不能为空")
    private BigDecimal balance;

    @Schema(description = "冻结余额")
    @NotNull(message = "冻结余额不能为空")
    private BigDecimal frozenBalance;

    @Schema(description = "可用余额")
    private BigDecimal availableBalance;

    @Schema(description = "总收入")
    @NotNull(message = "总收入不能为空")
    private BigDecimal totalIncome;

    @Schema(description = "总支出")
    @NotNull(message = "总支出不能为空")
    private BigDecimal totalExpend;

    @Schema(description = "币种")
    @Size(max=3, message="币种长度不能超过3个字符")
    private String currency;

    @Schema(description = "账户状态：0-冻结 1-正常 2-注销")
    @NotNull(message = "账户状态：0-冻结 1-正常 2-注销不能为空")
    private Integer status;

    @Schema(description = "版本号，用于乐观锁")
    @NotNull(message = "版本号，用于乐观锁不能为空")
    private Integer version;

    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    @NotNull(message = "逻辑删除：0-未删除 1-已删除不能为空")
    private Integer isDeleted;

    @Schema(description = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
