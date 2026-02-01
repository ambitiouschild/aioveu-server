package com.aioveu.refund.aioveu03RefundDelivery.converter;

import com.aioveu.refund.aioveu03RefundDelivery.model.entity.RefundDelivery;
import com.aioveu.refund.aioveu03RefundDelivery.model.form.RefundDeliveryForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: RefundDeliveryConverter
 * @Description TODO 退款物流信息（用于退货）对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 18:02
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface RefundDeliveryConverter {

    RefundDeliveryForm toForm(RefundDelivery entity);

    RefundDelivery toEntity(RefundDeliveryForm formData);
}
