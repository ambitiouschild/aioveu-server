package com.aioveu.registry.aioveu06RegistryCertificationContact.service;

import com.aioveu.registry.aioveu06RegistryCertificationContact.model.entity.RegistryCertificationContact;
import com.aioveu.registry.aioveu06RegistryCertificationContact.model.form.RegistryCertificationContactForm;
import com.aioveu.registry.aioveu06RegistryCertificationContact.model.query.RegistryCertificationContactQuery;
import com.aioveu.registry.aioveu06RegistryCertificationContact.model.vo.RegistryCertificationContactVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: RegistryCertificationContactService
 * @Description TODO 认证联系人服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:30
 * @Version 1.0
 **/

public interface RegistryCertificationContactService extends IService<RegistryCertificationContact> {

    /**
     *认证联系人分页列表
     *
     * @return {@link IPage<RegistryCertificationContactVo>} 认证联系人分页列表
     */
    IPage<RegistryCertificationContactVo> getRegistryCertificationContactPage(RegistryCertificationContactQuery queryParams);

    /**
     * 获取认证联系人表单数据
     *
     * @param id 认证联系人ID
     * @return 认证联系人表单数据
     */
    RegistryCertificationContactForm getRegistryCertificationContactFormData(Long id);

    /**
     * 新增认证联系人
     *
     * @param formData 认证联系人表单对象
     * @return 是否新增成功
     */
    boolean saveRegistryCertificationContact(RegistryCertificationContactForm formData);

    /**
     * 修改认证联系人
     *
     * @param id   认证联系人ID
     * @param formData 认证联系人表单对象
     * @return 是否修改成功
     */
    boolean updateRegistryCertificationContact(Long id, RegistryCertificationContactForm formData);

    /**
     * 删除认证联系人
     *
     * @param ids 认证联系人ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteRegistryCertificationContacts(String ids);
}
