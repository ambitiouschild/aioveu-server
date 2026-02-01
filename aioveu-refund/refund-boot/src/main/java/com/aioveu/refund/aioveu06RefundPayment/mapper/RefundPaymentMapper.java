package com.aioveu.refund.aioveu06RefundPayment.mapper;

import com.aioveu.refund.aioveu06RefundPayment.model.entity.RefundPayment;
import com.aioveu.refund.aioveu06RefundPayment.model.query.RefundPaymentQuery;
import com.aioveu.refund.aioveu06RefundPayment.model.vo.RefundPaymentVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: RefundPaymentMapper
 * @Description TODO 退款支付记录Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:29
 * @Version 1.0
 **/

@Mapper
public interface RefundPaymentMapper extends BaseMapper<RefundPayment> {

    /**
     * 获取退款支付记录分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<RefundPaymentVO>} 退款支付记录分页列表
     */
    Page<RefundPaymentVO> getRefundPaymentPage(Page<RefundPaymentVO> page, RefundPaymentQuery queryParams);
}
