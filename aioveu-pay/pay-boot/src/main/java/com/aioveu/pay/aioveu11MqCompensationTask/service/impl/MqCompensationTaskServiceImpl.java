package com.aioveu.pay.aioveu11MqCompensationTask.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pay.aioveu11MqCompensationTask.converter.MqCompensationTaskConverter;
import com.aioveu.pay.aioveu11MqCompensationTask.mapper.MqCompensationTaskMapper;
import com.aioveu.pay.aioveu11MqCompensationTask.model.entity.MqCompensationTask;
import com.aioveu.pay.aioveu11MqCompensationTask.model.form.MqCompensationTaskForm;
import com.aioveu.pay.aioveu11MqCompensationTask.model.query.MqCompensationTaskQuery;
import com.aioveu.pay.aioveu11MqCompensationTask.model.vo.MqCompensationTaskVo;
import com.aioveu.pay.aioveu11MqCompensationTask.service.MqCompensationTaskService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: MqCompensationTaskServiceImpl
 * @Description TODO MQ补偿任务服务实现类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 22:54
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class MqCompensationTaskServiceImpl extends ServiceImpl<MqCompensationTaskMapper, MqCompensationTask> implements MqCompensationTaskService {


    private final MqCompensationTaskConverter mqCompensationTaskConverter;

    /**
     * 获取MQ补偿任务分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<MqCompensationTaskVo>} MQ补偿任务分页列表
     */
    @Override
    public IPage<MqCompensationTaskVo> getMqCompensationTaskPage(MqCompensationTaskQuery queryParams) {
        Page<MqCompensationTaskVo> page = this.baseMapper.getMqCompensationTaskPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取MQ补偿任务表单数据
     *
     * @param id MQ补偿任务ID
     * @return MQ补偿任务表单数据
     */
    @Override
    public MqCompensationTaskForm getMqCompensationTaskFormData(Long id) {
        MqCompensationTask entity = this.getById(id);
        return mqCompensationTaskConverter.toForm(entity);
    }

    /**
     * 新增MQ补偿任务
     *
     * @param formData MQ补偿任务表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveMqCompensationTask(MqCompensationTaskForm formData) {
        MqCompensationTask entity = mqCompensationTaskConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新MQ补偿任务
     *
     * @param id   MQ补偿任务ID
     * @param formData MQ补偿任务表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateMqCompensationTask(Long id,MqCompensationTaskForm formData) {
        MqCompensationTask entity = mqCompensationTaskConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除MQ补偿任务
     *
     * @param ids MQ补偿任务ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteMqCompensationTasks(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的MQ补偿任务数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
