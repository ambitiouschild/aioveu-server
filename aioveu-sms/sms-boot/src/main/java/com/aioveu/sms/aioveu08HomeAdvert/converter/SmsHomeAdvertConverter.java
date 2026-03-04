package com.aioveu.sms.aioveu08HomeAdvert.converter;

import com.aioveu.sms.aioveu07HomeCategory.model.entity.SmsHomeCategory;
import com.aioveu.sms.aioveu07HomeCategory.model.vo.SmsHomeCategoryVO;
import com.aioveu.sms.aioveu08HomeAdvert.model.entity.SmsHomeAdvert;
import com.aioveu.sms.aioveu08HomeAdvert.model.form.SmsHomeAdvertForm;
import com.aioveu.sms.aioveu08HomeAdvert.model.vo.SmsHomeAdvertVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @ClassName: SmsHomeAdvertConverter
 * @Description TODO 首页广告配置（增加跳转路径）对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/4 12:37
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface SmsHomeAdvertConverter {

    SmsHomeAdvertForm toForm(SmsHomeAdvert entity);

    SmsHomeAdvert toEntity(SmsHomeAdvertForm formData);

    List<SmsHomeAdvertVO> toVO(List<SmsHomeAdvert>  entity);
}
