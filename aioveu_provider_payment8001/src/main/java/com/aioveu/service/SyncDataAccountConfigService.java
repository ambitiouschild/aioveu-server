package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.SyncDataAccountConfig;
import com.aioveu.form.SyncDataAccountConfigForm;
import com.aioveu.vo.FieldPlanVO;
import com.aioveu.vo.SyncDataAccountConfigVO;

import java.util.List;

/**
 * @Author： yao
 * @Date： 2024/11/27 10:43
 * @Describe：
 */
public interface SyncDataAccountConfigService extends IService<SyncDataAccountConfig> {

    /**
     * 分页查询
     * @param page
     * @param size
     * @param storeId
     * @return
     */
    IPage<SyncDataAccountConfigVO> getPageList(Integer page, Integer size, Long storeId);


    /**
     * 获取详情
     * @param id
     * @return
     */
    SyncDataAccountConfigVO detail(String id);
    /**
     * 根据门店id获取list
     * @param storeId
     * @return
     */
    List<SyncDataAccountConfig> getListByStoreId(Long storeId);

    /**
     * 更新状态
     * @param id
     * @param status
     * @return
     */
    boolean changeStatus(String id, Integer status);

    /**
     * 保存
     * @param form
     */
    String saveConfig(SyncDataAccountConfigForm form);

    /**
     * 获取登录状态
     * @param id
     * @return
     */
    String getLoginStatus(String id);

    /**
     * 设置登录验证码
     * @param id
     * @param loginCode
     * @return
     */
    boolean setLoginCode(String id, String loginCode);

    /**
     * 首次全量同步 同步整个店铺的双向同步
     * @param storeId
     * @return
     */
    boolean firstFullSync(Long storeId);

    /**
     * 同步数据 管理端 用户收到点击同步 非全量同步 完成整个店铺的同步
     * @return
     */
    List<FieldPlanVO> syncDataByStoreId(Long storeId);

    /**
     * 同步数据(非全量)，从单个第三方平台获取订场数据，插入到本系统中
     * @param config
     * @return
     */
    List<FieldPlanVO> syncPlatform2Qs(SyncDataAccountConfig config);

    /**
     * 全量同步 趣数的锁场到第三方平台 指定平台
     * @param storeId
     * @param platformCode
     * @return
     */
    boolean syncFullQs2Platform(Long storeId, String platformCode);

    /**
     * 全量同步数据，从第三方平台获取订场数据，插入到本系统中
     * @param id 指定同步账号的id
     * @return
     */
    boolean syncFullPlatform2QsById(String id);


    /**
     * 第三方平台全量同步到趣数
     * @param config
     * @return
     */
    boolean fullSyncPlatform2Qs(SyncDataAccountConfig config);

    /**
     * 平台初始化
     * @param storeId
     * @param platformCode
     * @return
     */
    boolean init(Long storeId, String platformCode);

    /**
     * 发送同步失败消息
     * @param name
     * @param platformName
     * @param venueName
     * @param time
     * @param fieldName
     * @param companyId
     * @param storeId
     * @param productId
     * @param error
     */
    void sendSyncFailNotice(String name, String platformName, String venueName, String time, String fieldName, Long companyId, Long storeId, String productId, String error);

    /**
     * 同步场馆
     * @param storeId
     * @param platformCode
     * @return
     */
    boolean syncVenue(Long storeId, String platformCode);

}
