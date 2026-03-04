package com.aioveu.sms.aioveu07HomeCategory.mapper;

import com.aioveu.sms.aioveu07HomeCategory.model.entity.SmsHomeCategory;
import com.aioveu.sms.aioveu07HomeCategory.model.query.SmsHomeCategoryQuery;
import com.aioveu.sms.aioveu07HomeCategory.model.vo.SmsHomeCategoryVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: SmsHomeCategoryMapper
 * @Description TODO 首页分类配置Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/4 12:13
 * @Version 1.0
 **/
@Mapper
public interface SmsHomeCategoryMapper extends BaseMapper<SmsHomeCategory> {

    /**
     * 获取首页分类配置分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<SmsHomeCategoryVO>} 首页分类配置分页列表
     */
    Page<SmsHomeCategoryVO> getSmsHomeCategoryPage(Page<SmsHomeCategoryVO> page, SmsHomeCategoryQuery queryParams);
}
