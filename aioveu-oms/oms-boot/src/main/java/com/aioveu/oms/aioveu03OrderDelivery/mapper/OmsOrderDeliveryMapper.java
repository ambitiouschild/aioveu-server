package com.aioveu.oms.aioveu03OrderDelivery.mapper;

import com.aioveu.oms.aioveu03OrderDelivery.model.query.OmsOrderDeliveryQuery;
import com.aioveu.oms.aioveu03OrderDelivery.model.vo.OmsOrderDeliveryVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.oms.aioveu03OrderDelivery.model.entity.OmsOrderDelivery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: TODO 订单物流记录Mapper接口
 * @Author: 雒世松
 * @Date: 2026-01-08 20:10
 * @param
 * @return:
 **/

@Mapper
public interface OmsOrderDeliveryMapper extends BaseMapper<OmsOrderDelivery> {

    /**
     * 获取订单物流记录分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<OmsOrderDeliveryVO>} 订单物流记录分页列表
     */
    Page<OmsOrderDeliveryVO> getOmsOrderDeliveryPage(Page<OmsOrderDeliveryVO> page, OmsOrderDeliveryQuery queryParams);

}
