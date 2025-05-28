package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_exercise_push_reward_config")
@Data
public class ExercisePushRewardConfig extends IdNameEntity {

    private Long exerciseId;

    private Long topicId;

    /**
     * 奖励类型 默认0 地推预约奖励(老) 1地推到店奖励 2到店礼 5下单给地推的奖励
     */
    private Integer rewardType;

    /**
     * 优惠券模板id
     */
    private Long couponTemplateId;

    private BigDecimal reward;

    /**
     * 奖品商品
     */
    private String rewardProduct;

    private String remark;

}
