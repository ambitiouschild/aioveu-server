package com.aioveu.oms.aioveu01Order.model.form;

import com.aioveu.oms.aioveu01Order.enums.OrderStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: OmsOrderForm
 * @Description TODO   订单发货表单对象
                         * 订单号（orderSn / orderId）一定要放在 URL 路径里
                         * 物流信息（公司、运单号、备注）放在 body（data）里
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/8 18:26
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "订单发货表单对象")
public class ShipOrderDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "物流公司")
    private String logisticsCompany;


    @Schema(description = "物流单号")
    private String  trackingNo;

    @Schema(description = "发货备注")
    private String  remark;
}
