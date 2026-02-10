package com.aioveu.pay.aioveu06PayFlow.converter;

import com.aioveu.pay.aioveu06PayFlow.model.entity.PayFlow;
import com.aioveu.pay.aioveu06PayFlow.model.form.PayFlowForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: PayFlowConverter
 * @Description TODO 支付流水对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/9 15:55
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface PayFlowConverter {

    PayFlowForm toForm(PayFlow entity);

    PayFlow toEntity(PayFlowForm formData);
}
