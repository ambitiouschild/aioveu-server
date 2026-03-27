package com.aioveu.registry.aioveu06RegistryCertificationContact.mapper;

import com.aioveu.registry.aioveu06RegistryCertificationContact.model.entity.RegistryCertificationContact;
import com.aioveu.registry.aioveu06RegistryCertificationContact.model.query.RegistryCertificationContactQuery;
import com.aioveu.registry.aioveu06RegistryCertificationContact.model.vo.RegistryCertificationContactVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: RegistryCertificationContactMapper
 * @Description TODO 认证联系人Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:28
 * @Version 1.0
 **/
@Mapper
public interface RegistryCertificationContactMapper extends BaseMapper<RegistryCertificationContact> {

    /**
     * 获取认证联系人分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<RegistryCertificationContactVo>} 认证联系人分页列表
     */
    Page<RegistryCertificationContactVo> getRegistryCertificationContactPage(Page<RegistryCertificationContactVo> page, RegistryCertificationContactQuery queryParams);

}
