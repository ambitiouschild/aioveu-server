package com.aioveu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aioveu.constant.PhoneCodeConstant;
import com.aioveu.dto.GradeWeekEnrollUserDTO;
import com.aioveu.dto.UserAvailableCouponDTO;
import com.aioveu.dto.UserGradeTipDTO;
import com.aioveu.entity.Company;
import com.aioveu.entity.WxOpenSubmitAudit;
import com.aioveu.enums.MsgOptionEnum;
import com.aioveu.enums.PayCategoryEnum;
import com.aioveu.enums.UserCouponStatus;
import com.aioveu.enums.WxOpenSubmitAuditStatus;
import com.aioveu.feign.FeignWxOpenAuthClient;
import com.aioveu.feign.WxMaUserClient;
import com.aioveu.feign.vo.WxMpXmlMessageVo;
import com.aioveu.feign.vo.WxOpenXmlMessageVo;
import com.aioveu.feign.vo.WxPayOrderNotifyResultVO;
import com.aioveu.feign.vo.WxPayOrderRefundNotifyResultVO;
import com.aioveu.service.*;
import com.aioveu.utils.JacksonUtils;
import com.aioveu.utils.SportDateUtils;
import com.aioveu.utils.XmlUtil;
import com.aioveu.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/21 0021 0:28
 */
@Slf4j
@Service
public class NoticeServiceImpl implements NoticeService {

    @Resource
    private WxMaUserClient wxMaUserClient;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private Environment environment;

    @Autowired
    private OrderRefundService orderRefundService;

    @Autowired
    private ProductOrderService productOrderService;

    @Autowired
    private RechargeOrderService rechargeOrderService;

    @Autowired
    private IUserCouponService userCouponService;

    @Autowired
    private GradeEnrollUserService gradeEnrollUserService;

    @Autowired
    private CompanyService companyService;

    @Resource
    private FeignWxOpenAuthClient wxOpenAuthClient;

    @Autowired
    private WxOpenAuthorizerService wxOpenAuthorizerService;

    @Autowired
    private WxOpenSubmitAuditService submitAuditService;

    @Autowired
    private MQMessageService mqMessageService;

    @Autowired
    private ChargingRechargeOrderService chargingRechargeOrderService;

    @Override
    public String weChatPayNotice(String xml) {
        return getCommonNotice(xml, PayCategoryEnum.EXERCISE.getCode());
    }

    @Override
    public String fieldWxPayNotice(String xml) {
        return getCommonNotice(xml, PayCategoryEnum.FIELD.getCode());
    }

    @Override
    public String vipWxPayNotice(String xml) {
        return getCommonNotice(xml, PayCategoryEnum.VIP.getCode());
    }

    @Override
    public String productWxPayNotice(String xml) {
        return getCommonNotice(xml, PayCategoryEnum.PRODUCT_ORDER.getCode());
    }

    @Override
    public String storeRechargeNotice(String xml) {
        return getCommonNotice(xml, PayCategoryEnum.STORE_RECHARGE.getCode());
    }

    @Override
    public String weChatRefundNotice(String xml) {
        CommonResponse<WxPayOrderRefundNotifyResultVO> response = wxMaUserClient.wxRefundNotice(xml);
        Map<String, String> sortMap = new TreeMap<>();
        if (response.isSuccess()) {
            WxPayOrderRefundNotifyResultVO refundNotifyResultVO = response.getData();
            if (orderRefundService.refundSuccessWechatNotice(refundNotifyResultVO)) {
                sortMap.put("return_code", "SUCCESS");
                sortMap.put("return_msg", "OK");
                return XmlUtil.getWeChatXmlStr(sortMap);
            }
        }
        sortMap.put("return_code", "FAIL");
        sortMap.put("return_msg", "更新状态失败");
        String result = XmlUtil.getWeChatXmlStr(sortMap);
        log.info("返回:" + result);
        return result;
    }

