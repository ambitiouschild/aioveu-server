package com.aioveu.pay.aioveu01PayOrder.mapper;


import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu01PayOrder.model.query.PayOrderQuery;
import com.aioveu.pay.aioveu01PayOrder.model.vo.PayOrderVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: PayOrderMapper
 * @Description TODO 支付订单Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 17:28
 * @Version 1.0
 **/

@Mapper
public interface PayOrderMapper extends BaseMapper<PayOrder>{

    /**
     * 获取支付订单分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<PayOrderVO>} 支付订单分页列表
     */
    Page<PayOrderVO> getPayOrderPage(Page<PayOrderVO> page, PayOrderQuery queryParams);
}
