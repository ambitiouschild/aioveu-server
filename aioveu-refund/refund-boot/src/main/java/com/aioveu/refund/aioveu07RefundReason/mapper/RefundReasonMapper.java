package com.aioveu.refund.aioveu07RefundReason.mapper;

import com.aioveu.refund.aioveu07RefundReason.model.entity.RefundReason;
import com.aioveu.refund.aioveu07RefundReason.model.query.RefundReasonQuery;
import com.aioveu.refund.aioveu07RefundReason.model.vo.RefundReasonVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: RefundReasonMapper
 * @Description TODO 退款原因分类Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:55
 * @Version 1.0
 **/

@Mapper
public interface RefundReasonMapper extends BaseMapper<RefundReason> {

    /**
     * 获取退款原因分类分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<RefundReasonVO>} 退款原因分类分页列表
     */
    Page<RefundReasonVO> getRefundReasonPage(Page<RefundReasonVO> page, RefundReasonQuery queryParams);
}
