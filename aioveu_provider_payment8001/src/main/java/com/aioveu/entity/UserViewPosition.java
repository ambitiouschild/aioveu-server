package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_user_view_position")
@Data
public class UserViewPosition extends IdEntity {

    private String userId;

    private Long categoryId;

    private String productId;

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
