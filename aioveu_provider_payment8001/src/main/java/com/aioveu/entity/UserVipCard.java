package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_user_vip_card")
@Data
public class UserVipCard extends IdNameEntity {

    private String vipCode;

    private String userId;

    private BigDecimal price;

    private Double discount;

    private Long vipCardId;

    private Long companyId;

    private Long storeId;

    private String categoryCode;

    private String productCategoryCode;

    private Date validTime;

    private BigDecimal balance;

    @TableField(exist = false)
    private String phone;

    private String username;

}
