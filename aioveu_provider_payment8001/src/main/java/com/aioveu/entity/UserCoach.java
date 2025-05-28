package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@TableName("sport_user_coach")
@Data
public class UserCoach extends IdEntity{

    @NotBlank(message = "coachId can not be null!")
    private Long coachId;

    @NotBlank(message = "userId can not be null!")
    private String userId;

    private Long companyId;

    private Long storeId;

}
