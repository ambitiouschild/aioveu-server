package com.aioveu.registry.aioveu08RegistryAppFilingRecord.service;

import com.aioveu.registry.aioveu08RegistryAppFilingRecord.model.entity.RegistryAppFilingRecord;
import com.aioveu.registry.aioveu08RegistryAppFilingRecord.model.form.RegistryAppFilingRecordForm;
import com.aioveu.registry.aioveu08RegistryAppFilingRecord.model.query.RegistryAppFilingRecordQuery;
import com.aioveu.registry.aioveu08RegistryAppFilingRecord.model.vo.RegistryAppFilingRecordVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: RegistryAppFilingRecordService
 * @Description TODO 小程序备案记录服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 19:17
 * @Version 1.0
 **/

public interface RegistryAppFilingRecordService extends IService<RegistryAppFilingRecord> {

    /**
     *小程序备案记录分页列表
     *
     * @return {@link IPage<RegistryAppFilingRecordVo>} 小程序备案记录分页列表
     */
    IPage<RegistryAppFilingRecordVo> getRegistryAppFilingRecordPage(RegistryAppFilingRecordQuery queryParams);

    /**
     * 获取小程序备案记录表单数据
     *
     * @param id 小程序备案记录ID
     * @return 小程序备案记录表单数据
     */
    RegistryAppFilingRecordForm getRegistryAppFilingRecordFormData(Long id);

    /**
     * 新增小程序备案记录
     *
     * @param formData 小程序备案记录表单对象
     * @return 是否新增成功
     */
    boolean saveRegistryAppFilingRecord(RegistryAppFilingRecordForm formData);

    /**
     * 修改小程序备案记录
     *
     * @param id   小程序备案记录ID
     * @param formData 小程序备案记录表单对象
     * @return 是否修改成功
     */
    boolean updateRegistryAppFilingRecord(Long id, RegistryAppFilingRecordForm formData);

    /**
     * 删除小程序备案记录
     *
     * @param ids 小程序备案记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteRegistryAppFilingRecords(String ids);
}
