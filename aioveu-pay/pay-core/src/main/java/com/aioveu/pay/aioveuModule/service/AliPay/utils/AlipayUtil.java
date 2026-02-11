package com.aioveu.pay.aioveuModule.service.AliPay.utils;

import cn.hutool.core.date.DateUtil;
import com.aioveu.pay.aioveuModule.model.vo.PaymentRequestDTO;
import com.aioveu.pay.aioveuModule.service.AliPay.config.AlipayConfig;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.*;

/**
 * @ClassName: AlipayUtil
 * @Description TODO  根据实际需求选择使用原生SDK还是EasySDK，建议新项目使用EasySDK，因为它更简单易用。
 *                      8. 注意事项
 *                      密钥管理
 *                      私钥必须妥善保管，不要提交到代码仓库
 *                      建议使用配置中心或KMS管理密钥
 *                      定期更换密钥
 *                      异常处理
 *                      所有支付宝接口调用都要有异常处理
 *                      记录详细的错误日志
 *                      设计重试机制
 *                      回调处理
 *                      一定要验证签名
 *                      处理幂等性
 *                      异步处理业务逻辑
 *                      对账
 *                      定期下载对账单
 *                      自动对账
 *                      处理差异订单
 *                      监控
 *                      监控支付成功率
 *                      监控回调成功率
 *                      监控对账结果
 *                      安全
 *                      防止重复支付
 *                      防止金额篡改
 *                      防止回调伪造
 *
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 18:12
 * @Version 1.0
 **/

@Component
public class AlipayUtil {
    @Autowired
    private AlipayConfig alipayConfig;

    /**
     * 验证支付宝签名
     */
    public boolean verifySign(Map<String, String> params) throws AlipayApiException {
        return AlipaySignature.rsaCheckV1(
                params,
                alipayConfig.getAlipayPublicKey(),
                alipayConfig.getCharset(),
                alipayConfig.getSignType()
        );
    }

    /**
     * 生成支付参数
     */
    public String generatePayParams(PaymentRequestDTO request) throws AlipayApiException {
        Map<String, String> params = new HashMap<>();

        // 公共参数
        params.put("app_id", alipayConfig.getAppId());
        params.put("method", "alipay.trade.app.pay");
        params.put("charset", alipayConfig.getCharset());
        params.put("sign_type", alipayConfig.getSignType());
        params.put("timestamp", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        params.put("version", "1.0");
        params.put("notify_url", alipayConfig.getNotifyUrl());

        // 业务参数
        Map<String, String> bizParams = new HashMap<>();
        bizParams.put("out_trade_no", request.getOrderNo());
        bizParams.put("total_amount", request.getAmount().toString());
        bizParams.put("subject", request.getSubject());
        bizParams.put("body", request.getBody());
        bizParams.put("timeout_express", request.getExpireMinutes() + "m");
        bizParams.put("product_code", "QUICK_MSECURITY_PAY");

        params.put("biz_content", JSON.toJSONString(bizParams));

        // 生成签名
        String sign = AlipaySignature.rsaSign(
                AlipaySignature.getSignContent(params),
                alipayConfig.getAppPrivateKey(),
                alipayConfig.getCharset(),
                alipayConfig.getSignType()
        );

        params.put("sign", sign);

        // 构建请求参数字符串
        return buildQueryString(params);
    }

    /**
     * 构建查询字符串
     */
    private String buildQueryString(Map<String, String> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);

        StringBuilder query = new StringBuilder();
        for (String key : keys) {
            String value = params.get(key);
            if (StringUtils.isNotBlank(value)) {
                if (query.length() > 0) {
                    query.append("&");
                }
                query.append(key).append("=").append(URLEncoder.encode(value));
            }
        }

        return query.toString();
    }

}
