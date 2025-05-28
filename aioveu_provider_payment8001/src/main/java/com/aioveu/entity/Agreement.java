package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_agreement")
@Data
public class Agreement extends IdNameEntity {

    private Long companyId;

    private Long storeId;

    private String productId;

    private String url;

}
