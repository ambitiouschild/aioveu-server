package com.aioveu.refund.aioveu01RefundOrder.converter;

import com.aioveu.refund.aioveu01RefundOrder.model.entity.RefundOrder;
import com.aioveu.refund.aioveu01RefundOrder.model.form.RefundOrderForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: RefundOrderConverter
 * @Description TODO  订单退款申请对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 16:29
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface RefundOrderConverter {

    RefundOrderForm toForm(RefundOrder entity);

    RefundOrder toEntity(RefundOrderForm formData);
}
