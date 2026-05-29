package com.aioveu.pay.aioveu13PayCallbackRecord.converter;


import com.aioveu.pay.aioveu13PayCallbackRecord.model.entity.PayCallbackRecord;
import com.aioveu.pay.aioveu13PayCallbackRecord.model.form.PayCallbackRecordForm;
import org.mapstruct.Mapper;


/**
 * @ClassName: PayCallbackRecordConverter
 * @Description TODO 支付回调记录对象转换器
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/29 18:12
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface PayCallbackRecordConverter {


    PayCallbackRecordForm toForm(PayCallbackRecord entity);

    PayCallbackRecord toEntity(PayCallbackRecordForm formData);
}
