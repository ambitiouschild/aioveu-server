package com.aioveu.oms.aioveu08MqConsumeRecord.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.oms.aioveu08MqConsumeRecord.converter.MqConsumeRecordConverter;
import com.aioveu.oms.aioveu08MqConsumeRecord.mapper.MqConsumeRecordMapper;
import com.aioveu.oms.aioveu08MqConsumeRecord.model.entity.MqConsumeRecord;
import com.aioveu.oms.aioveu08MqConsumeRecord.model.form.MqConsumeRecordForm;
import com.aioveu.oms.aioveu08MqConsumeRecord.model.query.MqConsumeRecordQuery;
import com.aioveu.oms.aioveu08MqConsumeRecord.model.vo.MqConsumeRecordVo;
import com.aioveu.oms.aioveu08MqConsumeRecord.service.MqConsumeRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: MqConsumeRecordServiceImpl
 * @Description TODO MQ消息消费记录服务实现类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 23:35
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class MqConsumeRecordServiceImpl extends ServiceImpl<MqConsumeRecordMapper, MqConsumeRecord> implements MqConsumeRecordService {

    private final MqConsumeRecordConverter mqConsumeRecordConverter;

    /**
     * 获取MQ消息消费记录分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<MqConsumeRecordVo>} MQ消息消费记录分页列表
     */
    @Override
    public IPage<MqConsumeRecordVo> getMqConsumeRecordPage(MqConsumeRecordQuery queryParams) {
        Page<MqConsumeRecordVo> page = this.baseMapper.getMqConsumeRecordPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取MQ消息消费记录表单数据
     *
     * @param id MQ消息消费记录ID
     * @return MQ消息消费记录表单数据
     */
    @Override
    public MqConsumeRecordForm getMqConsumeRecordFormData(Long id) {
        MqConsumeRecord entity = this.getById(id);
        return mqConsumeRecordConverter.toForm(entity);
    }

    /**
     * 新增MQ消息消费记录
     *
     * @param formData MQ消息消费记录表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveMqConsumeRecord(MqConsumeRecordForm formData) {
        MqConsumeRecord entity = mqConsumeRecordConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新MQ消息消费记录
     *
     * @param id   MQ消息消费记录ID
     * @param formData MQ消息消费记录表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateMqConsumeRecord(Long id,MqConsumeRecordForm formData) {
        MqConsumeRecord entity = mqConsumeRecordConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除MQ消息消费记录
     *
     * @param ids MQ消息消费记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteMqConsumeRecords(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的MQ消息消费记录数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
