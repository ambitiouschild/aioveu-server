package com.aioveu.registry.aioveu09RegistryOperationLog.mapper;

import com.aioveu.registry.aioveu09RegistryOperationLog.model.entity.RegistryOperationLog;
import com.aioveu.registry.aioveu09RegistryOperationLog.model.query.RegistryOperationLogQuery;
import com.aioveu.registry.aioveu09RegistryOperationLog.model.vo.RegistryOperationLogVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: RegistryOperationLogMapper
 * @Description TODO 操作日志Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 19:28
 * @Version 1.0
 **/
@Mapper
public interface RegistryOperationLogMapper extends BaseMapper<RegistryOperationLog> {

    /**
     * 获取操作日志分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<RegistryOperationLogVo>} 操作日志分页列表
     */
    Page<RegistryOperationLogVo> getRegistryOperationLogPage(Page<RegistryOperationLogVo> page, RegistryOperationLogQuery queryParams);

}
