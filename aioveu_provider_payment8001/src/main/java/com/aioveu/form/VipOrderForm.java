package com.aioveu.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @description 订场表单
 * @author: 雒世松
 * @date: 2025/1/10 0010 21:43
 */
@Data
public class VipOrderForm {

    private Long storeId;

    @NotNull(message = "VIP卡片ID不能为空")
    private Long vipCardId;

    private String coachUserId;

    @NotEmpty(message = "userId不能为空")
    private String userId;

}
