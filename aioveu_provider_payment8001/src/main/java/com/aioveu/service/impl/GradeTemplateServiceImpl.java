package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.GradeTemplateDao;
import com.aioveu.entity.*;
import com.aioveu.enums.DataStatus;
import com.aioveu.enums.DelayMessageType;
import com.aioveu.enums.GradeTimeType;
import com.aioveu.exception.SportException;
import com.aioveu.form.GradeTemplateForm;
import com.aioveu.service.*;
import com.aioveu.utils.IdUtils;
import com.aioveu.utils.JacksonUtils;
import com.aioveu.utils.SportDateUtils;
import com.aioveu.vo.GradeTemplateDetailVO;
import com.aioveu.vo.GradeTemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
public class GradeTemplateServiceImpl extends ServiceImpl<GradeTemplateDao, GradeTemplate> implements GradeTemplateService {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private GradeCouponTemplateService gradeCouponTemplateService;

    @Autowired
    private GradeCoachService gradeCoachService;

    @Autowired
    private MQMessageService mqMessageService;

    @Autowired
    private StoreCoachService storeCoachService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(GradeTemplateForm form) {
        log.info("创建课程模板:" + JacksonUtils.obj2Json(form));
        List<String> dateList;
        if (GradeTimeType.FREE_DAY.equals(GradeTimeType.of(form.getTimeType()))) {
            if (CollectionUtils.isEmpty(form.getDateList())) {
                throw new SportException("时间不能为空!");
            }
            dateList = form.getDateList();
        } else if (GradeTimeType.WEEK_DAY.equals(GradeTimeType.of(form.getTimeType()))) {
            if (CollectionUtils.isEmpty(form.getDateList())) {
                throw new SportException("时间不能为空!");
            }
            dateList = SportDateUtils.getWeekDayFromString(form.getDateList(), 14);
        } else if (GradeTimeType.MONTH_DAY.equals(GradeTimeType.of(form.getTimeType()))) {
            if (CollectionUtils.isEmpty(form.getDateList())) {
                throw new SportException("时间不能为空!");
            }
            dateList = SportDateUtils.getMonthDayFromString(form.getDateList(), 14);
        } else if (GradeTimeType.EVERY_DAY.equals(GradeTimeType.of(form.getTimeType()))) {
            dateList = SportDateUtils.getFutureDay(14);
        } else {
            dateList = new ArrayList<>();
        }
        String couponTemplateIds = form.getCouponTemplateIds();
        if (StringUtils.isNotEmpty(couponTemplateIds) && couponTemplateIds.contains("undefined")) {
            throw new SportException("课程券参数错误, 请重新选择!");
        }
        // 场地和教室 必须要选一个
        if (form.getGradeClassroomId() == null && CollectionUtils.isEmpty(form.getFieldIdList())) {
            throw new SportException("场地或者教室必须选择一项");
        }
        String[] couponTemplateSplit = couponTemplateIds.split(",");
        for (String couponTemplateIdItem : couponTemplateSplit) {
            String[] splitItem = couponTemplateIdItem.split("-");
            if (splitItem.length != 2) {
                throw new SportException("课程券参数错误, 请重新选择!");
            }
            if (Integer.parseInt(splitItem[1]) <= 0) {
                throw new SportException("消耗课券数量必须大于0!");
            }
        }
        GradeTemplate gradeTemplate = new GradeTemplate();
        if (form.getId() != null) {
            gradeTemplate = this.getById(form.getId());
        }
        BeanUtils.copyProperties(form, gradeTemplate);
        if (gradeTemplate.getId() == null) {
            gradeTemplate.setId(IdUtils.getStringId());
        }
        String startTime = form.getStartTime();
        String endTime = form.getEndTime();
        // 开始时间 和 结束时间 只能是整点 或者 半小时
        if (!SportDateUtils.isValidTimeFormat(startTime) || !SportDateUtils.isValidTimeFormat(endTime)) {
            throw new SportException("开始时间和结束时间只能是整点或者半小时");
        }
        gradeTemplate.setStartTime(Time.valueOf(startTime));
        gradeTemplate.setEndTime(Time.valueOf(endTime));
        if (gradeTemplate.getStartTime().after(gradeTemplate.getEndTime())) {
            log.error(gradeTemplate.toString());
            throw new SportException("开始时间不能大于结束时间!");
        }
        if (CollectionUtils.isNotEmpty(form.getDateList())) {
            gradeTemplate.setDateList(String.join(",", form.getDateList()));
        }
        if (CollectionUtils.isNotEmpty(form.getFieldIdList())) {
            gradeTemplate.setFieldIds(form.getFieldIdList().stream().map(Object::toString).collect(Collectors.joining(",")));
        } else {
            gradeTemplate.setFieldIds(null);
        }

        if (StringUtils.isEmpty(form.getId())) {
            save(gradeTemplate);
            // 每 周五 今天是周五
            for (String dayStr : dateList) {
                Date day;
                try {
                    day = DateUtils.parseDate(dayStr, "yyyy-MM-dd");
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new SportException("时间格式错误");
                }
                if(gradeTemplate.getStartDay().compareTo(day) > 0 || gradeTemplate.getEndDay().compareTo(day) < 0) {
                    continue;
                }
                createFullGrade(gradeTemplate, dayStr, startTime, endTime);
            }
        } else {
            updateById(gradeTemplate);
        }
        return true;
    }

