package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Time;

/**
 * <p>
 * 店铺场地表
 * </p>
 *
 * @author zyk
 * @since 2022-12-07
 */
@Data
@TableName("sport_venue_field")
public class VenueField extends IdNameEntity {

    private Long storeId;

    private Long venueId;

    private BigDecimal price;

    private BigDecimal vipPrice;

    private Time startTime;

    private Time endTime;

    @TableField(exist = false)
    private boolean disable;

}
