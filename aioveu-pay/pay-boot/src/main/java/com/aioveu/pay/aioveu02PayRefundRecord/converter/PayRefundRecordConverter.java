package com.aioveu.pay.aioveu02PayRefundRecord.converter;

import com.aioveu.pay.aioveu02PayRefundRecord.model.entity.PayRefundRecord;
import com.aioveu.pay.aioveu02PayRefundRecord.model.form.PayRefundRecordForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: PayRefundRecordConverter
 * @Description TODO  退款记录对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 18:52
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface PayRefundRecordConverter {

    PayRefundRecordForm toForm(PayRefundRecord entity);

    PayRefundRecord toEntity(PayRefundRecordForm formData);
}
