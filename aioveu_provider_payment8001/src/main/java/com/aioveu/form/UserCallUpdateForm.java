package com.aioveu.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/13 15:35
 */
@Data
public class UserCallUpdateForm {

    @NotNull(message = "Id不能为空")
    private Long id;

    private Boolean call;

    private Boolean intention;

    private String study;

    private String noIntentionReason;

    private Boolean priceSensitive;
}
