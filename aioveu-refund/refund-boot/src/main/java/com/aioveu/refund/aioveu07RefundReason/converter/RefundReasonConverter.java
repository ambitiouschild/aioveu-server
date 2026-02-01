package com.aioveu.refund.aioveu07RefundReason.converter;

import com.aioveu.refund.aioveu07RefundReason.model.entity.RefundReason;
import com.aioveu.refund.aioveu07RefundReason.model.form.RefundReasonForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: RefundReasonConverter
 * @Description TODO 退款原因分类对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:56
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface RefundReasonConverter {

    RefundReasonForm toForm(RefundReason entity);

    RefundReason toEntity(RefundReasonForm formData);
}
