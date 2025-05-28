package com.aioveu.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class StoreForm {

    private Long id;

    @NotEmpty(message = "名称不能为空")
    private String name;

    @NotEmpty(message = "分类不能为空")
    private String categoryIds;

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

    @NotNull(message = "经度不能为空")
    private Double longitude;

    @NotNull(message = "维度不能为空")
    private Double latitude;

//    @NotEmpty(message = "logo不能为空")
    private String logo;

    private String introduce;

    private Integer recommendOrder;

    private String tags;

    @NotEmpty(message = "小程序AppId不能为空")
    private String appId;

    /**
     * 小程序店铺路径
     */
    private String path;

    private String companyName;

    private String userId;

    private String phone;

    private Integer status;

    private Date createDate;


}
