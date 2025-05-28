package com.aioveu.receive;

import com.aioveu.config.mq.DirectMqConfig;
import com.aioveu.data.sync.DataSyncProcessorHolder;
import com.aioveu.data.sync.FieldSyncHandleError;
import com.aioveu.data.sync.FieldSyncMessage;
import com.aioveu.data.sync.parent.DataSyncProcessor;
import com.aioveu.dto.FieldPlanDTO;
import com.aioveu.entity.FieldPlan;
import com.aioveu.entity.StoreVenue;
import com.aioveu.entity.SyncDataAccountConfig;
import com.aioveu.enums.ChargingOptionEnum;
import com.aioveu.enums.FieldPlanStatus;
import com.aioveu.service.*;
import com.aioveu.utils.JacksonUtils;
import com.aioveu.utils.SportDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description 场地同步消息接收 解锁 锁场消息
 * @author: 雒世松
 * @date: 2024/12/19 0019 22:45
 */
@Slf4j
@Component
@RabbitListener(queues = DirectMqConfig.DIRECT_FIELD_SYNC)
public class DirectFieldSyncReceiver {

    @Autowired
    private SyncDataAccountConfigService syncDataAccountConfigService;

    @Autowired
    private FieldPlanService fieldPlanService;

    @Autowired
    private DataSyncProcessorHolder dataSyncProcessorHolder;

    @Autowired
    private StoreVenueService storeVenueService;

    @Autowired
    private VenueFieldService venueFieldService;

    @Autowired
    private StoreChargingOptionService storeChargingOptionService;

