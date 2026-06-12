package com.aioveu.oms.aioveu12OrderExportTask.mapper;


import com.aioveu.oms.aioveu12OrderExportTask.model.entity.OmsOrderExportTask;
import com.aioveu.oms.aioveu12OrderExportTask.model.query.OmsOrderExportTaskQuery;
import com.aioveu.oms.aioveu12OrderExportTask.model.vo.OmsOrderExportTaskVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

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
}
