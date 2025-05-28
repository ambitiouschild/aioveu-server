package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@Data
@TableName("sport_extension_position")
public class ExtensionPosition extends IdEntity {

    @NotNull(message = "themeId can not be null!")
    private Long themeId;

    @NotBlank(message = "extensionId can not be null!")
    private String extensionId;

    private Integer runStep;

    /**
     * 百度经度
     */
    private Double bLongitude;

    /**
     * 百度维度
     */
    private Double bLatitude;

    private Double longitude;

    private Double latitude;

    private String province;

    private String city;

    private String district;

    private String town;

    private String street;

    private String address;

    private String business;

    private String adcode;

}
