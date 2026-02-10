package com.aioveu.pay.aioveu09PayAccountFlow.converter;

import com.aioveu.pay.aioveu09PayAccountFlow.model.entity.PayAccountFlow;
import com.aioveu.pay.aioveu09PayAccountFlow.model.form.PayAccountFlowForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: PayAccountFlowConverter
 * @Description TODO 账户流水对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:30
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface PayAccountFlowConverter {

    PayAccountFlowForm toForm(PayAccountFlow entity);

    PayAccountFlow toEntity(PayAccountFlowForm formData);
}
