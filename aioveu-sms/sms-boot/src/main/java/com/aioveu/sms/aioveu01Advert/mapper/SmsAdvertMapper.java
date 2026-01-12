package com.aioveu.sms.aioveu01Advert.mapper;

import com.aioveu.sms.aioveu01Advert.model.vo.SmsAdvertVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.sms.aioveu01Advert.model.entity.SmsAdvert;
import com.aioveu.sms.aioveu01Advert.model.query.SmsAdvertQuery;
import org.apache.ibatis.annotations.Mapper;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 广告Mapper接口
 * @Date  2026/1/12 10:39
 * @Param
 * @return
 **/

@Mapper
public interface SmsAdvertMapper extends BaseMapper<SmsAdvert> {

    /**
     * 广告分页列表
     *
     * @param page
     * @param queryParams
     * @return
     */
    Page<SmsAdvert> getAdvertPage(Page<SmsAdvert> page, SmsAdvertQuery queryParams);

    /**
     * 获取广告分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<SmsAdvertVO>} 广告分页列表
     */
    Page<SmsAdvertVO> getSmsAdvertPage(Page<SmsAdvertVO> page, SmsAdvertQuery queryParams);
}
