package com.aioveu.pay.aioveu08PayAccount.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @ClassName: PayAccount
 * @Description TODO 支付账户实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:07
 * @Version 1.0
 **/

@Getter
@Setter
@TableName("pay_account")
public class PayAccount extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 账户编号
     */
    private String accountNo;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 账户类型：USER-用户账户 MERCHANT-商户账户 PLATFORM-平台账户
     */
    private String accountType;
    /**
     * 账户余额
     */
    private BigDecimal balance;
    /**
     * 冻结余额
     */
    private BigDecimal frozenBalance;
    /**
     * 可用余额
     */
    private BigDecimal availableBalance;
    /**
     * 总收入
     */
    private BigDecimal totalIncome;
    /**
     * 总支出
     */
    private BigDecimal totalExpend;
    /**
     * 币种
     */
    private String currency;
    /**
     * 账户状态：0-冻结 1-正常 2-注销
     */
    private Integer status;
    /**
     * 版本号，用于乐观锁
     */
    private Integer version;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
