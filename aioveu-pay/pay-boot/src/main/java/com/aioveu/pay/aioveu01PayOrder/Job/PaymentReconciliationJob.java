package com.aioveu.pay.aioveu01PayOrder.Job;


import cn.hutool.core.collection.CollUtil;
import com.aioveu.pay.aioveu01PayOrder.mapper.PayOrderMapper;
import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu01PayOrder.service.PaymentRecoveryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName: PaymentReconciliationJob
 * @Description TODO 定时任务（Job）——第三层兜底入口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/7/20 19:31
 * @Version 1.0
 **/
@Component
@Slf4j
public class PaymentReconciliationJob {

    @Resource
    private PayOrderMapper payOrderMapper;
    @Resource
    private PaymentRecoveryService recoveryService;

    /**
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void reconcile() {
        log.info("支付兜底Job开始执行");

        LocalDateTime maxCreatedAt = LocalDateTime.now().minusMinutes(30);
        LocalDateTime lastCreatedAt = LocalDateTime.now();
        Long lastId = Long.MAX_VALUE;
        int batchSize = 200;

        while (true) {
            List<PayOrder> orders = payOrderMapper
                    .selectPendingOrdersForQuery(
                            maxCreatedAt,
                            lastCreatedAt,
                            lastId,
                            batchSize
                    );

            if (CollUtil.isEmpty(orders)) {
                break;
            }

            orders.forEach(o -> recoveryService.recover(o.getPaymentNo()));

            PayOrder last = orders.get(orders.size() - 1);
            lastId = last.getId();
            lastCreatedAt = last.getCreateTime();
        }

        log.info("支付兜底Job执行结束");
    }
}
