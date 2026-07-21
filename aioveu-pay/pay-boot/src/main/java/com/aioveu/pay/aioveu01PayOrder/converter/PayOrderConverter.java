package com.aioveu.pay.aioveu01PayOrder.converter;

import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.model.aioveuPayment.request.PaymentRequestOmsToPayDTO;
import com.aioveu.pay.model.aioveu01PayOrder.vo.PayOrderVO;
import com.aioveu.pay.aioveu07PayNotify.model.vo.PayNotifyDTO;
import com.aioveu.pay.model.aioveu01PayOrder.form.PayOrderForm;
import com.aioveu.pay.model.aioveuPayment.request.PaymentRequestPayToTPPDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * @ClassName: PayOrderConverter
 * @Description TODO  支付订单对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 17:31
 * @Version 1.0
 **/

//方案4：如果使用 MapStruct  // 必须是 "spring"   // Service 中使用 // 正确注入 MapStruct Mapper  // 接口类型
@Mapper(componentModel = "spring")
public interface PayOrderConverter {



    @Named("enumToCode")
    default Integer enumToCode(BaseEnum baseEnum) {
        return baseEnum == null ? null : baseEnum.getCode();
    }
    //java: Can't map property "BigDecimal paymentAmount" to "BigDecimal paymentAmount".
    // Consider to declare/implement a mapping method: "BigDecimal map(BigDecimal value)".
    // 或者使用@Mapping的source属性
    @Mapping(target = "bizType", source = "bizType", qualifiedByName = "enumToCode")
    @Mapping(target = "paymentStatus", source = "paymentStatus", qualifiedByName = "enumToCode")
    @Mapping(target = "paymentChannel", source = "paymentChannel", qualifiedByName = "enumToCode")
    @Mapping(target = "paymentMethod", source = "paymentMethod", qualifiedByName = "enumToCode")
    PayOrderForm toForm(PayOrder entity);

    PayOrderVO toVO(PayOrder entity);


    @Mapping(target = "paymentChannel",
            expression = "java(com.aioveu.common.enums.pay.PaymentChannelEnum.fromCode(form.getPaymentChannel(), PaymentChannelEnum.class))")
    @Mapping(target = "paymentMethod",
            expression = "java(com.aioveu.common.enums.pay.PaymentMethodEnum.fromCode(form.getPaymentMethod(), PaymentMethodEnum.class))")
    @Mapping(target = "bizType",
            expression = "java(com.aioveu.common.enums.pay.PaymentBizTypeEnum.fromCode(form.getBizType(), PaymentBizTypeEnum.class))")
    PayOrder toEntity(PayOrderForm formData);

    PayNotifyDTO  toPayNotifyDTO(PayOrder order );


    //是的，MapStruct 只会映射你显式指定的字段。如果没有添加 @Mapping注解，它只会自动映射同名同类型的字段。
    // 没有 @Mapping 注解时，只映射同名同类型字段
    @Mapping(target = "paymentNo", expression = "java(generateTradeNo())")
    PayOrder toPayOrder(PaymentRequestPayToTPPDTO paymentRequestPayToTPPDTO);

    default String generateTradeNo() {
        return "PAY" + System.currentTimeMillis() + (int)((Math.random() * 9 + 1) * 100000);
    }
}
