package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.SyncDataAccountConfigDao;
import com.aioveu.data.sync.DataSyncProcessorHolder;
import com.aioveu.data.sync.FieldSyncHandleError;
import com.aioveu.data.sync.FieldSyncMessage;
import com.aioveu.data.sync.common.CommonVenue;
import com.aioveu.data.sync.parent.DataSyncProcessor;
import com.aioveu.dto.FieldPlanDTO;
import com.aioveu.entity.OperateLog;
import com.aioveu.entity.StoreConfig;
import com.aioveu.entity.SyncDataAccountConfig;
import com.aioveu.enums.*;
import com.aioveu.exception.SportException;
import com.aioveu.form.SyncDataAccountConfigForm;
import com.aioveu.service.*;
import com.aioveu.utils.EncryptionUtil;
import com.aioveu.utils.JacksonUtils;
import com.aioveu.utils.SportDateUtils;
import com.aioveu.vo.FieldPlanVO;
import com.aioveu.vo.SyncDataAccountConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * @Author： yao
 * @Date： 2024/11/27 10:46
 * @Describe：
 */
@Slf4j
@Service
public class SyncDataAccountConfigServiceImpl extends ServiceImpl<SyncDataAccountConfigDao, SyncDataAccountConfig> implements SyncDataAccountConfigService {

    @Autowired
    private FieldPlanService fieldPlanService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisLockRegistry redisLockRegistry;

    @Autowired
    private MQMessageService mqMessageService;

    @Autowired
    private StoreConfigService storeConfigService;

    @Autowired
    private StoreChargingOptionService storeChargingOptionService;

