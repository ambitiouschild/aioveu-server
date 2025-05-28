package com.aioveu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.aioveu.entity.StoreConfig;
import com.aioveu.entity.SyncDataAccountConfig;
import com.aioveu.enums.DataStatus;
import com.aioveu.service.StoreConfigService;
import com.aioveu.service.SyncDataAccountConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * 定时任务同步数据
 * @Author： yao
 * @Date： 2024/11/29 10:13
 * @Describe： 从第三方平台同步数据到本项目库
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/external-task")
public class ScheduledExternalDataSyncController {

    @Autowired
    private SyncDataAccountConfigService syncDataAccountConfigService;

    private static String executeLimitKey = "scheduled_execute_datasync_limit";

    @Autowired
    private ThreadPoolTaskExecutor syncDataServiceExecutor;

    @Resource
    private RedisLockRegistry redisLockRegistry;

    @Autowired
    private StoreConfigService storeConfigService;

    @GetMapping("syncData")
    public boolean fetchData(@RequestParam(required = false) Long storeId,
                          @RequestParam(required = false) String platformCode){

        Lock lock = redisLockRegistry.obtain(executeLimitKey);
        if (lock.tryLock()) {
            try {
                log.info("同步任务开始执行");
                QueryWrapper<SyncDataAccountConfig> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(SyncDataAccountConfig::getStatus, DataStatus.NORMAL.getCode());
                if (storeId != null){
                    wrapper.lambda().eq(SyncDataAccountConfig::getStoreId, storeId);
                }
                if (StringUtils.isNotBlank(platformCode)){
                    wrapper.lambda().eq(SyncDataAccountConfig::getPlatformCode, platformCode);
                }
                List<SyncDataAccountConfig> configs = syncDataAccountConfigService.list(wrapper);
                if (CollectionUtils.isEmpty(configs)){
                    log.info("未查询到同步配置");
                    return false;
                }
                // 按店铺进行分组 一个个店铺分别同步 每个店铺的第三方账号数据同步是同步的，不同店之间是异步的
                Map<Long, List<SyncDataAccountConfig>> storeSyncGroup = configs.stream().collect(Collectors.groupingBy(SyncDataAccountConfig::getStoreId));
                for (Map.Entry<Long, List<SyncDataAccountConfig>> entry : storeSyncGroup.entrySet()){
                    Long itemStoreId = entry.getKey();
                    StoreConfig storeConfig = storeConfigService.getStoreConfig("FIELD_FULL_SYNC", itemStoreId);
                    if (storeConfig == null || !Boolean.parseBoolean(storeConfig.getValue())) {
                        log.warn("店铺:{}需要先完成订场的全量同步才能进行定时同步", itemStoreId);
                        continue;
                    }
                    StoreConfig fieldOpenTimeSync = storeConfigService.getStoreConfig("FIELD_OPEN_TIME_SYNC", itemStoreId);
                    if (fieldOpenTimeSync == null || !Boolean.parseBoolean(fieldOpenTimeSync.getValue())) {
                        log.warn("店铺:{}未开启定时同步", itemStoreId);
                        continue;
                    }
                    syncDataServiceExecutor.execute(() -> {
                        log.info("店铺:{}开始进行定时同步", itemStoreId);
                        try {
                            List<SyncDataAccountConfig> syncDataAccountConfigs = entry.getValue();
                            for (SyncDataAccountConfig syncDataAccountConfig : syncDataAccountConfigs){
                                syncDataAccountConfigService.syncPlatform2Qs(syncDataAccountConfig);
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                            log.error("店铺:{}同步异常:{}", itemStoreId, e);
                        }
                    });
                }
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        } else {
            log.error("同步正在执行中，本次同步忽略");
        }
        return true;
    }
}
