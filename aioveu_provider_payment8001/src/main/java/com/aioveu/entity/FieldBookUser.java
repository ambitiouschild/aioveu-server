package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_field_book_user")
@Data
public class FieldBookUser extends IdEntity {

    private Long fieldPlanId;

    private String username;

    private String userId;

    private Integer gender;

    private String phone;

    private String orderId;

}
