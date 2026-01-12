package com.aioveu.sms.aioveu03CouponHistory.converter;

import com.aioveu.sms.aioveu03CouponHistory.model.entity.SmsCouponHistory;
import com.aioveu.sms.aioveu03CouponHistory.model.form.SmsCouponHistoryForm;
import org.mapstruct.Mapper;

/**
  *@ClassName: SmsCouponHistoryConverter
  *@Description TODO 优惠券领取/使用记录对象转换器
  *@Author 可我不敌可爱
  *@Author 雒世松
  *@Date 2026/1/12 11:59
  *@Version 1.0
  **/

@Mapper(componentModel = "spring")
public interface SmsCouponHistoryConverter {

    SmsCouponHistoryForm toForm(SmsCouponHistory entity);

    SmsCouponHistory toEntity(SmsCouponHistoryForm formData);
}
