package com.aioveu.ums.aioveu02MemberAddress.model.form;

import com.aioveu.common.web.constraint.CheckCityValid;
import com.aioveu.common.web.constraint.CityType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Description: TODO 地址表单对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:59
 * @param
 * @return:
 **/

@Schema(description = "地址表单对象")
@Data
public class AddressForm {

    @Schema(description="地址ID")
    private Long id;

    @Schema(description="收货人姓名")
    private String consigneeName;

    @Schema(description="收货人手机号")
    @Pattern(regexp = "^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$", message = "{phone.valid}")
    private String consigneeMobile;

    @Schema(description="省")
    @CheckCityValid(CityType.PROVINCE)
    private String province;

    @Schema(description="市")
    @CheckCityValid(CityType.CITY)
    private String city;

    @Schema(description="区")
    @CheckCityValid(CityType.AREA)
    private String area;

    @Schema(description="详细地址")
    @Length(min = 1, max = 100, message = "{text.length.min}，{text.length.max}")
    private String detailAddress;

    @Schema(description="是否默认地址(1:是;0:否)")
    private Integer defaulted;

}



