package com.aioveu.oms.aioveu02OrderItem.mapper;

import com.aioveu.oms.aioveu02OrderItem.model.query.OmsOrderItemQuery;
import com.aioveu.oms.aioveu02OrderItem.model.vo.OmsOrderItemVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.oms.aioveu02OrderItem.model.entity.OmsOrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: TODO 订单商品信息Mapper接口
 * @Author: 雒世松
 * @Date: 2025/6/5 18:08
 * @param
 * @return:
 **/

@Mapper
public interface OmsOrderItemMapper extends BaseMapper<OmsOrderItem> {


    /**
     * 获取订单商品信息分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<OmsOrderItemVO>} 订单商品信息分页列表
     */
    Page<OmsOrderItemVO> getOmsOrderItemPage(Page<OmsOrderItemVO> page, OmsOrderItemQuery queryParams);

}
