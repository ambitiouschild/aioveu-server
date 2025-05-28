package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description 用户拨打记录
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName(value = "sport_user_call", excludeProperty = {"callStatusName", "intentionName", "tagList"})
@Data
public class UserCall extends IdEntity {

    private Long userInfoId;

    private String userId;

    /**
     * 拨打状态 默认0 未拨打 1拨打 2微信
     */
    private Integer callStatus;

    private String callStatusName;

    /**
     * 跟进状态 默认0 新客户未联系 1 未接通 2无明确意向 3有意向
     */
    private Integer intention;

    private String intentionName;

    /**
     * 说明
     */
    private String introduce;

    private Boolean priceSensitive;

    private List<UserTag> tagList;

    private Date nextContactTime;

}
