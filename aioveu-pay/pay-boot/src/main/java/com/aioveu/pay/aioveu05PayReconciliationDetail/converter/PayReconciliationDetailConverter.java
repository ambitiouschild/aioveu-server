package com.aioveu.pay.aioveu05PayReconciliationDetail.converter;

import com.aioveu.pay.aioveu05PayReconciliationDetail.model.entity.PayReconciliationDetail;
import com.aioveu.pay.aioveu05PayReconciliationDetail.model.form.PayReconciliationDetailForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: PayReconciliationDetailConverter
 * @Description TODO 对账明细对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/9 14:33
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface PayReconciliationDetailConverter {

    PayReconciliationDetailForm toForm(PayReconciliationDetail entity);

    PayReconciliationDetail toEntity(PayReconciliationDetailForm formData);
}
