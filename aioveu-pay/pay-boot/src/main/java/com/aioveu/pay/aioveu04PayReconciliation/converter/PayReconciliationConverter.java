package com.aioveu.pay.aioveu04PayReconciliation.converter;

import com.aioveu.pay.aioveu04PayReconciliation.model.entity.PayReconciliation;
import com.aioveu.pay.aioveu04PayReconciliation.model.form.PayReconciliationForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: PayReconciliationConverter
 * @Description TODO 支付对账对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 20:35
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface PayReconciliationConverter {

    PayReconciliationForm toForm(PayReconciliation entity);

    PayReconciliation toEntity(PayReconciliationForm formData);
}
