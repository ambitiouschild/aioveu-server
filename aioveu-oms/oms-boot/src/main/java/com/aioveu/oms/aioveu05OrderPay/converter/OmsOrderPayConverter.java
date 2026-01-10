package com.aioveu.oms.aioveu05OrderPay.converter;

import com.aioveu.oms.aioveu05OrderPay.model.entity.OmsOrderPay;
import com.aioveu.oms.aioveu05OrderPay.model.form.OmsOrderPayForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: OmsOrderPayConverter
 * @Description TODO  支付信息对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 17:00
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface OmsOrderPayConverter {

    OmsOrderPayForm toForm(OmsOrderPay entity);

    OmsOrderPay toEntity(OmsOrderPayForm formData);
}
