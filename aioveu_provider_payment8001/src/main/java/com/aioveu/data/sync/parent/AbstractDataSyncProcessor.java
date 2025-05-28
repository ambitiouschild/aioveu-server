package com.aioveu.data.sync.parent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.aioveu.config.RestTemplateConfig;
import com.aioveu.data.sync.FieldSyncHandleError;
import com.aioveu.data.sync.common.CommonField;
import com.aioveu.data.sync.common.CommonVenue;
import com.aioveu.dto.FieldPlanDTO;
import com.aioveu.entity.SyncDataAccountConfig;
import com.aioveu.entity.VenueFieldSyncAlias;
import com.aioveu.entity.VenueSyncAlias;
import com.aioveu.exception.SportException;
import com.aioveu.request.RestTemplateUtils;
import com.aioveu.request.response.DynamicProxyIP;
import com.aioveu.request.response.DynamicProxyResponse;
import com.aioveu.service.VenueFieldSyncAliasService;
import com.aioveu.service.VenueSyncAliasService;
import com.aioveu.utils.RedisUtil;
import com.aioveu.utils.SportDateUtils;
import com.aioveu.vo.CommonResponse;
import com.aioveu.vo.FieldPlanVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * @description 数据同步抽象父类 每种数据同步方法继承该父类
 * @author: 雒世松
 * @date: 2025/3/21 15:29
 */
@Slf4j
public abstract class AbstractDataSyncProcessor implements DataSyncProcessor {

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    @Autowired
    protected StringRedisTemplate stringRedisTemplate;

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    protected VenueFieldSyncAliasService venueFieldSyncAliasService;

    protected final String LAST_MSG_TIME = "LAST_MSG_TIME:";

    @Autowired
    protected VenueSyncAliasService venueSyncAliasService;

    @Resource
    private RedisLockRegistry redisLockRegistry;

    /**
     * 获取代理 RestTemplate
     * @return
     */
    protected RestTemplate getProxyRestTemplate() {
        return RestTemplateConfig.createRestTemplate(5000, 5000, new ObjectMapper(), false);
    }

    private DynamicProxyIP requestDynamicProxyIP(Long storeId) {
//        String ipUrl = "http://v2.api.juliangip.com/company/postpay/getips?auto_white=1&city=上海&ip_remain=1&num=1&pt=1&result_type=json2&trade_no=6470897611490002&sign=a97747a07c18f42ec36b7804c4c07c1d";
        String ipUrl = "http://v2.api.juliangip.com/company/postpay/getips?auto_white=1&city=上海&ip_remain=1&num=1&pt=1&result_type=json2&trade_no=6905359155796966&sign=f3efc77361aad7ff0ee6af15e47125af";
        CommonResponse<DynamicProxyResponse> response = RestTemplateUtils.simpleGet(restTemplate, ipUrl, new ParameterizedTypeReference<CommonResponse<DynamicProxyResponse>>(){});
        if (response != null && response.getCode() == 200) {
            DynamicProxyResponse dynamicProxyResponse = response.getData();
            if (dynamicProxyResponse != null && dynamicProxyResponse.getCount() > 0) {
                List<DynamicProxyIP> proxyList = dynamicProxyResponse.getProxyList();
                DynamicProxyIP dynamicProxyIP = proxyList.get(0);
                // 缓存有效动态IP给后续调用 保留10秒钟
                RedisUtil.set("SYNC_DATA_PROXY_IP:" + storeId, dynamicProxyIP, dynamicProxyIP.getIpRemain() - 10, TimeUnit.SECONDS, redisTemplate);
                return dynamicProxyIP;
            }
        } else if (response != null){
            log.error("获取代理ip失败" + response.getMsg());
        }
        return null;
    }

