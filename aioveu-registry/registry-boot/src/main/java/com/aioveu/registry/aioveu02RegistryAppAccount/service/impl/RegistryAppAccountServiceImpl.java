package com.aioveu.registry.aioveu02RegistryAppAccount.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.registry.aioveu02RegistryAppAccount.converter.RegistryAppAccountConverter;
import com.aioveu.registry.aioveu02RegistryAppAccount.mapper.RegistryAppAccountMapper;
import com.aioveu.registry.aioveu02RegistryAppAccount.model.entity.RegistryAppAccount;
import com.aioveu.registry.aioveu02RegistryAppAccount.model.form.RegistryAppAccountForm;
import com.aioveu.registry.aioveu02RegistryAppAccount.model.query.RegistryAppAccountQuery;
import com.aioveu.registry.aioveu02RegistryAppAccount.model.vo.RegistryAppAccountVo;
import com.aioveu.registry.aioveu02RegistryAppAccount.service.RegistryAppAccountService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: RegistryAppAccountServiceImpl
 * @Description TODO 小程序账号服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:07
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class RegistryAppAccountServiceImpl extends ServiceImpl<RegistryAppAccountMapper, RegistryAppAccount> implements RegistryAppAccountService {


    private final RegistryAppAccountConverter registryAppAccountConverter;

    /**
     * 获取小程序账号分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<RegistryAppAccountVo>} 小程序账号分页列表
     */
    @Override
    public IPage<RegistryAppAccountVo> getRegistryAppAccountPage(RegistryAppAccountQuery queryParams) {
        Page<RegistryAppAccountVo> page = this.baseMapper.getRegistryAppAccountPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取小程序账号表单数据
     *
     * @param id 小程序账号ID
     * @return 小程序账号表单数据
     */
    @Override
    public RegistryAppAccountForm getRegistryAppAccountFormData(Long id) {
        RegistryAppAccount entity = this.getById(id);
        return registryAppAccountConverter.toForm(entity);
    }

    /**
     * 新增小程序账号
     *
     * @param formData 小程序账号表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveRegistryAppAccount(RegistryAppAccountForm formData) {
        RegistryAppAccount entity = registryAppAccountConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新小程序账号
     *
     * @param id   小程序账号ID
     * @param formData 小程序账号表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateRegistryAppAccount(Long id,RegistryAppAccountForm formData) {
        RegistryAppAccount entity = registryAppAccountConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除小程序账号
     *
     * @param ids 小程序账号ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteRegistryAppAccounts(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的小程序账号数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
