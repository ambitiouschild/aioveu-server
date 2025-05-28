package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @description 商铺
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_store")
@Data
public class Store extends IdNameEntity{

    @NotNull(message = "公司不能为空")
    private Long companyId;

    @NotEmpty(message = "地址不能为空")
    private String address;

    @NotNull(message = "省份不能为空")
    private Long provinceId;

    @NotNull(message = "城市不能为空")
    private Long cityId;

    @NotNull(message = "区域不能为空")
    private Long regionId;

    @NotNull(message = "商圈不能为空")
    private Long businessAreaId;

    @NotEmpty(message = "电话不能为空")
    private String tel;

    private Double longitude;

    private Double latitude;

    private String logo;

    private String introduce;

    private Integer recommendOrder;

    private String tags;

    private String appId;

    /**
     * 小程序店铺路径
     */
    private String path;

    /**
     * 主题光环
     */
    private String topicLogo;

    /**
     * 分类编号
     */
    private String categoryCode;


}
