package com.aioveu.oms.aioveu01Order.controller.app;

import cn.hutool.core.util.StrUtil;
import com.aioveu.common.result.Result;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: PaymentHealthController
 * @Description TODO  添加支付前的健康检查
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/23 12:06
 * @Version 1.0
 **/

@RestController
@RequestMapping("/api/payment")
@Slf4j
public class PaymentHealthController {

    @Autowired(required = false)
    private WxPayService wxPayService;

    /**
     * 支付服务健康检查
     */
    @GetMapping("/health")
    public Result<String> healthCheck(@RequestParam String paymentMethod) {
        try {
            if ("WX_JSAPI".equalsIgnoreCase(paymentMethod)) {
                return checkWxPayHealth();
            }
            return Result.success("支付服务正常");
        } catch (Exception e) {
            log.error("支付服务健康检查失败", e);
            return Result.failed("支付服务异常: " + e.getMessage());
        }
    }

    private Result<String> checkWxPayHealth() {
        if (wxPayService == null) {
            return Result.failed("微信支付服务未配置");
        }

        WxPayConfig config = wxPayService.getConfig();
        if (config == null) {
            return Result.failed("微信支付配置为空");
        }

        // 检查必要配置
        List<String> missingConfigs = new ArrayList<>();
        if (StrUtil.isBlank(config.getAppId())) {
            missingConfigs.add("AppId");
        }
        if (StrUtil.isBlank(config.getMchId())) {
            missingConfigs.add("MchId");
        }
        if (StrUtil.isBlank(config.getMchKey())) {
            missingConfigs.add("MchKey");
        }

        if (!missingConfigs.isEmpty()) {
            return Result.failed("微信支付配置缺失: " + String.join(", ", missingConfigs));
        }

        return Result.success("微信支付服务正常");
    }
}
