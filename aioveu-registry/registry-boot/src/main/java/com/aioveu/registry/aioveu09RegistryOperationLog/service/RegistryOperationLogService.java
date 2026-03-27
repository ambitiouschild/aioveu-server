package com.aioveu.registry.aioveu09RegistryOperationLog.service;

import com.aioveu.registry.aioveu09RegistryOperationLog.model.entity.RegistryOperationLog;
import com.aioveu.registry.aioveu09RegistryOperationLog.model.form.RegistryOperationLogForm;
import com.aioveu.registry.aioveu09RegistryOperationLog.model.query.RegistryOperationLogQuery;
import com.aioveu.registry.aioveu09RegistryOperationLog.model.vo.RegistryOperationLogVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: RegistryOperationLogService
 * @Description TODO 操作日志服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 19:30
 * @Version 1.0
 **/

public interface RegistryOperationLogService extends IService<RegistryOperationLog> {

    /**
     *操作日志分页列表
     *
     * @return {@link IPage<RegistryOperationLogVo>} 操作日志分页列表
     */
    IPage<RegistryOperationLogVo> getRegistryOperationLogPage(RegistryOperationLogQuery queryParams);

    /**
     * 获取操作日志表单数据
     *
     * @param id 操作日志ID
     * @return 操作日志表单数据
     */
    RegistryOperationLogForm getRegistryOperationLogFormData(Long id);

    /**
     * 新增操作日志
     *
     * @param formData 操作日志表单对象
     * @return 是否新增成功
     */
    boolean saveRegistryOperationLog(RegistryOperationLogForm formData);

    /**
     * 修改操作日志
     *
     * @param id   操作日志ID
     * @param formData 操作日志表单对象
     * @return 是否修改成功
     */
    boolean updateRegistryOperationLog(Long id, RegistryOperationLogForm formData);

    /**
     * 删除操作日志
     *
     * @param ids 操作日志ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteRegistryOperationLogs(String ids);
}
