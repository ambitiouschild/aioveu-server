package com.aioveu.oms.aioveu04OrderLog.mapper;

import com.aioveu.oms.aioveu04OrderLog.model.query.OmsOrderLogQuery;
import com.aioveu.oms.aioveu04OrderLog.model.vo.OmsOrderLogVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.oms.aioveu04OrderLog.model.entity.OmsOrderLog;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: TODO 订单操作历史记录Mapper接口
 * @Author: 雒世松
 * @Date: 2025/6/5 18:08
 * @param
 * @return:
 **/

@Mapper
public interface OmsOrderLogMapper extends BaseMapper<OmsOrderLog> {

    /**
     * 获取订单操作历史记录分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<OmsOrderLogVO>} 订单操作历史记录分页列表
     */
    Page<OmsOrderLogVO> getOmsOrderLogPage(Page<OmsOrderLogVO> page, OmsOrderLogQuery queryParams);

}
