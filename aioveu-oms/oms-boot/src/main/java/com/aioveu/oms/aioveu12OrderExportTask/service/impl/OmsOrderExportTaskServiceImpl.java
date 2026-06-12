package com.aioveu.oms.aioveu12OrderExportTask.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.oms.aioveu12OrderExportTask.converter.OmsOrderExportTaskConverter;
import com.aioveu.oms.aioveu12OrderExportTask.mapper.OmsOrderExportTaskMapper;
import com.aioveu.oms.aioveu12OrderExportTask.model.entity.OmsOrderExportTask;
import com.aioveu.oms.aioveu12OrderExportTask.model.form.OmsOrderExportTaskForm;
import com.aioveu.oms.aioveu12OrderExportTask.model.query.OmsOrderExportTaskQuery;
import com.aioveu.oms.aioveu12OrderExportTask.model.vo.OmsOrderExportTaskVo;
import com.aioveu.oms.aioveu12OrderExportTask.service.OmsOrderExportTaskService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: OmsOrderExportTaskServiceImpl
 * @Description TODO 订单导出任务服务实现类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/12 18:26
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class OmsOrderExportTaskServiceImpl extends ServiceImpl<OmsOrderExportTaskMapper, OmsOrderExportTask> implements OmsOrderExportTaskService {


    private final OmsOrderExportTaskConverter omsOrderExportTaskConverter;

    /**
     * 获取订单导出任务分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<OmsOrderExportTaskVo>} 订单导出任务分页列表
     */
    @Override
    public IPage<OmsOrderExportTaskVo> getOmsOrderExportTaskPage(OmsOrderExportTaskQuery queryParams) {
        Page<OmsOrderExportTaskVo> page = this.baseMapper.getOmsOrderExportTaskPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取订单导出任务表单数据
     *
     * @param id 订单导出任务ID
     * @return 订单导出任务表单数据
     */
    @Override
    public OmsOrderExportTaskForm getOmsOrderExportTaskFormData(Long id) {
        OmsOrderExportTask entity = this.getById(id);
        return omsOrderExportTaskConverter.toForm(entity);
    }

    /**
     * 新增订单导出任务
     *
     * @param formData 订单导出任务表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveOmsOrderExportTask(OmsOrderExportTaskForm formData) {
        OmsOrderExportTask entity = omsOrderExportTaskConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新订单导出任务
     *
     * @param id   订单导出任务ID
     * @param formData 订单导出任务表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateOmsOrderExportTask(Long id,OmsOrderExportTaskForm formData) {
        OmsOrderExportTask entity = omsOrderExportTaskConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除订单导出任务
     *
     * @param ids 订单导出任务ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteOmsOrderExportTasks(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的订单导出任务数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
