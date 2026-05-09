package com.aioveu.pay.aioveu10MqSendRecord.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pay.aioveu10MqSendRecord.converter.MqSendRecordConverter;
import com.aioveu.pay.aioveu10MqSendRecord.mapper.MqSendRecordMapper;
import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu10MqSendRecord.model.form.MqSendRecordForm;
import com.aioveu.pay.aioveu10MqSendRecord.model.query.MqSendRecordQuery;
import com.aioveu.pay.aioveu10MqSendRecord.model.vo.MqSendRecordVo;
import com.aioveu.pay.aioveu10MqSendRecord.service.MqSendRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: MqSendRecordServiceImpl
 * @Description TODO MQ消息发送记录服务实现类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 21:48
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class MqSendRecordServiceImpl extends ServiceImpl<MqSendRecordMapper, MqSendRecord> implements MqSendRecordService {

    private final MqSendRecordConverter mqSendRecordConverter;

    /**
     * 获取MQ消息发送记录分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<MqSendRecordVo>} MQ消息发送记录分页列表
     */
    @Override
    public IPage<MqSendRecordVo> getMqSendRecordPage(MqSendRecordQuery queryParams) {
        Page<MqSendRecordVo> page = this.baseMapper.getMqSendRecordPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取MQ消息发送记录表单数据
     *
     * @param id MQ消息发送记录ID
     * @return MQ消息发送记录表单数据
     */
    @Override
    public MqSendRecordForm getMqSendRecordFormData(Long id) {
        MqSendRecord entity = this.getById(id);
        return mqSendRecordConverter.toForm(entity);
    }

    /**
     * 新增MQ消息发送记录
     *
     * @param formData MQ消息发送记录表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveMqSendRecord(MqSendRecordForm formData) {
        MqSendRecord entity = mqSendRecordConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新MQ消息发送记录
     *
     * @param id   MQ消息发送记录ID
     * @param formData MQ消息发送记录表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateMqSendRecord(Long id,MqSendRecordForm formData) {
        MqSendRecord entity = mqSendRecordConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除MQ消息发送记录
     *
     * @param ids MQ消息发送记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteMqSendRecords(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的MQ消息发送记录数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }


}
