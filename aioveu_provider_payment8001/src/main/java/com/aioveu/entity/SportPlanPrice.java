package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * 计划价格表
 * </p>
 *
 * @author zyk
 * @since 2022-12-07
 */
@Data
@TableName("sport_plan_price")
public class SportPlanPrice extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer planId;

    private Integer membershipPrice;

    private Integer normalPrice;

}
