package com.aioveu.ums.aioveu02MemberAddress.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: UmsMemberAddressVO
 * @Description TODO  会员收货地址视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/12 15:30
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "会员收货地址视图对象")
public class UmsMemberAddressVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "地址ID")
    private Long id;
    @Schema(description = "会员ID")
    private Long memberId;
    @Schema(description = "收货人姓名")
    private String consigneeName;
    @Schema(description = "收货人联系方式")
    private String consigneeMobile;
    @Schema(description = "国家")
    private String country;
    @Schema(description = "省份")
    private String province;
    @Schema(description = "城市")
    private String city;
    @Schema(description = "区/县")
    private String district;
    @Schema(description = "街道")
    private String street;
    @Schema(description = "详细地址")
    private String detailAddress;
    @Schema(description = "邮政编码")
    private String postalCode;
    @Schema(description = "是否默认地址(0=否,1=是)")
    private Integer defaulted;
    @Schema(description = "地址标签(家,公司,学校等)")
    private String addressTag;
    @Schema(description = "经度")
    private BigDecimal longitude;
    @Schema(description = "纬度")
    private BigDecimal latitude;
    @Schema(description = "状态(0=已删除,1=正常)")
    private Integer status;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
