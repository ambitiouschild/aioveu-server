package com.aioveu.sms.aioveu01Advert.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.sms.aioveu01Advert.model.entity.SmsAdvert;
import com.aioveu.sms.aioveu01Advert.model.query.AdvertPageQuery;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SmsAdvertMapper extends BaseMapper<SmsAdvert> {

    /**
     * 广告分页列表
     *
     * @param page
     * @param queryParams
     * @return
     */
    Page<SmsAdvert> getAdvertPage(Page<SmsAdvert> page, AdvertPageQuery queryParams);
}
