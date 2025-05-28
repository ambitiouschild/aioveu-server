package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @description 用户付款信息
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_user_payment_info")
@Data
public class UserPaymentInfo extends IdNameEntity{

    @NotEmpty(message = "账户不能为空")
    private String account;

    @NotEmpty(message = "姓名不能为空")
    private String fullName;

    @NotEmpty(message = "用户不能为空")
    private String userId;

    private String bank;

    private String remark;

}
