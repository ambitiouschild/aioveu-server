package com.aioveu.sms.aioveu08HomeAdvert.mapper;

import com.aioveu.sms.aioveu08HomeAdvert.model.entity.SmsHomeAdvert;
import com.aioveu.sms.aioveu08HomeAdvert.model.query.SmsHomeAdvertQuery;
import com.aioveu.sms.aioveu08HomeAdvert.model.vo.SmsHomeAdvertVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: SmsHomeAdvertMapper
 * @Description TODO 首页广告配置（增加跳转路径）Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/4 12:35
 * @Version 1.0
 **/
@Mapper
public interface SmsHomeAdvertMapper extends BaseMapper<SmsHomeAdvert> {

    /**
     * 获取首页广告配置（增加跳转路径）分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<SmsHomeAdvertVO>} 首页广告配置（增加跳转路径）分页列表
     */
    Page<SmsHomeAdvertVO> getSmsHomeAdvertPage(Page<SmsHomeAdvertVO> page, SmsHomeAdvertQuery queryParams);
}
