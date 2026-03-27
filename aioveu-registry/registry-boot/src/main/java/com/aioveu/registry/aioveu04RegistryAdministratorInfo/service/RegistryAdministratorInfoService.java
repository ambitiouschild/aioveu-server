package com.aioveu.registry.aioveu04RegistryAdministratorInfo.service;

import com.aioveu.registry.aioveu04RegistryAdministratorInfo.model.entity.RegistryAdministratorInfo;
import com.aioveu.registry.aioveu04RegistryAdministratorInfo.model.form.RegistryAdministratorInfoForm;
import com.aioveu.registry.aioveu04RegistryAdministratorInfo.model.query.RegistryAdministratorInfoQuery;
import com.aioveu.registry.aioveu04RegistryAdministratorInfo.model.vo.RegistryAdministratorInfoVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: RegistryAdministratorInfoService
 * @Description TODO 管理员信息服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:48
 * @Version 1.0
 **/

public interface RegistryAdministratorInfoService extends IService<RegistryAdministratorInfo> {

    /**
     *管理员信息分页列表
     *
     * @return {@link IPage<RegistryAdministratorInfoVo>} 管理员信息分页列表
     */
    IPage<RegistryAdministratorInfoVo> getRegistryAdministratorInfoPage(RegistryAdministratorInfoQuery queryParams);

    /**
     * 获取管理员信息表单数据
     *
     * @param id 管理员信息ID
     * @return 管理员信息表单数据
     */
    RegistryAdministratorInfoForm getRegistryAdministratorInfoFormData(Long id);

    /**
     * 新增管理员信息
     *
     * @param formData 管理员信息表单对象
     * @return 是否新增成功
     */
    boolean saveRegistryAdministratorInfo(RegistryAdministratorInfoForm formData);

    /**
     * 修改管理员信息
     *
     * @param id   管理员信息ID
     * @param formData 管理员信息表单对象
     * @return 是否修改成功
     */
    boolean updateRegistryAdministratorInfo(Long id, RegistryAdministratorInfoForm formData);

    /**
     * 删除管理员信息
     *
     * @param ids 管理员信息ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteRegistryAdministratorInfos(String ids);
}
