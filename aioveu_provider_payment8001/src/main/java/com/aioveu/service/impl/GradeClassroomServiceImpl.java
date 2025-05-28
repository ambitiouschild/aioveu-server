package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.GradeClassroomDao;
import com.aioveu.entity.FieldPlan;
import com.aioveu.entity.Grade;
import com.aioveu.entity.GradeClassroom;
import com.aioveu.entity.GradeTemplate;
import com.aioveu.enums.FieldPlanStatus;
import com.aioveu.form.ActiveVenueFieldForm;
import com.aioveu.service.*;
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
public class GradeClassroomServiceImpl extends ServiceImpl<GradeClassroomDao, GradeClassroom> implements GradeClassroomService {

    @Autowired
    private FieldPlanService fieldPlanService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private StoreVenueService storeVenueService;

    @Autowired
    private VenueFieldService venueFieldService;

    @Autowired
    private GradeTemplateService gradeTemplateService;

    @Override
    public List<IdNameVO> getByStoreId(Long storeId) {
        QueryWrapper<GradeClassroom> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GradeClassroom::getStoreId, storeId)
                .orderByDesc(GradeClassroom::getCreateDate);
        return list(queryWrapper).stream().map(item -> {
            IdNameVO idNameVO = new IdNameVO();
            BeanUtils.copyProperties(item, idNameVO);
            return idNameVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<IdNameVO> getByVenueId(Long venueId) {
        QueryWrapper<GradeClassroom> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GradeClassroom::getVenueId, venueId)
                .orderByAsc(GradeClassroom::getCreateDate);
        return list(queryWrapper).stream().map(item -> {
            IdNameVO idNameVO = new IdNameVO();
            BeanUtils.copyProperties(item, idNameVO);
            return idNameVO;
        }).collect(Collectors.toList());
    }

    @Override
    public FieldDayPlanVO fieldPlayByVenueId(Long venueId, String dayStr) {
        List<FieldPlan> fieldPlanList = fieldPlanService.getByVenueIdAndDay(venueId, dayStr);
        if (CollectionUtils.isEmpty(fieldPlanList)) {
            return null;
        }
        FieldDayPlanVO fieldDayPlan = new FieldDayPlanVO();
        fieldDayPlan.setWeek(SportDateUtils.getDayOfWeek(dayStr));
        try {
            Date date = DateUtils.parseDate(dayStr, "yyyy-MM-dd");
            fieldDayPlan.setDateStr(DateFormatUtils.format(date, "MM/dd"));
            fieldDayPlan.setDate(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Map<Long, List<FieldPlan>> fieldMap = fieldPlanList.stream().collect(Collectors.groupingBy(FieldPlan::getFieldId));
        List<FieldPlan> fieldPlans = fieldMap.get(fieldPlanList.get(0).getFieldId());
        // 处理时间
        List<HourVO> hourList = new ArrayList<>(fieldPlans.size());
        for (FieldPlan fp : fieldPlans) {
            Date start = SportDateUtils.combineDateAndTime(fp.getFieldDay(), fp.getStartTime());
            Date end = SportDateUtils.combineDateAndTime(fp.getFieldDay(), fp.getEndTime());
            HourVO hourVO = new HourVO();
            hourVO.setFKey(DateFormatUtils.format(start, "yyyy-MM-dd HH:mm"));
            hourVO.setKey(hourVO.getFKey());
            hourVO.setVal(SportDateUtils.timeFormat(fp.getStartTime(), "HH:mm"));
            hourVO.setEndVal(SportDateUtils.timeFormat(fp.getEndTime(), "HH:mm"));
            hourVO.setEndKey(DateFormatUtils.format(end, "yyyy-MM-dd HH:mm"));

            Date now = new Date();
            if (end.before(now)) {
                hourVO.setStatus(FieldPlanStatus.Invalid.getCode());
            } else {
                hourVO.setStatus(FieldPlanStatus.Normal.getCode());
            }
            hourList.add(hourVO);
        }
        boolean isHalf = hourList.get(0).getVal().contains(":30") || hourList.get(0).getEndVal().contains(":30");

        fieldDayPlan.setHourList(hourList);

        // 处理场地 或者 教室
        List<IdNameVO> classroomList = getByVenueId(venueId);
        if (CollectionUtils.isNotEmpty(classroomList)) {
            try {
                Date start = DateUtils.parseDate(dayStr + " 00:00", "yyyy-MM-dd HH:mm");
                Date end = DateUtils.parseDate(dayStr + " 23:59", "yyyy-MM-dd HH:mm");
                List<Grade> classroomGradeList = gradeService.getClassroomGradeList(venueId, start, end);
                Map<Long, List<Grade>> classIdMap = classroomGradeList.stream().collect(Collectors.groupingBy(Grade::getGradeClassroomId));
                List<FieldDayTimePlanVO> fieldList = new ArrayList<>(classroomList.size());
                int minuteNum = isHalf ? 30 : 60;
                for (IdNameVO idName : classroomList) {
                    FieldDayTimePlanVO fieldDayTimePlan = new FieldDayTimePlanVO();
                    fieldDayTimePlan.setName(idName.getName());
                    fieldDayTimePlan.setId(idName.getId());

                    List<Grade> grades = classIdMap.get(idName.getId());
                    Map<String, FieldDayTimePlanItemVO> timeMap = new HashMap<>();
                    if (CollectionUtils.isNotEmpty(grades)) {
                        for (Grade grade : grades) {
                            Date endTime = grade.getEndTime();
                            Date startTime = grade.getStartTime();

                            int numberOfMinute = SportDateUtils.getNumberOfMinute(startTime, endTime, minuteNum);
                            for (int i = 0; i < numberOfMinute; i++) {
                                String timeKey = DateFormatUtils.format(DateUtils.addMinutes(startTime, i * minuteNum), "yyyy-MM-dd HH:mm");
                                FieldDayTimePlanItemVO item = new FieldDayTimePlanItemVO();
                                item.setId(grade.getId());
                                // 班级预订
                                item.setStatus(FieldPlanStatus.Predetermine.getCode());
                                item.setName(grade.getName());
                                if (StringUtils.isEmpty(grade.getRemark())) {
                                    item.setRemark("");
                                } else {
                                    item.setRemark(grade.getRemark());
                                }
                                timeMap.put(timeKey, item);
                            }
                        }
                    }
                    fieldDayTimePlan.setTimeMap(timeMap);
                    fieldList.add(fieldDayTimePlan);
                }
                fieldDayPlan.setFieldList(fieldList);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return fieldDayPlan;
    }


    @Override
    public List<AvailableIdNameVO> getActiveClassroomList(ActiveVenueFieldForm form) {
        if (form.getStartTime().split(":").length == 2) {
            form.setStartTime(form.getStartTime() + ":00");
        }
        if (form.getEndTime().split(":").length == 2) {
            form.setEndTime(form.getEndTime() + ":00");
        }
        Long selectedClassroomId = null;
        if (StringUtils.isNotEmpty(form.getGradeTemplateId())) {
            GradeTemplate gradeTemplate = gradeTemplateService.getById(form.getGradeTemplateId());
            selectedClassroomId = gradeTemplate.getGradeClassroomId();
        }
        List<IdNameVO> classroomList = getByVenueId(form.getVenueId());
        List<AvailableIdNameVO> availableIdNameList = classroomList.stream()
                .map(item -> {
                    AvailableIdNameVO available = new AvailableIdNameVO();
                    available.setDisable(false);
                    available.setName(item.getName());
                    available.setId(item.getId());
                    return available;
                }).collect(Collectors.toList());

        List<String> dateList = venueFieldService.getDateListByTimeType(form.getDateList(), form.getTimeType());
        List<Grade> timeList = new ArrayList<>(dateList.size());
        for (String date : dateList) {
            Grade grade = new Grade();
            try {
                Date endTime = DateUtils.parseDate(date + " " + form.getEndTime(), "yyyy-MM-dd HH:mm:ss");
                if (endTime.compareTo(form.getEndDay()) > 0) {
                    continue;
                }
                Date startTime = DateUtils.parseDate(date + " " + form.getStartTime(), "yyyy-MM-dd HH:mm:ss");
                grade.setStartTime(startTime);
                grade.setEndTime(endTime);
                timeList.add(grade);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        if (CollectionUtils.isNotEmpty(timeList)) {
            List<Long> usedClassroomId = gradeService.getUsedClassroomId(form.getVenueId(), timeList);
            if (CollectionUtils.isEmpty(usedClassroomId)) {
                return availableIdNameList;
            }
            if (selectedClassroomId != null) {
                usedClassroomId.remove(selectedClassroomId);
            }
            for (AvailableIdNameVO item : availableIdNameList) {
                if (usedClassroomId.contains(item.getId())) {
                    item.setDisable(true);
                }
            }
        }
        return availableIdNameList;
    }
}
