package com.aioveu.registry.aioveu03RegistryEnterpriseQualification.mapper;

import com.aioveu.registry.aioveu03RegistryEnterpriseQualification.model.entity.RegistryEnterpriseQualification;
import com.aioveu.registry.aioveu03RegistryEnterpriseQualification.model.query.RegistryEnterpriseQualificationQuery;
import com.aioveu.registry.aioveu03RegistryEnterpriseQualification.model.vo.RegistryEnterpriseQualificationVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: RegistryEnterpriseQualificationMapper
 * @Description TODO 企业资质Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:32
 * @Version 1.0
 **/
@Mapper
public interface RegistryEnterpriseQualificationMapper extends BaseMapper<RegistryEnterpriseQualification> {

    /**
     * 获取企业资质分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<RegistryEnterpriseQualificationVo>} 企业资质分页列表
     */
    Page<RegistryEnterpriseQualificationVo> getRegistryEnterpriseQualificationPage(Page<RegistryEnterpriseQualificationVo> page, RegistryEnterpriseQualificationQuery queryParams);

}
