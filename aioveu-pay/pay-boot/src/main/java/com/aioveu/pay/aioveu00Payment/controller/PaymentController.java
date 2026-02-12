package com.aioveu.pay.aioveu00Payment.controller;

import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu00Payment.service.PaymentService;
import com.aioveu.pay.aioveuModule.model.vo.PaymentParamsVO;
import com.aioveu.pay.aioveuModule.model.vo.PaymentRequestDTO;
import com.aioveu.pay.aioveuModule.model.vo.PaymentResultVO;
import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/v1/pay-order")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;  // 支付服务

    /**
     * 创建支付订单
     */
    @Operation(summary ="创建前端调用第三方支付所需的支付参数")
    @PostMapping("/create")
//    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrder:oms-order:query')")
    @Log( value = "创建前端调用第三方支付所需的支付参数",module = LogModuleEnum.PAY)
    public Result<PaymentParamsVO> createPayment(@RequestBody PaymentRequestDTO request) {

        log.info("收到支付请求: {}", JSON.toJSONString(request));
        return paymentService.unifiedPayment(request);
    }

    /**
     * 微信支付回调
     * 微信回调是XML格式
     */
    @Operation(summary ="微信支付回调")
    @PostMapping("/callback/wechat")
//    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrder:oms-order:query')")
    @Log( value = "微信支付回调",module = LogModuleEnum.PAY)
    public String wechatCallback(HttpServletRequest request) {

//        return paymentService.handleCallback(request);

        try {
            // 1. 获取请求体（XML格式）
            String xmlData = getRequestBody(request);
            log.info("收到微信支付回调: {}", xmlData);

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
    public String alipayCallback(HttpServletRequest request) {

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
