package com.aioveu.pay.aioveu01PayOrder.mapper;


import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu01PayOrder.model.query.PayOrderQuery;
import com.aioveu.pay.model.aioveu01PayOrder.vo.PayOrderVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

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


    /**
     * 根据支付订单号查询（带锁）
     */
    @Select("SELECT * FROM pay_order WHERE payment_no = #{paymentNo} FOR UPDATE")
    PayOrder selectByPaymentNoWithLock(@Param("paymentNo") String paymentNo);



    /**
     * 根据商户支付订单号查询支付订单
     * @param paymentNo 商户支付订单号
     * @return
     */
    default PayOrder getPayOrderByNo(String paymentNo) {
        LambdaQueryWrapper<PayOrder> wrapper = new LambdaQueryWrapper<>();

        // 查询条件：发送失败
        wrapper.eq(PayOrder::getPaymentNo, paymentNo); //
        return selectOne(wrapper);
    }


    /**
     * 根据商户支付订单号查询支付订单
     * @param paymentNo 商户支付订单号
     * @return
     */
    default PayOrder getPayOrderByNoAndTenantId(String paymentNo,Long tenantId) {
        LambdaQueryWrapper<PayOrder> wrapper = new LambdaQueryWrapper<>();

        // 查询条件：发送失败
        wrapper.eq(PayOrder::getPaymentNo, paymentNo)
                .eq(PayOrder::getTenantId, tenantId); //
        return selectOne(wrapper);
    }


    /**
     * 游标分页：查询需要兜底查微信的订单
     */
    List<PayOrder> selectPendingOrdersForQuery(
            @Param("maxCreatedAt") LocalDateTime maxCreatedAt,
            @Param("lastCreatedAt") LocalDateTime lastCreatedAt,
            @Param("lastId") Long lastId,
            @Param("limit") int limit
    );

    /**
     * 状态机更新：只允许非终态 → 终态
     */
    int updateStatusIfNonTerminal(@Param("id") Long id,
                                  @Param("status") Integer status,
                                  @Param("thirdNo") String thirdNo,
                                  @Param("paymentTime") LocalDateTime paymentTime);


    /**
     * 仅更新查询时间
     */
    int updateLastQueryTime(@Param("id") Long id,
                            @Param("time") LocalDateTime time);
}
