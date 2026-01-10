package com.aioveu.oms.aioveu04OrderLog.converter;

import com.aioveu.oms.aioveu04OrderLog.model.entity.OmsOrderLog;
import com.aioveu.oms.aioveu04OrderLog.model.form.OmsOrderLogForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: OmsOrderLogConverter
 * @Description TODO 订单操作历史记录对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 16:40
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface OmsOrderLogConverter {

    OmsOrderLogForm toForm(OmsOrderLog entity);

    OmsOrderLog toEntity(OmsOrderLogForm formData);
}