    /**
     * 发送班级最小人数检查的消息
     *
     * @param gradeId
     * @param startTime
     */
    private void sendGradeMiniNumberCheckMsg(Long gradeId, Date startTime) {
        sendMQMessage(gradeId, DateUtils.addHours(startTime, -2), DelayMessageType.GRADE_MINI_CHECK.getCode());
    }

    /**
     * 发送班级完成时候的通知
     *
     * @param gradeId
     * @param finishTime
     */
    private void sendGradeFinish(Long gradeId, Date finishTime) {
        sendMQMessage(gradeId, finishTime, DelayMessageType.GRADE_FINISH.getCode());
    }

    /**
     * 发送MQ消息
     *
     * @param gradeId
     * @param noticeTime
     * @param type
     */
    private void sendMQMessage(Long gradeId, Date noticeTime, int type) {
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("gradeId", gradeId);
        msgMap.put("type", type);
        log.info("班级{}发送MQ消息，类型:{},完成时间:{}", gradeId, type, DateFormatUtils.format(noticeTime, "yyyy-MM-dd HH:mm:ss"));
        mqMessageService.sendDelayMsgByDate(msgMap, noticeTime);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Long createFullGrade(GradeTemplate gradeTemplate, String dayStr, String startTime, String endTime) {
        Grade grade = createByGradeTemplate(gradeTemplate, dayStr, startTime, endTime);
        if (grade == null) {
            return null;
        }
        Grade old = gradeService.getByTemplateIdAndTime(gradeTemplate.getId(), grade.getStartTime(), grade.getEndTime());
        if (old != null) {
            log.error("课程:{} 已存在时间:{} - {}", old.getId(), dayStr + " " + startTime, endTime);
            return null;
        }


        // 根据班级模板 创建班级
        if (gradeService.save(grade)) {
            // 创建成功之后根据上课时间 添加开课前2小时的延迟消息，用于检测最小人数是否满足
            sendGradeMiniNumberCheckMsg(grade.getId(), grade.getStartTime());
            // 发送课程完成的通知
            sendGradeFinish(grade.getId(), DateUtils.addSeconds(grade.getEndTime(), 1));
            List<GradeCouponTemplate> gradeCourseList = new ArrayList<>();
            String[] couponTemplateIdArray = gradeTemplate.getCouponTemplateIds().split(",");
            for (String idHour : couponTemplateIdArray) {
                GradeCouponTemplate gc = new GradeCouponTemplate();
                gc.setGradeId(grade.getId());
                String[] idHourArray = idHour.split("-");
                if (idHourArray.length > 1) {
                    gc.setCouponTemplateId(Long.parseLong(idHourArray[0]));
                    gc.setClassHour(NumberUtils.toInt(idHourArray[1], 1));
                } else {
                    gc.setCouponTemplateId(Long.parseLong(idHourArray[0]));
                    gc.setClassHour(gradeTemplate.getClassHour());
                }
                gradeCourseList.add(gc);
            }
            gradeCouponTemplateService.saveBatch(gradeCourseList);
            List<GradeCoach> gradeCoachList = new ArrayList<>();
            String[] coachIdArray = gradeTemplate.getCoachIds().split(",");
            for (String id : coachIdArray) {
                GradeCoach gc = new GradeCoach();
                gc.setGradeId(grade.getId());
                gc.setCoachId(Long.parseLong(id));
                gradeCoachList.add(gc);
            }
            gradeCoachService.saveBatch(gradeCoachList);
            gradeService.lockField(grade);
            return grade.getId();
        }
        return null;
    }

    @Override
    public IPage<GradeTemplateVO> templateList(Long storeId, String date, Integer page, Integer size) {
        String startDate = null;
        String endDate = null;
        if (StringUtils.isNotBlank(date)){
            startDate = date + " 00:00:00";
            endDate = date + " 23:59:59";
        }
        IPage<GradeTemplateVO> gradeTemplateList = getBaseMapper().templateList(new Page<>(page, size), storeId, startDate, endDate);
        if (CollectionUtils.isNotEmpty(gradeTemplateList.getRecords())) {
            for (GradeTemplateVO item : gradeTemplateList.getRecords()) {
                item.setTimeRule(getTimeRule(item.getTimeType(), item.getDateList(), item.getStartTime(), item.getEndTime()));
            }
        }
        return gradeTemplateList;
    }

    @Override
    public GradeTemplateDetailVO detail(String id) {
        GradeTemplateDetailVO gradeTemplate = getBaseMapper().getDetail(id);
        if (gradeTemplate != null) {
            if (StringUtils.isNotEmpty(gradeTemplate.getDateStr())) {
                gradeTemplate.setDateList(Arrays.asList(gradeTemplate.getDateStr().split(",")));
            }
            if (StringUtils.isNotEmpty(gradeTemplate.getFieldIds())) {
                List<Long> fieldIdList = Arrays.stream(gradeTemplate.getFieldIds().split(",")).map(t -> new Long(t)).collect(Collectors.toList());
                gradeTemplate.setFieldIdList(fieldIdList);
            }
        }
        return gradeTemplate;
    }

    @Override
    public boolean templateUpdate(GradeTemplateForm form) {
        if (StringUtils.isEmpty(form.getId())) {
            throw new SportException("模板id不能为空");
        }
        return create(form);
    }

    @Override
    public Grade createByGradeTemplate(GradeTemplate gradeTemplate, String dayStr, String startTime, String endTime) {
        Grade grade = new Grade();
        grade.setName(gradeTemplate.getName());
        grade.setGradeClassroomId(gradeTemplate.getGradeClassroomId());
        grade.setExceed(gradeTemplate.getExceed());
        grade.setLimitNumber(gradeTemplate.getLimitNumber());
        grade.setStoreId(gradeTemplate.getStoreId());
        grade.setGradeTemplateId(gradeTemplate.getId());
        grade.setVenueId(gradeTemplate.getVenueId());
        grade.setFieldIds(gradeTemplate.getFieldIds());
        grade.setSharedVenue(gradeTemplate.getSharedVenue());
        StringBuilder sb = new StringBuilder();
        String coachIds = gradeTemplate.getCoachIds();
        if (StringUtils.isNotEmpty(coachIds)) {
            String[] coachIdArray = coachIds.split(",");
            List<StoreCoach> storeCoaches = storeCoachService.listByIds(Arrays.asList(coachIdArray));
            sb.append(storeCoaches.stream().map(StoreCoach::getName).collect(Collectors.joining(","))).append(" ");
        }
        if (GradeTimeType.WEEK_DAY.equals(GradeTimeType.of(gradeTemplate.getTimeType()))) {
            sb.append("每周课");
        } else if (GradeTimeType.FREE_DAY.equals(GradeTimeType.of(gradeTemplate.getTimeType()))) {
            sb.append("临时课");
        } else if (GradeTimeType.MONTH_DAY.equals(GradeTimeType.of(gradeTemplate.getTimeType()))) {
            sb.append("每月课");
        } else if (GradeTimeType.EVERY_DAY.equals(GradeTimeType.of(gradeTemplate.getTimeType()))) {
            sb.append("每天课");
        }
        grade.setRemark(sb.toString());
        try {
            grade.setStartTime(DateUtils.parseDate(dayStr + " " + startTime, "yyyy-MM-dd HH:mm:ss"));
            grade.setEndTime(DateUtils.parseDate(dayStr + " " + endTime, "yyyy-MM-dd HH:mm:ss"));
            // 如果班级开始时间 在当前时间前面 说明已过期 状态为下架
            if (grade.getStartTime().before(new Date())) {
                grade.setStatus(2);
            }
            if (grade.getStartTime().after(grade.getEndTime())) {
                throw new SportException("开始时间不能大于结束时间");
            }
            return grade;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteById(String id) {
        GradeTemplate gt = new GradeTemplate();
        gt.setId(id);
        gt.setStatus(DataStatus.DELETE.getCode());
        return updateById(gt);
    }

    @Override
    public boolean changeStatus(String id, Integer status) {
        GradeTemplate gt = new GradeTemplate();
        gt.setId(id);
        gt.setStatus(status);
        return updateById(gt);
    }

    @Override
    public List<GradeTemplate> getUpdateTemplate() {
        Date now = new Date();
        QueryWrapper<GradeTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GradeTemplate::getStatus, DataStatus.NORMAL.getCode())
                .in(GradeTemplate::getTimeType, GradeTimeType.WEEK_DAY.getCode(),
                        GradeTimeType.MONTH_DAY.getCode(), GradeTimeType.EVERY_DAY.getCode())
                .le(GradeTemplate::getStartDay, now)
                .ge(GradeTemplate::getEndDay, now);
        return list(queryWrapper);
    }

    /**
     * 获取时间规则
     *
     * @param timeType  时间类型 默认0 自由时间 1每星期 2每月 3每天
     * @param dateList
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public String getTimeRule(Integer timeType, String dateList, Date startTime, Date endTime) {
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
}
