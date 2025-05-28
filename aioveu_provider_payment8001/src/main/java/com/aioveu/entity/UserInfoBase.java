package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@Data
public class UserInfoBase extends IdNameEntity {

    private Long companyId;

    private String phone;

    private Integer age;

    private Long provinceId;

    private String provinceName;

    private Long cityId;

    private String cityName;

    private Long regionId;

    private String regionName;

    private Long businessAreaId;

    private String businessAreaName;

    private String address;

    private Integer status;

    private Date lastCall;

    private String channelCategory;

    private String channelName;

    private String childName;

    private Integer childGender;

    private Integer childAge;

    private String childInterestCategory;

    private String childSportBaseRemark;

    @TableField(exist = false)
    private String groupName;

}
