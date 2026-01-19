package com.aioveu.ums.aioveu02MemberAddress.model.form;

import com.aioveu.common.web.constraint.CheckCityValid;
import com.aioveu.common.web.constraint.CityType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: TODO 会员收货地址表单对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:59
 * @param
 * @return:
 **/

@Schema(description = "会员收货地址表单对象")
@Data
public class UmsMemberAddressForm implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "地址ID")
    private Long id;

    @Schema(description="地址关联会员ID")
    private Long memberId;

    @Schema(description="收货人姓名")
    @NotBlank(message = "收货人姓名不能为空")
    @Size(max=64, message="收货人姓名长度不能超过64个字符")
    private String consigneeName;

    @Schema(description="收货人手机号")
    @NotBlank(message = "收货人联系方式不能为空")
    @Pattern(regexp = "^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$", message = "{phone.valid}")
    @Size(max=20, message="收货人联系方式长度不能超过20个字符")
    private String consigneeMobile;

    @Schema(description = "国家")
    @Size(max=50, message="国家长度不能超过50个字符")
    private String country;

    @Schema(description = "省份")
    @Size(max=64, message="省份长度不能超过64个字符")
    private String province;

    @Schema(description = "城市")
    @Size(max=64, message="城市长度不能超过64个字符")
    private String city;

    @Schema(description = "区/县")
    @Size(max=64, message="区/县长度不能超过64个字符")
    private String district;

    @Schema(description = "街道")
    @Size(max=200, message="街道长度不能超过200个字符")
    private String street;

    @Schema(description="详细地址")
    @NotBlank(message = "详细地址不能为空")
    @Size(max=255, message="详细地址长度不能超过255个字符")
    @Length(min = 1, max = 100, message = "{text.length.min}，{text.length.max}")
    private String detailAddress;

    @Schema(description = "邮政编码")
    @Size(max=6, message="邮政编码长度不能超过6个字符")
    private String postalCode;

    @Schema(description = "是否默认地址(0=否,1=是)")
    private Integer defaulted;

    @Schema(description = "地址标签(家,公司,学校等)")
    @Size(max=20, message="地址标签(家,公司,学校等)长度不能超过20个字符")
    private String addressTag;

    @Schema(description = "经度")
    private BigDecimal longitude;

    @Schema(description = "纬度")
    private BigDecimal latitude;

    @Schema(description = "状态(0=已删除,1=正常)")
    private Integer status;

}



