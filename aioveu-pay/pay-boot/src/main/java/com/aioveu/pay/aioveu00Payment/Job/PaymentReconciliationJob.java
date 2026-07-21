package com.aioveu.pay.aioveu00Payment.Job;


import cn.hutool.core.collection.CollUtil;
import com.aioveu.pay.aioveu01PayOrder.mapper.PayOrderMapper;
import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu00Payment.service.PaymentRecoveryService;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
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
    //方案 1（最推荐）：Job / 补偿任务 主动忽略多租户
    //支付兜底 Job = 必须无视租户  Job 的语义是“系统级修复”，不是“租户级业务”
    @InterceptorIgnore(tenantLine = "true")
    @Scheduled(cron = "0 */5 * * * ?")  //把“每分钟”改成“每 2~5 分钟”
    public void reconcile() {
        log.info("支付兜底Job开始执行");

        LocalDateTime maxCreatedAt = LocalDateTime.now().minusMinutes(30);  //-- ✅ 最早创建时间（now - 30min）
        LocalDateTime lastCreatedAt = LocalDateTime.now();   //- ✅ 最晚创建时间（now）
        Long lastId = Long.MAX_VALUE;    //-- ✅ 游标分页（防重复）
        int batchSize = 200;



        /*
        * “扫描最近 30 分钟内、仍未进入终态的支付单，按时间倒序、ID 倒序分批处理，用于修复回调丢失 / MQ 丢失 / 服务异常导致的支付状态不一致问题。”
👉 这是标准的“支付第三层兜底”
        * */
        /*
        * 👉 只扫最近 30 分钟的订单
                为什么这么做？
                支付回调一般在秒级 ~ 分钟级到达
                30 分钟后还没回调，大概率是：
                第三方没通知
                MQ 丢了
                服务宕机过
                ✅ 这是标准兜底窗口
                ⚠️ 但也意味着：
                超过 30 分钟的“僵尸单”不会被扫到
        *   你现在这个扫描，是以 pay_order.create_time（订单创建时间）为唯一标准的
        *   ✅ 只扫「最近 30 分钟内创建的、且状态仍是非终态的支付单（pay_order）」
        *
        * ✅ 正确理解是：
            LocalDateTime.now()→ 每次 Job 执行时重新算
            create_time >= now - 30min→ 每次都在“重新框定一个固定长度的时间段”
            *
              * ✅ Job 是每分钟执行一次
                ✅ 但每次执行，扫的 create_time区间是“新的”
                ✅ 加上 id < lastId游标，保证不会重复扫
                ❌ 不是每分钟把最近 30 分钟全量扫一遍
                *
                * 👉 一笔单最多只会在连续的 30 次 Job 中被扫到
                    👉 不会无限重复
*
        * */


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
