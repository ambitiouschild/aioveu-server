package com.aioveu.pay.aioveu08PayAccount.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: PayAccountVO
 * @Description TODO 支付账户视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:10
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "支付账户视图对象")
public class PayAccountVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "账户编号")
    private String accountNo;
    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "账户类型：USER-用户账户 MERCHANT-商户账户 PLATFORM-平台账户")
    private String accountType;
    @Schema(description = "账户余额")
    private BigDecimal balance;
    @Schema(description = "冻结余额")
    private BigDecimal frozenBalance;
    @Schema(description = "可用余额")
    private BigDecimal availableBalance;
    @Schema(description = "总收入")
    private BigDecimal totalIncome;
    @Schema(description = "总支出")
    private BigDecimal totalExpend;
    @Schema(description = "币种")
    private String currency;
    @Schema(description = "账户状态：0-冻结 1-正常 2-注销")
    private Integer status;
    @Schema(description = "版本号，用于乐观锁")
    private Integer version;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
