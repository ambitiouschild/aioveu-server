package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.ExerciseCountTemplateDao;
import com.aioveu.entity.*;
import com.aioveu.enums.*;
import com.aioveu.exception.SportException;
import com.aioveu.form.ExerciseCountTemplateForm;
import com.aioveu.form.ExerciseForm;
import com.aioveu.service.*;
import com.aioveu.utils.IdUtils;
import com.aioveu.utils.SportDateUtils;
import com.aioveu.vo.ExerciseCountPayVO;
import com.aioveu.vo.ExerciseCountTemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
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
public class ExerciseCountTemplateServiceImpl extends ServiceImpl<ExerciseCountTemplateDao, ExerciseCountTemplate> implements ExerciseCountTemplateService, IApprovedService {

    @Autowired
    private ExerciseCountTemplateDao exerciseCountTemplateDao;

    @Autowired
    private SpecialDayService specialDayService;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @Autowired
    private GradeTemplateService gradeTemplateService;

    @Autowired
    private StoreVenueService storeVenueService;

    @Override
    public boolean periodicUpdate() {
        log.info("按次活动模板更新服务启动...");
        List<ExerciseCountTemplate> templateList = getUpdateTemplate();
        if (CollectionUtils.isEmpty(templateList)) {
            log.info("没有需要更新按次活动模板");
        } else {
            log.info("需要更新的课程模板:{}", templateList.size());
            for (ExerciseCountTemplate item : templateList) {
                log.info("按次活动模板:{}开始更新", item.getId());
                Date future = DateUtils.addDays(new Date(), 7);
                Date startTime = null;
                if (GradeTimeType.WEEK_DAY.equals(GradeTimeType.of(item.getTimeType()))) {
                    String weekDay = SportDateUtils.getDayOfWeek(future);
                    if (item.getDateList().contains(weekDay)) {
                        startTime = future;
                    }
                } else if (GradeTimeType.MONTH_DAY.equals(GradeTimeType.of(item.getTimeType()))) {
                    String day = DateFormatUtils.format(future, "dd");
                    if (item.getDateList().contains(day)) {
                        startTime = future;
                    }
                } else if (GradeTimeType.EVERY_DAY.equals(GradeTimeType.of(item.getTimeType()))) {
                    startTime = future;
                }
                if (startTime == null) {
                    log.warn("开始时间为空, 按次活动模板:{}不满足时间需求", item.getId());
                    continue;
                }
                String dayStr = DateFormatUtils.format(startTime, "yyyy-MM-dd");
                String startTimeStr = item.getExerciseStartTime().toString();
                String endTimeStr = item.getExerciseEndTime().toString();
                createExerciseCount(item, dayStr, startTimeStr, endTimeStr);
            }
        }
        return false;
    }

    @Override
    public String createTemplate(ExerciseCountTemplateForm form, String username) {
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
            dateList = SportDateUtils.getWeekDayFromString(form.getDateList());
        } else if (GradeTimeType.MONTH_DAY.equals(GradeTimeType.of(form.getTimeType()))) {
            if (CollectionUtils.isEmpty(form.getDateList())) {
                throw new SportException("时间不能为空!");
            }
            dateList = SportDateUtils.getMonthDayFromString(form.getDateList());
        } else if (GradeTimeType.EVERY_DAY.equals(GradeTimeType.of(form.getTimeType()))) {
            dateList = SportDateUtils.getFuture7Day();
        } else {
            dateList = new ArrayList<>();
        }
        if (form.getOriginalPrice() == null) {
            form.setOriginalPrice(form.getPrice());
        }
        String userId = userService.getUserIdFromCache(username);
        ExerciseCountTemplate template = new ExerciseCountTemplate();
        BeanUtils.copyProperties(form, template);
        String startTime = form.getStartTime() + ":00";
        String endTime = form.getEndTime() + ":00";
        template.setExerciseStartTime(Time.valueOf(startTime));
        template.setExerciseEndTime(Time.valueOf(endTime));

        template.setCreateUserId(userId);
        Store store = storeService.getById(form.getStoreId());
        if (store == null) {
            throw new SportException("店铺id错误!");
        }
        template.setCompanyId(store.getCompanyId());
        if (template.getExerciseStartTime().after(template.getExerciseEndTime())) {
            log.error(template.toString());
            throw new SportException("开始时间不能大于结束时间!");
        }