    @Override
    public DynamicProxyIP getDynamicProxyIP(Long storeId) {
        Lock lock = redisLockRegistry.obtain("GET_SYNC_DATA_PROXY_IP_" + storeId);
        try {
            lock.lock();
            DynamicProxyIP dynamicProxyIP = RedisUtil.get("SYNC_DATA_PROXY_IP:" + storeId, redisTemplate);
            if (dynamicProxyIP != null) {
                return dynamicProxyIP;
            }
            dynamicProxyIP = requestDynamicProxyIP(storeId);
            // 获取动态IP存在失败情况，失败重试再去获取
            int tryCount = 0;
            while (dynamicProxyIP == null && tryCount < 5) {
                dynamicProxyIP = requestDynamicProxyIP(storeId);
                tryCount += 1;
            }
            if (dynamicProxyIP != null) {
                log.info("店铺:{}动态IP获取:{}", storeId, dynamicProxyIP.getIp());
            }
            return dynamicProxyIP;
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public boolean verifyAccount(SyncDataAccountConfig config) {
        return getAuthConfig(config) != null;
    }

    /**
     * 缓存配置
     * @param storeId
     * @param configKey
     * @param value
     */
    protected void savePlatformConfig(Long storeId, String configKey, Object value) {
        String key = "FIELD_SYNC_CONFIG:" + getPlatformPrefix() + "_" + storeId;
        RedisUtil.putToMap(key, configKey, value, redisTemplate);
    }

    /**
     * 获取配置
     * @param storeId
     * @param configKey
     * @return
     */
    protected Object getPlatformConfig(Long storeId, String configKey) {
        String key = "FIELD_SYNC_CONFIG:" + getPlatformPrefix() + "_" + storeId;
        return RedisUtil.getFromMap(key, configKey, redisTemplate);
    }

    /**
     * 获取平台缓存场馆的key
     * @param storeId
     * @return
     */
    protected String getVenueCacheKey(Long storeId) {
        return "FIELD_SYNC_VENUE:" + getPlatformPrefix() + "_" + storeId;
    }

    @Override
    public boolean clearCache(Long storeId) {
        // 清除平台配置缓存
        RedisUtil.deleteByKey("FIELD_SYNC_CONFIG:" + getPlatformPrefix() + "_" + storeId, redisTemplate);
        // 清除球馆缓存
        return RedisUtil.deleteByKey(getVenueCacheKey(storeId), redisTemplate);
    }

    @Override
    public boolean clearLoginCache(Long storeId) {
        // 请求登录缓存
        return RedisUtil.deleteByKey(getPlatformPrefix() + "_TOKEN:" + storeId, redisTemplate);
    }

    /**
     * 全量同步 趣数平台的锁场同步到第三方平台
     * @param fieldPlanList
     * @param config
     * @param action 操作: 锁场(Lock) 或者 解锁(unLock)
     * @return
     */
    public abstract List<FieldSyncHandleError> syncFieldQs2Platform(List<FieldPlanDTO> fieldPlanList, SyncDataAccountConfig config, String action);

    @Override
    public List<FieldSyncHandleError> lockField(List<FieldPlanDTO> fieldPlanList, SyncDataAccountConfig config) {
        return syncFieldQs2Platform(fieldPlanList, config, DataSyncProcessor.LOCK);
    }

    @Override
    public List<FieldSyncHandleError> unLockField(List<FieldPlanDTO> fieldPlanList, SyncDataAccountConfig config) {
        return syncFieldQs2Platform(fieldPlanList, config, DataSyncProcessor.UNLOCK);
    }

    /**
     * 获取平台前缀
     * @return
     */
    public abstract String getPlatformPrefix();

    /**
     * 获取认证信息
     * @param config
     * @return
     */
    protected String getAuthConfig(SyncDataAccountConfig config) {
        String auth = stringRedisTemplate.opsForValue().get(getPlatformPrefix() + "_TOKEN:" + config.getStoreId());
        if (auth == null) {
            return login(config);
        }
        return auth;
    }

    /**
     * 保存认证信息
     * @param auth
     * @param storeId
     * @param expireInSeconds
     */
    protected void saveAuthConfig(String auth, Long storeId, Integer expireInSeconds) {
        stringRedisTemplate.opsForValue().set(getPlatformPrefix() + "_TOKEN:" + storeId, auth, expireInSeconds, TimeUnit.SECONDS);
    }

    /**
     * 第三方平台消息同步订场
     * @param config
     * @param lastTime
     * @param auth
     * @return
     */
    public abstract List<FieldPlanVO> fetchMsgFieldPlans(SyncDataAccountConfig config, Long lastTime, String auth);

    /**
     * 请求获取第三方平台场馆信息
     * @param config
     * @param key
     * @param storeVenueList
     * @param venueFieldMap
     * @return
     */
    public abstract List<CommonVenue> requestVenue(SyncDataAccountConfig config, String key, List<VenueSyncAlias> storeVenueList, Map<Long, List<VenueFieldSyncAlias>> venueFieldMap);

    @Override
    public List<FieldPlanVO> regularSyncField(SyncDataAccountConfig config) {
        String lastTimeStr = stringRedisTemplate.opsForValue().get(LAST_MSG_TIME + getPlatformPrefix() + "_"+ config.getStoreId());
        Long lastTime = null;
        if (StringUtils.isNotEmpty(lastTimeStr)){
            lastTime = Long.parseLong(lastTimeStr);
        }
        String auth = getAuthConfig(config);
        List<FieldPlanVO> fieldPlanVOList = fetchMsgFieldPlans(config, lastTime, auth);
        // 针对过期时间的订场进行过滤
        if (CollectionUtils.isNotEmpty(fieldPlanVOList)){
            return fieldPlanVOList.stream().filter(item -> !SportDateUtils.isExpire(item.getFieldDay(), item.getEndTime(), new Date())).collect(Collectors.toList());
        }
        return fieldPlanVOList;
    }

    /**
     * 保存最后的同步时间
     * @param storeId
     * @param lastTimeStr
     */
    protected void saveLastSyncTime(Long storeId, String lastTimeStr) {
        stringRedisTemplate.opsForValue().set(LAST_MSG_TIME + getPlatformPrefix() + "_"+ storeId, lastTimeStr, 6, TimeUnit.DAYS);
    }

    /**
     * 是否是新订单判断处理
     * @param lastTime
     * @param orderTime
     * @return
     */
    protected boolean newFieldOrder(Long lastTime, Date orderTime) {
        if (lastTime != null) {
            //上次同步时间不为空 跟同步时间比较
            if (orderTime.getTime() <= lastTime) {
                // 订单时间小于 上次同步时间 则为历史数据 不需要再同步
                return false;
            }
        } else {
            // 上次同步时间为空 那么就和当前时间比较 由于定时同步10分钟触发一次 这里需要减去10分钟
            Date now = DateUtils.addMinutes(new Date(), -10);
//            Date now = DateUtils.addHours(new Date(), -10);
            return orderTime.getTime() > now.getTime();
        }
        return true;
    }


    /**
     * 订场时间 是否支持半小时的统一处理
     * @param platformHalfHour
     * @param originalTime
     * @param start 是否是开始时间
     * @return
     */
    protected Time getTimeByConfig(Integer platformHalfHour, Time originalTime, boolean start) {
        // 订场时间是否支持半小时
        if (platformHalfHour != null && platformHalfHour != 1){
            // 不支持，将半点转为整点，比如09:30:00-10:30:00转为09:00:00-11:00:00
            // 使用 Calendar 来修改时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(originalTime);

            if (start) {
                if(calendar.get(Calendar.MINUTE) != 0){
                    calendar.set(Calendar.MINUTE, 0);
                }
            } else {
                if(calendar.get(Calendar.MINUTE) != 0){
                    calendar.set(Calendar.MINUTE, 60);
                }
            }
            return new Time(calendar.getTimeInMillis());
        } else {
            // 支持，则不处理
            return originalTime;
        }
    }

    /**
     * 获取场馆信息
     * @param config
     * @return
     */
    @Override
    public List<CommonVenue> getVenueFields(SyncDataAccountConfig config) {
        String key = getVenueCacheKey(config.getStoreId());
        return RedisUtil.getList(key, redisTemplate);
    }

    @Override
    public List<Long> getVenueIdList(SyncDataAccountConfig config) {
        List<CommonVenue> venueFields = getVenueFields(config);
        if (CollectionUtils.isNotEmpty(venueFields)){
            List<VenueSyncAlias> storeVenueList = venueSyncAliasService.findByStoreIdAndPlatform(config.getStoreId(), config.getPlatformCode());
            List<Long> venueIdList = new ArrayList<>(venueFields.size());
            for (VenueSyncAlias storeVenue : storeVenueList) {
                CommonVenue dpVenue = getVenueByName(storeVenue.getAliasName(), config);
                if (dpVenue != null) {
                    venueIdList.add(storeVenue.getId());
                }
            }
            return venueIdList;
        }
        return Collections.emptyList();
    }

    /**
     * 根据场馆名称获取场馆
     * @param venueName
     * @param config
     * @return
     */
    protected CommonVenue getVenueByName(String venueName, SyncDataAccountConfig config){
        if (StringUtils.isEmpty(venueName)){
            return null;
        }
        List<CommonVenue> venueList = getVenueFields(config);
        if (CollectionUtils.isEmpty(venueList)){
            return null;
        }
        Optional<CommonVenue> qydVenue = venueList.stream().filter(t -> venueName.equals(t.getName())).findFirst();
        return qydVenue.orElse(null);
    }

    /**
     * 通过场地名称查找具体场地
     * @param fields
     * @param fieldName
     * @return
     */
    protected CommonField getFieldByName(List<CommonField> fields, String fieldName){
        if (CollectionUtils.isEmpty(fields)){
            return null;
        }
        Optional<CommonField> dpField = fields.stream().filter(t -> StringUtils.isNotBlank(fieldName) && fieldName.equals(t.getName())).findFirst();
        return dpField.orElse(null);
    }

    @Override
    public boolean syncVenue(SyncDataAccountConfig config) {
        // 1、清除场馆缓存
        RedisUtil.deleteByKey(getVenueCacheKey(config.getStoreId()), redisTemplate);
        // 2、请求获取场馆 并缓存
        String key = getVenueCacheKey(config.getStoreId());
        List<CommonVenue> venueFields = requestThirdPlatformVenue(config, key);
        if (CollectionUtils.isNotEmpty(venueFields)){
            return true;
        }
        log.error("未同步到场馆和场地信息");
        throw new SportException("未同步到场馆和场地信息, 请检查场馆场地名称是否一致");
    }

    /**
     * 请求缓存第三方场馆场地
     * @param config
     * @param key
     * @return
     */
    private List<CommonVenue> requestThirdPlatformVenue(SyncDataAccountConfig config, String key) {
        List<VenueSyncAlias> storeVenueList = venueSyncAliasService.findByStoreIdAndPlatform(config.getStoreId(), config.getPlatformCode());
        if (CollectionUtils.isEmpty(storeVenueList)) {
            log.error("店铺:{}未配置平台场馆", config.getStoreId());
            throw new SportException("当前店铺未配置第三方平台场馆");
        }
        // 针对场馆的场地别名是否配置进行校验
        List<VenueFieldSyncAlias> venueFieldList = venueFieldSyncAliasService.findByStoreIdAndPlatform(config.getStoreId(), config.getPlatformCode());
        Map<Long, List<VenueFieldSyncAlias>> venueFieldMap = venueFieldList.stream().collect(Collectors.groupingBy(VenueFieldSyncAlias::getVenueId));
        for (VenueSyncAlias storeVenue : storeVenueList) {
            List<VenueFieldSyncAlias> fieldList = venueFieldMap.get(storeVenue.getVenueId());
            if (CollectionUtils.isEmpty(fieldList)) {
                throw new SportException("场馆:" + storeVenue.getVenueName() + "未配置场地别名");
            }
        }
        return requestVenue(config, key, storeVenueList, venueFieldMap);
    }



}
