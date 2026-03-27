package com.aioveu.registry.aioveu09RegistryOperationLog.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.registry.aioveu09RegistryOperationLog.converter.RegistryOperationLogConverter;
import com.aioveu.registry.aioveu09RegistryOperationLog.mapper.RegistryOperationLogMapper;
import com.aioveu.registry.aioveu09RegistryOperationLog.model.entity.RegistryOperationLog;
import com.aioveu.registry.aioveu09RegistryOperationLog.model.form.RegistryOperationLogForm;
import com.aioveu.registry.aioveu09RegistryOperationLog.model.query.RegistryOperationLogQuery;
import com.aioveu.registry.aioveu09RegistryOperationLog.model.vo.RegistryOperationLogVo;
import com.aioveu.registry.aioveu09RegistryOperationLog.service.RegistryOperationLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: RegistryOperationLogServiceImpl
 * @Description TODO 操作日志服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 19:30
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class RegistryOperationLogServiceImpl extends ServiceImpl<RegistryOperationLogMapper, RegistryOperationLog> implements RegistryOperationLogService {

    private final RegistryOperationLogConverter registryOperationLogConverter;

    /**
     * 获取操作日志分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<RegistryOperationLogVo>} 操作日志分页列表
     */
    @Override
    public IPage<RegistryOperationLogVo> getRegistryOperationLogPage(RegistryOperationLogQuery queryParams) {
        Page<RegistryOperationLogVo> page = this.baseMapper.getRegistryOperationLogPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取操作日志表单数据
     *
     * @param id 操作日志ID
     * @return 操作日志表单数据
     */
    @Override
    public RegistryOperationLogForm getRegistryOperationLogFormData(Long id) {
        RegistryOperationLog entity = this.getById(id);
        return registryOperationLogConverter.toForm(entity);
    }

    /**
     * 新增操作日志
     *
     * @param formData 操作日志表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveRegistryOperationLog(RegistryOperationLogForm formData) {
        RegistryOperationLog entity = registryOperationLogConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新操作日志
     *
     * @param id   操作日志ID
     * @param formData 操作日志表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateRegistryOperationLog(Long id,RegistryOperationLogForm formData) {
        RegistryOperationLog entity = registryOperationLogConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除操作日志
     *
     * @param ids 操作日志ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteRegistryOperationLogs(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的操作日志数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