        if (form.getId() != null) {
            updateById(template);
        } else {
            // 提交到审核
            // 默认待审核
            template.setStatus(2);
            template.setId(IdUtils.getStringId());
            if (CollectionUtils.isNotEmpty(dateList)) {
                template.setDateList(String.join(",", dateList));
            }
            if (save(template)) {
                Audit audit = new Audit();
                audit.setAuditTitle("用户新建" + template.getName() + "按次活动");
                audit.setAuditType(AuditType.Count_Exercise_Create.getCode());
                audit.setJsonVal(template.getId());
                audit.setStoreId(template.getStoreId());
                audit.setCreateUserId(template.getCreateUserId());
                audit.setAuditService("ExerciseCountTemplateServiceImpl");
                auditService.submit(audit);
            }
        }
        return template.getId();
    }

    @Override
    public List<ExerciseCountTemplateVO> getUserTemplate(String username, String appId) {
        String userId = userService.getUserIdFromCache(username);
        Long companyId = companyService.getCompanyIdByAppId(appId);
        List<ExerciseCountTemplate> templateList = getBaseMapper().getUserTemplate(userId, companyId);
        return templateList.stream().map(item -> {
            ExerciseCountTemplateVO exerciseCountTemplateVO = new ExerciseCountTemplateVO();
            exerciseCountTemplateVO.setName(item.getName());
            exerciseCountTemplateVO.setStoreName(item.getStoreName());
            exerciseCountTemplateVO.setId(item.getId());
            exerciseCountTemplateVO.setDateRange(SportDateUtils.get2Day2DayShow(item.getStartDay(), item.getEndDay()));
            exerciseCountTemplateVO.setTimeRange(gradeTemplateService.getTimeRule(item.getTimeType(), item.getDateList(), SportDateUtils.time2Day(item.getExerciseStartTime()), SportDateUtils.time2Day(item.getExerciseEndTime())));
            exerciseCountTemplateVO.setCreateDate(DateFormatUtils.format(item.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
            exerciseCountTemplateVO.setStatusText(CountExerciseStatus.of(item.getStatus()).getDescription());
            return exerciseCountTemplateVO;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean approved(String id) {
        ExerciseCountTemplate template = getById(id);
        ExerciseCountTemplate et = new ExerciseCountTemplate();
        et.setId(id);
        et.setStatus(1);
        if (template != null && updateById(et)) {
            String[] dateList = template.getDateList().split(",");
            String startTime = template.getExerciseStartTime().toString();
            String endTime = template.getExerciseEndTime().toString();
            // 每 周五 今天是周五
            for (String dayStr : dateList) {
                createExerciseCount(template, dayStr, startTime, endTime);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean reject(String jsonVal) {
        ExerciseCountTemplate et = new ExerciseCountTemplate();
        et.setId(jsonVal);
        et.setStatus(4);
        return updateById(et);
    }

    /**
     * 创建按次活动
     * @param item
     * @param dayStr
     * @param startTimeStr
     * @param endTimeStr
     * @return
     */
    private boolean createExerciseCount(ExerciseCountTemplate item, String dayStr, String startTimeStr, String endTimeStr) {
        //判断模板是否跳过节假日
        if (item.getSkipHoliday() != null && item.getSkipHoliday()) {
            SpecialDay specialDay = specialDayService.getByDay(dayStr);
            try {
                if (specialDay != null) {
                    // 特殊日期
                    if (specialDay.getSpecialType() == 1) {
                        // 法定节假日
                        log.info("课程模板:{}跳过法定节假日", item.getId());
                        return false;
                    }
                } else if (SportDateUtils.isWeekDay(DateUtils.parseDate(dayStr, "yyyy-MM-dd"))) {
                    // 普通日期 判断是否是周末
                    log.info("课程模板:{}跳过普通周末节假日", item.getId());
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 创建 按次活动
        ExerciseForm exerciseForm = new ExerciseForm();
        BeanUtils.copyProperties(item, exerciseForm);
        exerciseForm.setCreateDate(new Date());
        exerciseForm.setUpdateDate(new Date());
        try {
            exerciseForm.setStartTime(new Date());
            exerciseForm.setExerciseStartTime(DateUtils.parseDate(dayStr + " " + startTimeStr, "yyyy-MM-dd HH:mm:ss"));
            exerciseForm.setExerciseEndTime(DateUtils.parseDate(dayStr + " " + endTimeStr, "yyyy-MM-dd HH:mm:ss"));
            exerciseForm.setEndTime(exerciseForm.getExerciseEndTime());

            if (exerciseForm.getStartTime().after(exerciseForm.getEndTime())) {
                throw new SportException("开始时间不能大于结束时间");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        exerciseForm.setCategoryId(categoryService.getByCode(ExerciseCategory.COUNT.getCode()));
        exerciseForm.setStatus(1);
        if (exerciseService.createOrUpdate(exerciseForm)) {
            return exerciseCountTemplateDao.saveExerciseCountTemplateId(exerciseForm.getId(), item.getId()) > 0;
        }
        return false;
    }

    /**
     * 获取待更新的模板
     * @return
     */
    private List<ExerciseCountTemplate> getUpdateTemplate() {
        Date now = new Date();
        QueryWrapper<ExerciseCountTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExerciseCountTemplate::getStatus, DataStatus.NORMAL.getCode())
                .in(ExerciseCountTemplate::getTimeType, GradeTimeType.WEEK_DAY.getCode(),
                        GradeTimeType.MONTH_DAY.getCode(), GradeTimeType.EVERY_DAY.getCode())
                .le(ExerciseCountTemplate::getStartDay, now)
                .ge(ExerciseCountTemplate::getEndDay, now);
        return list(queryWrapper);
    }

    @Override
    public boolean deleteById(String id) {
        ExerciseCountTemplate et = new ExerciseCountTemplate();
        et.setId(id);
        et.setStatus(DataStatus.DELETE.getCode());
        return updateById(et);
    }

    @Override
    public ExerciseCountTemplate getByExerciseId(Long exerciseId) {
        String templateId = getBaseMapper().getExerciseCountTemplateIdByExerciseId(exerciseId);
        return getById(templateId);
    }

    @Override
    public ExerciseCountPayVO getPayInfo(Long exerciseId) {
        return getBaseMapper().getPayInfo(exerciseId);
    }

    @Override
    public ExerciseCountTemplateForm detail(String id) {
        ExerciseCountTemplate template = getById(id);
        if (template == null) {
            throw new SportException("按次活动模板不存在");
        }
        ExerciseCountTemplateForm form = new ExerciseCountTemplateForm();
        BeanUtils.copyProperties(template, form);
        form.setStartTime(template.getExerciseStartTime().toString());
        form.setEndTime(template.getExerciseEndTime().toString());
        StoreVenue storeVenue = storeVenueService.getById(template.getVenueId());
        if (storeVenue != null) {
            form.setVenueName(storeVenue.getName());
        }
        return form;
    }
}
