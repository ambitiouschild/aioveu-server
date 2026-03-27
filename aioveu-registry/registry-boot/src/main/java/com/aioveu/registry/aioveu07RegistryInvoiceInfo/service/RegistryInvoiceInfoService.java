package com.aioveu.registry.aioveu07RegistryInvoiceInfo.service;

import com.aioveu.registry.aioveu07RegistryInvoiceInfo.model.entity.RegistryInvoiceInfo;
import com.aioveu.registry.aioveu07RegistryInvoiceInfo.model.form.RegistryInvoiceInfoForm;
import com.aioveu.registry.aioveu07RegistryInvoiceInfo.model.query.RegistryInvoiceInfoQuery;
import com.aioveu.registry.aioveu07RegistryInvoiceInfo.model.vo.RegistryInvoiceInfoVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: RegistryInvoiceInfoService
 * @Description TODO 发票信息服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:40
 * @Version 1.0
 **/

public interface RegistryInvoiceInfoService extends IService<RegistryInvoiceInfo> {

    /**
     *发票信息分页列表
     *
     * @return {@link IPage<RegistryInvoiceInfoVo>} 发票信息分页列表
     */
    IPage<RegistryInvoiceInfoVo> getRegistryInvoiceInfoPage(RegistryInvoiceInfoQuery queryParams);

    /**
     * 获取发票信息表单数据
     *
     * @param id 发票信息ID
     * @return 发票信息表单数据
     */
    RegistryInvoiceInfoForm getRegistryInvoiceInfoFormData(Long id);

    /**
     * 新增发票信息
     *
     * @param formData 发票信息表单对象
     * @return 是否新增成功
     */
    boolean saveRegistryInvoiceInfo(RegistryInvoiceInfoForm formData);

    /**
     * 修改发票信息
     *
     * @param id   发票信息ID
     * @param formData 发票信息表单对象
     * @return 是否修改成功
     */
    boolean updateRegistryInvoiceInfo(Long id, RegistryInvoiceInfoForm formData);

    /**
     * 删除发票信息
     *
     * @param ids 发票信息ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteRegistryInvoiceInfos(String ids);
}
