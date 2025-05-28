package com.aioveu.controller;

import com.aioveu.annotation.IgnoreResponseAdvice;
import com.aioveu.service.NoticeService;
import com.aioveu.service.UnifiedNoticeService;
import com.aioveu.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;

@Slf4j
@RestController
@RequestMapping("/api/v1/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private UnifiedNoticeService unifiedNoticeService;

    @IgnoreResponseAdvice
    @PostMapping("/wechat-pay")
    public String wxPayNotice(@RequestBody String xmlData) {
        return noticeService.weChatPayNotice(xmlData);
    }

    @IgnoreResponseAdvice
    @PostMapping("/wechat-recharge-pay")
    public String weChatRechargePayNotice(@RequestBody String xmlData) {
        return noticeService.weChatRechargePayNotice(xmlData);
    }

    @IgnoreResponseAdvice
    @PostMapping("/field-wechat-pay")
    public String fieldWxPayNotice(@RequestBody String xmlData) {
        return noticeService.fieldWxPayNotice(xmlData);
    }

    @IgnoreResponseAdvice
    @PostMapping("/vip-wechat-pay")
    public String vipWxPayNotice(@RequestBody String xmlData) {
        return noticeService.vipWxPayNotice(xmlData);
    }

    @IgnoreResponseAdvice
    @PostMapping("/product-wechat-pay")
    public String productWxPayNotice(@RequestBody String xmlData) {
        return noticeService.productWxPayNotice(xmlData);
    }

    @IgnoreResponseAdvice
    @PostMapping("/store-recharge-wechat-pay")
    public String storeRechargeNotice(@RequestBody String xmlData) {
        return noticeService.storeRechargeNotice(xmlData);
    }

    @IgnoreResponseAdvice
    @GetMapping("/wechat-pay-test")
    public String wxPayNoticeTest() {
        Map<String, String> sortMap = new TreeMap<>();
        sortMap.put("return_code", "SUCCESS");
        sortMap.put("return_msg", "OK");
        return XmlUtil.getWeChatXmlStr(sortMap);
    }

    @IgnoreResponseAdvice
    @GetMapping("/test-str")
    public String testStr() throws Exception {
        Thread.sleep(6000);
        return "i am just text";
    }

    @GetMapping("/test-text")
    public String testText() {
        return "i am text";
    }

    @GetMapping("code/{phone}")
    public boolean phoneCode(@PathVariable String phone, @RequestParam(required = false) Long storeId, @RequestParam(required = false) Long companyId) throws Exception {
        return unifiedNoticeService.sendLoginCode(companyId, storeId, phone);
    }

    @IgnoreResponseAdvice
    @PostMapping("/wxRefund")
    public String wxRefund(@RequestBody String xmlData) {
        log.info("微信退款成功回调了:{}", xmlData);
        return noticeService.weChatRefundNotice(xmlData);
    }

    /**
     *  修改手机号发送验证码
     */
    @GetMapping("/change-phone/{phone}")
    public String changePhone(@PathVariable String phone, @RequestParam(required = false) Long storeId, @RequestParam(required = false) Long companyId) throws Exception {
        return unifiedNoticeService.changePhoneCode(companyId, storeId, phone);
    }

    /**
     *  修改手机号发送验证码
     */
    @GetMapping("/enroll-grade/{phone}")
    public boolean enrollGrade(@PathVariable String phone, @RequestParam(required = false) Long storeId, @RequestParam(required = false) Long companyId) throws Exception {
        return unifiedNoticeService.enrollGrade(companyId, storeId, phone);
    }

    /**
     *  帮忙取消约课发送验证码
     */
    @GetMapping("/cancel-enroll-grade/{phone}")
    public boolean cancelEnrollGrade(@PathVariable String phone, @RequestParam(required = false) Long storeId, @RequestParam(required = false) Long companyId) throws Exception {
        return unifiedNoticeService.cancelEnrollGrade(companyId, storeId, phone);
    }
    /**
     * 用于接收平台推送给微信第三方平台账号的消息与事件，如授权事件通知、component_verify_ticket等
     * @param requestBody
     * @param timestamp
     * @param nonce
     * @param signature
     * @param encType
     * @param msgSignature
     * @return
     */
    @IgnoreResponseAdvice
    @PostMapping("/wx-open/receive-ticket")
    public String wxOpenReceiveTicket(@RequestBody(required = false) String requestBody,
                                      @RequestParam("timestamp") String timestamp,
                                      @RequestParam("nonce") String nonce,
                                      @RequestParam("signature") String signature,
                                      @RequestParam(name = "encrypt_type", required = false) String encType,
                                      @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        return noticeService.wxOpenReceiveTicket(requestBody, timestamp,nonce,signature,encType,msgSignature);
    }

    /**
     * 微信第三方平台消息与事件接收
     * 用于代授权的公众号或小程序的接收平台推送的消息与事件
     * @param requestBody
     * @param appId
     * @param signature
     * @param timestamp
     * @param nonce
     * @param openid
     * @param encType
     * @param msgSignature
     */
    @IgnoreResponseAdvice
    @PostMapping("/wx-open/{appId}/callback")
    public void wxOpenCallBack(@RequestBody(required = false) String requestBody,
                                 @PathVariable("appId") String appId,
                                 @RequestParam("signature") String signature,
                                 @RequestParam("timestamp") String timestamp,
                                 @RequestParam("nonce") String nonce,
                                 @RequestParam("openid") String openid,
                                 @RequestParam("encrypt_type") String encType,
                                 @RequestParam("msg_signature") String msgSignature) {
        noticeService.wxOpenCallBack(requestBody,appId,signature, timestamp,nonce,openid,encType,msgSignature);
    }

    @IgnoreResponseAdvice
    @PostMapping("/hf-pay-callback")
    public String fieldHuiFuNotice(@RequestParam Map<String, Object> params) {
        return noticeService.huiFuPayCallback(params);
    }

    @IgnoreResponseAdvice
    @PostMapping("/hf-refund-callback")
    public String huiFuRefund(@RequestParam Map<String, Object> params) {
        return noticeService.huiFuRefund(params);
    }

}
