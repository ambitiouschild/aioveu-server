package com.aioveu.refund.aioveu04RefundOperationLog.mapper;

import com.aioveu.refund.aioveu04RefundOperationLog.model.entity.RefundOperationLog;
import com.aioveu.refund.aioveu04RefundOperationLog.model.query.RefundOperationLogQuery;
import com.aioveu.refund.aioveu04RefundOperationLog.model.vo.RefundOperationLogVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: RefundOperationLogMapper
 * @Description TODO  退款操作记录（用于审计）Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 18:18
 * @Version 1.0
 **/

@Mapper
public interface RefundOperationLogMapper extends BaseMapper<RefundOperationLog> {

    /**
     * 获取退款操作记录（用于审计）分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<RefundOperationLogVO>} 退款操作记录（用于审计）分页列表
     */
    Page<RefundOperationLogVO> getRefundOperationLogPage(Page<RefundOperationLogVO> page, RefundOperationLogQuery queryParams);
}
