package com.aioveu.registry.aioveu05RegistryCertification.service;

import com.aioveu.registry.aioveu05RegistryCertification.model.entity.RegistryCertification;
import com.aioveu.registry.aioveu05RegistryCertification.model.form.RegistryCertificationForm;
import com.aioveu.registry.aioveu05RegistryCertification.model.query.RegistryCertificationQuery;
import com.aioveu.registry.aioveu05RegistryCertification.model.vo.RegistryCertificationVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: RegistryCertificationService
 * @Description TODO 认证记录服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:19
 * @Version 1.0
 **/

public interface RegistryCertificationService extends IService<RegistryCertification> {

    /**
     *认证记录分页列表
     *
     * @return {@link IPage<RegistryCertificationVo>} 认证记录分页列表
     */
    IPage<RegistryCertificationVo> getRegistryCertificationPage(RegistryCertificationQuery queryParams);

    /**
     * 获取认证记录表单数据
     *
     * @param id 认证记录ID
     * @return 认证记录表单数据
     */
    RegistryCertificationForm getRegistryCertificationFormData(Long id);

    /**
     * 新增认证记录
     *
     * @param formData 认证记录表单对象
     * @return 是否新增成功
     */
    boolean saveRegistryCertification(RegistryCertificationForm formData);

    /**
     * 修改认证记录
     *
     * @param id   认证记录ID
     * @param formData 认证记录表单对象
     * @return 是否修改成功
     */
    boolean updateRegistryCertification(Long id, RegistryCertificationForm formData);

    /**
     * 删除认证记录
     *
     * @param ids 认证记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteRegistryCertifications(String ids);
}
