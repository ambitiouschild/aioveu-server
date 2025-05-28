package com.aioveu.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/28 0028 21:39
 */
@Data
public class UserBaseInfoVO  {

    private String parentName;

    private String provinceName;

    private String cityName;

    private String regionName;

    private String businessAreaName;

    private String channel;

    private String remark;

    private String childName;

    private Integer childAge;

    private Integer childGender;

    /**
     * 客户标签
     */
    private List<String> tags;

    /**
     * 省份id
     */
    private Long provinceId;

    /**
     * 市id
     */
    private Long cityId;

    /**
     * 区id
     */
    private Long regionId;

    /**
     * 区id
     */
    private Long businessAreaId;

}