    /**
     * 收到mq消息 mq消息是自己平台发送的锁场和解锁场地
     * 解析消息，获取消息中的门店id、场馆id、场地id、日期、开始时间、结束时间
     * 根据门店id获取同步数据第三方账户配置
     *      配置没有，则结束
     *      有配置，则获取token
     * 获取小程序与第三方系统门店、场馆、场地对应关系
     * 调用锁场、解锁接口
     * @param fieldSyncMessage
     */
    @RabbitHandler
    public void process(FieldSyncMessage fieldSyncMessage) {
        log.info("DirectFieldSyncReceiver消费者收到消息  : " + JacksonUtils.obj2Json(fieldSyncMessage));
        try {
            List<Long> fieldPlanIdList = fieldSyncMessage.getFieldPlanIdList();
            if (CollectionUtils.isEmpty(fieldPlanIdList)) {
                log.error("同步订场数据为空");
                return;
            }
            List<FieldPlan> fieldPlans = fieldPlanService.listByIds(fieldPlanIdList);
            StoreVenue storeVenue = storeVenueService.getById(fieldPlans.get(0).getVenueId());
            List<FieldPlanDTO> fieldPlanList = new ArrayList<>(fieldPlanIdList.size());
            Date now = new Date();
            for (FieldPlan fieldPlan : fieldPlans) {
                FieldPlanDTO item = new FieldPlanDTO();
                item.setCompanyId(storeVenue.getCompanyId());
                item.setStoreId(storeVenue.getStoreId());
                item.setId(fieldPlan.getId());
                item.setStatus(fieldSyncMessage.getStatus());
                item.setFieldId(fieldPlan.getFieldId());
                item.setFieldDay(fieldPlan.getFieldDay());
                item.setStartTime(fieldPlan.getStartTime());
                item.setEndTime(fieldPlan.getEndTime());
                item.setRemark(fieldPlan.getRemark());

                item.setVenueName(storeVenue.getName());
                item.setFieldName(venueFieldService.getNameById(fieldPlan.getFieldId()));

                // 对比场地时间 过滤掉过期时间的订场同步
                if (SportDateUtils.isExpire(fieldPlan.getFieldDay(), fieldPlan.getEndTime(), now)) {
                    log.info("已过期的订场，不做同步:{}, 时间:{}", item.getId(), DateFormatUtils.format(fieldPlan.getFieldDay(), "yyyy-MM-dd") + fieldPlan.getEndTime());
                    continue;
                }

                fieldPlanList.add(item);
            }
            //4、6 锁，0、1解锁
            int status = fieldSyncMessage.getStatus();
            Long storeId = fieldPlanList.get(0).getStoreId();
            List<SyncDataAccountConfig> configs = syncDataAccountConfigService.getListByStoreId(storeId);
            if (CollectionUtils.isEmpty(configs)){
                log.info("storeId = {},未查询到该门店的同步配置", storeId);
                return;
            }
            // 订场同步之前先进行检查 如果次数不足进行提醒
            if (!storeChargingOptionService.chargingCheck(storeVenue.getCompanyId(), storeVenue.getStoreId(), ChargingOptionEnum.FIELD_SYNC, 1)) {
                return;
            }

            for (SyncDataAccountConfig config : configs) {
                if (config.getPlatformCode().equals(fieldSyncMessage.getChannel())) {
                    log.info("同平台订场同步直接跳过");
                    continue;
                }
                log.info("配置id={},门店id={},门店名称={},开始同步到{}", config.getId(), config.getStoreId(), config.getPlatformStoreName(), config.getPlatformName());
                DataSyncProcessor dataSyncProcessor = dataSyncProcessorHolder.findDataSyncProcessor(config.getPlatformHandler());
                if (!dataSyncProcessor.verifyAccount(config)) {
                    log.error("配置id={},账号={}登录失败，未获取到登录token", config.getId(), config.getPlatformName());
                    continue;
                }
                if (FieldPlanStatus.Occupy.getCode() == status || FieldPlanStatus.Predetermine.getCode() == status) {
                    // status 占用或者预订 其他平台进行锁场
                    List<FieldSyncHandleError> errorList = dataSyncProcessor.lockField(fieldPlanList, config);
                    if (CollectionUtils.isNotEmpty(errorList)) {
                        for (FieldSyncHandleError error : errorList) {
                            List<FieldPlanDTO> fieldPlanDTOList = error.getFieldPlanDTOList();
                            FieldPlanDTO fieldPlanDTO = fieldPlanDTOList.get(0);
                            //第三方平台锁场失败 发送通知
                            syncDataAccountConfigService.sendSyncFailNotice("第三方平台锁场失败", config.getPlatformName(), fieldPlanDTO.getVenueName(), SportDateUtils.formatDateTime(fieldPlanDTO.getFieldDay(), fieldPlanDTO.getStartTime(), fieldPlanDTO.getEndTime()),
                                    fieldPlanDTO.getFieldName(), config.getCompanyId(), config.getStoreId(), config.getId(), error.getMsg());
                        }
                    }
                } else if (FieldPlanStatus.Normal.getCode() == status) {
                    // status = 1 表示解锁 那么其他平台需要调用解锁 使场地状态正常
                    List<FieldSyncHandleError> errorList = dataSyncProcessor.unLockField(fieldPlanList, config);
                    if (CollectionUtils.isNotEmpty(errorList)) {
                        //第三方平台解锁失败 发送通知
                        for (FieldSyncHandleError error : errorList) {
                            List<FieldPlanDTO> fieldPlanDTOList = error.getFieldPlanDTOList();
                            FieldPlanDTO fieldPlanDTO = fieldPlanDTOList.get(0);
                            syncDataAccountConfigService.sendSyncFailNotice("第三方平台解锁失败", config.getPlatformName(), fieldPlanDTO.getVenueName(), SportDateUtils.formatDateTime(fieldPlanDTO.getFieldDay(), fieldPlanDTO.getStartTime(), fieldPlanDTO.getEndTime()),
                                    fieldPlanDTO.getFieldName(), config.getCompanyId(), config.getStoreId(), config.getId(), error.getMsg());
                        }
                    }
                }
            }
            // 订场同步扣费
            storeChargingOptionService.charging(storeVenue.getCompanyId(), storeVenue.getStoreId(), ChargingOptionEnum.FIELD_SYNC, 1);
        }catch (Exception e){
            log.error("DirectFieldSyncReceiver同步异常{}",e.getMessage());
        }finally {
            log.info("DirectFieldSyncReceiver同步结束");
        }
    }


 
}