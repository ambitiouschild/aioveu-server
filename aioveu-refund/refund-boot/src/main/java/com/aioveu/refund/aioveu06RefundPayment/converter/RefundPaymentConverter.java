package com.aioveu.refund.aioveu06RefundPayment.converter;

import com.aioveu.refund.aioveu06RefundPayment.model.entity.RefundPayment;
import com.aioveu.refund.aioveu06RefundPayment.model.form.RefundPaymentForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: RefundPaymentConverter
 * @Description TODO 退款支付记录对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:30
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface RefundPaymentConverter {

    RefundPaymentForm toForm(RefundPayment entity);

    RefundPayment toEntity(RefundPaymentForm formData);
}
