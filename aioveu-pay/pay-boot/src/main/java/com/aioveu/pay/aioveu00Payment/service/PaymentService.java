package com.aioveu.pay.aioveu00Payment.service;

import com.aioveu.common.result.Result;

import com.aioveu.pay.aioveuModule.model.vo.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @ClassName: PaymentService
 * @Description TODO 支付流程核心接口
 *                    幂等性处理
 *                  支付订单创建幂等
 *                  回调处理幂等
 *                  退款申请幂等
 *                  分布式事务
 *                  使用Seata AT模式
 *                  补偿机制
 *                  最终一致性
 *                  并发控制
 *                  账户余额乐观锁
 *                  Redis分布式锁
 *                  数据库行锁
 *                  安全控制
 *                  参数签名验
 *                  回调签名验证
 *                  敏感信息加密
 *                  监控告警
 *                  支付成功率监控
 *                  对账差异告警
 *                  异步通知重试监控
 *                  这个支付系统设计支持：
 *                  多种支付渠道接入
 *                  完整的支付退款流程
 *                  账户余额管理
 *                  自动对账功能
 *                  异步通知重试
 *                  分布式事务支持
 *                  高并发处理能力
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 17:11
 * @Version 1.0
 **/

public interface PaymentService {

    /**
     * 统一支付接口
     *
     * @return
     *
     */
    Result<PaymentParamsVO> unifiedPayment(PaymentRequestDTO request);

    /**
     * 处理支付回调
     *
     * @return
     */
    Result<Void> handleCallback(PaymentCallbackDTO callback);

    /**
     * 处理微信回调
     *
     * @return
     */
    String handleWechatCallback(String xmlData);

    /**
     * 处理支付宝回调
     *
     * @return
     */
    String handleAlipayCallback(Map<String, String> params);


//    /**
//     * 统一退款接口
//     *
//     * @return
//     */
//    Result<RefundResultVO> unifiedRefund(RefundRequestDTO request);


//    Boolean validatePaymentRequest(PaymentRequestDTO request);



}
