package com.aioveu.order.model.aioveu01Order.form;

import com.aioveu.common.enums.oms.LogisticsTypeEnum;
import com.aioveu.common.enums.pay.PaymentChannelEnum;
import com.aioveu.common.enums.pay.PaymentMethodEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description: TODO 微信发货上下文（统一手动/自动的参数来源）
 * @Author: 雒世松
 * @Date: 2026/10/47 10:48
 * @param
 * @return:
 **/
/**
 * 微信发货上下文（统一手动/自动的参数来源）
 */
@Data
@Schema(description ="微信发货上下文")
public class ShippingContext {

    private String trackingNo;
    private String expressCompanyCode;
    private String receiverPhone;
    private LogisticsTypeEnum logisticsType; // 前端/DB 传的覆盖值，可为 null

}
