package com.aioveu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.FieldPlanTemplateDao;
import com.aioveu.data.sync.FieldSyncMessage;
import com.aioveu.entity.*;
import com.aioveu.enums.*;
import com.aioveu.exception.SportException;
import com.aioveu.form.FieldPlanTemplateForm;
import com.aioveu.service.*;
import com.aioveu.utils.IdUtils;
import com.aioveu.utils.PriceRuleUtils;
import com.aioveu.utils.SportDateUtils;
import com.aioveu.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FieldPlanTemplateServiceImpl extends ServiceImpl<FieldPlanTemplateDao, FieldPlanTemplate> implements FieldPlanTemplateService {

    @Autowired
    private FieldPlanService fieldPlanService;

    @Autowired
    private FieldPlanLockService fieldPlanLockService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private MQMessageService mqMessageService;

    @Autowired
    private StoreVenueService storeVenueService;

    @Override
    public List<FieldPlanTemplateVO> templateList(Long storeId, String dateStr) {
        List<FieldPlanTemplateVO> list = this.getBaseMapper().getByStoreId(storeId, dateStr);
        if (CollectionUtils.isNotEmpty(list)) {
            for (FieldPlanTemplateVO item : list) {
                item.setTimeRule(getTimeRule(item.getTimeType(), item.getDateList(), item.getStartTime(), item.getEndTime()));
            }
        }
        return list;
    }

    private String getTimeRule(Integer timeType, String dateList, Date startTime, Date endTime) {
        String hourTime = SportDateUtils.getMergeOnlyHourTime(startTime, endTime);
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(dateList)) {
            dateList = "";
        }
        if (timeType == null) {

        } else if (timeType == 0) {
            sb.append(dateList);
        } else if (timeType == 1) {
            sb.append("每周");
            sb.append(" ");
            sb.append(dateList);
        } else if (timeType == 2) {
            sb.append("每月");
            sb.append(" ");
            sb.append(dateList);
        } else if (timeType == 3) {
            sb.append("每天");
            sb.append(" ");
            sb.append(dateList);
        }
        sb.append(" ");
        sb.append(hourTime);
        return sb.toString();
    }

    @Override
    public boolean changeStatus(String id, Integer status) {
        FieldPlanTemplate gt = new FieldPlanTemplate();
        gt.setId(id);
        gt.setStatus(status);

        FieldPlanTemplate byId = getById(id);
        //上架校验
        if (status == DataStatus.NORMAL.getCode()){
            //同一场馆不允许设置不同价格单位
            QueryWrapper<FieldPlanTemplate> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(FieldPlanTemplate::getStatus, DataStatus.NORMAL.getCode())
                    .eq(FieldPlanTemplate::getVenueId, byId.getVenueId())
                    .ne(FieldPlanTemplate::getId, id);
            List<FieldPlanTemplate> queryList = list(queryWrapper);
            if (CollectionUtils.isNotEmpty(queryList)){
                FieldPlanTemplate fieldPlanTemplate1 = queryList.stream()
                        .filter(item -> byId.getPriceTimeUnit() != item.getPriceTimeUnit())
                        .findFirst().orElse(null);
                if (fieldPlanTemplate1 != null){
                    throw new SportException("同一场馆不允许设置不同价格单位!");
                }
            }
        }

        return updateById(gt);
    }

    @Override
    public List<FieldPlanTemplate> getUpdateTemplate() {
        Date now = new Date();
        QueryWrapper<FieldPlanTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FieldPlanTemplate::getStatus, DataStatus.NORMAL.getCode())
                .in(FieldPlanTemplate::getTimeType, FieldPlanTimeType.WEEK_DAY.getCode(),
                        FieldPlanTimeType.MONTH_DAY.getCode(), FieldPlanTimeType.EVERY_DAY.getCode())
                .le(FieldPlanTemplate::getStartDay, now)
                .ge(FieldPlanTemplate::getEndDay, now);
        return list(queryWrapper);
    }

    @Override
    public boolean create(FieldPlanTemplateForm form) {
        List<String> dateList;
        if (FieldPlanTimeType.FREE_DAY.equals(FieldPlanTimeType.of(form.getTimeType()))) {
            if (CollectionUtils.isEmpty(form.getDateList()) || form.getDateList().contains(null)) {
                throw new SportException("时间不能为空!");
            }
            dateList = form.getDateList();
        } else if (FieldPlanTimeType.WEEK_DAY.equals(FieldPlanTimeType.of(form.getTimeType()))) {
            if (CollectionUtils.isEmpty(form.getDateList()) || form.getDateList().contains(null)) {
                throw new SportException("时间不能为空!");
            }
            dateList = SportDateUtils.getWeekDayFromString(form.getDateList(), 14);
        } else if (FieldPlanTimeType.MONTH_DAY.equals(FieldPlanTimeType.of(form.getTimeType()))) {
            if (CollectionUtils.isEmpty(form.getDateList()) || form.getDateList().contains(null)) {
                throw new SportException("时间不能为空!");
            }
            dateList = SportDateUtils.getMonthDayFromString(form.getDateList(), 14);
        } else if (FieldPlanTimeType.EVERY_DAY.equals(FieldPlanTimeType.of(form.getTimeType()))) {
            dateList = SportDateUtils.getFutureDay(14);
        } else {
            dateList = new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(dateList)){
            throw new SportException("无效订场模版!");
        }

        if (PriceUnitType.of(form.getPriceTimeUnit()) == null) {
            throw new SportException("价格单位错误!");
        }
        FieldPlanTemplate fieldPlanTemplate = new FieldPlanTemplate();
        BeanUtils.copyProperties(form, fieldPlanTemplate);
        if (fieldPlanTemplate.getId() == null) {
            fieldPlanTemplate.setId(IdUtils.getStringId());
        }
        StoreVenue storeVenue = storeVenueService.getById(form.getVenueId());
        fieldPlanTemplate.setCompanyId(storeVenue.getCompanyId());
        fieldPlanTemplate.setStoreId(storeVenue.getStoreId());
        String startTime = form.getStartTime() + ":00";
        String endTime = form.getEndTime() + ":00";
        fieldPlanTemplate.setStartTime(Time.valueOf(startTime));
        fieldPlanTemplate.setEndTime(Time.valueOf(endTime));
        if (fieldPlanTemplate.getStartTime().after(fieldPlanTemplate.getEndTime())) {
            log.error(fieldPlanTemplate.toString());
            throw new SportException("开始时间不能大于结束时间!");
        }
        if (CollectionUtils.isNotEmpty(form.getDateList())) {
            fieldPlanTemplate.setDateList(String.join(",", form.getDateList()));
        }
        if (CollectionUtils.isNotEmpty(form.getPriceRules())) {
            fieldPlanTemplate.setPriceRule(JSONObject.toJSONString(form.getPriceRules()));
        }
        if (!CollectionUtils.isEmpty(form.getLockRules())) {
//            for (int i = 0; i < form.getLockRules().size() - 1; i++) {
//                for (int j = 0; j < form.getLockRules().size() - i - 1; j++) {
//                    PriceRule item1 = form.getLockRules().get(j);
//                    PriceRule item2 = form.getLockRules().get(j + 1);
//                    if (!item1.getType().equals(item2.getType())) {
//                        continue;
//                    }
//                    // 将列表转换为Stream
//                    Stream<Long> stream1 = item1.getFieldIdList().stream().filter(t -> t != null);
//                    Stream<Long> stream2 = item2.getFieldIdList().stream().filter(t -> t != null);
//                    // 找出两个Stream的重复元素
//                    List<Long> commonElements = Stream.concat(stream1, stream2)
//                            .distinct()
//                            .collect(Collectors.toList());
//                    if (commonElements.size() > 0) {
//                        if (item1.getType().equals(0)) {
//                            if (StringUtils.isEmpty(item1.getTimeFrom()) || StringUtils.isEmpty(item1.getTimeFrom())
//                                    || StringUtils.isEmpty(item1.getTimeFrom()) || StringUtils.isEmpty(item1.getTimeFrom())) {
//                                continue;
//                            }
//                        } else if (item1.getType().equals(1)) {
//                            if (StringUtils.isEmpty(item1.getTimeFrom()) || StringUtils.isEmpty(item1.getTimeFrom())
//                                    || StringUtils.isEmpty(item1.getTimeFrom()) || StringUtils.isEmpty(item1.getTimeFrom())) {
//                                continue;
//                            }
//                        }
//                    }
//                }
//            }
            fieldPlanTemplate.setLockRule(JSONObject.toJSONString(form.getLockRules()));
        } else {
            fieldPlanTemplate.setLockRule(null);
        }
        //同一场馆不允许设置不同价格单位
        QueryWrapper<FieldPlanTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FieldPlanTemplate::getStatus, DataStatus.NORMAL.getCode())
                .eq(FieldPlanTemplate::getVenueId, fieldPlanTemplate.getVenueId());
        List<FieldPlanTemplate> queryList = list(queryWrapper);
        if (CollectionUtils.isNotEmpty(queryList)){
            FieldPlanTemplate fieldPlanTemplate1 = queryList.stream()
                    .filter(item -> !item.getId().equals(form.getId()) && form.getPriceTimeUnit() != item.getPriceTimeUnit())
                    .findFirst().orElse(null);
            if (fieldPlanTemplate1 != null){
                throw new SportException("同一场馆不允许设置不同价格单位!");
            }
        }
        fieldPlanTemplate.setFieldIds(String.join(",", form.getFieldIdList().stream().map(t -> t.toString()).collect(Collectors.toList())));
        if (StringUtils.isEmpty(form.getId())) {
            save(fieldPlanTemplate);
            //通过mq异步生成订场计划
            Map<String, Object> msgMap = new HashMap<>();
            msgMap.put("id", fieldPlanTemplate.getId());
            msgMap.put("type", DirectMessageType.FIELD_PLAN_TEMPLATE_CREATE.getCode());
            mqMessageService.sendCreateFieldPlanMessage(msgMap);
        } else {
            updateById(fieldPlanTemplate);
        }
        return true;
    }

    @Override
    public List<Long> createFullFieldPlanById(String id) {
        FieldPlanTemplate form = getById(id);

        List<String> dateList;
        List<String> list = new ArrayList<>();
        if (StringUtils.isNotBlank(form.getDateList())){
            list = Arrays.asList(form.getDateList().split(","));
        }
        if (FieldPlanTimeType.FREE_DAY.equals(FieldPlanTimeType.of(form.getTimeType()))) {
            dateList = list;
        } else if (FieldPlanTimeType.WEEK_DAY.equals(FieldPlanTimeType.of(form.getTimeType()))) {
            dateList = SportDateUtils.getWeekDayFromString(list, 14);
        } else if (FieldPlanTimeType.MONTH_DAY.equals(FieldPlanTimeType.of(form.getTimeType()))) {
            dateList = SportDateUtils.getMonthDayFromString(list, 14);
        } else if (FieldPlanTimeType.EVERY_DAY.equals(FieldPlanTimeType.of(form.getTimeType()))) {
            dateList = SportDateUtils.getFutureDay(14);
        } else {
            dateList = new ArrayList<>();
        }
        List<Long> result = new ArrayList<>();
        for (String dayStr : dateList) {
            result.addAll(createFullFieldPlan(form, dayStr, form.getStartTime().toString(), form.getEndTime().toString()));
        }
        return result;
    }

    @Override
    public List<Long> createFullFieldPlan(FieldPlanTemplate fieldPlanTemplate, String dayStr, String startTime, String endTime) {
        try {
            List<Long> fieldPlanIdList = new ArrayList<>();
            Date day = DateUtils.parseDate(dayStr, "yyyy-MM-dd");
            //校验创建时间与模版时间比较，非有效时间内不生成订场计划
            if(fieldPlanTemplate.getStartDay().compareTo(day) > 0 || fieldPlanTemplate.getEndDay().compareTo(day) < 0) {
                return fieldPlanIdList;
            }
            Date startDate = DateUtils.parseDate(dayStr + " " + startTime, "yyyy-MM-dd HH:mm:ss");
            Date endDate = DateUtils.parseDate(dayStr + " " + endTime, "yyyy-MM-dd HH:mm:ss");
            //1小时场地、或半小时场地，计算间隔开始、结束时间
            int interval = fieldPlanTemplate.getPriceTimeUnit();
            int total = 24;
            if (interval != PriceUnitType.OneHour.getCode()){
                total = 24 * (PriceUnitType.OneHour.getCode() / interval);
            }

            for (int i = 0; i < total; i++) {
                Date startDateVal = DateUtils.addMinutes(startDate, interval * i);
                Date endDateVal = DateUtils.addMinutes(startDate, interval * (i + 1));
                List<FieldPlan> fieldPlanList = createByFieldPlanTemplate(fieldPlanTemplate, dayStr, new Time(startDateVal.getTime()), new Time(endDateVal.getTime()));

                List<Long> fieldPlanIds = new ArrayList<>(fieldPlanList.size());
                FieldSyncMessage fieldSyncMessage = new FieldSyncMessage();
                for (FieldPlan fieldPlan : fieldPlanList) {
                    if (fieldPlan == null) {
                        continue;
                    }
                    FieldPlan old = fieldPlanService.getByTemplateIdAndTime(fieldPlan.getFieldId(), fieldPlan.getFieldDay(), fieldPlan.getStartTime(), fieldPlan.getEndTime());
                    if (old != null) {
                        log.error("场地:{} 已存在时间:{} - {}", old.getFieldId(), dayStr + " " + startTime, endTime);
                        continue;
                    }
                    if (fieldPlanService.save(fieldPlan)) {
                        fieldPlanIdList.add(fieldPlan.getId());
                        //自动生成的场地锁场状态，发送mq消息
                        if (Objects.equals(fieldPlan.getStatus(), FieldPlanStatus.Occupy.getCode())){
                            fieldSyncMessage.setStatus(fieldPlan.getStatus());
                            fieldPlanIds.add(fieldPlan.getId());
                        }
                    }
                }
                //场地状态变动，发送mq消息通知 用于同步锁场/解锁到第三方平台
                fieldSyncMessage.setChannel(FieldPlanLockChannels.Current.getCode());
                fieldSyncMessage.setFieldPlanIdList(fieldPlanIds);
                mqMessageService.sendFieldSyncMessage(fieldSyncMessage);
                if (endDateVal.compareTo(endDate) >= 0) {
                    break;
                }
            }
            return fieldPlanIdList;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<FieldPlan> createByFieldPlanTemplate(FieldPlanTemplate fieldPlanTemplate, String dayStr, Time startTime, Time endTime) {
        List<FieldPlan> fieldPlans = new ArrayList<>();
        for (String fieldIdStr : fieldPlanTemplate.getFieldIds().split(",")) {
            Long fieldId = new Long(fieldIdStr);
            FieldPlan fieldPlan = new FieldPlan();
            fieldPlan.setFieldId(fieldId);
            fieldPlan.setVenueId(fieldPlanTemplate.getVenueId());
            fieldPlan.setStoreId(fieldPlanTemplate.getStoreId());
            fieldPlan.setCompanyId(fieldPlanTemplate.getCompanyId());
            try {
                fieldPlan.setFieldDay(DateUtils.parseDate(dayStr, "yyyy-MM-dd"));
                fieldPlan.setStartTime(startTime);
                fieldPlan.setEndTime(endTime);
                Date startDate = DateUtils.parseDate(dayStr + " " + startTime, "yyyy-MM-dd HH:mm:ss");
                PriceRule priceRule = PriceRuleUtils.matchingPriceRule(fieldPlanTemplate.getPriceRules(), startDate);
                if (priceRule == null) {
                    log.info(String.format("场地模版%s，时间：%s未匹配到价格规则", fieldPlanTemplate.getName(), DateFormatUtils.format(startDate, "yyyy-MM-dd HH:mm:ss")));
                    continue;
                }
                fieldPlan.setVipPrice(priceRule.getVipPrice());
                fieldPlan.setPrice(priceRule.getPrice());
                fieldPlan.setCoachPrice(priceRule.getCoachPrice());

                List<PriceRule> lockRules = new ArrayList<>();
                List<FieldPlanLock> fieldPlanLockList = this.fieldPlanLockService.getByVenueId(fieldPlanTemplate.getVenueId(), fieldPlan.getFieldDay());
                for (FieldPlanLock fieldPlanLock : fieldPlanLockList) {
                    lockRules.addAll(fieldPlanLock.getLockRules());
                }
                PriceRule lockRule = PriceRuleUtils.matchingPriceRule(lockRules, startDate, fieldId);
                if (lockRule != null) {
                    FieldPlanTimeType fieldPlanTimeType = FieldPlanTimeType.of(lockRule.getType());
                    String remark = String.format("%s %s,过期日%s", fieldPlanTimeType.getDescription(), lockRule.getRemark(), lockRule.getExpiryDate());
                    fieldPlan.setStatus(FieldPlanStatus.Occupy.getCode());
                    fieldPlan.setRemark(remark);
                    fieldPlan.setFieldPlanLockId(lockRule.getId());
                    fieldPlan.setLockRemark(remark);
                } else {
                    fieldPlan.setStatus(FieldPlanStatus.Normal.getCode());
                }
                List<Grade> gradeList = this.gradeService.getGradeByField(fieldPlan.getFieldId(), dayStr + " " + startTime);
                if (!CollectionUtils.isEmpty(gradeList)) {
                    fieldPlan.setFieldPlanLockId(null);
                    fieldPlan.setStatus(FieldPlanStatus.Occupy.getCode());
                    fieldPlan.setGradeIds(String.join(",", gradeList.stream().map(t -> t.getId().toString()).collect(Collectors.toList())));
                    List<GradeDetailVO> gradeDetailVOS = gradeService.getGradeDetailByIds(fieldPlan.getGradeIds());
                    fieldPlan.setLockRemark(String.join(";",gradeDetailVOS.stream().map(t-> {
                        FieldPlanTimeType timeType = FieldPlanTimeType.of(t.getTimeType());
                        return String.format("%s %s,%s", timeType.getDescription(), t.getName(),t.getCoachNames());
                    }).collect(Collectors.toList())));
                }
                fieldPlans.add(fieldPlan);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return fieldPlans;
    }
}
