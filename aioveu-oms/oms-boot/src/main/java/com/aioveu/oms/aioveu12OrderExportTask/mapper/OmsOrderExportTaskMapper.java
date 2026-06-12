package com.aioveu.oms.aioveu12OrderExportTask.mapper;


import com.aioveu.oms.aioveu12OrderExportTask.model.entity.OmsOrderExportTask;
import com.aioveu.oms.aioveu12OrderExportTask.model.query.OmsOrderExportTaskQuery;
import com.aioveu.oms.aioveu12OrderExportTask.model.vo.OmsOrderExportTaskVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

import static com.baomidou.mybatisplus.extension.toolkit.Db.getOne;
import static com.baomidou.mybatisplus.extension.toolkit.Db.lambdaQuery;

/**
 * @ClassName: OmsOrderExportTaskMapper
 * @Description TODO 订单导出任务Mapper接口
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/12 18:22
 * @Version 1.0
 **/
@Mapper
public interface OmsOrderExportTaskMapper extends BaseMapper<OmsOrderExportTask> {

    /**
     * 获取订单导出任务分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<OmsOrderExportTaskVo>} 订单导出任务分页列表
     */
    Page<OmsOrderExportTaskVo> getOmsOrderExportTaskPage(Page<OmsOrderExportTaskVo> page, OmsOrderExportTaskQuery queryParams);


    /**
     * 根据导出任务No查询导出任务
     * @param exportNo 导出任务No
     * @return 导出任务列表
     */
    default OmsOrderExportTask getByExportNo(String exportNo, Long tenantId) {
        LambdaQueryWrapper<OmsOrderExportTask> wrapper = new LambdaQueryWrapper<>();

        // 查询条件：发送失败
        wrapper.eq(OmsOrderExportTask::getOrderExportTaskNo, exportNo)
                .eq(OmsOrderExportTask::getTenantId, tenantId)
                .eq(OmsOrderExportTask::getIsDeleted, 0)   // 未删除
                .orderByAsc(OmsOrderExportTask::getCreateTime); // 按发送时间升序

        return getOne(wrapper);
    }
}
