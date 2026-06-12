package com.aioveu.oms.aioveu12OrderExportTask.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.oms.aioveu01Order.model.query.OrderExportQuery;
import com.aioveu.oms.aioveu12OrderExportTask.converter.OmsOrderExportTaskConverter;
import com.aioveu.oms.aioveu12OrderExportTask.enums.OrdeExportTaskStatusEnum;
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
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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


    private final OmsOrderExportTaskMapper omsOrderExportTaskMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createExportTask(OrderExportQuery query, String token, String clientId) {

        // 1️解析用户
//        Jwt jwt = jwtDecoder.decode(token.replace("Bearer ", ""));
//        Long operatorId = jwt.getClaim("userId", Long.class);
//        Long tenantId = jwt.getClaim("tenantId", Long.class);
        Long operatorId = SecurityUtils.getMemberId();
        Long tenantId = SecurityUtils.getTenantId();

        // 2️权限校验
//        checkExportPermission(operatorId, tenantId);

        // 3️创建任务
        OmsOrderExportTask task = new OmsOrderExportTask();
        BeanUtils.copyProperties(query, task);
        task.setTenantId(tenantId);
        task.setOperatorId(operatorId);
        task.setClientId(clientId);
        task.setStatus(OrdeExportTaskStatusEnum.PENDING);
        task.setCreateTime(LocalDateTime.now());

        omsOrderExportTaskMapper.insert(task);
        return task.getId();
    }

}
