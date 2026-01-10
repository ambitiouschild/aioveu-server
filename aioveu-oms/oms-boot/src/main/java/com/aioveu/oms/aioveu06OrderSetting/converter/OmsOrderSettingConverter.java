package com.aioveu.oms.aioveu06OrderSetting.converter;

import com.aioveu.oms.aioveu06OrderSetting.model.entity.OmsOrderSetting;
import com.aioveu.oms.aioveu06OrderSetting.model.form.OmsOrderSettingForm;
import org.mapstruct.Mapper;


/**
 * @ClassName: OmsOrderSettingConverter
 * @Description TODO  订单配置信息对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 17:16
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface OmsOrderSettingConverter {

    OmsOrderSettingForm toForm(OmsOrderSetting entity);

    OmsOrderSetting toEntity(OmsOrderSettingForm formData);
}
