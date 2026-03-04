package com.aioveu.sms.aioveu07HomeCategory.converter;

import com.aioveu.sms.aioveu01Advert.model.entity.SmsAdvert;
import com.aioveu.sms.aioveu07HomeCategory.model.entity.SmsHomeCategory;
import com.aioveu.sms.aioveu07HomeCategory.model.form.SmsHomeCategoryForm;
import com.aioveu.sms.aioveu07HomeCategory.model.vo.SmsHomeCategoryVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @ClassName: SmsHomeCategoryConverter
 * @Description TODO 首页分类配置对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/4 12:15
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface SmsHomeCategoryConverter {

    SmsHomeCategoryForm toForm(SmsHomeCategory entity);

    SmsHomeCategory toEntity(SmsHomeCategoryForm formData);

    List<SmsHomeCategoryVO> toVO(List<SmsHomeCategory>  entity);
}
