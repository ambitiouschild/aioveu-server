package com.aioveu.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description 订场表单
 * @author: 雒世松
 * @date: 2025/1/10 0010 21:43
 */
@Data
public class FieldForm {

    @NotEmpty(message = "userId不能为空")
    private String userId;

    @NotEmpty(message = "手机号码不能为空")
    private String phone;

    @NotEmpty(message = "姓名不能为空")
    private String username;

    private Integer gender;

    private Long venueId;

    private Long userVipCardId;

    private Long userCouponId;

    /**
     * 场地列表
     */
    private List<Long> fieldIdList;

}
