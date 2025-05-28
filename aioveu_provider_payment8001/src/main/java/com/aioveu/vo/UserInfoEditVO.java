package com.aioveu.vo;

import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/28 0028 21:39
 */
@Data
public class UserInfoEditVO extends BaseNameVO {

    private String childName;

    private String channelCategory;

    private String channelName;

    private Integer age;

    private String regionName;

    private String provinceName;

    private String cityName;

    private String businessAreaName;

    private String address;

    private Integer childGender;

    private Integer childAge;

    /**
     * 孩子兴趣品类
     */
    private String childInterestCategory;

    /**
     * 运动基础备注
     */
    private String childSportBaseRemark;

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
