package com.aioveu.registry.aioveu02RegistryAppAccount.service;

import com.aioveu.registry.aioveu02RegistryAppAccount.model.entity.RegistryAppAccount;
import com.aioveu.registry.aioveu02RegistryAppAccount.model.form.RegistryAppAccountForm;
import com.aioveu.registry.aioveu02RegistryAppAccount.model.query.RegistryAppAccountQuery;
import com.aioveu.registry.aioveu02RegistryAppAccount.model.vo.RegistryAppAccountVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: RegistryAppAccountService
 * @Description TODO 小程序账号服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:06
 * @Version 1.0
 **/

public interface RegistryAppAccountService extends IService<RegistryAppAccount> {

    /**
     *小程序账号分页列表
     *
     * @return {@link IPage<RegistryAppAccountVo>} 小程序账号分页列表
     */
    IPage<RegistryAppAccountVo> getRegistryAppAccountPage(RegistryAppAccountQuery queryParams);

    /**
     * 获取小程序账号表单数据
     *
     * @param id 小程序账号ID
     * @return 小程序账号表单数据
     */
    RegistryAppAccountForm getRegistryAppAccountFormData(Long id);

    /**
     * 新增小程序账号
     *
     * @param formData 小程序账号表单对象
     * @return 是否新增成功
     */
    boolean saveRegistryAppAccount(RegistryAppAccountForm formData);

    /**
     * 修改小程序账号
     *
     * @param id   小程序账号ID
     * @param formData 小程序账号表单对象
     * @return 是否修改成功
     */
    boolean updateRegistryAppAccount(Long id, RegistryAppAccountForm formData);

    /**
     * 删除小程序账号
     *
     * @param ids 小程序账号ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteRegistryAppAccounts(String ids);
}