    private Boolean payResult(String orderId, Date payFinishTime, String payCallCategory) {
        boolean result = false;
        try {
            if (PayCategoryEnum.EXERCISE.getCode().equals(payCallCategory)) {
                // 课程活动 支付更新
                result = orderService.updateOrder2Success(orderId, payFinishTime);
            } else if (PayCategoryEnum.RECHARGE.getCode().equals(payCallCategory)) {
                // 充值订单 支付更新 暂时没有使用
                result = rechargeOrderService.updateOrder2Success(orderId, payFinishTime);
            } else if (PayCategoryEnum.FIELD.getCode().equals(payCallCategory)) {
                // 订场订单 支付更新
                result = orderService.updateFieldOrder2Success(orderId, payFinishTime);
            } else if (PayCategoryEnum.VIP.getCode().equals(payCallCategory)) {
                // 会员卡 支付更新
                result = orderService.updateVipOrder2Success(orderId, payFinishTime);
            } else if (PayCategoryEnum.PRODUCT_ORDER.getCode().equals(payCallCategory)) {
                // 签到支付更新
                result = productOrderService.updateOrder2Success(orderId, payFinishTime);
            } else if (PayCategoryEnum.STORE_RECHARGE.getCode().equals(payCallCategory)) {
                // 增值服务 支付更新
                result = chargingRechargeOrderService.updateOrder2Success(orderId, payFinishTime);
            }
            log.info("微信支付官方通知更新订单:{}, 结果:{}", orderId, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getCommonNotice(String xml, String payCallCategory) {
        log.info("微信支付通知:{}, 类型:{}", xml, payCallCategory);
        Map<String, String> sortMap = new TreeMap<>();
        CommonResponse<WxPayOrderNotifyResultVO> response = wxMaUserClient.wxPayNotice(xml);
        if (response.isSuccess()) {
            WxPayOrderNotifyResultVO wxPayOrderNotifyResultVO = response.getData();
            boolean result = false;
            if (wxPayOrderNotifyResultVO.getReturnCode().equalsIgnoreCase("SUCCESS")) {
                String orderId = wxPayOrderNotifyResultVO.getOutTradeNo();
                if (wxPayOrderNotifyResultVO.getResultCode().equalsIgnoreCase("SUCCESS")) {
                    try {
                        Date payFinishTime = org.apache.commons.lang3.time.DateUtils.parseDate(wxPayOrderNotifyResultVO.getTimeEnd(), "yyyyMMddHHmmss");
                        result = payResult(orderId, payFinishTime, payCallCategory);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    log.error("微信支付失败:{}, code:{}, orderId:{}", wxPayOrderNotifyResultVO.getErrCodeDes(), wxPayOrderNotifyResultVO.getErrCode(), orderId);
                }
            }
            if (result) {
                log.info("状态更新成功");
                sortMap.put("return_code", "SUCCESS");
                sortMap.put("return_msg", "OK");
            }
        }
        if (sortMap.get("return_code") == null) {
            log.info("状态更新失败");
            sortMap.put("return_code", "FAIL");
            sortMap.put("return_msg", "更新状态失败");
        }
        return XmlUtil.getWeChatXmlStr(sortMap);
    }

    @Override
    public String weChatRechargePayNotice(String xml) {
        return getCommonNotice(xml, PayCategoryEnum.RECHARGE.getCode());
    }

    @Override
    public String getCode(String phone, String cachePrefix) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(PhoneCodeConstant.SPORT_PHONE_CODE + cachePrefix + phone);
    }

    @Override
    public boolean everyWeekGradeTips() {
        Date now = new Date();
        Date end = DateUtils.addDays(now, 7);
        try {
            Date endTime = org.apache.commons.lang3.time.DateUtils.parseDate(DateFormatUtils.format(end, "yyyy-MM-dd") + " 23:00", "yyyy-MM-dd HH:mm");

            List<GradeWeekEnrollUserDTO> timeRangeEnrollList = gradeEnrollUserService.getTimeRangeEnrollList(now, endTime);
            Map<Long, List<GradeWeekEnrollUserDTO>> companyEnrollMap = timeRangeEnrollList.stream().collect(Collectors.groupingBy(GradeWeekEnrollUserDTO::getCompanyId));

            List<UserAvailableCouponDTO> userGradeCouponList = userCouponService.getUserGradeCoupon();
            Map<Long, List<UserAvailableCouponDTO>> companyCouponMap = userGradeCouponList.stream().collect(Collectors.groupingBy(UserAvailableCouponDTO::getCompanyId));
            Map<Long, List<UserGradeTipDTO>> noticeMap = new HashMap<>();
            // 遍历 已报名 用户公司
            for (Map.Entry<Long, List<GradeWeekEnrollUserDTO>> entry : companyEnrollMap.entrySet()) {
                List<GradeWeekEnrollUserDTO> timeRangeEnrollCompanyList = entry.getValue();
                Map<String, List<GradeWeekEnrollUserDTO>> companyUserEnrollMap = timeRangeEnrollCompanyList.stream().collect(Collectors.groupingBy(GradeWeekEnrollUserDTO::getUserId));
                List<UserAvailableCouponDTO> userAvailableCouponCompanyList = companyCouponMap.get(entry.getKey());
                // 用户优惠券
                Map<String, List<UserAvailableCouponDTO>> companyUserCouponMap = null;
                if (CollectionUtils.isNotEmpty(userAvailableCouponCompanyList)) {
                    companyUserCouponMap = userAvailableCouponCompanyList.stream().collect(Collectors.groupingBy(UserAvailableCouponDTO::getUserId));
                }
                // 遍历已约课用户
                for (Map.Entry<String, List<GradeWeekEnrollUserDTO>> item : companyUserEnrollMap.entrySet()) {
                    // 同一家公司 如果这个用户已经约课了 则不进行优惠券的短信提醒
                    if (companyUserCouponMap != null) {
                        companyUserCouponMap.remove(item.getKey());
                    }
                    List<GradeWeekEnrollUserDTO> userEnrollList = item.getValue();
                    String gradeTime = userEnrollList.stream().map(u -> SportDateUtils.getDayOfWeek(u.getStartTime())).collect(Collectors.joining("、"));

                    List<UserGradeTipDTO> userGradeTipDTOList = noticeMap.computeIfAbsent(entry.getKey(), k -> new ArrayList<>());
                    UserGradeTipDTO userGradeTipDTO = new UserGradeTipDTO();
                    userGradeTipDTO.setUserId(item.getKey());
                    userGradeTipDTO.setPhone(userEnrollList.get(0).getPhone());
                    userGradeTipDTO.setUsername(userEnrollList.get(0).getUsername());
                    userGradeTipDTO.setCompanyId(entry.getKey());
                    userGradeTipDTO.setStoreId(userEnrollList.get(0).getStoreId());
                    userGradeTipDTO.setStoreName(userEnrollList.get(0).getStoreName());
                    userGradeTipDTO.setGradeTime(gradeTime);
                    userGradeTipDTOList.add(userGradeTipDTO);
                }
                if (companyUserCouponMap != null) {
                    // 遍历该公司未约课 有课券的用户
                    for (Map.Entry<String, List<UserAvailableCouponDTO>> item : companyUserCouponMap.entrySet()) {
                        List<UserGradeTipDTO> userGradeTipDTOList = noticeMap.computeIfAbsent(entry.getKey(), k -> new ArrayList<>());
                        UserGradeTipDTO userGradeTipDTO = createUserGradeTipDTO(item, entry.getKey());
                        userGradeTipDTOList.add(userGradeTipDTO);
                    }
                    // 对于该公司 未约课的用户 上面已添加 不需要再次添加
                    companyCouponMap.remove(entry.getKey());
                }
            }
            // 遍历 未报名 用户公司 这种情况是 该公司下面的用户本周都未约课
            for (Map.Entry<Long, List<UserAvailableCouponDTO>> entry : companyCouponMap.entrySet()) {
                List<UserAvailableCouponDTO> userAvailableCouponCompanyList = companyCouponMap.get(entry.getKey());
                Map<String, List<UserAvailableCouponDTO>> companyUserCouponMap = userAvailableCouponCompanyList.stream().collect(Collectors.groupingBy(UserAvailableCouponDTO::getUserId));
                // 遍历该公司未约课 有课券的用户
                for (Map.Entry<String, List<UserAvailableCouponDTO>> item : companyUserCouponMap.entrySet()) {
                    List<UserGradeTipDTO> userGradeTipDTOList = noticeMap.computeIfAbsent(entry.getKey(), k -> new ArrayList<>());
                    UserGradeTipDTO userGradeTipDTO = createUserGradeTipDTO(item, entry.getKey());
                    userGradeTipDTOList.add(userGradeTipDTO);
                }
            }

            String[] activeProfiles = environment.getActiveProfiles();
            String activeProfile = activeProfiles[0];

            // 遍历本次短信通知
            for (Map.Entry<Long, List<UserGradeTipDTO>> entry : noticeMap.entrySet()) {
                Company company = companyService.getById(entry.getKey());
                if (company.getBalance().doubleValue() <= 0) {
                    log.error("公司:{}没有余额，上课提醒短信发送失败", company.getName());
                    continue;
                }
                List<UserGradeTipDTO> noticeList = entry.getValue();
                BigDecimal money = new BigDecimal("0.045").multiply(new BigDecimal(noticeList.size()));
                if (company.getBalance().compareTo(money) < 0) {
                    log.error("公司:{}余额不足，上课提醒短信发送失败", company.getName());
                    continue;
                }
                // 发送短信
                for (UserGradeTipDTO notice : noticeList) {
                    try {
                        log.info("短信通知:" + JacksonUtils.obj2Json(notice));
                        if (activeProfile.equalsIgnoreCase("prod")) {
                            Map<String, Object> msgMap = new HashMap<>();
                            msgMap.put("name", notice.getUsername());
                            msgMap.put("store", notice.getStoreName());
                            msgMap.put("phone", notice.getPhone());
                            if (notice.getGradeTime() != null) {
                                msgMap.put("week", notice.getGradeTime());
                                msgMap.put("minute", gradeEnrollUserService.getCancelGradeMinute(notice.getStoreId()));
                                mqMessageService.sendNoticeMessage(msgMap, MsgOptionEnum.WEEK_GRADE_MSG.getCode(), notice.getStoreId());
                            } else {
                                // 没有课券 不进行提醒
                                if (notice.getToUsedCouponNum() == 0 && notice.getCouponNum() == 0) {
                                    continue;
                                }
                                // 课券 提醒
                                // 未使用课券
                                msgMap.put("unUsedNum", notice.getCouponNum());
                                // 已使用课券
                                msgMap.put("usedNum", notice.getUsedCouponNum());
                                // 已约课 待使用课券
                                msgMap.put("toUsedNum", notice.getToUsedCouponNum());
                                mqMessageService.sendNoticeMessage(msgMap, MsgOptionEnum.WEEK_GRADE_COUPON_MSG.getCode(), notice.getStoreId());
                            }
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建 UserGradeTipDTO
     * @param item
     * @param companyId
     * @return
     */
    private UserGradeTipDTO createUserGradeTipDTO(Map.Entry<String, List<UserAvailableCouponDTO>> item, Long companyId) {
        List<UserAvailableCouponDTO> userCouponList = item.getValue();
        long unUseCount = userCouponList.stream().filter(uc -> Objects.equals(uc.getStatus(), UserCouponStatus.USABLE.getCode())).count();
        long usedCount = userCouponList.stream().filter(uc -> Objects.equals(uc.getStatus(), UserCouponStatus.USED.getCode())).count();
        long tempUsedCount = userCouponList.stream().filter(uc -> Objects.equals(uc.getStatus(), UserCouponStatus.TEMP_USED.getCode())).count();

        UserGradeTipDTO userGradeTipDTO = new UserGradeTipDTO();
        userGradeTipDTO.setUserId(item.getKey());
        userGradeTipDTO.setPhone(userCouponList.get(0).getPhone());
        userGradeTipDTO.setUsername(userGradeTipDTO.getPhone());
        userGradeTipDTO.setCompanyId(companyId);
        userGradeTipDTO.setCouponNum((int) unUseCount);
        userGradeTipDTO.setUsedCouponNum((int) usedCount);
        userGradeTipDTO.setToUsedCouponNum((int) tempUsedCount);
        userGradeTipDTO.setStoreName(userCouponList.get(0).getStoreName());
        userGradeTipDTO.setStoreId(userCouponList.get(0).getStoreId());
        return userGradeTipDTO;
    }

    @Override
    public String wxOpenReceiveTicket(String requestBody, String timestamp, String nonce, String signature, String encType, String msgSignature) {
        log.info(
                "wxOpenReceiveTicket接收微信请求：[signature=[{}], encType=[{}], msgSignature=[{}],"
                        + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                signature, encType, msgSignature, timestamp, nonce, requestBody);
        CommonResponse<WxOpenXmlMessageVo> commonResponse = wxOpenAuthClient.receiveTicket(requestBody, timestamp, nonce, signature, encType, msgSignature);
        WxOpenXmlMessageVo data = commonResponse.getData();
        String infoType = data.getInfoType();
        if("unauthorized".equals(infoType)){
            wxOpenAuthorizerService.revokeAuth(data.getAppId(), data.getAuthorizerAppid());
        }
        return "success";
    }

    @Override
    public String wxOpenCallBack(String requestBody, String appId, String signature, String timestamp, String nonce, String openid, String encType, String msgSignature) {
        log.info(
                "wxOpenCallBack接收微信请求：[appId=[{}], openid=[{}], signature=[{}], encType=[{}], msgSignature=[{}],"
                        + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                appId, openid, signature, encType, msgSignature, timestamp, nonce, requestBody);
        CommonResponse<WxMpXmlMessageVo> commonResponse = wxOpenAuthClient.callBack(requestBody, appId, signature, timestamp, nonce, openid,encType, msgSignature);
        if (commonResponse.isSuccess()){
            //TODO 2025记录微信开放平台推送的消息和事件信息
            WxMpXmlMessageVo data = commonResponse.getData();
            log.info("wxOpenCallBack数据:{}", JacksonUtils.obj2Json(data));
            if (WxOpenSubmitAuditStatus.valueOf(data.getEvent()) != null){
                WxOpenSubmitAuditStatus auditStatus = WxOpenSubmitAuditStatus.valueOf(data.getEvent());
                WxOpenSubmitAudit entity = new WxOpenSubmitAudit();
                entity.setAuditStatus(auditStatus.getCode());
                entity.setResultDate(new Date(data.getCreateTime()));
                entity.setFailReason(data.getReason());
                submitAuditService.updateAuditResultByAppId(appId, entity);
            }
        }
        return "success";
    }

    @Override
    public String huiFuPayCallback(Map<String, Object> params) {
        log.info("hui-fu:{}", JacksonUtils.obj2Json(params));
        // 使用汇付公钥验签
//        if (!RsaUtils.verify(data, PUBLIC_KEY, sign)) {
//            // 验签失败处理
//            return "";
//        }
        if ("00000000".equals(params.get("resp_code"))) {
            String respDataStr = params.get("resp_data") + "";
            JSONObject respData = JSONObject.parseObject(respDataStr);
            if ("S".equals(respData.getString("trans_stat"))) {
                // 交易成功
                String orderId = respData.getString("req_seq_id");
                String remark = respData.getString("remark");
                Date payFinishTime = null;
                try {
                    payFinishTime = org.apache.commons.lang3.time.DateUtils.parseDate(respData.getString("end_time"), "yyyyMMddHHmmss");
                    boolean result = payResult(orderId, payFinishTime, remark);
                    if (result) {
                        return "success";
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            } else {
                log.error(respData.get("bank_code") + ":" + respData.get("bank_message"));
            }
        } else {
            log.error(params.get("resp_code") + ":" + params.get("resp_desc"));
        }
        return "fail";
    }

    @Override
    public String huiFuRefund(Map<String, Object> params) {
        log.info("hui-fu-refund:{}", JacksonUtils.obj2Json(params));
        if ("00000000".equals(params.get("resp_code"))) {
            String respDataStr = params.get("resp_data") + "";
            JSONObject respData = JSONObject.parseObject(respDataStr);
            if ("S".equals(respData.getString("trans_stat"))) {
                // 退款成功
                String refundId = respData.getString("req_seq_id");
                String userReceivedAccount = "";
                JSONObject wxResponse = respData.getJSONObject("wx_response");
                if (wxResponse != null) {
                    userReceivedAccount = wxResponse.getString("user_received_account");
                }
                String transFinishTime = respData.getString("trans_finish_time");
                try {
                    Date refundFinishTime = org.apache.commons.lang3.time.DateUtils.parseDate(transFinishTime, "yyyyMMddHHmmss");
                    if (orderRefundService.refundSuccess(refundId, refundFinishTime, userReceivedAccount)) {
                        return "success";
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            } else {
                log.error(respData.get("bank_code") + ":" + respData.get("bank_message"));
            }
        } else {
            log.error(params.get("resp_code") + ":" + params.get("resp_desc"));
        }
        return "fail";
    }
}
