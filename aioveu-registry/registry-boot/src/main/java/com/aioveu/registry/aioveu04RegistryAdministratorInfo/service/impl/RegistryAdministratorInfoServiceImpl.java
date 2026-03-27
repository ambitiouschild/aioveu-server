package com.aioveu.registry.aioveu04RegistryAdministratorInfo.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.registry.aioveu04RegistryAdministratorInfo.converter.RegistryAdministratorInfoConverter;
import com.aioveu.registry.aioveu04RegistryAdministratorInfo.mapper.RegistryAdministratorInfoMapper;
import com.aioveu.registry.aioveu04RegistryAdministratorInfo.model.entity.RegistryAdministratorInfo;
import com.aioveu.registry.aioveu04RegistryAdministratorInfo.model.form.RegistryAdministratorInfoForm;
import com.aioveu.registry.aioveu04RegistryAdministratorInfo.model.query.RegistryAdministratorInfoQuery;
import com.aioveu.registry.aioveu04RegistryAdministratorInfo.model.vo.RegistryAdministratorInfoVo;
import com.aioveu.registry.aioveu04RegistryAdministratorInfo.service.RegistryAdministratorInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: RegistryAdministratorInfoServiceImpl
 * @Description TODO 管理员信息服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:48
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class RegistryAdministratorInfoServiceImpl extends ServiceImpl<RegistryAdministratorInfoMapper, RegistryAdministratorInfo> implements RegistryAdministratorInfoService {

    private final RegistryAdministratorInfoConverter registryAdministratorInfoConverter;

    /**
     * 获取管理员信息分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<RegistryAdministratorInfoVo>} 管理员信息分页列表
     */
    @Override
    public IPage<RegistryAdministratorInfoVo> getRegistryAdministratorInfoPage(RegistryAdministratorInfoQuery queryParams) {
        Page<RegistryAdministratorInfoVo> page = this.baseMapper.getRegistryAdministratorInfoPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取管理员信息表单数据
     *
     * @param id 管理员信息ID
     * @return 管理员信息表单数据
     */
    @Override
    public RegistryAdministratorInfoForm getRegistryAdministratorInfoFormData(Long id) {
        RegistryAdministratorInfo entity = this.getById(id);
        return registryAdministratorInfoConverter.toForm(entity);
    }

    /**
     * 新增管理员信息
     *
     * @param formData 管理员信息表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveRegistryAdministratorInfo(RegistryAdministratorInfoForm formData) {
        RegistryAdministratorInfo entity = registryAdministratorInfoConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新管理员信息
     *
     * @param id   管理员信息ID
     * @param formData 管理员信息表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateRegistryAdministratorInfo(Long id,RegistryAdministratorInfoForm formData) {
        RegistryAdministratorInfo entity = registryAdministratorInfoConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除管理员信息
     *
     * @param ids 管理员信息ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteRegistryAdministratorInfos(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的管理员信息数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
