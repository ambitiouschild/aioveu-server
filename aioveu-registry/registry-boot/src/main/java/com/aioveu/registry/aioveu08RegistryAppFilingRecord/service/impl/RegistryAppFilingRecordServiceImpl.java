package com.aioveu.registry.aioveu08RegistryAppFilingRecord.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.registry.aioveu08RegistryAppFilingRecord.converter.RegistryAppFilingRecordConverter;
import com.aioveu.registry.aioveu08RegistryAppFilingRecord.mapper.RegistryAppFilingRecordMapper;
import com.aioveu.registry.aioveu08RegistryAppFilingRecord.model.entity.RegistryAppFilingRecord;
import com.aioveu.registry.aioveu08RegistryAppFilingRecord.model.form.RegistryAppFilingRecordForm;
import com.aioveu.registry.aioveu08RegistryAppFilingRecord.model.query.RegistryAppFilingRecordQuery;
import com.aioveu.registry.aioveu08RegistryAppFilingRecord.model.vo.RegistryAppFilingRecordVo;
import com.aioveu.registry.aioveu08RegistryAppFilingRecord.service.RegistryAppFilingRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: RegistryAppFilingRecordServiceImpl
 * @Description TODO 小程序备案记录服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 19:17
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class RegistryAppFilingRecordServiceImpl extends ServiceImpl<RegistryAppFilingRecordMapper, RegistryAppFilingRecord> implements RegistryAppFilingRecordService {


    private final RegistryAppFilingRecordConverter registryAppFilingRecordConverter;

    /**
     * 获取小程序备案记录分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<RegistryAppFilingRecordVo>} 小程序备案记录分页列表
     */
    @Override
    public IPage<RegistryAppFilingRecordVo> getRegistryAppFilingRecordPage(RegistryAppFilingRecordQuery queryParams) {
        Page<RegistryAppFilingRecordVo> page = this.baseMapper.getRegistryAppFilingRecordPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取小程序备案记录表单数据
     *
     * @param id 小程序备案记录ID
     * @return 小程序备案记录表单数据
     */
    @Override
    public RegistryAppFilingRecordForm getRegistryAppFilingRecordFormData(Long id) {
        RegistryAppFilingRecord entity = this.getById(id);
        return registryAppFilingRecordConverter.toForm(entity);
    }

    /**
     * 新增小程序备案记录
     *
     * @param formData 小程序备案记录表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveRegistryAppFilingRecord(RegistryAppFilingRecordForm formData) {
        RegistryAppFilingRecord entity = registryAppFilingRecordConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新小程序备案记录
     *
     * @param id   小程序备案记录ID
     * @param formData 小程序备案记录表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateRegistryAppFilingRecord(Long id,RegistryAppFilingRecordForm formData) {
        RegistryAppFilingRecord entity = registryAppFilingRecordConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除小程序备案记录
     *
     * @param ids 小程序备案记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteRegistryAppFilingRecords(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的小程序备案记录数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }


}
