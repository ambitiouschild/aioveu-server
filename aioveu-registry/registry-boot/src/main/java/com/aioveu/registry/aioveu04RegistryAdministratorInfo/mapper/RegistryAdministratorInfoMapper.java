package com.aioveu.registry.aioveu04RegistryAdministratorInfo.mapper;

import com.aioveu.registry.aioveu04RegistryAdministratorInfo.model.entity.RegistryAdministratorInfo;
import com.aioveu.registry.aioveu04RegistryAdministratorInfo.model.query.RegistryAdministratorInfoQuery;
import com.aioveu.registry.aioveu04RegistryAdministratorInfo.model.vo.RegistryAdministratorInfoVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: RegistryAdministratorInfoMapper
 * @Description TODO 管理员信息Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:46
 * @Version 1.0
 **/
@Mapper
public interface RegistryAdministratorInfoMapper extends BaseMapper<RegistryAdministratorInfo> {

    /**
     * 获取管理员信息分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<RegistryAdministratorInfoVo>} 管理员信息分页列表
     */
    Page<RegistryAdministratorInfoVo> getRegistryAdministratorInfoPage(Page<RegistryAdministratorInfoVo> page, RegistryAdministratorInfoQuery queryParams);

}
