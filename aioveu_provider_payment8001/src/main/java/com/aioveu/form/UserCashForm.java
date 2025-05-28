package com.aioveu.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

/**
 * @description 健身馆提现申请
 * @author: 雒世松
 * @date: 2025/12/13 15:35
 */
@Data
public class UserCashForm {

    @NotEmpty(message = "userId不能为空")
    private String userId;

    @Min(value = 1, message = "提现金额不得少于1元")
    private BigDecimal money;

    @NotEmpty(message = "姓名不能为空")
    private String fullName;

    @NotEmpty(message = "AppId不能为空")
    private String appId;
}
