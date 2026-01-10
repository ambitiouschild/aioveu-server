package com.aioveu.oms.aioveu03OrderDelivery.converter;

import com.aioveu.oms.aioveu03OrderDelivery.model.entity.OmsOrderDelivery;
import com.aioveu.oms.aioveu03OrderDelivery.model.form.OmsOrderDeliveryForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
  *@ClassName: OmsOrderDeliveryConverter
  *@Description TODO 订单物流记录对象转换器
  *@Author 可我不敌可爱
  *@Author 雒世松
  *@Date 2026/1/8 20:22
  *@Version 1.0
  **/

@Mapper(componentModel = "spring")
public interface OmsOrderDeliveryConverter {

    OmsOrderDeliveryForm toForm(OmsOrderDelivery entity);

    OmsOrderDelivery toEntity(OmsOrderDeliveryForm formData);
}
