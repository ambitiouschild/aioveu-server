package com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.mapper;

import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.model.entity.ManagerMenuHomeBanner;
import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.model.query.ManagerMenuHomeBannerQuery;
import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.model.vo.ManagerMenuHomeBannerVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;


/**
 * @ClassName: ManagerMenuHomeBannerMapper
 * @Description TODO 管理端app首页滚播栏Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/4 15:39
 * @Version 1.0
 **/
@Mapper
public interface ManagerMenuHomeBannerMapper extends BaseMapper<ManagerMenuHomeBanner> {

    /**
     * 获取管理端app首页滚播栏分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<ManagerMenuHomeBannerVo>} 管理端app首页滚播栏分页列表
     */
    Page<ManagerMenuHomeBannerVo> getManagerMenuHomeBannerPage(Page<ManagerMenuHomeBannerVo> page, ManagerMenuHomeBannerQuery queryParams);

}
