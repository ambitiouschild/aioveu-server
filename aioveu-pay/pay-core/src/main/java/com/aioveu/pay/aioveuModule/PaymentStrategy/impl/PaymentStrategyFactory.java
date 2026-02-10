package com.aioveu.pay.aioveuModule.PaymentStrategy.impl;

import com.aioveu.pay.aioveuModule.PaymentStrategy.PaymentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: PaymentStrategyFactory
 * @Description TODO   支付策略工厂 - 使用适配器模式
 *                   8.1 证书管理
 *                  证书获取：从微信支付商户平台下载API证书
 *                  证书更新：证书有效期为一年，需定期更新
 *                  安全存储：不要将证书提交到代码仓库
 *                  平台证书：需要自动或手动下载平台证书
 *                  8.2 回调处理
 *                  签名验证：必须验证回调签名
 *                  幂等处理：防止重复处理回调
 *                  异步处理：回调处理应快速返回，业务逻辑异步执行
 *                  日志记录：记录完整的回调信息
 *                  8.3 金额处理
 *                  单位转换：微信支付单位为分，系统需要转换
 *                  精度处理：使用BigDecimal处理金额
 *                  范围检查：检查金额范围（微信单笔支付最大金额为5万元）
 *                  8.4 异常处理
 *                  网络异常：重试机制
 *                  签名异常：记录详细日志
 *                  业务异常：区分可重试异常和不可重试异常
 *                  8.5 监控告警
 *                  支付成功率：监控各渠道支付成功率
 *                  回调成功率：监控回调处理成功率
 *                  退款成功率：监控退款处理成功率
 *                  对账差异：监控对账差异率
 *                  8.6 性能优化
 *                  连接池：配置HTTP连接池
 *                  异步处理：耗时操作异步执行
 *                  缓存优化：缓存平台证书等不常变的数据
 *                  批量处理：批量查询订单状态
 *
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 20:43
 * @Version 1.0
 **/

@Component
public class PaymentStrategyFactory {

    private final Map<String, PaymentStrategy> strategyMap;

    @Autowired
    public PaymentStrategyFactory(WeChatPayStrategyAdapter weChatPayService,
                                  AlipayStrategyAdapter alipayService) {
        this.strategyMap = new HashMap<>();

        // 微信支付策略 - 所有微信支付方式都使用同一个适配器
        strategyMap.put("WECHAT_JSAPI", weChatPayService);
        strategyMap.put("WECHAT_APP", weChatPayService);
        strategyMap.put("WECHAT_NATIVE", weChatPayService);
        strategyMap.put("WECHAT_H5", weChatPayService);

        // 支付宝策略
        strategyMap.put("ALIPAY_APP", alipayService);
        strategyMap.put("ALIPAY_WAP", alipayService);
        strategyMap.put("ALIPAY_PAGE", alipayService);
    }

    /**
     * 获取支付策略
     */
    public PaymentStrategy getStrategy(String channel) {
        PaymentStrategy strategy = strategyMap.get(channel);
        if (strategy == null) {
            throw new IllegalArgumentException("不支持的支付渠道: " + channel);
        }
        return strategy;
    }
}
