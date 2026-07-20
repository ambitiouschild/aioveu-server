package com.aioveu.pay.aioveu01PayOrder.service;



/**
 * @ClassName: PaymentRecoveryService
 * @Description TODO PaymentRecoveryService（核心兜底逻辑）
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/7/20 19:31
 * @Version 1.0
 **/
public interface PaymentRecoveryService {


    /**
     * 单笔订单兜底查单
     */
    void recover(String paymentNo);
}
