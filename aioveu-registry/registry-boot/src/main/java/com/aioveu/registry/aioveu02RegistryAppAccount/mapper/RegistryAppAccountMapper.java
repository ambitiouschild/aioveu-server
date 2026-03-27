package com.aioveu.registry.aioveu02RegistryAppAccount.mapper;

import com.aioveu.registry.aioveu02RegistryAppAccount.model.entity.RegistryAppAccount;
import com.aioveu.registry.aioveu02RegistryAppAccount.model.query.RegistryAppAccountQuery;
import com.aioveu.registry.aioveu02RegistryAppAccount.model.vo.RegistryAppAccountVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: RegistryAppAccountMapper
 * @Description TODO 小程序账号Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:04
 * @Version 1.0
 **/
@Mapper
public interface RegistryAppAccountMapper extends BaseMapper<RegistryAppAccount> {

    /**
     * 获取小程序账号分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<RegistryAppAccountVo>} 小程序账号分页列表
     */
    Page<RegistryAppAccountVo> getRegistryAppAccountPage(Page<RegistryAppAccountVo> page, RegistryAppAccountQuery queryParams);
}
