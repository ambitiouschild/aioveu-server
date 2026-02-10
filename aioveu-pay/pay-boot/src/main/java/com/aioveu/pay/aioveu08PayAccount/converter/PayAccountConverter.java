package com.aioveu.pay.aioveu08PayAccount.converter;

import com.aioveu.pay.aioveu08PayAccount.model.entity.PayAccount;
import com.aioveu.pay.aioveu08PayAccount.model.form.PayAccountForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: PayAccountConverter
 * @Description TODO 支付账户对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:12
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface PayAccountConverter {

    PayAccountForm toForm(PayAccount entity);

    PayAccount toEntity(PayAccountForm formData);
}
