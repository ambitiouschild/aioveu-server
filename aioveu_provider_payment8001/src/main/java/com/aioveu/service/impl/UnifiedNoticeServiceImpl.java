package com.aioveu.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.aioveu.constant.PhoneCodeConstant;
import com.aioveu.constant.ResultEnum;
import com.aioveu.entity.*;
import com.aioveu.enums.ChargingOptionEnum;
import com.aioveu.exception.SportException;
import com.aioveu.feign.WxMaUserClient;
import com.aioveu.feign.form.MpCommonNoticeForm;
import com.aioveu.service.*;
import com.aioveu.utils.AliSmsUtil;
import com.aioveu.utils.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description 统一通知服务
 * @author: 雒世松
 * @date: 2025/2/13 0021 0:26
 */
@Slf4j
@Service
public class UnifiedNoticeServiceImpl implements UnifiedNoticeService {

    @Autowired
    private MessageConfigService messageConfigService;

    @Resource
    private WxMaUserClient wxMaUserClient;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserOpenIdService userOpenIdService;

    @Autowired
    private MessageReceiverService messageReceiveService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private Environment environment;

    @Autowired
    private StoreChargingOptionService storeChargingOptionService;

    @Autowired
    private StoreService storeService;

    @Override
    public boolean commonNoticeSend(Map<String, Object> msgParam) {
        Long storeId = Long.parseLong(msgParam.get("storeId") + "");
        String msgCode = msgParam.get("msgCode").toString();
        List<MessageConfig> msgConfigList = messageConfigService.getMessageConfigByStoreAndMsgCode(storeId, msgCode);
        if (CollectionUtils.isEmpty(msgConfigList)) {
            log.error("店铺:{}, 消息类型:{} 没有配置", storeId, msgCode);
            return false;
        } else {
            for (MessageConfig msgConfig : msgConfigList) {
                try {
                    if (msgConfig.getMsgKey() != null && msgParam.get(msgConfig.getMsgKey()) == null) {
                        log.info("消息:{} 参数缺少:{}参数", msgConfig.getMsgCode(), msgConfig.getMsgKey());
                        continue;
                    }
                    if ("wechat_mp".equals(msgConfig.getNoticeCode())) {
                        // 发生公众号消息
                        commonMpNotice(msgConfig, msgParam, storeId);
                    } else if ("system".equals(msgConfig.getNoticeCode())) {
                        // 发送系统消息
                        Message message = new Message();
                        message.setStatus(1);
                        message.setStoreId(storeId);
                        message.setCompanyId(msgConfig.getCompanyId());
                        if (msgParam.get("userId") != null) {
                            message.setUserId(msgParam.get("userId") + "");
                        }

                        Map<String, Object> config = msgConfig.getConfig();
                        message.setName(config.get("name") + "");
                        message.setMsgType(Integer.parseInt(config.get("msgType") + ""));

                        List<String> dataList = (List<String>) config.get("dataList");
                        StringBuilder sb = new StringBuilder();
                        for (String data : dataList) {
                            String[] dataValueArr = data.split(":");
                            sb.append(dataValueArr[1]).append("：").append(msgParam.get(dataValueArr[0])).append("<br>");
                        }
                        message.setContent(sb.toString());

                        if (message.getMsgType() == 2) {
                            if (storeChargingOptionService.chargingCheck(msgConfig.getCompanyId(), msgConfig.getStoreId(), ChargingOptionEnum.SYSTEM_OPERATE, 1)) {
                                messageService.create(message);
                                storeChargingOptionService.charging(msgConfig.getCompanyId(), msgConfig.getStoreId(), ChargingOptionEnum.SYSTEM_OPERATE, 1);
                            }
                        } else {
                            messageService.create(message);
                        }
                    } else if ("wechat_mini_app".equals(msgConfig.getNoticeCode())) {
                        commonMiniAppNotice(msgConfig, msgParam);
                    } else if ("sms".equals(msgConfig.getNoticeCode())) {
                        Map<String, Object> config = msgConfig.getConfig();
                        String templateCode = config.get("templateCode") + "";
                        JSONObject jsonObject = new JSONObject();
                        Map<String, String> dataMap = (Map<String, String>) config.get("dataMap");
                        // 短信发送数据
                        for (String dataKey : dataMap.keySet()) {
                            String dataValue = dataMap.get(dataKey);
                            String[] dataValueArr = dataValue.split(":");
                            jsonObject.put(dataKey, msgParam.get(dataValueArr[0]) + "");
                        }
                        String templateParam = JSONUtil.toJsonStr(jsonObject);
                        String outId = null;
                        if (config.get("outId") != null && msgParam.get(config.get("outId") + "") != null) {
                            outId = msgParam.get(config.get("outId") + "") + "";
                        }
                        String receiveUser = null;
                        List<MessageReceiver> receiveList = messageReceiveService.getList(msgConfig.getStoreId(), msgConfig.getId());
                        if (CollectionUtils.isNotEmpty(receiveList)) {
                            receiveUser = receiveList.stream().map(MessageReceiver::getPhone).collect(Collectors.joining(","));
                        }
                        if (StringUtils.isEmpty(receiveUser)) {
                            // 消息未配置发送用户 检查是否指定发送用户
                            if (msgParam.get("phone") != null) {
                                receiveUser = msgParam.get("phone") + "";
                            } else {
                                log.error("消息配置:{}未配置发送用户", msgConfig.getId());
                                return false;
                            }
                        } else if (msgParam.get("phone") != null){
                            receiveUser = receiveUser + "," + msgParam.get("phone");
                        }
                        // 发送给具体用户
                        String[] receiveUsers = receiveUser.split(",");

                        int count = receiveUsers.length;
                        if (count > 0) {
                            //检查商户 服务号消息余额，不足 提醒 并且不进行发送
                            if (storeChargingOptionService.chargingCheck(msgConfig.getCompanyId(), storeId, ChargingOptionEnum.SMS, count)) {
                                for (String phone : receiveUsers) {
                                    String bizId = AliSmsUtil.sendSms(phone, templateCode, templateParam, outId);
                                    log.info("短信:{}发送成功:{}", phone, bizId);
                                }
                                storeChargingOptionService.charging(msgConfig.getCompanyId(), msgConfig.getStoreId(), ChargingOptionEnum.SMS, count);
                            }
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 通用小程序消息发送
     * @param msgConfig
     * @param msgParam
     * @return
     */
    private boolean commonMiniAppNotice(MessageConfig msgConfig, Map<String, Object> msgParam) {
        if (msgParam.get("miniOpenId") == null) {
            log.error("消息接收人的openId未配置");
            return false;
        }
        Map<String, Object> config = msgConfig.getConfig();
        MpCommonNoticeForm form = getMpCommonNoticeForm(config, msgParam);
        form.setMiniAppId(form.getAppId());
        if (config.get("pagePath") != null) {
            form.setMiniAppId(config.get("miniAppId") + "");
            String pagePath = config.get("pagePath").toString();
            String newUrl = replaceParameter(pagePath, msgParam);
            form.setMiniPagePath(newUrl);
        }
        form.setToUser(msgParam.get("miniOpenId") + "");

        if (storeChargingOptionService.chargingCheck(msgConfig.getCompanyId(), msgConfig.getStoreId(), ChargingOptionEnum.MINI_APP_MSG, 1)) {
            wxMaUserClient.commonMiniAppNotice(form);
            storeChargingOptionService.charging(msgConfig.getCompanyId(), msgConfig.getStoreId(), ChargingOptionEnum.MINI_APP_MSG, 1);
        }
        return true;
    }

    /**
     * 通用服务号消息发送
     * @param msgConfig
     * @param msgParam
     * @return
     */
    private boolean commonMpNotice(MessageConfig msgConfig, Map<String, Object> msgParam, Long storeId) {
        Map<String, Object> config = msgConfig.getConfig();
        String openId = null;
        if (config.get("toUser") != null && msgParam.get("userId") != null) {
            // 消息需要发送给消息用户
            String userId = msgParam.get("userId") + "";
            // 查询用户的openId
            openId = userOpenIdService.getMpOpenIdByStoreIdAndUserId(storeId, userId, true);
        }

        String receiveUser = null;
        List<MessageReceiver> receiveList = messageReceiveService.getList(msgConfig.getStoreId(), msgConfig.getId());
        if (CollectionUtils.isNotEmpty(receiveList)) {
            receiveUser = receiveList.stream().map(MessageReceiver::getOpenId).collect(Collectors.joining(","));
        }
        if (StringUtils.isEmpty(receiveUser)) {
            // 消息未配置发送用户 检查是否指定发送用户
            if (StringUtils.isNotEmpty(openId)) {
                receiveUser = openId;
            } else {
                log.error("消息配置:{}未配置发送用户", msgConfig.getId());
                return false;
            }
        } else if (openId != null){
            receiveUser = receiveUser + "," + openId;
        }
        MpCommonNoticeForm form = getMpCommonNoticeForm(config, msgParam);
        if (config.get("miniAppId") != null && config.get("pagePath") != null) {
            form.setMiniAppId(config.get("miniAppId") + "");
            String pagePath = config.get("pagePath").toString();
            String newUrl = replaceParameter(pagePath, msgParam);
            form.setMiniPagePath(newUrl);
        }
        // 发送给具体用户
        String[] receiveUsers = receiveUser.split(",");
        int count = receiveUsers.length;
        if (count > 0) {
            //检查商户 服务号消息余额，不足 提醒 并且不进行发送
            if (storeChargingOptionService.chargingCheck(msgConfig.getCompanyId(), storeId, ChargingOptionEnum.MP_MSG, count)) {
                for (String toUser : receiveUsers) {
                    form.setToUser(toUser);
                    wxMaUserClient.commonMpNotice(form);
                }
                storeChargingOptionService.charging(msgConfig.getCompanyId(), msgConfig.getStoreId(), ChargingOptionEnum.MP_MSG, count);
            }
        }
        return true;
    }

    private MpCommonNoticeForm getMpCommonNoticeForm(Map<String, Object> config, Map<String, Object> msgParam) {
        MpCommonNoticeForm form = new MpCommonNoticeForm();
        form.setAppId(config.get("appId") + "");
        form.setTemplateId(config.get("templateId") + "");
        Map<String, String> dataMap = (Map<String, String>) config.get("dataMap");
        // 服务号消息设置数据
        for (String dataKey : dataMap.keySet()) {
            String dataValue = dataMap.get(dataKey);
            String[] dataValueArr = dataValue.split(":");
            dataMap.put(dataKey, msgParam.get(dataValueArr[0]) + "");
        }
        form.setDataMap(dataMap);
        return form;
    }

    /**
     * url参数解析，从msgParam传递参数给url
     * @param url
     * @param msgParam
     * @return
     */
    public String replaceParameter(String url, Map<String, Object> msgParam) {
        try{
            URI uri = new URI(url);
            String query = uri.getQuery();
            Map<String, String> params = new HashMap<>();

            // 解析参数
            if (query != null) {
                String[] pairs = query.split("&");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    String key = keyValue[0];
                    if (keyValue.length > 1){
                        String value = keyValue[1];
                        if(msgParam.containsKey(keyValue[1])) {
                            value = msgParam.get(keyValue[1]).toString();
                        }
                        params.put(key, value);
                    }
                }
            }
            // 重新拼接URL
            StringBuilder newQuery = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (newQuery.length() > 0) {
                    newQuery.append("&");
                }
                newQuery.append(entry.getKey()).append("=").append(entry.getValue());
            }
            return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), newQuery.toString(), uri.getFragment()).toString();
        }catch (Exception e){
            log.error("replaceParameter error:{}",e.getMessage());
        }
        return url;
    }

    @Override
    public boolean sendLoginCode(Long companyId, Long storeId, String phone) throws Exception {
        return StringUtils.isNotEmpty(sendPhoneCode(companyId, storeId, phone, PhoneCodeConstant.CODE_TYPE_LOGIN));
    }

    /**
     * 发送手机验证码
     * @param storeId
     * @param phone
     * @param cachePrefix
     * @return
     * @throws Exception
     */
    private String sendPhoneCode(Long companyId, Long storeId, String phone, String cachePrefix) throws Exception {
        if (companyId == null && storeId == null) {
            log.info("不做短信计费校验");
        } else if (companyId == null){
            Store store = storeService.getById(storeId);
            companyId = store.getCompanyId();
        }
        if (!storeChargingOptionService.chargingCheck(companyId, storeId, ChargingOptionEnum.SMS, 1)) {
            throw new SportException("短信服务异常, 请联系商家处理");
        }
        if (StringUtils.isNotEmpty(phone) && phone.length() == 11) {
            ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
            if (valueOperations.get(PhoneCodeConstant.SPORT_PHONE_CODE_SEND + cachePrefix + phone) != null) {
                throw new SportException(ResultEnum.PARAM_ERROR.getCode(), phone + "发送太频繁,请稍后尝试!");
            }
            String code = IdUtils.get4NumberCode();
            String[] activeProfiles = environment.getActiveProfiles();
            String activeProfile = activeProfiles[0];
            if (activeProfile.equalsIgnoreCase("dev")) {
                log.info("开发环境，不发送短信：" + code);
            } else if (PhoneCodeConstant.CODE_TYPE_LOGIN.equals(cachePrefix)) {
                // 发送登录验证码
                AliSmsUtil.justCode(phone, code, null);
            } else if (PhoneCodeConstant.CODE_TYPE_CHANGE.equals(cachePrefix)) {
                // 发送修改手机号验证码
                AliSmsUtil.justCode(phone, code, null);
            } else if (PhoneCodeConstant.CODE_TYPE_ENROLL_GRADE.equals(cachePrefix)) {
                // 帮约课发送验证码
                AliSmsUtil.appointmentClass(phone, code, null);
            } else if (PhoneCodeConstant.CODE_TYPE_CANCEL_GRADE.equals(cachePrefix)) {
                // 帮约课发送验证码
                AliSmsUtil.cancelClassCode(phone, code, null);
            } else if (PhoneCodeConstant.CODE_TYPE_REGISTER_BIND.equals(cachePrefix)) {
                // 发送注册绑定验证码
                AliSmsUtil.justCode(phone, code, null);
            }
            log.info(phone + "的验证码:" + code);
            valueOperations.set(PhoneCodeConstant.SPORT_PHONE_CODE_SEND + cachePrefix + phone, phone, 50, TimeUnit.SECONDS);
            valueOperations.set(PhoneCodeConstant.SPORT_PHONE_CODE + cachePrefix + phone, code, 30, TimeUnit.MINUTES);
            if (companyId != null) {
                storeChargingOptionService.charging(companyId, storeId, ChargingOptionEnum.SMS, 1);
            }
            return code;
        } else {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "手机号码填写错误, 请检查！");
        }
    }

    @Override
    public boolean sendRegisterBindCode(Long companyId, Long storeId, String phone) {
        try {
            return StringUtils.isNotEmpty(sendPhoneCode(companyId, storeId, phone, PhoneCodeConstant.CODE_TYPE_REGISTER_BIND));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String changePhoneCode(Long companyId, Long storeId, String phone) throws Exception {
        // SPORT_PHONE_CODE_SEND_CHANGE_17621190026
        return sendPhoneCode(companyId, storeId, phone, PhoneCodeConstant.CODE_TYPE_CHANGE);
    }

    @Override
    public boolean enrollGrade(Long companyId, Long storeId, String phone) throws Exception {
        String code = sendPhoneCode(companyId, storeId, phone, PhoneCodeConstant.CODE_TYPE_ENROLL_GRADE);
        return StringUtils.isNotEmpty(code);
    }

    @Override
    public boolean cancelEnrollGrade(Long companyId, Long storeId, String phone) throws Exception {
        String code = sendPhoneCode(companyId, storeId, phone, PhoneCodeConstant.CODE_TYPE_CANCEL_GRADE);
        return StringUtils.isNotEmpty(code);
    }

}
