package com.aioveu.registry.aioveu03RegistryEnterpriseQualification.service;

import com.aioveu.registry.aioveu03RegistryEnterpriseQualification.model.entity.RegistryEnterpriseQualification;
import com.aioveu.registry.aioveu03RegistryEnterpriseQualification.model.form.RegistryEnterpriseQualificationForm;
import com.aioveu.registry.aioveu03RegistryEnterpriseQualification.model.query.RegistryEnterpriseQualificationQuery;
import com.aioveu.registry.aioveu03RegistryEnterpriseQualification.model.vo.RegistryEnterpriseQualificationVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: RegistryEnterpriseQualificationService
 * @Description TODO 企业资质服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:34
 * @Version 1.0
 **/

public interface RegistryEnterpriseQualificationService extends IService<RegistryEnterpriseQualification> {

    /**
     *企业资质分页列表
     *
     * @return {@link IPage<RegistryEnterpriseQualificationVo>} 企业资质分页列表
     */
    IPage<RegistryEnterpriseQualificationVo> getRegistryEnterpriseQualificationPage(RegistryEnterpriseQualificationQuery queryParams);

    /**
     * 获取企业资质表单数据
     *
     * @param id 企业资质ID
     * @return 企业资质表单数据
     */
    RegistryEnterpriseQualificationForm getRegistryEnterpriseQualificationFormData(Long id);

    /**
     * 新增企业资质
     *
     * @param formData 企业资质表单对象
     * @return 是否新增成功
     */
    boolean saveRegistryEnterpriseQualification(RegistryEnterpriseQualificationForm formData);

    /**
     * 修改企业资质
     *
     * @param id   企业资质ID
     * @param formData 企业资质表单对象
     * @return 是否修改成功
     */
    boolean updateRegistryEnterpriseQualification(Long id, RegistryEnterpriseQualificationForm formData);

    /**
     * 删除企业资质
     *
     * @param ids 企业资质ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteRegistryEnterpriseQualifications(String ids);
}
