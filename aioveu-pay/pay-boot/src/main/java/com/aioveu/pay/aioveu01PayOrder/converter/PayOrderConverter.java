package com.aioveu.pay.aioveu01PayOrder.converter;

import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu01PayOrder.model.form.PayOrderForm;
import com.aioveu.pay.aioveu07PayNotify.model.vo.PayNotifyDTO;
import com.aioveu.pay.aioveuModule.model.vo.PaymentRequestDTO;
import com.ibm.icu.math.BigDecimal;
import org.mapstruct.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapping;

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

    //java: Can't map property "BigDecimal paymentAmount" to "BigDecimal paymentAmount".
    // Consider to declare/implement a mapping method: "BigDecimal map(BigDecimal value)".
    // 或者使用@Mapping的source属性
    PayOrderForm toForm(PayOrder entity);


    PayOrder toEntity(PayOrderForm formData);

    PayNotifyDTO  toPayNotifyDTO(PayOrder order );


    //是的，MapStruct 只会映射你显式指定的字段。如果没有添加 @Mapping注解，它只会自动映射同名同类型的字段。
    // 没有 @Mapping 注解时，只映射同名同类型字段
    @Mapping(target = "paymentNo", expression = "java(generateTradeNo())")
    @Mapping(target = "paymentStatus", constant = "0")
    @Mapping(target = "bizType", constant = "bizType") //必填
    @Mapping(target = "paymentChannel", constant = "channel") //必填
    PayOrder toPayOrder(PaymentRequestDTO paymentRequestDTO);

    default String generateTradeNo() {
        return "PAY" + System.currentTimeMillis() + (int)((Math.random() * 9 + 1) * 100000);
    }
}
