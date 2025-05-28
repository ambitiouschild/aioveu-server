package com.aioveu.form;

import com.aioveu.entity.FieldPlan;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/10 0010 21:43
 */
@Data
public class FieldOrderForm {

    @NotEmpty(message = "手机不能为空")
    private String phone;

    @NotEmpty(message = "姓名不能为空")
    private String name;

    private String gender;

    @NotEmpty(message = "userId不能为空")
    private String userId;

    private List<FieldPlan> fieldPlans;

    private Long userCouponId;

    private String wxPayAppId;

    private String remark;

}
