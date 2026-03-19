package com.aioveu.tenant.aioveu15TenantWxApp.mapper;

import com.aioveu.tenant.aioveu15TenantWxApp.model.entity.TenantWxApp;
import com.aioveu.tenant.aioveu15TenantWxApp.model.query.TenantWxAppQuery;
import com.aioveu.tenant.aioveu15TenantWxApp.model.vo.TenantWxAppVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: TenantWxAppMapper
 * @Description TODO 租户与微信小程序关联Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/19 17:03
 * @Version 1.0
 **/
@Mapper
public interface TenantWxAppMapper extends BaseMapper<TenantWxApp> {

    /**
     * 获取租户与微信小程序关联分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<TenantWxAppVo>} 租户与微信小程序关联分页列表
     */
    Page<TenantWxAppVo> getTenantWxAppPage(Page<TenantWxAppVo> page, TenantWxAppQuery queryParams);
}
