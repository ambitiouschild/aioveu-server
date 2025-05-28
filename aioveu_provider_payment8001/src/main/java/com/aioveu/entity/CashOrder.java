package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_cash_order")
@Data
public class CashOrder extends StringIdNameEntity {

    /**
     * 提现金额
     */
    private BigDecimal amount;

    private String payType;

    private String appId;

    private String userId;

    private String remark;

    // status 默认1 待审核 0 删除 4成功

}
