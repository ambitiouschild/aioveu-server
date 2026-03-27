package com.aioveu.registry.aioveu07RegistryInvoiceInfo.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.registry.aioveu07RegistryInvoiceInfo.converter.RegistryInvoiceInfoConverter;
import com.aioveu.registry.aioveu07RegistryInvoiceInfo.mapper.RegistryInvoiceInfoMapper;
import com.aioveu.registry.aioveu07RegistryInvoiceInfo.model.entity.RegistryInvoiceInfo;
import com.aioveu.registry.aioveu07RegistryInvoiceInfo.model.form.RegistryInvoiceInfoForm;
import com.aioveu.registry.aioveu07RegistryInvoiceInfo.model.query.RegistryInvoiceInfoQuery;
import com.aioveu.registry.aioveu07RegistryInvoiceInfo.model.vo.RegistryInvoiceInfoVo;
import com.aioveu.registry.aioveu07RegistryInvoiceInfo.service.RegistryInvoiceInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: RegistryInvoiceInfoServiceImpl
 * @Description TODO 发票信息服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:41
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class RegistryInvoiceInfoServiceImpl extends ServiceImpl<RegistryInvoiceInfoMapper, RegistryInvoiceInfo> implements RegistryInvoiceInfoService {


    private final RegistryInvoiceInfoConverter registryInvoiceInfoConverter;

    /**
     * 获取发票信息分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<RegistryInvoiceInfoVo>} 发票信息分页列表
     */
    @Override
    public IPage<RegistryInvoiceInfoVo> getRegistryInvoiceInfoPage(RegistryInvoiceInfoQuery queryParams) {
        Page<RegistryInvoiceInfoVo> page = this.baseMapper.getRegistryInvoiceInfoPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取发票信息表单数据
     *
     * @param id 发票信息ID
     * @return 发票信息表单数据
     */
    @Override
    public RegistryInvoiceInfoForm getRegistryInvoiceInfoFormData(Long id) {
        RegistryInvoiceInfo entity = this.getById(id);
        return registryInvoiceInfoConverter.toForm(entity);
    }

    /**
     * 新增发票信息
     *
     * @param formData 发票信息表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveRegistryInvoiceInfo(RegistryInvoiceInfoForm formData) {
        RegistryInvoiceInfo entity = registryInvoiceInfoConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新发票信息
     *
     * @param id   发票信息ID
     * @param formData 发票信息表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateRegistryInvoiceInfo(Long id,RegistryInvoiceInfoForm formData) {
        RegistryInvoiceInfo entity = registryInvoiceInfoConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除发票信息
     *
     * @param ids 发票信息ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteRegistryInvoiceInfos(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的发票信息数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
