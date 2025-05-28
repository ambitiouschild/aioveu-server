package com.aioveu.service;

import java.util.Map;

/**
 * @description 统一通知服务
 * @author: 雒世松
 * @date: 2025/2/13 0021 0:26
 */
public interface UnifiedNoticeService {

    /**
     * 通知发送
     * @param msgParam
     * @return
     */
    boolean commonNoticeSend(Map<String, Object> msgParam);

    /**
     * 发送手机验证码
     * @param storeId
     * @param phone
     * @return
     * @throws Exception
     */
    boolean sendLoginCode(Long companyId, Long storeId, String phone) throws Exception;

    /**
     * 注册绑定验证码
     * @param storeId
     * @param phone
     * @return
     * @throws Exception
     */
    boolean sendRegisterBindCode(Long companyId, Long storeId, String phone);

    /**
     * 修改手机号发送验证码
     * @param storeId
     * @param phone 手机号码
     * @return
     * @throws Exception
     */
    String changePhoneCode(Long companyId, Long storeId, String phone) throws Exception;

    /**
     * 教练帮约课验证码
     * @param storeId
     * @param phone 手机号码
     * @return
     * @throws Exception
     */
    boolean enrollGrade(Long companyId, Long storeId, String phone) throws Exception;

    /**
     * 教练帮取消约课验证码
     * @param storeId
     * @param phone 手机号码
     * @return
     * @throws Exception
     */
    boolean cancelEnrollGrade(Long companyId, Long storeId, String phone) throws Exception;

}
