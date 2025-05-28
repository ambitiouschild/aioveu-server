package com.aioveu.form;

import com.aioveu.entity.ExerciseCoupon;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description 灵活课包
 * @author: 雒世松
 * @date: 2025/8/27 0027 6:20
 */
@Data
public class ExercisePushRewardConfigForm {

    @NotEmpty(message = "主题不能为空")
    private Long topicId;

    @NotEmpty(message = "活动不能为空")
    private Long exerciseId;

    @NotEmpty(message = "预约奖励不能为空")
    private BigDecimal makeAnAppointmentReward;

    @NotEmpty(message = "到店奖励不能为空")
    private BigDecimal reachReward;

    @NotEmpty(message = "到店礼不能为空")
    private String rewardProduct;

    /**
     * 优惠券模板id
     */
    private Long couponTemplateId;

}
