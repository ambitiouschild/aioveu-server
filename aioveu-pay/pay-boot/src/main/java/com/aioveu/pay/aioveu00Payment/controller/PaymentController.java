package com.aioveu.pay.aioveu00Payment.controller;

import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu00Payment.service.PaymentService;
import com.aioveu.pay.model.aioveuPayment.PaymentParamsVO;
import com.aioveu.pay.model.aioveuPayment.request.PaymentRequestFEToOmsDTO;
import com.aioveu.pay.model.aioveuPayment.request.PaymentRequestOmsToPayDTO;
import com.aioveu.pay.model.aioveuPayment.request.PaymentRequestPayToTPPDTO;
import com.aioveu.pay.model.aioveuPayment.PaymentStatusVO;
import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: PaymentController
 * @Description TODO 支付控制器接收请求
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/11 12:40
 * @Version 1.0
 **/

@Slf4j
@RestController
@RequestMapping("/aioveu/api/v8/admin/pay/pay-order")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;  // 支付服务



    /**
     * 来自oms的订单支付请求
     */
    @Operation(summary ="来自oms-pay的订单支付请求")
    @PostMapping("/createPaymentOmsToPay")
//    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrder:oms-order:query')")
    @Log( value = "来自oms的订单支付请求",module = LogModuleEnum.PAY)
    public Result<PaymentParamsVO> createPaymentOmsToPay(@RequestBody PaymentRequestOmsToPayDTO paymentForm) {

        log.info("【Pay微服务PaymentController】来自oms的订单支付请求: {}", JSON.toJSONString(paymentForm));

        PaymentParamsVO PaymentParamsVO = paymentService.createPaymentOmsToPay(paymentForm);

        return  Result.success(PaymentParamsVO);
    }


    /**
     * 创建前端调用第三方支付所需的支付参数
     */
    @Operation(summary ="来自pay-wechat的订单支付请求,创建前端调用第三方支付所需的支付参数")
    @PostMapping("/createPaymentPayToTPP")
//    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrder:oms-order:query')")
    @Log( value = "来自pay-wechat的订单支付请求,创建前端调用第三方支付所需的支付参数",module = LogModuleEnum.PAY)
    public Result<PaymentParamsVO> createPayment(@RequestBody PaymentRequestPayToTPPDTO request) {

        log.info("【Pay微服务PaymentController】收到支付请求: {}", JSON.toJSONString(request));

        PaymentParamsVO PaymentParamsVO = paymentService.createPaymentPayToTPP(request);

        return  Result.success(PaymentParamsVO);
    }



    /**
     * 微信支付回调
     * 微信回调：支付结果通知
     * 微信回调是XML格式
     * 注意：这是微信主动调用的，不是前端调用的
     */
    @Operation(summary ="微信支付回调")
    @PostMapping("/wxpay/notify")
//    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrder:oms-order:query')")
    @Log( value = "微信支付回调,微信只认 XML",module = LogModuleEnum.PAY)
    public String wxPayNotify(HttpServletRequest request) {


        //   https://crushapi.aioveu.com/api/v1/pay-order/wxpay/notify
//        return paymentService.handleCallback(request);

        log.info("接收到微信支付回调");

        try {
            // 1. 获取请求体（XML格式）
            String xmlData = getRequestBody(request);
            log.info("【微信回调】微信支付回调原始数据: {}", xmlData);
            log.info("【微信回调】开始处理, XML长度: {}", xmlData.length());

            // 2. 调用service处理
            String result = paymentService.handleWechatCallback(xmlData);
            log.info("微信回调处理结果: {}", result);

            return result;

        } catch (Exception e) {
            log.error("处理微信回调异常", e);
            return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[系统异常]]></return_msg></xml>";
        }
    }






    /**
     * 支付宝回调
     * 支付宝回调是表单格式
     */
    @Operation(summary ="支付宝回调")
    @PostMapping("/callback/alipay")
