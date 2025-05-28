package com.aioveu.data.sync.parent;

import com.aioveu.data.sync.FieldSyncHandleError;
import com.aioveu.data.sync.common.CommonVenue;
import com.aioveu.dto.FieldPlanDTO;
import com.aioveu.entity.SyncDataAccountConfig;
import com.aioveu.request.response.DynamicProxyIP;
import com.aioveu.vo.FieldPlanVO;

import java.util.List;

/**
 * @description 数据同步抽象接口
 * @author: 雒世松
 * @date: 2025/3/21 15:41
 */
public interface DataSyncProcessor {

    /**
     * 锁场
     */
    String LOCK = "Lock";

    /**
     * 解锁
     */
    String UNLOCK = "unLock";

    /**
     * 第三方平台锁场
     * @param fieldPlanList
     * @param config
     * @return
     */
    List<FieldSyncHandleError> lockField(List<FieldPlanDTO> fieldPlanList, SyncDataAccountConfig config);

    /**
     * 第三方平台解锁场
     * @param fieldPlanList
     * @param config
     * @return
     */
    List<FieldSyncHandleError> unLockField(List<FieldPlanDTO> fieldPlanList, SyncDataAccountConfig config);

    /**
     * 获取动态的代理IP
     * @param storeId
     * @return
     */
    DynamicProxyIP getDynamicProxyIP(Long storeId);

    /**
     * 初始化基础数据到缓存
     * @param config
     */
    boolean initData(SyncDataAccountConfig config);

    /**
     * 校验用户账号密码是否正确，并且缓存认证信息
     * @param config
     * @return
     */
    boolean verifyAccount(SyncDataAccountConfig config);

    /**
     * 清理缓存
     * @param storeId
     * @return
     */
    boolean clearCache(Long storeId);

    /**
     * 定期同步订场信息
     * @param config
     * @return
     */
    List<FieldPlanVO> regularSyncField(SyncDataAccountConfig config);

    /**
     * 全量同步订场信息 从平台同步到趣数
     * @param config
     * @return
     */
    List<FieldPlanVO> fullSyncFieldPlatform2Qs(SyncDataAccountConfig config);

    /**
     * 获取平台的可订场天数
     * @return
     */
    int getFieldBookDay();

    /**
     * 获取匹配的场馆id
     * @param config
     * @return
     */
    List<Long> getVenueIdList(SyncDataAccountConfig config);

    /**
     * 清理登录缓存
     * @param storeId
     * @return
     */
    boolean clearLoginCache(Long storeId);

    /**
     * 平台登录
     * @param config
     * @return
     */
    String login(SyncDataAccountConfig config);

    /**
     * 同步场馆
     * @param config
     * @return
     */
    boolean syncVenue(SyncDataAccountConfig config);

    /**
     * 获取已缓存的第三方场馆
     * @param config
     * @return
     */
    List<CommonVenue> getVenueFields(SyncDataAccountConfig config);

}
