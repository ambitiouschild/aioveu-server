package com.aioveu.pay.aioveu01PayOrder.converter;

import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu01PayOrder.model.form.PayOrderForm;
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

@Mapper(componentModel = "spring")
public interface PayOrderConverter {

    //java: Can't map property "BigDecimal paymentAmount" to "BigDecimal paymentAmount".
    // Consider to declare/implement a mapping method: "BigDecimal map(BigDecimal value)".
    // 或者使用@Mapping的source属性
    PayOrderForm toForm(PayOrder entity);


    PayOrder toEntity(PayOrderForm formData);


}