    @Override
    public IPage<SyncDataAccountConfigVO> getPageList(Integer page, Integer size, Long storeId) {
        QueryWrapper<SyncDataAccountConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().ne(SyncDataAccountConfig::getStatus, DataStatus.DELETE.getCode())
                .eq(SyncDataAccountConfig::getStoreId, storeId)
                .orderByAsc(SyncDataAccountConfig::getCreateDate)
                .orderByAsc(SyncDataAccountConfig::getStatus);
        IPage<SyncDataAccountConfig> pageList = page(new Page<>(page, size), queryWrapper);
        List<SyncDataAccountConfig> records = pageList.getRecords();
        if (CollectionUtils.isEmpty(records)){
            return null;
        }
        List<SyncDataAccountConfigVO> dtoList = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            SyncDataAccountConfigVO configVO = new SyncDataAccountConfigVO();
            BeanUtils.copyProperties(records.get(i),configVO);
            dtoList.add(configVO);
        }
        IPage<SyncDataAccountConfigVO> configVOPage = new Page<>();
        configVOPage.setRecords(dtoList);
        configVOPage.setTotal(pageList.getTotal());
        return configVOPage;
    }

    @Override
    public SyncDataAccountConfigVO detail(String id) {
        SyncDataAccountConfig config = getById(id);
        SyncDataAccountConfigVO vo = new SyncDataAccountConfigVO();
        if (config != null){
            BeanUtils.copyProperties(config, vo);
        }
        return vo;
    }

    @Override
    public List<SyncDataAccountConfig> getListByStoreId(Long storeId) {
        QueryWrapper<SyncDataAccountConfig> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SyncDataAccountConfig::getStoreId, storeId)
                .eq(SyncDataAccountConfig::getStatus, DataStatus.NORMAL.getCode());
        return list(wrapper);
    }

    @Override
    public boolean changeStatus(String id, Integer status) {
        SyncDataAccountConfig config = new SyncDataAccountConfig();
        config.setId(id);
        config.setStatus(status);
        return updateById(config);
    }

    @Override
    public String saveConfig(SyncDataAccountConfigForm form) {
        if (form.getId() == null && StringUtils.isBlank(form.getPlatformPassword())) {
            throw new SportException("平台登录密码不能为空");
        }
        if (StringUtils.isNotBlank(form.getNewPlatformPassword())
                || StringUtils.isNotBlank(form.getPlatformPassword())){
            if (!form.getNewPlatformPassword().equals(form.getPlatformPassword())){
                throw new SportException("确认密码和密码不一致！");
            }
            form.setPlatformPassword(EncryptionUtil.encryptWithAES(form.getPlatformPassword(),EncryptionUtil.AES_KEY));
        }
        if (form.getId() != null && StringUtils.isBlank(form.getPlatformPassword())) {
            SyncDataAccountConfig syncDataAccountConfig = getById(form.getId());
            form.setPlatformPassword(syncDataAccountConfig.getPlatformPassword());
        }
        SyncDataAccountConfig config = new SyncDataAccountConfig();
        BeanUtils.copyProperties(form, config);
        config.setPlatformHandler(form.getPlatformCode().toLowerCase(Locale.ROOT));

        //校验账号密码是否有效
        DataSyncProcessor dataSyncProcessor = dataSyncProcessorHolder.findDataSyncProcessor(config.getPlatformHandler());
        if (FieldPlanLockChannels.DP.getCode().equals(config.getPlatformCode())) {
            if (config.getId() == null) {
                // 进行登录触发
                if (!dataSyncProcessor.verifyAccount(config)) {
                    throw new SportException("平台信息保存失败, 请稍后重试");
                }
                save(config);
                return config.getId();
            } else {
                this.updateById(config);
                if (form.getReLogin() != null && form.getReLogin()) {
                    // 需要重新登录 触发登录
                    dataSyncProcessor.login(config);
                }
                return config.getId();
            }
        } else {
            if (!dataSyncProcessor.verifyAccount(config)){
                throw new SportException("平台账号密码校验失败");
            }
            // 缓存平台数据
            dataSyncProcessor.initData(config);
            if (config.getId() != null) {
                this.updateById(config);
                return config.getId();
            } else {
                if (FieldPlanLockChannels.LHD.getCode().equals(config.getPlatformCode())) {
                    config.setPlatformUrl("https://map-merchant.51yundong.me/#/user/guide");
                }
                this.save(config);
                return config.getId();
            }
        }
    }

    @Autowired
    private DataSyncProcessorHolder dataSyncProcessorHolder;

    @Override
    public boolean syncFullPlatform2QsById(String id) {
        SyncDataAccountConfig config = getById(id);
        if (config == null){
            throw new SportException("id错误");
        }
        if (!Objects.equals(config.getStatus(), DataStatus.NORMAL.getCode())){
            throw new SportException("该平台已下架或删除");
        }
        return fullSyncPlatform2Qs(config);
    }

    @Override
    public boolean fullSyncPlatform2Qs(SyncDataAccountConfig config) {
        String key = config.getPlatformCode() + "_full_" + config.getId();
        Lock lock = redisLockRegistry.obtain(key);
        if (lock.tryLock()) {
            try {
                log.info("门店={},平台={}，code={},全量同步开始执行", config.getStoreId(), config.getPlatformName(), config.getPlatformCode());
                DataSyncProcessor dataSyncProcessor = dataSyncProcessorHolder.findDataSyncProcessor(config.getPlatformHandler());
                if (dataSyncProcessor == null) {
                    log.error("dataSyncProcessor not found:" + config.getPlatformHandler());
                    return false;
                }
                //从其他平台获取全量订场数据
                List<FieldPlanVO> list = dataSyncProcessor.fullSyncFieldPlatform2Qs(config);
                if (CollectionUtils.isEmpty(list)) {
                    log.info("门店={},平台={}，code={},全量同步未获取到新的锁场数据", config.getStoreId(), config.getPlatformName(), config.getPlatformCode());
                    return false;
                }
                log.info("本次全量同步锁场订场:{}", list.size());
                fieldPlanService.syncThirdPlatform2Qs(list);
                log.info("id={}, 门店={},平台={}，platformStoreId={},全量同步结束执行",config.getId(), config.getStoreId(), config.getPlatformName(), config.getPlatformCode());
                return true;
            }catch (Exception e){
                log.error("全量同步异常：id={}, platformCode={},error={}", config.getId(), config.getPlatformCode(), e);
            }finally {
                try {
                    lock.unlock();
                }catch (Exception e){
                    log.error("lock.unlock()异常,error={}",e);
                }
            }
        } else {
            throw new SportException( config.getPlatformName() + "平台正在全量同步中，请勿重复提交");
        }
        return false;
    }

    @Override
    public List<FieldPlanVO> syncPlatform2Qs(SyncDataAccountConfig config) {
        String key = config.getPlatformCode() + "_" + config.getId();
        Lock lock = redisLockRegistry.obtain(key);
        if (lock.tryLock()) {
            try {
                log.info("门店={},平台={}，code={},同步开始执行", config.getStoreId(), config.getPlatformName(), config.getPlatformCode());
                DataSyncProcessor dataSyncProcessor = dataSyncProcessorHolder.findDataSyncProcessor(config.getPlatformHandler());
                if (dataSyncProcessor == null) {
                    log.error("dataSyncProcessor not found:" + config.getPlatformHandler());
                    return Collections.emptyList();
                }
                //从其他平台获取新订场数据
                List<FieldPlanVO> list = dataSyncProcessor.regularSyncField(config);
                if (CollectionUtils.isEmpty(list)) {
                    log.info("门店={},平台={}，code={},未获取到新的锁场数据", config.getStoreId(), config.getPlatformName(), config.getPlatformCode());
                    return list;
                }
                log.info("本次同步锁场订场:{}", JacksonUtils.obj2Json(list));
                List<FieldPlanVO> resultList = fieldPlanService.syncThirdPlatform2Qs(list);
                log.info("id={}, 门店={},平台={}，platformStoreId={},同步结束执行",config.getId(), config.getStoreId(), config.getPlatformName(), config.getPlatformCode());
                //场地状态变动，发送mq消息通知 用于同步锁场/解锁到 其他第三方平台
                // 根据场地状态进行分组 把锁场的和解锁的放到一起处理
                Map<Integer, List<FieldPlanVO>> statusFieldPlanMap = resultList.stream().collect(Collectors.groupingBy(FieldPlanVO::getStatus));
                for (Map.Entry<Integer, List<FieldPlanVO>> entry : statusFieldPlanMap.entrySet()) {
                    FieldSyncMessage fieldSyncMessage = new FieldSyncMessage();
                    fieldSyncMessage.setStatus(entry.getKey());
                    fieldSyncMessage.setChannel(FieldPlanLockChannels.of(config.getPlatformCode()).getCode());

                    List<FieldPlanVO> needSyncFieldPlanList = entry.getValue();
                    List<Long> fieldPlanIdList = new ArrayList<>(needSyncFieldPlanList.size());
                    for (FieldPlanVO fieldPlanVO : needSyncFieldPlanList) {
                        if (fieldPlanVO.getIds() != null) {
                            fieldPlanIdList.addAll(fieldPlanVO.getIds());
                        }
                    }
                    fieldSyncMessage.setFieldPlanIdList(fieldPlanIdList);
                    mqMessageService.sendFieldSyncMessage(fieldSyncMessage);
                }
                return resultList;
            }catch (Exception e){
                log.error("同步异常：id={}, storeId={}, platformCode={},error={}", config.getId(), config.getStoreId(), config.getPlatformCode(), e);
            }finally {
                lock.unlock();
            }
        } else {
            throw new SportException( config.getPlatformName() + "平台正在同步中，请勿重复提交");
        }
        return Collections.emptyList();
    }

    @Override
    public List<FieldPlanVO> syncDataByStoreId(Long storeId) {
        List<FieldPlanVO> res = new ArrayList<>();
        List<SyncDataAccountConfig> configs = this.getListByStoreId(storeId);
        if (CollectionUtils.isEmpty(configs)) {
            throw new SportException("该门店没有配置同步账户");
        }
        // 定时或者手动同步之前 需要先判断是否进行过全量同步操作
        StoreConfig storeConfig = storeConfigService.getStoreConfig("FIELD_FULL_SYNC", storeId);
        if (storeConfig == null || !Boolean.parseBoolean(storeConfig.getValue())) {
            throw new SportException("需要先完成订场的全量同步");
        }

        // 订场同步之前先进行检查 如果次数不足进行提醒
        if (!storeChargingOptionService.chargingCheck(storeConfig.getCompanyId(), storeConfig.getStoreId(), ChargingOptionEnum.FIELD_SYNC, 1)) {
            throw new SportException("订场同步服务可用次数不足,请进行充值");
        }

        String key = "syncDataByStoreId" + "_" + storeId;
        Lock lock = redisLockRegistry.obtain(key);
        if (lock.tryLock()) {
            try {
                for (int i = 0; i < configs.size(); i++) {
                    SyncDataAccountConfig config = configs.get(i);
                    List<FieldPlanVO> result = syncPlatform2Qs(config);
                    List<FieldPlanVO> fiedlLockFailList = result.stream()
                            .filter(
                                    item -> Objects.equals(item.getSyncStatus(), DataStatus.DELETE.getCode())
                                            && Objects.equals(item.getStatus(), FieldPlanStatus.Occupy.getCode())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(fiedlLockFailList)) {
                        res.addAll(fiedlLockFailList);
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        } else {
            log.error("该{}门店正在同步，请勿频繁操作", storeId);
            throw new SportException("该门店正在同步，请勿频繁操作");
        }
        return res;
    }

    @Override
    public boolean syncFullQs2Platform(Long storeId, String platformCode) {
        String key = "QS_TO_PLATFORM_" + platformCode + "_" + storeId;
        Lock lock = redisLockRegistry.obtain(key);
        if (lock.tryLock()) {
            try {
                SyncDataAccountConfig config = getByStoreIdAndPlatformCode(storeId, platformCode);
                DataSyncProcessor dataSyncProcessor = dataSyncProcessorHolder.findDataSyncProcessor(config.getPlatformHandler());
                List<Long> venueIdList = dataSyncProcessor.getVenueIdList(config);
                if (CollectionUtils.isEmpty(venueIdList)) {
                    throw new SportException(platformCode + "没有匹配到需要同步的场馆");
                }
                int fieldBookDay = dataSyncProcessor.getFieldBookDay();
                List<FieldPlanDTO> fieldLockedList = fieldPlanService.getFieldLockedList4BookDay(storeId, fieldBookDay, platformCode, venueIdList);
                if (CollectionUtils.isEmpty(fieldLockedList)) {
                    throw new SportException("没有需要同步的锁场数据");
                }
                List<FieldSyncHandleError> errorList = dataSyncProcessor.lockField(fieldLockedList, config);
                if (CollectionUtils.isNotEmpty(errorList)) {
                    for (FieldSyncHandleError error : errorList) {
                        List<FieldPlanDTO> fieldPlanDTOList = error.getFieldPlanDTOList();
                        FieldPlanDTO fieldPlanDTO = fieldPlanDTOList.get(0);
                        //第三方平台锁场失败 发送通知
                        sendSyncFailNotice("第三方平台锁场失败", config.getPlatformName(), fieldPlanDTO.getVenueName(), SportDateUtils.formatDateTime(fieldPlanDTO.getFieldDay(), fieldPlanDTO.getStartTime(), fieldPlanDTO.getEndTime()),
                                fieldPlanDTO.getFieldName(), config.getCompanyId(), config.getStoreId(), config.getId(), error.getMsg());
                    }
                }
                return CollectionUtils.isEmpty(errorList);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        } else {
            throw new SportException(platformCode + "正在同步，请勿重复提交");
        }
        return false;
    }


    @Override
    public String getLoginStatus(String id) {
        SyncDataAccountConfig config = getById(id);
        if (config != null && config.getPlatformCode().equals(FieldPlanLockChannels.DP.getCode())) {
            String needCode = stringRedisTemplate.opsForValue().get("DP_LOGIN_NEED_SMS_CODE_" + config.getStoreId());
            if (StringUtils.isNotBlank(needCode)) {
                stringRedisTemplate.delete("DP_LOGIN_NEED_SMS_CODE_" + config.getStoreId());
                return "needCode";
            }
            String error = stringRedisTemplate.opsForValue().get("DP_LOGIN_ERROR_" + config.getStoreId());
            if (StringUtils.isNotBlank(error)) {
                stringRedisTemplate.delete("DP_LOGIN_NEED_SMS_CODE_" + config.getStoreId());
                if ("success".equals(error)) {
                    log.info("美团点评登录成功");
                    DataSyncProcessor dataSyncProcessor = dataSyncProcessorHolder.findDataSyncProcessor(config.getPlatformHandler());
                    // 初始化
                    dataSyncProcessor.initData(config);
                }
                return error;
            }
            return "login";
        }
        throw new SportException("id错误");
    }

    @Override
    public boolean setLoginCode(String id, String loginCode) {
        if (loginCode.length() != 6) {
            throw new SportException("验证码错误");
        }
        SyncDataAccountConfig config = getById(id);
        stringRedisTemplate.opsForValue().set("DP_LOGIN_SMS_CODE_" + config.getStoreId(), loginCode, 10, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public boolean firstFullSync(Long storeId) {
        // 完整的全量同步 先各个第三方平台同步到趣数，最后趣数再同步到第三方平台
        List<SyncDataAccountConfig> configList = getListByStoreId(storeId);
        if (CollectionUtils.isNotEmpty(configList)) {
            log.info("本次店铺:{} 同步平台:{}", storeId, configList.size());
            // 先检查 是否完成 场馆的同步
            for (SyncDataAccountConfig config : configList) {
                DataSyncProcessor dataSyncProcessor = dataSyncProcessorHolder.findDataSyncProcessor(config.getPlatformHandler());
                List<CommonVenue> venueFields = dataSyncProcessor.getVenueFields(config);
                if (CollectionUtils.isEmpty(venueFields)) {
                    throw new SportException(config.getPlatformName() + "未完成场馆的同步");
                }
            }

            log.info("开始平台同步订场到趣数");
            for (SyncDataAccountConfig config : configList) {
                fullSyncPlatform2Qs(config);
            }
            log.info("平台同步订场到趣数结束");
            log.info("开始趣数同步订场到平台");
            for (SyncDataAccountConfig config : configList) {
                syncFullQs2Platform(config.getStoreId(), config.getPlatformCode());
            }
            log.info("趣数同步订场到平台结束");
            StoreConfig storeConfig = storeConfigService.getStoreConfig("FIELD_FULL_SYNC", storeId);
            if (storeConfig != null) {
                storeConfig.setValue("true");
                storeConfigService.saveOrUpdateStoreConfig(storeConfig);
            }
            return true;
        }
        throw new SportException("该店铺下没有配置同步账号");
    }

    /**
     * 通过店铺和平台编号查找平台配置
     * @param storeId
     * @param platformCode
     * @return
     */
    private SyncDataAccountConfig getByStoreIdAndPlatformCode(Long storeId, String platformCode) {
        QueryWrapper<SyncDataAccountConfig> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SyncDataAccountConfig::getStoreId, storeId)
                .eq(SyncDataAccountConfig::getPlatformCode, platformCode)
                .eq(SyncDataAccountConfig::getStatus, DataStatus.NORMAL.getCode());
        SyncDataAccountConfig config = getOne(wrapper);
        if (config == null) {
            throw new SportException(platformCode + "对应的平台不存在");
        }
        return config;
    }

    @Override
    public boolean init(Long storeId, String platformCode) {
        SyncDataAccountConfig config = getByStoreIdAndPlatformCode(storeId, platformCode);
        DataSyncProcessor dataSyncProcessor = dataSyncProcessorHolder.findDataSyncProcessor(config.getPlatformHandler());
        if (dataSyncProcessor.verifyAccount(config)) {
            return dataSyncProcessor.initData(config);
        } else {
            throw new SportException(platformCode + "校验不通过");
        }
    }

    /**
     * 发送锁场或者解锁失败通知
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
    @Override
    public void sendSyncFailNotice(String name, String platformName, String venueName, String time, String fieldName, Long companyId, Long storeId, String productId, String error) {
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("name", name);
        msgMap.put("platformName", platformName);
        msgMap.put("venueName", venueName);
        msgMap.put("time", time);
        msgMap.put("fieldName", fieldName);
        msgMap.put("error", error);
        if (name.contains("锁场")) {
            msgMap.put("syncMethod", "同步到" + platformName + "锁场");
        } else {
            msgMap.put("syncMethod", "同步到" + platformName + "解锁");
        }
        if ("存在非可售场次时段".equals(error) || "场次数据处于非锁场状态".equals(error) || "场次处于非锁场状态".equals(error) || "场地处于非锁状态".equals(error)) {
            log.info("常规错误，不发短信:{}", error);
        } else {
            msgMap.put("sms", "发送短信");
        }
        mqMessageService.sendNoticeMessage(msgMap, MsgOptionEnum.FIELD_SYNC_PLATFORM_FAIL.getCode(), storeId);

        OperateLog operateLog = new OperateLog();
        operateLog.setName(name);
        operateLog.setOperateType(2);
        operateLog.setOperateTime(new Date());
        operateLog.setUsername("系统");
        operateLog.setCompanyId(companyId);
        operateLog.setStoreId(storeId);
        operateLog.setDetail(platformName + "-" + venueName + "-" + fieldName + "-" + time + "-" + error);
        operateLog.setProductId(productId);
        operateLog.setCategoryCode(OperateLogCategoryEnum.FIELD.getCode());
        mqMessageService.sendOperateLogMessage(operateLog);
    }

    @Override
    public boolean syncVenue(Long storeId, String platformCode) {
        SyncDataAccountConfig config = getByStoreIdAndPlatformCode(storeId, platformCode);
        DataSyncProcessor dataSyncProcessor = dataSyncProcessorHolder.findDataSyncProcessor(config.getPlatformHandler());
        return dataSyncProcessor.syncVenue(config);
    }
}
