package com.aioveu.pay.aioveuModule.service.AliPay.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: AlipayCallbackController
 * @Description TODO 支付宝回调处理Controller
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 18:11
 * @Version 1.0
 **/

@RestController
@RequestMapping("/api/payment/alipay")
@Slf4j
public class AlipayCallbackController {

    @Autowired
    private AlipayServiceImpl alipayService;

    @Autowired
    private PaymentService paymentService;

    /**
     * 支付宝异步通知回调
     */
    @PostMapping("/notify")
    public String notifyCallback(HttpServletRequest request) {
        Map<String, String> params = convertRequestParams(request);

        try {
            // 1. 验证签名
            boolean signVerified = alipayService.verifyCallback(params);

            if (!signVerified) {
                log.error("支付宝回调签名验证失败: {}", params);
                return "failure";
            }

            // 2. 验证商户号
            String appId = params.get("app_id");
            if (!alipayConfig.getAppId().equals(appId)) {
                log.error("支付宝回调app_id不匹配: {}", appId);
                return "failure";
            }

            // 3. 获取业务参数
            String outTradeNo = params.get("out_trade_no");     // 商户订单号
            String tradeNo = params.get("trade_no");            // 支付宝交易号
            String tradeStatus = params.get("trade_status");    // 交易状态
            BigDecimal totalAmount = new BigDecimal(params.get("total_amount")); // 订单金额

            // 4. 构建回调DTO
            PaymentCallbackDTO callback = PaymentCallbackDTO.builder()
                    .paymentNo(outTradeNo)
                    .thirdPaymentNo(tradeNo)
                    .transactionNo(tradeNo)
                    .paymentTime(new Date())
                    .amount(totalAmount)
                    .success("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus))
                    .callbackParams(params)
                    .build();

            // 5. 处理回调
            paymentService.handleCallback(callback);

            // 6. 返回success表示处理成功
            return "success";

        } catch (Exception e) {
            log.error("处理支付宝回调异常", e);
            return "failure";
        }
    }

    /**
     * 支付宝同步返回
     */
    @GetMapping("/return")
    public String returnCallback(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = convertRequestParams(request);

        try {
            // 验证签名
            boolean signVerified = alipayService.verifyCallback(params);

            if (signVerified) {
                String outTradeNo = params.get("out_trade_no");
                // 跳转到成功页面
                return "redirect:/payment/success?paymentNo=" + outTradeNo;
            } else {
                return "redirect:/payment/failure?reason=sign_verify_failed";
            }
        } catch (Exception e) {
            log.error("处理支付宝同步返回异常", e);
            return "redirect:/payment/failure?reason=system_error";
        }
    }

    /**
     * 转换请求参数
     */
    private Map<String, String> convertRequestParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();

        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        return params;
    }
}
