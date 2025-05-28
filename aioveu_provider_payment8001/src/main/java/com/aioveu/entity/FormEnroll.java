package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_form_enroll")
@Data
public class FormEnroll extends IdNameEntity {

    private String description;

    private Date startTime;

    private Date endTime;

    private String successMsg;

    /**
     * 报名 公众号微信通知
     */
    private String serviceOpenId;

    /**
     * 限制人数
     */
    private Integer limitNumber;

}
