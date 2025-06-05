package com.aioveu.sms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.sms.model.entity.SmsAdvert;
import com.aioveu.sms.model.query.AdvertPageQuery;
import com.aioveu.sms.model.vo.AdvertPageVO;
import com.aioveu.sms.model.vo.BannerVO;

import java.util.List;

public interface SmsAdvertService extends IService<SmsAdvert> {

    /**
     * 广告分页列表
     *
     * @param queryParams
     * @return
     */
    Page<AdvertPageVO> getAdvertPage(AdvertPageQuery queryParams);

    List<BannerVO> getBannerList();
}
