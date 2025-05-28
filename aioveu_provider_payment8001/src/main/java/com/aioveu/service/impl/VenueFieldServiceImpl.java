package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.VenueFieldDao;
import com.aioveu.entity.Company;
import com.aioveu.entity.FieldPlan;
import com.aioveu.entity.VenueField;
import com.aioveu.enums.DataStatus;
import com.aioveu.enums.FieldPlanStatus;
import com.aioveu.enums.FieldPlanTimeType;
import com.aioveu.exception.SportException;
import com.aioveu.form.ActiveVenueFieldForm;
import com.aioveu.service.CompanyService;
import com.aioveu.service.FieldPlanService;
import com.aioveu.service.GradeService;
import com.aioveu.service.VenueFieldService;
import com.aioveu.utils.JacksonUtils;
import com.aioveu.utils.SportDateUtils;
import com.aioveu.vo.GradeDetailVO;
import com.aioveu.vo.VenueFieldItemVO;
import com.aioveu.vo.VenueFieldVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class VenueFieldServiceImpl extends ServiceImpl<VenueFieldDao, VenueField> implements VenueFieldService {

    @Autowired
    private FieldPlanService fieldPlanService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private CompanyService companyService;

    @Override
    public List<VenueFieldVO> getFieldByVenueId(Long venueId, Long companyId, String day) {
        List<VenueFieldVO> list = getBaseMapper().getFieldByVenueId(venueId, day);
        Company company = companyService.getById(companyId);
        int beforeBookingMinutes = company.getBeforeBookingMinutes() == null ? 0 : company.getBeforeBookingMinutes();
        for (VenueFieldVO venueFieldVO : list) {
            for (VenueFieldItemVO venueFieldItemVO : venueFieldVO.getTimeList()) {
                try {
                    if (venueFieldItemVO.getStatus() == 1) {
                        Date date = DateUtils.parseDate(day + " " + venueFieldItemVO.getStartTime(), "yyyy-MM-dd HH:mm:ss");
                        if (DateUtils.addMinutes(new Date(), beforeBookingMinutes).compareTo(date) >= 0) {
                            venueFieldItemVO.setStatus(FieldPlanStatus.Invalid.getCode());
                        }
                    }
                    if (venueFieldItemVO.getStatus() == 4 && !StringUtils.isEmpty(venueFieldItemVO.getGradeIds())) {
                        List<GradeDetailVO> gradeDetailVOS = gradeService.getGradeDetailByIds(venueFieldItemVO.getGradeIds());
                        venueFieldItemVO.setGradeDetailVOList(gradeDetailVOS);
                        venueFieldItemVO.setRemark(String.join(";",gradeDetailVOS.stream().map(t-> {
                            FieldPlanTimeType timeType = FieldPlanTimeType.of(t.getTimeType());
                            return String.format("%s %s,%s", timeType.getDescription(), t.getName(),t.getCoachNames());
                        }).collect(Collectors.toList())));
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return list;
    }

    @Override
    public List<VenueFieldVO> getFieldByVenueId(Long venueId, String day) {
        return getBaseMapper().getFieldByVenueId(venueId, day);
    }

    @Override
    public List<VenueField> findByVenueId(Long venueId) {
        QueryWrapper<VenueField> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(VenueField::getStatus, DataStatus.NORMAL.getCode(), DataStatus.LOW.getCode()).eq(VenueField::getVenueId, venueId).orderByAsc(VenueField::getCreateDate);
        return list(queryWrapper);
    }

    @Override
    public void createFieldPlan(Date now) throws Exception {
        Date planDate = DateUtils.addDays(now, 6);
        String planDateStr = DateFormatUtils.format(planDate, "yyyy-MM-dd");
        log.info("计划日期:" + planDateStr);
        QueryWrapper<VenueField> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(VenueField::getStatus, DataStatus.NORMAL.getCode());
        List<VenueField> venueFieldList = list(queryWrapper);
        log.info("场地:" + venueFieldList.size());
        for (VenueField vf : venueFieldList) {
            log.info(JacksonUtils.obj2Json(vf));
            Date start = DateUtils.parseDate(planDateStr + " " + vf.getStartTime().toString(), "yyyy-MM-dd HH:mm:ss");
            Date end = DateUtils.parseDate(planDateStr + " " + vf.getEndTime().toString(), "yyyy-MM-dd HH:mm:ss");

            // 当前日期
            Calendar c1 = Calendar.getInstance();
            c1.setTime(start);
            Calendar c2 = Calendar.getInstance();
            c2.setTime(end);

            int hour1 = c1.get(Calendar.HOUR_OF_DAY);
            int hour2 = c2.get(Calendar.HOUR_OF_DAY);
            int hourSize = hour2 - hour1;
            List<FieldPlan> fieldPlanList = new ArrayList<>(hourSize);
            for (int i = 0; i < hourSize; i++) {
                FieldPlan fieldPlan = new FieldPlan();
                fieldPlan.setFieldId(vf.getId());
                fieldPlan.setVenueId(vf.getVenueId());
                fieldPlan.setFieldDay(planDate);
                fieldPlan.setPrice(vf.getPrice());
                fieldPlan.setVipPrice(vf.getVipPrice());
                fieldPlan.setStartTime(new Time(DateUtils.addHours(start, i).getTime()));
                fieldPlan.setEndTime(new Time(DateUtils.addHours(start, i + 1).getTime()));
                fieldPlanList.add(fieldPlan);
            }
            fieldPlanService.saveBatch(fieldPlanList);
        }
    }

    @Override
    public List<String> getDateListByTimeType(List<String> formDateList, Integer timeType) {
        if (CollectionUtils.isEmpty(formDateList) && !FieldPlanTimeType.EVERY_DAY.equals(FieldPlanTimeType.of(timeType))) {
            return new ArrayList<>();
        }
        List<String> dateList;
        if (FieldPlanTimeType.FREE_DAY.equals(FieldPlanTimeType.of(timeType))) {
            dateList = formDateList;
        } else if (FieldPlanTimeType.WEEK_DAY.equals(FieldPlanTimeType.of(timeType))) {
            dateList = SportDateUtils.getWeekDayFromString(formDateList);
        } else if (FieldPlanTimeType.MONTH_DAY.equals(FieldPlanTimeType.of(timeType))) {
            dateList = SportDateUtils.getMonthDayFromString(formDateList);
        } else if (FieldPlanTimeType.EVERY_DAY.equals(FieldPlanTimeType.of(timeType))) {
            dateList = SportDateUtils.getFuture7Day();
        } else {
            dateList = new ArrayList<>();
        }
        return dateList;
    }

    @Override
    public List<VenueField> getActiveVenueFieldList(ActiveVenueFieldForm form) {
        if (form.getVenueId() == null || form.getTimeType() == null || StringUtils.isEmpty(form.getStartTime())
                || StringUtils.isEmpty(form.getEndTime()) || form.getStartDay() == null
                || form.getEndDay() == null) {
            return new ArrayList<>();
        }
        List<VenueField> venueFieldList = this.findByVenueId(form.getVenueId());
        if (form.getGradeTemplateId() != null) {
            return venueFieldList;
        }
        String fieldIds = venueFieldList.stream().map(t -> t.getId().toString()).collect(Collectors.joining(","));
        if (form.getStartTime().split(":").length == 2) {
            form.setStartTime(form.getStartTime() + ":00");
        }
        if (form.getEndTime().split(":").length == 2) {
            form.setEndTime(form.getEndTime() + ":00");
        }
        List<Long> invalidFieldIds = new ArrayList<>();
        List<String> dateList = getDateListByTimeType(form.getDateList(), form.getTimeType());
        for (String date : dateList) {
            Date startTime = null;
            Date endTime = null;
            try {
                Date now = new Date();
                startTime = DateUtils.parseDate(date + " " + form.getStartTime(), "yyyy-MM-dd HH:mm:ss");
                endTime = DateUtils.parseDate(date + " " + form.getEndTime(), "yyyy-MM-dd HH:mm:ss");
                if (endTime.compareTo(now) <= 0) continue;
                String format = DateFormatUtils.format(form.getEndDay(), "yyyy-MM-dd");
                Date endDate = DateUtils.parseDate(format + " " + form.getEndTime(), "yyyy-MM-dd HH:mm:ss");
                if (endTime.compareTo(endDate) > 0) continue;
            } catch (ParseException e) {
                throw new SportException(e.getMessage());
            }
            List<FieldPlan> fieldPlanList = this.fieldPlanService.getFieldByGrade(fieldIds, date, form.getStartTime(), form.getEndTime());
            Date finalStartTime = startTime;
            Date finalEndTime = endTime;
            invalidFieldIds.addAll(fieldPlanList.stream().filter(t -> !fieldPlanService.checkFieldByGrades(t, form.getGradeId(), finalStartTime, finalEndTime, form.getSharedVenue())).map(FieldPlan::getFieldId).collect(Collectors.toList()));
        }
        for (VenueField t : venueFieldList) {
            t.setDisable(invalidFieldIds.contains(t.getId()));
        }
        return venueFieldList;
    }


    @Override
    public List<VenueFieldVO> getFieldVenuesByStoreId(Long storeId) {
        //TODO 2025 增加缓存
        List<VenueFieldVO> fieldVenues = getBaseMapper().getFieldVenuesByStoreId(storeId);
        return fieldVenues;
    }

    @Override
    public String getNameById(Long id) {
        VenueField venueField = getById(id);
        if (venueField != null) {
            return venueField.getName();
        }
        return "";
    }
}
