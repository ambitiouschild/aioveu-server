package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExercisePushRewardConfigVO {

    private Integer id;

    private String name;

    private String exerciseName;

    private String topicName;

    private BigDecimal reward;

    private Date createDate;

    private String rewardProduct;

    private Integer rewardType;
}
