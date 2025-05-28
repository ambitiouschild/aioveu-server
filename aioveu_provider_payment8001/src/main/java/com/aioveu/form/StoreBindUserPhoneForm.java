package com.aioveu.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/10 0010 21:43
 */
@Data
public class StoreBindUserPhoneForm {
    @NotEmpty(message = "id不能为空")
    private Long id;

    private Long storeId;

    @NotEmpty(message = "手机号不能为空")
    private String phone;

    private Long companyId;


}
