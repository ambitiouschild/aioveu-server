package com.aioveu.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("sport_coach_certificate")
@Data
public class CoachCertificate extends IdEntity{

    private String certificate;

    private Long coachId;

}
