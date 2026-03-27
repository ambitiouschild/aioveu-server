package com.aioveu.registry.aioveu05RegistryCertification.mapper;

import com.aioveu.registry.aioveu05RegistryCertification.model.entity.RegistryCertification;
import com.aioveu.registry.aioveu05RegistryCertification.model.query.RegistryCertificationQuery;
import com.aioveu.registry.aioveu05RegistryCertification.model.vo.RegistryCertificationVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: RegistryCertificationMapper
 * @Description TODO 认证记录Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:15
 * @Version 1.0
 **/
@Mapper
public interface RegistryCertificationMapper extends BaseMapper<RegistryCertification> {


    /**
     * 获取认证记录分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<RegistryCertificationVo>} 认证记录分页列表
     */
    Page<RegistryCertificationVo> getRegistryCertificationPage(Page<RegistryCertificationVo> page, RegistryCertificationQuery queryParams);

}
