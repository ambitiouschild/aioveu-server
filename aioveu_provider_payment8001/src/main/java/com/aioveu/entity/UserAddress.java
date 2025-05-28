package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @description 公司
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_user_address")
@Data
public class UserAddress extends IdEntity{

    @NotNull(message = "省份不能为空")
    private Long provinceId;

    @NotNull(message = "城市不能为空")
    private Long cityId;

    @NotNull(message = "区域不能为空")
    private Long regionId;

    @NotEmpty(message = "地址不能为空")
    private String address;

    private String userId;

    private String username;

    @NotEmpty(message = "电话不能为空")
    private String phone;

    private Boolean defaultAddress;

}
