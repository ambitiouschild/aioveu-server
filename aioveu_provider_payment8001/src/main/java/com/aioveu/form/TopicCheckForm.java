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
public class TopicCheckForm {

    @NotNull(message = "店铺id不能为空")
    private Long storeId;

    @NotEmpty(message = "userId不能为空")
    private String userId;

    @NotNull(message = "位置信息不能为空")
    private Double longitude;

    @NotNull(message = "位置信息不能为空")
    private Double latitude;

}
