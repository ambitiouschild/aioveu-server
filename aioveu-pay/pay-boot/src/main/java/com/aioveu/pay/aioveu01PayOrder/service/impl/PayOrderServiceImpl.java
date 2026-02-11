package com.aioveu.pay.aioveu01PayOrder.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pay.aioveu01PayOrder.converter.PayOrderConverter;
import com.aioveu.pay.aioveu01PayOrder.mapper.PayOrderMapper;
import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu01PayOrder.model.form.PayOrderForm;
import com.aioveu.pay.aioveu01PayOrder.model.query.PayOrderQuery;
import com.aioveu.pay.aioveu01PayOrder.model.vo.PayOrderVO;
import com.aioveu.pay.aioveu01PayOrder.service.PayOrderService;
import com.aioveu.pay.aioveuModule.enums.PaymentStatusEnum;
import com.aioveu.pay.aioveuModule.model.vo.PaymentCallbackDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: PayOrderServiceImpl
 * @Description TODO 支付订单服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 17:33
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements PayOrderService {

    private final PayOrderConverter payOrderConverter;

    /**
     * 获取支付订单分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PayOrderVO>} 支付订单分页列表
     */
    @Override
    public IPage<PayOrderVO> getPayOrderPage(PayOrderQuery queryParams) {
        Page<PayOrderVO> pageVO = this.baseMapper.getPayOrderPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取支付订单表单数据
     *
     * @param id 支付订单ID
     * @return 支付订单表单数据
     */
    @Override
    public PayOrderForm getPayOrderFormData(Long id) {
        PayOrder entity = this.getById(id);
        return payOrderConverter.toForm(entity);
    }

    /**
     * 新增支付订单
     *
     * @param formData 支付订单表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePayOrder(PayOrderForm formData) {
        PayOrder entity = payOrderConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新支付订单
     *
     * @param id   支付订单ID
     * @param formData 支付订单表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePayOrder(Long id,PayOrderForm formData) {
        PayOrder entity = payOrderConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除支付订单
     *
     * @param ids 支付订单ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePayOrders(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的支付订单数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

    /**
     * 根据支付单号查询支付订单
     *
     * @param paymentNo 支付单号
     * @return PayOrder
     */
    @Override
    public PayOrder selectByPaymentNo(String paymentNo){

        PayOrder payOrder = this.getOne(
                new LambdaQueryWrapper<PayOrder>()
                        .eq(PayOrder::getPaymentNo, paymentNo)
        );

        log.info("根据支付单号查询支付订单:{}",payOrder);

        return payOrder;
    }

    /**
     *  更新订单状态
     *
     * @param order 支付单号,callback
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateOrderStatus(PayOrder order, PaymentCallbackDTO callback){

        // 设置状态
        if (Boolean.TRUE.equals(callback.getSuccess())) {
            order.setPaymentStatus(PaymentStatusEnum.SUCCESS.getValue());
            order.setPaymentTime(callback.getPaymentTime());
            order.setThirdPaymentNo(callback.getThirdPaymentNo());
            order.setErrorCode(null);
            order.setErrorMessage(null);
        } else {
            order.setPaymentStatus(PaymentStatusEnum.FAILED.getValue());
            order.setErrorCode(callback.getErrorCode());
            order.setErrorMessage(callback.getErrorMessage());
        }

        // 更新其他字段
//        order.setUpdateTime(new Date());
        order.setUpdateBy("system_callback");

        // 更新数据库

        // 记录状态变更日志
//        payOrderLogService.logStatusChange(order.getPaymentNo(),
//                PaymentStatusEnum.PENDING.getValue(),
//                order.getPaymentStatus(),
//                "回调通知");
        return this.save(order);
    }


    /**
     *  处理业务逻辑
     *
     * @param order 支付单号,callback
     * @return Boolean
     */
    @Override
    public void handleBusinessLogic(PayOrder order, PaymentCallbackDTO callback) {

        if (!PaymentStatusEnum.SUCCESS.getValue().equals(order.getPaymentStatus())) {
            return;  // 只有支付成功才处理业务
        }

        // 根据业务类型处理
        String bizType = order.getBizType();
//        String bizId = order.getBizId();

        switch (bizType) {
            case "ORDER_PAY":  // 订单支付
//                orderService.paySuccess(bizId, order);
                break;
            case "RECHARGE":   // 充值
//                userBalanceService.recharge(bizId, order);
                break;
            case "VIP_PAY":    // VIP购买
//                vipService.activateVip(bizId, order);
                break;
            default:
                log.warn("【业务处理】未知业务类型: {}", bizType);
        }

    }
}
