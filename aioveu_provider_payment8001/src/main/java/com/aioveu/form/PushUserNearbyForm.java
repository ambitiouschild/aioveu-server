package com.aioveu.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/10 0010 21:43
 */
@Data
public class PushUserNearbyForm {

    @NotNull(message = "主题id不能为空")
    private Long themeId;

    @NotNull(message = "店铺id不能为空")
    private Long storeId;

    private Double latitude;

    private Double longitude;

    private Integer distance;

}
