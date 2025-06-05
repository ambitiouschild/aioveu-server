package com.aioveu.sms.converter;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.sms.model.entity.SmsAdvert;
import com.aioveu.sms.model.vo.AdvertPageVO;
import com.aioveu.sms.model.vo.BannerVO;
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
public interface AdvertConverter {

    AdvertPageVO entity2PageVo(SmsAdvert entity);

    Page<AdvertPageVO> entity2PageVo(Page<SmsAdvert> po);

    BannerVO entity2BannerVo(SmsAdvert entity);
    
    List<BannerVO> entity2BannerVo(List<SmsAdvert> entities);
}