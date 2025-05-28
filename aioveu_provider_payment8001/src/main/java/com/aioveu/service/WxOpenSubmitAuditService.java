package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.WxOpenSubmitAudit;

/**
 * @Author： yao
 * @Date： 2025/2/14 15:28
 * @Describe：
 */
public interface WxOpenSubmitAuditService extends IService<WxOpenSubmitAudit> {

    /**
     * 获取正在审核的记录
     * @param appId
     * @return
     */
    WxOpenSubmitAudit getLatestSubmitAudit(String appId);

    Boolean updateStatus(Long id,Integer status);

    Boolean updateStatusByAppId(String appId,Integer status);

    /**
     * 更新审核结果
     * @param appId
     * @param wxOpenSubmitAudit
     * @return
     */
    Boolean updateAuditResultByAppId(String appId, WxOpenSubmitAudit wxOpenSubmitAudit);

}