//    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrder:oms-order:query')")
    @Log( value = "支付宝回调",module = LogModuleEnum.PAY)
    public String  alipayCallback(HttpServletRequest request) {

        try {
            // 1. 获取所有参数
            Map<String, String> params = getRequestParams(request);
            log.info("收到支付宝回调: {}", JSON.toJSONString(params));

            // 2. 调用service处理
            String result = paymentService.handleAlipayCallback(params);
            log.info("支付宝回调处理结果: {}", result);

            return result;

        } catch (Exception e) {
            log.error("处理支付宝回调异常", e);
            return "failure";
        }
    }


    @PostMapping("/mock/wxpay/notify")
    @Operation(summary = "手动模拟微信支付回调（仅测试环境）")
    //POST /api/v1/pay-order/dev/mock/wxpay/notify?paymentNo=P202602110001
    //POST /api/v1/pay-order/dev/mock/wxpay/notify?paymentNo=P202602110001&resultCode=FAIL
    public Result<String> mockWxPayNotify(@RequestParam String paymentNo,
                                  @RequestParam(defaultValue = "SUCCESS") String resultCode) {

        log.info("【DEV】手动模拟微信支付回调, paymentNo={}, resultCode={}", paymentNo, resultCode);

        String xml = buildMockWxPayXml(paymentNo, resultCode);
        paymentService.handleWechatCallback(xml);
        return Result.success();
    }

    @PostMapping("/mock/alipay/notify")
    @Operation(summary = "手动模拟支付宝支付回调（仅测试环境）")
    //POST /api/v1/pay-order/dev/mock/alipay/notify?paymentNo=P202602110001
    public Result<String> mockAliPayNotify(@RequestParam String paymentNo,
                                   @RequestParam(defaultValue = "TRADE_SUCCESS") String tradeStatus) {

        log.info("【DEV】手动模拟支付宝支付回调, paymentNo={}, tradeStatus={}", paymentNo, tradeStatus);

        Map<String, String> params = buildMockAliPayParams(paymentNo, tradeStatus);
        paymentService.handleAlipayCallback(params);
        return Result.success();
    }



    /*
    * 微信：构造模拟 XML（✅ 走真实回调）
    * */
    private String buildMockWxPayXml(String paymentNo, String resultCode) {

        return "<xml>\n" +
                "<appid><![CDATA[wx1234567890]]></appid>\n" +
                "<mch_id><![CDATA[1234567890]]></mch_id>\n" +
                "<nonce_str><![CDATA[test_nonce]]></nonce_str>\n" +
                "<sign><![CDATA[test_sign]]></sign>\n" +
                "<result_code><![CDATA[" + resultCode + "]]></result_code>\n" +
                "<return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "<out_trade_no><![CDATA[" + paymentNo + "]]></out_trade_no>\n" +
                "<transaction_id><![CDATA[MOCK_" + paymentNo + "]]></transaction_id>\n" +
                "<total_fee><![CDATA[100]]></total_fee>\n" +
                "<bank_type><![CDATA[CFT]]></bank_type>\n" +
                "<openid><![CDATA[oUpF8uMuAJO_M2pxb1Q9zNjWeS6o]]></openid>\n" +
                "<time_end><![CDATA[20260211123000]]></time_end>\n" +
                "</xml>";
    }


    /*
    * 支付宝：构造模拟参数
    * */
    private Map<String, String> buildMockAliPayParams(String paymentNo, String tradeStatus) {

        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", paymentNo);
        params.put("trade_no", "MOCK_ALI_" + paymentNo);
        params.put("trade_status", tradeStatus);
        params.put("total_amount", "1.00");
        params.put("app_id", "mock_app_id");
        params.put("seller_id", "2088123456789012");
        params.put("charset", "utf-8");
        params.put("sign_type", "RSA2");
        params.put("sign", "mock_sign");

        return params;
    }
    /**
     * 获取请求体
     */
    private String getRequestBody(HttpServletRequest request) {
        try {
            return request.getReader().lines()
                    .reduce("", (accumulator, actual) -> accumulator + actual);
        } catch (Exception e) {
            log.error("读取请求体失败", e);
            return "";
        }
    }

    /**
     * 获取请求参数
     */
    private Map<String, String> getRequestParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();

        // 获取GET参数
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            params.put(paramName, request.getParameter(paramName));
        }

        return params;
    }
}
