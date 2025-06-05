package com.aioveu.oms.controller.app;

import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.aioveu.oms.model.vo.WxPayResponseVO;
import com.aioveu.oms.service.app.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: TODO 微信回调接口
 * @Author: 雒世松
 * @Date: 2025/6/5 18:05
 * @param
 * @return:
 **/

@Tag(name = "App-微信支付回调接口")
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/callback-api/v1/wx-pay")
public class WxPayCallbackController {

    private final OrderService orderService;

    /**
     * 微信下单支付结果回调
     *
     * @param notifyData 加密数据
     * @param headers    请求头
     * @return {"code": "SUCCESS", "message": "成功"}
     */
    @PostMapping("/notify-order-v3")
    public WxPayResponseVO wxPayOrderNotify(@RequestBody String notifyData,
                                            @RequestHeader HttpHeaders headers) throws WxPayException {
        SignatureHeader signatureHeader = getSignatureHeaderByHttpHeaders(headers);
        orderService.handleWxPayOrderNotify(signatureHeader, notifyData);
        return new WxPayResponseVO()
                .setCode(WxPayConstants.ResultCode.SUCCESS)
                .setMessage("成功");
    }

    /**
     * 微信退款结果回调
     *
     * @param notifyData 加密数据
     * @param headers    请求头
     * @return {"code": "SUCCESS", "message": "成功"}
     */
    @PostMapping("/notify-refund-v3")
    public WxPayResponseVO wxPayRefundNotify(@RequestBody String notifyData,
                                             @RequestHeader HttpHeaders headers) throws WxPayException {
        SignatureHeader signatureHeader = getSignatureHeaderByHttpHeaders(headers);
        orderService.handleWxPayRefundNotify(signatureHeader, notifyData);
        return new WxPayResponseVO()
                .setCode(WxPayConstants.ResultCode.SUCCESS)
                .setMessage("成功");
    }

    private SignatureHeader getSignatureHeaderByHttpHeaders(HttpHeaders headers) {
        SignatureHeader signatureHeader = new SignatureHeader();
        signatureHeader.setSignature(headers.getFirst("Wechatpay-Signature"));
        signatureHeader.setSerial(headers.getFirst("Wechatpay-Serial"));
        signatureHeader.setTimeStamp(headers.getFirst("Wechatpay-Timestamp"));
        signatureHeader.setNonce(headers.getFirst("Wechatpay-Nonce"));
        return signatureHeader;
    }
}
