package com.aioveu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.OrderTradeConfirmDao;
import com.aioveu.entity.Order;
import com.aioveu.entity.OrderTradeConfirm;
import com.aioveu.enums.PayType;
import com.aioveu.feign.HuiFuClient;
import com.aioveu.feign.form.TradeConfirmForm;
import com.aioveu.service.OrderService;
import com.aioveu.service.OrderTradeConfirmService;
import com.aioveu.utils.IdUtils;
import com.aioveu.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class OrderTradeConfirmServiceImpl extends ServiceImpl<OrderTradeConfirmDao, OrderTradeConfirm> implements OrderTradeConfirmService {

    @Autowired
    private OrderService orderService;

    @Resource
    private HuiFuClient huiFuClient;


    @Override
    public boolean confirm(Order order, Date payFinishTime) {
        if (PayType.HF.getCode().equals(order.getPayType())) {
            OrderTradeConfirm orderTradeConfirm = new OrderTradeConfirm();
            orderTradeConfirm.setId(IdUtils.getStrNumberId("FZ"));
            orderTradeConfirm.setName(order.getName());
            orderTradeConfirm.setPayTime(payFinishTime);
            orderTradeConfirm.setOrderId(order.getId());
            orderTradeConfirm.setCompanyId(order.getCompanyId());
            orderTradeConfirm.setStoreId(order.getStoreId());
            orderTradeConfirm.setPayAppId(order.getPayAppId());
            orderTradeConfirm.setStatus(1);
            orderTradeConfirm.setActualAmount(order.getActualAmount());
            // 商户实收金额 需要扣除手续费 扣除 0.97% 交易手续费
            orderTradeConfirm.setStoreActualAmount(orderTradeConfirm.getActualAmount().multiply(BigDecimal.valueOf(0.9904)).setScale(2, RoundingMode.HALF_UP));
            // 平台获得 0.6的服务费
            orderTradeConfirm.setPlatformAmount(BigDecimal.valueOf(0.6));
            // 商户实收金额 减去平台服务费 剩下给商户 也就是分账金额
            orderTradeConfirm.setSplitAmount(orderTradeConfirm.getStoreActualAmount().subtract(orderTradeConfirm.getPlatformAmount()));
            if (save(orderTradeConfirm)) {
                // 发起汇付交易分账确认单
                TradeConfirmForm form = new TradeConfirmForm();
                form.setOrderId(order.getId());
                form.setTradeConfirmId(orderTradeConfirm.getId());
                form.setRemark(orderTradeConfirm.getRemark());
                form.setPayDate(orderTradeConfirm.getPayTime());
                form.setHuiFuId(orderTradeConfirm.getPayAppId());
                form.setDivAmt(orderTradeConfirm.getSplitAmount().setScale(2, RoundingMode.HALF_UP).toString());
                CommonResponse<Boolean> response = huiFuClient.tradeConfirm(form);
                OrderTradeConfirm oc = new OrderTradeConfirm();
                oc.setId(orderTradeConfirm.getId());
                if (response.isSuccess()) {
                    oc.setStatus(2);

                    Order updateOrder = new Order();
                    updateOrder.setId(order.getId());
                    updateOrder.setTradeConfirmId(orderTradeConfirm.getId());
                    orderService.updateById(updateOrder);
                    updateById(oc);
                    return true;
                } else {
                    // 分账发起失败 需要进行通知给系统管理员
                    log.error("分账:{}确认发起失败:{}", orderTradeConfirm.getId(), response.getMessage());
                    oc.setStatus(20);
                    updateById(oc);
                }
            }
        }
        return false;
    }

    @Override
    public boolean confirmCancel(String tradeConfirmId) {
        OrderTradeConfirm confirm = getById(tradeConfirmId);
        if (confirm != null) {
            // 发起汇付交易分账确认单
            TradeConfirmForm form = new TradeConfirmForm();
            form.setOrderId(confirm.getOrderId());
            form.setTradeConfirmId(confirm.getId());
            form.setRemark(confirm.getRemark());
            form.setPayDate(confirm.getPayTime());
            form.setHuiFuId(confirm.getPayAppId());
            form.setDivAmt(confirm.getSplitAmount().setScale(2, RoundingMode.HALF_UP).toString());
            CommonResponse<Boolean> response = huiFuClient.tradeConfirmCancel(form);
            OrderTradeConfirm oc = new OrderTradeConfirm();
            oc.setId(tradeConfirmId);
            if (response.isSuccess()) {
                oc.setStatus(9);
                updateById(oc);
                return true;
            } else {
                // 分账发起失败 需要进行通知给系统管理员
                log.error("分账:{}取消失败:{}", tradeConfirmId, response.getMessage());
                oc.setStatus(24);
                updateById(oc);
            }
        }
        return false;
    }
}
