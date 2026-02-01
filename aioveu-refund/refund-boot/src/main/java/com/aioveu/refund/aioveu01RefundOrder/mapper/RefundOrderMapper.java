package com.aioveu.refund.aioveu01RefundOrder.mapper;

import com.aioveu.refund.aioveu01RefundOrder.model.entity.RefundOrder;
import com.aioveu.refund.aioveu01RefundOrder.model.query.RefundOrderQuery;
import com.aioveu.refund.aioveu01RefundOrder.model.vo.RefundOrderVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;


/**
 * @ClassName: RefundOrderMapper
 * @Description TODO  订单退款申请Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 16:24
 * @Version 1.0
 **/

@Mapper
public interface RefundOrderMapper extends BaseMapper<RefundOrder> {

    /**
     * 获取订单退款申请分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<RefundOrderVO>} 订单退款申请分页列表
     */
    Page<RefundOrderVO> getRefundOrderPage(Page<RefundOrderVO> page, RefundOrderQuery queryParams);
}
