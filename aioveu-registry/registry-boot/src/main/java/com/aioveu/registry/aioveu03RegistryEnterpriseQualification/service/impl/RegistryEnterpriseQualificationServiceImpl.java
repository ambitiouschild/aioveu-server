package com.aioveu.registry.aioveu03RegistryEnterpriseQualification.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.registry.aioveu03RegistryEnterpriseQualification.converter.RegistryEnterpriseQualificationConverter;
import com.aioveu.registry.aioveu03RegistryEnterpriseQualification.mapper.RegistryEnterpriseQualificationMapper;
import com.aioveu.registry.aioveu03RegistryEnterpriseQualification.model.entity.RegistryEnterpriseQualification;
import com.aioveu.registry.aioveu03RegistryEnterpriseQualification.model.form.RegistryEnterpriseQualificationForm;
import com.aioveu.registry.aioveu03RegistryEnterpriseQualification.model.query.RegistryEnterpriseQualificationQuery;
import com.aioveu.registry.aioveu03RegistryEnterpriseQualification.model.vo.RegistryEnterpriseQualificationVo;
import com.aioveu.registry.aioveu03RegistryEnterpriseQualification.service.RegistryEnterpriseQualificationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: RegistryEnterpriseQualificationServiceImpl
 * @Description TODO 企业资质服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:35
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class RegistryEnterpriseQualificationServiceImpl extends ServiceImpl<RegistryEnterpriseQualificationMapper, RegistryEnterpriseQualification> implements RegistryEnterpriseQualificationService {

    private final RegistryEnterpriseQualificationConverter registryEnterpriseQualificationConverter;

    /**
     * 获取企业资质分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<RegistryEnterpriseQualificationVo>} 企业资质分页列表
     */
    @Override
    public IPage<RegistryEnterpriseQualificationVo> getRegistryEnterpriseQualificationPage(RegistryEnterpriseQualificationQuery queryParams) {
        Page<RegistryEnterpriseQualificationVo> page = this.baseMapper.getRegistryEnterpriseQualificationPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取企业资质表单数据
     *
     * @param id 企业资质ID
     * @return 企业资质表单数据
     */
    @Override
    public RegistryEnterpriseQualificationForm getRegistryEnterpriseQualificationFormData(Long id) {
        RegistryEnterpriseQualification entity = this.getById(id);
        return registryEnterpriseQualificationConverter.toForm(entity);
    }

    /**
     * 新增企业资质
     *
     * @param formData 企业资质表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveRegistryEnterpriseQualification(RegistryEnterpriseQualificationForm formData) {
        RegistryEnterpriseQualification entity = registryEnterpriseQualificationConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新企业资质
     *
     * @param id   企业资质ID
     * @param formData 企业资质表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateRegistryEnterpriseQualification(Long id,RegistryEnterpriseQualificationForm formData) {
        RegistryEnterpriseQualification entity = registryEnterpriseQualificationConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除企业资质
     *
     * @param ids 企业资质ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteRegistryEnterpriseQualifications(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的企业资质数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
