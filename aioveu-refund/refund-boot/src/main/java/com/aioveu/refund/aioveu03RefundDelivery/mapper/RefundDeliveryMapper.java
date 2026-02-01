package com.aioveu.refund.aioveu03RefundDelivery.mapper;

import com.aioveu.refund.aioveu03RefundDelivery.model.entity.RefundDelivery;
import com.aioveu.refund.aioveu03RefundDelivery.model.query.RefundDeliveryQuery;
import com.aioveu.refund.aioveu03RefundDelivery.model.vo.RefundDeliveryVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: RefundDeliveryMapper
 * @Description TODO  退款物流信息（用于退货）Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 18:01
 * @Version 1.0
 **/

@Mapper
public interface RefundDeliveryMapper extends BaseMapper<RefundDelivery> {

    /**
     * 获取退款物流信息（用于退货）分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<RefundDeliveryVO>} 退款物流信息（用于退货）分页列表
     */
    Page<RefundDeliveryVO> getRefundDeliveryPage(Page<RefundDeliveryVO> page, RefundDeliveryQuery queryParams);
}
