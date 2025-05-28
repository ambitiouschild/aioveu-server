package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_exercise_vip_card")
@Data
public class ExerciseVipCard extends IdEntity {

    private Long exerciseId;

    private Long vipCardId;

}
