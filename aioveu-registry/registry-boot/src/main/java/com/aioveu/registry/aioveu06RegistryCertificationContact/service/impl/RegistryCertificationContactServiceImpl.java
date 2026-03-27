package com.aioveu.registry.aioveu06RegistryCertificationContact.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.registry.aioveu06RegistryCertificationContact.converter.RegistryCertificationContactConverter;
import com.aioveu.registry.aioveu06RegistryCertificationContact.mapper.RegistryCertificationContactMapper;
import com.aioveu.registry.aioveu06RegistryCertificationContact.model.entity.RegistryCertificationContact;
import com.aioveu.registry.aioveu06RegistryCertificationContact.model.form.RegistryCertificationContactForm;
import com.aioveu.registry.aioveu06RegistryCertificationContact.model.query.RegistryCertificationContactQuery;
import com.aioveu.registry.aioveu06RegistryCertificationContact.model.vo.RegistryCertificationContactVo;
import com.aioveu.registry.aioveu06RegistryCertificationContact.service.RegistryCertificationContactService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: RegistryCertificationContactServiceImpl
 * @Description TODO 认证联系人服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:31
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class RegistryCertificationContactServiceImpl extends ServiceImpl<RegistryCertificationContactMapper, RegistryCertificationContact> implements RegistryCertificationContactService {

    private final RegistryCertificationContactConverter registryCertificationContactConverter;

    /**
     * 获取认证联系人分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<RegistryCertificationContactVo>} 认证联系人分页列表
     */
    @Override
    public IPage<RegistryCertificationContactVo> getRegistryCertificationContactPage(RegistryCertificationContactQuery queryParams) {
        Page<RegistryCertificationContactVo> page = this.baseMapper.getRegistryCertificationContactPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取认证联系人表单数据
     *
     * @param id 认证联系人ID
     * @return 认证联系人表单数据
     */
    @Override
    public RegistryCertificationContactForm getRegistryCertificationContactFormData(Long id) {
        RegistryCertificationContact entity = this.getById(id);
        return registryCertificationContactConverter.toForm(entity);
    }

    /**
     * 新增认证联系人
     *
     * @param formData 认证联系人表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveRegistryCertificationContact(RegistryCertificationContactForm formData) {
        RegistryCertificationContact entity = registryCertificationContactConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新认证联系人
     *
     * @param id   认证联系人ID
     * @param formData 认证联系人表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateRegistryCertificationContact(Long id,RegistryCertificationContactForm formData) {
        RegistryCertificationContact entity = registryCertificationContactConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除认证联系人
     *
     * @param ids 认证联系人ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteRegistryCertificationContacts(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的认证联系人数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }


}
