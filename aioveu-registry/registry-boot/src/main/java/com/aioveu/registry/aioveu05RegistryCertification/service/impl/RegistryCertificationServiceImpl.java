package com.aioveu.registry.aioveu05RegistryCertification.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.registry.aioveu05RegistryCertification.converter.RegistryCertificationConverter;
import com.aioveu.registry.aioveu05RegistryCertification.mapper.RegistryCertificationMapper;
import com.aioveu.registry.aioveu05RegistryCertification.model.entity.RegistryCertification;
import com.aioveu.registry.aioveu05RegistryCertification.model.form.RegistryCertificationForm;
import com.aioveu.registry.aioveu05RegistryCertification.model.query.RegistryCertificationQuery;
import com.aioveu.registry.aioveu05RegistryCertification.model.vo.RegistryCertificationVo;
import com.aioveu.registry.aioveu05RegistryCertification.service.RegistryCertificationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: RegistryCertificationServiceImpl
 * @Description TODO 认证记录服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:19
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class RegistryCertificationServiceImpl extends ServiceImpl<RegistryCertificationMapper, RegistryCertification> implements RegistryCertificationService {

    private final RegistryCertificationConverter registryCertificationConverter;

    /**
     * 获取认证记录分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<RegistryCertificationVo>} 认证记录分页列表
     */
    @Override
    public IPage<RegistryCertificationVo> getRegistryCertificationPage(RegistryCertificationQuery queryParams) {
        Page<RegistryCertificationVo> page = this.baseMapper.getRegistryCertificationPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取认证记录表单数据
     *
     * @param id 认证记录ID
     * @return 认证记录表单数据
     */
    @Override
    public RegistryCertificationForm getRegistryCertificationFormData(Long id) {
        RegistryCertification entity = this.getById(id);
        return registryCertificationConverter.toForm(entity);
    }

    /**
     * 新增认证记录
     *
     * @param formData 认证记录表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveRegistryCertification(RegistryCertificationForm formData) {
        RegistryCertification entity = registryCertificationConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新认证记录
     *
     * @param id   认证记录ID
     * @param formData 认证记录表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateRegistryCertification(Long id,RegistryCertificationForm formData) {
        RegistryCertification entity = registryCertificationConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除认证记录
     *
     * @param ids 认证记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteRegistryCertifications(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的认证记录数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
