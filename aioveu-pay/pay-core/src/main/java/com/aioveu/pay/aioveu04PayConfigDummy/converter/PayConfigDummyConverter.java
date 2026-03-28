package com.aioveu.pay.aioveu04PayConfigDummy.converter;

import com.aioveu.pay.aioveu04PayConfigDummy.model.entity.PayConfigDummy;
import com.aioveu.pay.aioveu04PayConfigDummy.model.form.PayConfigDummyForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: PayConfigDummyConverter
 * @Description TODO 模拟支付配置对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:31
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface PayConfigDummyConverter {

    PayConfigDummyForm toForm(PayConfigDummy entity);

    PayConfigDummy toEntity(PayConfigDummyForm formData);
}
