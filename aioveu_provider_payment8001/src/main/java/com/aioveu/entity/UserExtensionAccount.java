package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_user_extension_account")
@Data
public class UserExtensionAccount extends IdNameEntity {

    private String userId;

    private BigDecimal balance;

}
