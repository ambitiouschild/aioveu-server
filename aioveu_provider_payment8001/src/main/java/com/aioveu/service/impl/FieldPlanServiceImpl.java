package com.aioveu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.auth.common.model.LoginVal;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.dao.FieldPlanDao;
import com.aioveu.data.sync.FieldSyncMessage;
import com.aioveu.dto.FieldBookUserDTO;
import com.aioveu.dto.FieldPlanDTO;
import com.aioveu.entity.*;
import com.aioveu.enums.*;
import com.aioveu.exception.SportException;
import com.aioveu.service.*;
import com.aioveu.utils.JacksonUtils;
import com.aioveu.utils.SportCommonUtils;
import com.aioveu.utils.SportDateUtils;
import com.aioveu.vo.FieldPlanVO;
import com.aioveu.vo.PriceRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class FieldPlanServiceImpl extends ServiceImpl<FieldPlanDao, FieldPlan> implements FieldPlanService {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private FieldPlanTemplateService fieldPlanTemplateService;

    @Autowired
    private SpecialDayService specialDayService;

    @Autowired
    private FieldPlanLockService fieldPlanLockService;

    @Autowired
    private SystemMonitorNotifyService systemMonitorNotifyService;

    @Autowired
    private MQMessageService mqMessageService;

    @Autowired
    private StoreVenueService storeVenueService;

    @Autowired
    private FieldPlanService fieldPlanService;

    @Autowired
    private VenueFieldService venueFieldService;

    @Autowired
    private StoreService storeService;

    @Override
    public IPage<FieldPlanVO> listByCondition(int page, int size, Long storeId, Long venueId, Long fieldId, String date, Integer status) {
        return getBaseMapper().listByCondition(new Page<>(page, size), storeId, venueId, fieldId, date, status);
    }

    @Override
    public FieldPlan getByTemplateIdAndTime(Long fieldId, Date day, Time startTime, Time endTime) {
        QueryWrapper<FieldPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FieldPlan::getFieldId, fieldId)
                .eq(FieldPlan::getFieldDay, day)
                .eq(FieldPlan::getStartTime, startTime)
                .eq(FieldPlan::getEndTime, endTime)
                .ne(FieldPlan::getStatus, DataStatus.DELETE.getCode());
        return getOne(queryWrapper);
    }

    @Override
    public List<FieldBookUserDTO> getFieldBookUserDetail(String orderId) {
        return getBaseMapper().getFieldBookUserDetail(orderId);
    }


    @Override
    public List<FieldPlanDTO> getByUser(String userId, Long companyId, Integer status) {
        return getBaseMapper().getByUser(userId, companyId, status);
    }

    @Override
    public List<FieldBookUser> getFieldBookUserByNotUsed(String userId, Long venueId) {
        return getBaseMapper().getFieldBookUserByNotUsed(userId, venueId);
    }

    @Override
    public boolean transferLockToFieldPlan() {
        log.info("订场更新服务启动...");
        List<FieldPlanTemplate> fieldPlanTemplateList = fieldPlanTemplateService.getUpdateTemplate();
        for (FieldPlanTemplate fieldPlanTemplate : fieldPlanTemplateList) {
            List<PriceRule> lockRules = fieldPlanTemplate.getLockRules();
            if (!CollectionUtils.isEmpty(lockRules)) {
                for (PriceRule lockRule : lockRules) {
                    FieldPlanLock fieldPlanLock = new FieldPlanLock();
                    fieldPlanLock.setName(lockRule.getRemark());
                    fieldPlanLock.setVenueId(fieldPlanTemplate.getVenueId());
                    fieldPlanLock.setExpiryDate(fieldPlanTemplate.getEndDay());
                    List<PriceRule> priceRules = new ArrayList<>();
                    if (CollectionUtils.isEmpty(lockRule.getFieldIdList())) {
                        lockRule.setFieldIdList(Arrays.stream(fieldPlanTemplate.getFieldIds().split(",")).map(t -> Long.valueOf(t)).collect(Collectors.toList()));
                    }
                    priceRules.add(lockRule);
                    fieldPlanLock.setStatus(1);
                    fieldPlanLock.setLockRule(JSONObject.toJSONString(priceRules));
                    this.fieldPlanLockService.save(fieldPlanLock);
                }
            }
        }
        return true;
    }

    @Override
    public boolean markExpireFieldPlan(String day) {
        UpdateWrapper<FieldPlan> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(FieldPlan::getFieldDay, day)
                .eq(FieldPlan::getStatus, FieldPlanStatus.Normal.getCode())
                .set(FieldPlan::getStatus, FieldPlanStatus.Invalid.getCode());
        return update(updateWrapper);
    }

    @Override
    public boolean expiryFieldPlanAddNew() {
        Set<String> errorList = new HashSet<>();
        try {
            Date now = new Date();
            log.info("订场更新服务启动...");
            // 将昨天未使用的场地状态标记为过期
            String oldDay = DateFormatUtils.format(DateUtils.addDays(now, -1), "yyyy-MM-dd");
            fieldPlanService.markExpireFieldPlan(oldDay);

            List<FieldPlanTemplate> fieldPlanTemplateList = fieldPlanTemplateService.getUpdateTemplate();
            if (CollectionUtils.isEmpty(fieldPlanTemplateList)) {
                log.info("没有需要更新订场模板");
                return false;
            }
            log.info("需要更新的订场模板:{}", fieldPlanTemplateList.size());
            for (FieldPlanTemplate fieldPlanTemplate : fieldPlanTemplateList) {
                log.info("订场模板:{}开始更新", fieldPlanTemplate.getId());
//                Date now = null;
//                try {
//                    now = DateUtils.parseDate("2024-09-28", "yyyy-MM-dd");
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                Date future = DateUtils.addDays(now, 14);
                Date startTime = null;
                if (FieldPlanTimeType.WEEK_DAY.equals(FieldPlanTimeType.of(fieldPlanTemplate.getTimeType()))) {
                    String weekDay = SportDateUtils.getDayOfWeek(future);
                    if (fieldPlanTemplate.getDateList().contains(weekDay)) {
                        startTime = future;
                    }
                } else if (FieldPlanTimeType.MONTH_DAY.equals(FieldPlanTimeType.of(fieldPlanTemplate.getTimeType()))) {
                    String day = DateFormatUtils.format(future, "dd");
                    if (fieldPlanTemplate.getDateList().contains(day)) {
                        startTime = future;
                    }
                } else if (FieldPlanTimeType.EVERY_DAY.equals(FieldPlanTimeType.of(fieldPlanTemplate.getTimeType()))) {
                    startTime = future;
                }
                if (startTime == null) {
                    log.warn("订场模板:{}没有不满足时间需求", fieldPlanTemplate.getId());
                    continue;
                }
                String dayStr = DateFormatUtils.format(startTime, "yyyy-MM-dd");
                String startTimeStr = fieldPlanTemplate.getStartTime().toString();
                String endTimeStr = fieldPlanTemplate.getEndTime().toString();

                //判断模板是否跳过节假日
                if (fieldPlanTemplate.getSkipHoliday() != null && fieldPlanTemplate.getSkipHoliday()) {
                    if (specialDayService.isHoliday(dayStr)) {
                        // 属于放假
                        log.info("订场模板:{}跳过周末和法定节假日", fieldPlanTemplate.getId());
                        continue;
                    }
                }
                List<Long> res = fieldPlanTemplateService.createFullFieldPlan(fieldPlanTemplate, dayStr, startTimeStr, endTimeStr);
                if (res == null){
                    errorList.add(fieldPlanTemplate.getId());
                }
            }
            log.info("订场模板任务执行结束");
            return true;
        } finally {
            if (!errorList.isEmpty()){
                //TODO 2025 luyao 通知改造成通用消息通知
                systemMonitorNotifyService.sendMonitorMail("订场模板任务执行失败通知", "订场模版id："+StringUtils.join(errorList,"，"));
            }
        }
    }

    @Override
    public void saveFieldPlan(FieldPlanVO fieldPlan) {
        if (!CollectionUtils.isEmpty(fieldPlan.getIds())) {
            for (Long id : fieldPlan.getIds()) {
                FieldPlan fieldPlan1 = new FieldPlan();
                fieldPlan1.setId(id);
                fieldPlan1.setPrice(fieldPlan.getPrice());
                fieldPlan1.setVipPrice(fieldPlan.getVipPrice());
                this.updateById(fieldPlan1);
            }
        }
    }

    @Override
    public void changeStatus(List<Long> ids, String remark, Integer status) {
        if (StringUtils.isNotBlank(remark) && remark.length() > 18) {
            throw new SportException("备注不能超过18个字符");
        }
        List<Long> fieldPlanList = new ArrayList<>(ids.size());
        // status 4 锁场 1 解锁
        for (Long id : ids) {
            FieldPlan fieldPlan = this.getById(id);
            if (status == 1 && fieldPlan.getStatus() == 6 && OauthUtils.getCurrentUserRoles().stream().anyMatch(s -> s.startsWith("admin"))) {
                // 对于已预订 并且需要对场地进行解锁 并且角色是管理员 允许进行解锁
            } else {
                if (fieldPlan.getStatus() == 6)
                    throw new SportException("已被预定，无法修改");
                if (!FieldPlanLockChannels.Current.getCode().equals(fieldPlan.getLockChannel()))
                    throw new SportException("第三方平台锁场不允许解锁");
            }

            FieldPlan fp = new FieldPlan();
            fp.setId(id);
            fp.setRemark(remark);
            fp.setStatus(status);
            if (status == 1) {
                fp.setGradeIds(null);
                fp.setFieldPlanLockId(null);
            }
            fp.setLockChannel(FieldPlanLockChannels.Current.getCode());
            this.updateById(fp);
            fieldPlanList.add(id);

            // 记录操作日志
            OperateLog operateLog = new OperateLog();
            if (status == 1) {
                operateLog.setName("场地解锁");
            } else if (status == 4) {
                operateLog.setName("场地锁场");
            } else {
                continue;
            }
            LoginVal currentUser = OauthUtils.getCurrentUser();

            operateLog.setOperateType(2);
            operateLog.setOperateTime(new Date());
            operateLog.setUserId(currentUser.getUserId());
            if (currentUser.getNickname() != null) {
                operateLog.setUsername(currentUser.getNickname());
            } else {
                operateLog.setUsername(currentUser.getUsername());
            }
            StoreVenue storeVenue = storeVenueService.getById(fieldPlan.getVenueId());
            operateLog.setRoleCode(String.join(",", OauthUtils.getCurrentUserRoles()));
            operateLog.setCompanyId(storeVenue.getCompanyId());
            operateLog.setStoreId(storeVenue.getStoreId());
            operateLog.setDetail(operateLog.getName() + " 备注:" + remark);
            operateLog.setProductId(id + "");
            operateLog.setCategoryCode(OperateLogCategoryEnum.FIELD.getCode());
            mqMessageService.sendOperateLogMessage(operateLog);
        }
        //场地状态变动，发送mq消息通知 用于同步锁场/解锁到第三方平台
        FieldSyncMessage fieldSyncMessage = new FieldSyncMessage();
        fieldSyncMessage.setStatus(status);
        fieldSyncMessage.setChannel(FieldPlanLockChannels.Current.getCode());
        fieldSyncMessage.setFieldPlanIdList(fieldPlanList);
        mqMessageService.sendFieldSyncMessage(fieldSyncMessage);
    }

    @Override
    public List<FieldPlan> getFieldByGrade(Grade grade) {
        String day = DateFormatUtils.format(grade.getEndTime(), "yyyy-MM-dd");
        String timeFrom = DateFormatUtils.format(grade.getStartTime(), "HH:mm:ss");
        String timeTo = DateFormatUtils.format(grade.getEndTime(), "HH:mm:ss");
        return this.getFieldByGrade(grade.getFieldIds(), day, timeFrom, timeTo);
    }

    @Override
    public List<FieldPlan> getFieldByGrade(String fieldIds, String day, String timeFrom, String timeTo) {
        if (StringUtils.isEmpty(fieldIds)) {
            return new ArrayList<>();
        }
        return this.getBaseMapper().getFieldByGrade(fieldIds, day, timeFrom, timeTo);
    }

    @Override
    public List<FieldPlan> getFieldByGradeId(Long gradeId) {
        return this.getBaseMapper().getFieldByGradeId(gradeId);
    }

    @Override
    public boolean checkFieldByGrades(FieldPlan fieldPlan, Long gradeId, Date dateFrom, Date dateTo, Integer sharedVenue) {
        if (fieldPlan.getStatus().equals(FieldPlanStatus.Predetermine.getCode())) {
            return false;
        }
        if (fieldPlan.getStatus().equals(FieldPlanStatus.Occupy.getCode())) {
            if (StringUtils.isEmpty(fieldPlan.getGradeIds())) {
                return false;
            }
            if (sharedVenue.equals(1)) {
                return true;
            }
            if (gradeId != null && SportCommonUtils.containString(fieldPlan.getGradeIds(), gradeId.toString())) {
                return true;
            }
            for (String idstr : fieldPlan.getGradeIds().split(",")) {
                Grade grade = this.gradeService.getById(new Long(idstr));
                if (grade != null && SportDateUtils.isOverlap(dateFrom, dateTo, grade.getStartTime(), grade.getEndTime())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public List<FieldPlanVO> syncThirdPlatform2Qs(List<FieldPlanVO> fieldPlanList) {
        StoreVenue storeVenue = storeVenueService.getById(fieldPlanList.get(0).getVenueId());
        for (FieldPlanVO fieldPlan : fieldPlanList) {
            QueryWrapper<FieldPlan> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(FieldPlan::getVenueId, fieldPlan.getVenueId()).
                    eq(FieldPlan::getFieldId, fieldPlan.getFieldId()).
                    eq(FieldPlan::getFieldDay, fieldPlan.getFieldDay()).
                    ge(FieldPlan::getStartTime, fieldPlan.getStartTime());
            String endTime = fieldPlan.getEndTime().toString();
            if (!"00:00:00".equals(endTime)){
                queryWrapper.lambda().
                        le(FieldPlan::getStartTime, endTime). // 小于等于
                        ge(FieldPlan::getEndTime, fieldPlan.getStartTime()). // 大于等于
                        le(FieldPlan::getEndTime, endTime);
            }
            String params = JacksonUtils.obj2Json(fieldPlan);
            Integer status = fieldPlan.getStatus();
            List<FieldPlan> matchFieldPlanList = list(queryWrapper);
            List<Long> idList = new ArrayList<>(matchFieldPlanList.size());
            if (CollectionUtils.isNotEmpty(matchFieldPlanList)) {
                // 匹配到趣数的订场
                for (FieldPlan item : matchFieldPlanList) {
                    FieldPlan fp = new FieldPlan();
                    fp.setId(item.getId());
                    fp.setStatus(status);
                    //同步数据，如果是锁场，则需要设置锁场平台，否则还原
                    if (FieldPlanStatus.Normal.getCode().equals(status)) {
                        // 进行一下状态对比 判断是否冲突
                        // 如果解锁场地 看下之前锁场的是不是本平台 如果不是则存在冲突 需要进行通知 并且不进行场地解锁
                        if (!item.getLockChannel().equals(fieldPlan.getLockChannel())) {
                            // 此状态用于前端手动点击同步展示冲突使用
                            fieldPlan.setSyncStatus(DataStatus.DELETE.getCode());
                            //TODO 2025 fxl 异常日志记录
                            // 店铺名称 场馆名称 场地名称 日期 时间 平台 趣数锁场渠道 趣数锁场备注 备注 趣数场地状态
                            sendConflictNoticeMessage(storeVenue.getStoreId(), fieldPlan.getFieldId(), storeVenue.getName(), fieldPlan, item);
                            continue;
                        }
                        // 解锁场地
                        fp.setLockChannel(FieldPlanLockChannels.Current.getCode());
                        // 备注清空
                        fp.setRemark("");
                    }else {
                        // 如果锁场 则判断场地状态是不是可用状态
                        if (!FieldPlanStatus.Normal.getCode().equals(item.getStatus())) {
                            // 此状态用于前端手动点击同步展示冲突使用
                            fieldPlan.setSyncStatus(DataStatus.DELETE.getCode());
                            //TODO 2025 fxl 异常日志记录
                            //如果场地状态不可用 存在锁场冲突 需要进行通知
                            // 店铺名称 场馆名称 场地名称 日期 时间 平台 趣数锁场渠道 趣数锁场备注 备注 趣数场地状态
                            sendConflictNoticeMessage(storeVenue.getStoreId(), fieldPlan.getFieldId(), storeVenue.getName(), fieldPlan, item);
                            continue;
                        }
                        // 锁场地
                        fp.setLockChannel(fieldPlan.getLockChannel());
                        fp.setLockRemark(fieldPlan.getLockRemark());
                        fp.setRemark(fieldPlan.getRemark());
                    }
                    if (updateById(fp)) {
                        idList.add(item.getId());
                    }
                }
                fieldPlan.setIds(idList);
            } else {
                fieldPlan.setSyncStatus(DataStatus.DELETE.getCode());
                //TODO 2025 fxl 异常日志记录
                log.error("未匹配到趣数场地:{}", JacksonUtils.obj2Json(fieldPlan));
            }
            //第三方平台同步到趣数订场操作日志记录
            String detail = fieldPlan.getLockChannel() + fieldPlan.getLockRemark() + fieldPlan.getRemark();
            sendOperateLogMessage(FieldPlanStatus.Normal.getCode().equals(status) ? "第三方平台场地解锁" : "第三方平台场地锁场", params, storeVenue.getCompanyId(), storeVenue.getStoreId(), detail, "");
        }
        return fieldPlanList;
    }

    @Override
    public FieldPlanDTO getFiledNameById(Long id) {
        return this.baseMapper.getFiledNameById(id);
    }

    @Override
    public List<FieldPlanDTO> getFieldLockedList4BookDay(Long storeId, int fieldBookDay, String platformCode, List<Long> venueIdList) {
        try {
            Date start = SportDateUtils.getDayNoHour(0);
            Date end = DateUtils.addDays(start, fieldBookDay - 1);
            List<FieldPlanDTO> fieldPlanDTOList = getBaseMapper().getFieldLockedList4BookDayAndTime(storeId, start, end, platformCode, venueIdList);
            if (CollectionUtils.isNotEmpty(fieldPlanDTOList)) {
                return fieldPlanDTOList.stream().filter(item -> {
                    // 将当天已过期的订场给排除掉
                    Date fieldStartTime = SportDateUtils.combineDateAndTime(item.getFieldDay(), item.getStartTime());
                    return fieldStartTime.after(new Date());
                }).collect(Collectors.toList());
            }
            return null;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 发送操作日志
     * @param name
     * @param params
     * @param companyId
     * @param storeId
     * @param detail
     * @param productId
     */
    private void sendOperateLogMessage(String name, String params, Long companyId, Long storeId, String detail, String productId) {
        OperateLog operateLog = new OperateLog();
        operateLog.setName(name);
        operateLog.setOperateType(2);
        operateLog.setOperateTime(new Date());
        operateLog.setUserId("system");
        operateLog.setUsername("系统");
        operateLog.setParams(params);
        operateLog.setRoleCode("system_operation");
        operateLog.setCompanyId(companyId);
        operateLog.setStoreId(storeId);
        operateLog.setOperateTime(new Date());
        operateLog.setDetail(detail);
        operateLog.setProductId(productId);
        operateLog.setCategoryCode(OperateLogCategoryEnum.FIELD.getCode());
        mqMessageService.sendOperateLogMessage(operateLog);
    }

    @Override
    public List<FieldPlan> getByVenueIdAndDay(Long venueId, String day) {
        QueryWrapper<FieldPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FieldPlan::getVenueId, venueId).
                eq(FieldPlan::getFieldDay, day)
                .orderByAsc(FieldPlan::getStartTime);
        return list(queryWrapper);
    }

    /**
     * 发送场地同步冲突提醒
     * @param storeId
     * @param fieldId
     * @param venueName
     * @param fieldPlan
     * @param quShuField
     */
    private void sendConflictNoticeMessage(Long storeId, Long fieldId, String venueName, FieldPlanVO fieldPlan, FieldPlan quShuField) {
        // 店铺名称 场馆名称 场地名称 日期 时间 平台 趣数锁场渠道 趣数锁场备注 备注 趣数场地状态
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("storeName", storeService.getNameByStoreId(storeId));
        msgMap.put("venueName", venueName);
        msgMap.put("fieldName", venueFieldService.getNameById(fieldId));
        msgMap.put("platformName", FieldPlanLockChannels.of(fieldPlan.getLockChannel()).getDescription());
        msgMap.put("fieldDay", DateFormatUtils.format(fieldPlan.getFieldDay(), "yyyy-MM-dd"));
        msgMap.put("startTime", fieldPlan.getStartTime().toString());
        msgMap.put("endTime", fieldPlan.getEndTime().toString());
        msgMap.put("time", msgMap.get("fieldDay") + " " + msgMap.get("startTime") + "~" + msgMap.get("endTime"));
        msgMap.put("venueField", msgMap.get("venueName") + " " + msgMap.get("fieldName"));
        if (FieldPlanStatus.Normal.getCode().equals(fieldPlan.getStatus())) {
            msgMap.put("action", msgMap.get("platformName") +  "同步到趣数解锁冲突");
        } else {
            msgMap.put("action", msgMap.get("platformName") +  "同步到趣数锁场冲突");
        }
        msgMap.put("quShuLockChannel", FieldPlanLockChannels.of(quShuField.getLockChannel()).getDescription());
        mqMessageService.sendNoticeMessage(msgMap, MsgOptionEnum.FIELD_SYNC_CONFLICT_MSG.getCode(), storeId);
        log.error(msgMap.get("action") + ":第三方:{}, 趣数:{}", JacksonUtils.obj2Json(fieldPlan), JacksonUtils.obj2Json(quShuField));
    }

}
