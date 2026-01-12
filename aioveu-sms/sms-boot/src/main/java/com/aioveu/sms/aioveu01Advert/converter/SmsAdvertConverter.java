package com.aioveu.sms.aioveu01Advert.converter;


import com.aioveu.sms.aioveu01Advert.model.form.SmsAdvertForm;
import com.aioveu.sms.aioveu01Advert.model.vo.SmsAdvertVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.sms.aioveu01Advert.model.entity.SmsAdvert;
import com.aioveu.sms.aioveu01Advert.model.vo.BannerVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Description: TODO advert实体转换器
 * @Author: 雒世松
 * @Date: 2025/6/5 18:45
 * @param
 * @return:
 **/

@Mapper(componentModel = "spring")
public interface SmsAdvertConverter {

    SmsAdvertVO entity2PageVo(SmsAdvert entity);

    Page<SmsAdvertVO> entity2PageVo(Page<SmsAdvert> po);

    BannerVO entity2BannerVo(SmsAdvert entity);
    
    List<BannerVO> entity2BannerVo(List<SmsAdvert> entities);

    SmsAdvertForm toForm(SmsAdvert entity);

    SmsAdvert toEntity(SmsAdvertForm formData);
}