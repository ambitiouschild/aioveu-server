package com.aioveu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.auth.common.model.LoginVal;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.dao.GradeDao;
import com.aioveu.data.sync.FieldSyncMessage;
import com.aioveu.dto.CancelGradeMessageDTO;
import com.aioveu.entity.*;
import com.aioveu.enums.*;
import com.aioveu.exception.SportException;
import com.aioveu.form.GradeEnrollUserForm;
import com.aioveu.form.GradeForm;
import com.aioveu.form.UserGradeListForm;
import com.aioveu.service.*;
import com.aioveu.utils.DateUtil;
import com.aioveu.utils.FileUtil;
import com.aioveu.utils.SportCommonUtils;
import com.aioveu.utils.SportDateUtils;
import com.aioveu.vo.*;
import com.aioveu.vo.user.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class GradeServiceImpl extends ServiceImpl<GradeDao, Grade> implements GradeService, IApprovedService {

    @Autowired
    private GradeTemplateService gradeTemplateService;

    @Autowired
    private GradeEnrollUserService gradeEnrollUserService;

    @Autowired
    private GradeFixedUserService gradeFixedUserService;

    @Autowired
    private SpecialDayService specialDayService;

    @Autowired
    private UserService userService;

    @Autowired
    private GradeCoachService gradeCoachService;

    @Autowired
    private GradeCancelRecordService gradeCancelRecordService;

    @Autowired
    private GradeDao gradeDao;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FieldPlanService fieldPlanService;

    @Autowired
    private SystemMonitorNotifyService systemMonitorNotifyService;

    @Autowired
    private MQMessageService mqMessageService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private StoreConfigService storeConfigService;

    @Override
    public IPage<GradeVO> getByStoreId(Long storeId, String date, String status, Long coachId, String startTime, String endTime, Integer page, Integer size) {
        if (!StringUtils.isEmpty(endTime)) {
            endTime += " 23:59:59";
        } else if (StringUtils.isEmpty(date)) {
            date = DateUtil.getToday();
        }
        return getBaseMapper().getByStoreId(new Page<>(page, size), storeId, date, status, categoryService.getByCode(ExerciseCategory.EXPERIENCE.getCode()), coachId, startTime, endTime);
    }

    @Override
    public GradeDetailVO detail(Long id) {
        GradeDetailVO detail = getBaseMapper().getGradeDetailById(id);
        if (detail != null) {
            detail.setEnrollNumber(gradeEnrollUserService.enrollNumber(id).size());
            if (StringUtils.isNotEmpty(detail.getFieldIds())) {
                List<Long> fieldIdList = Arrays.stream(detail.getFieldIds().split(",")).map(t -> new Long(t)).collect(Collectors.toList());
                detail.setFieldIdList(fieldIdList);
            }
        }
        return detail;
    }

    @Override
    public boolean approved(String jsonVal) {
        GradeForm gradeForm = JSONObject.parseObject(jsonVal, GradeForm.class);
        return this.updateGrade(gradeForm);
    }

    @Override
    public boolean reject(String jsonVal) {
        return true;
    }


    @Override
    public List<GradeDetailVO> getGradeDetailByIds(String ids) {
        return getBaseMapper().getGradeDetailByIds(ids);
    }

    @Override
    public void cancelLockField(Grade grade) {
        List<FieldPlan> fieldPlanList = this.fieldPlanService.getFieldByGradeId(grade.getId());
        List<Long> fieldPlanIdList = new ArrayList<>(fieldPlanList.size());
        FieldSyncMessage fieldSyncMessage = new FieldSyncMessage();
        for (FieldPlan fieldPlan : fieldPlanList) {
            if (!Objects.equals(fieldPlan.getStatus(), FieldPlanStatus.Occupy.getCode())) continue;
            String gradeIds = SportCommonUtils.removeString(fieldPlan.getGradeIds(), grade.getId().toString());
            fieldPlan.setGradeIds(gradeIds);
            if (StringUtils.isEmpty(gradeIds)) {
                fieldPlan.setStatus(FieldPlanStatus.Normal.getCode());
            }
            this.fieldPlanService.updateById(fieldPlan);
            fieldSyncMessage.setStatus(fieldPlan.getStatus());
            fieldPlanIdList.add(fieldPlan.getId());
        }
        //场地状态变动，发送mq消息通知 用于同步锁场/解锁到第三方平台
        fieldSyncMessage.setChannel(FieldPlanLockChannels.Current.getCode());
        fieldSyncMessage.setFieldPlanIdList(fieldPlanIdList);
        mqMessageService.sendFieldSyncMessage(fieldSyncMessage);
    }

    @Override
    public void lockField(Grade grade) {
        List<FieldPlan> fieldPlanList = this.fieldPlanService.getFieldByGrade(grade);
        List<GradeDetailVO> gradeDetailVOS = this.getGradeDetailByIds(grade.getId().toString());
        GradeDetailVO gradeDetailVO = gradeDetailVOS.get(0);
        FieldPlanTimeType timeType = FieldPlanTimeType.of(gradeDetailVO.getTimeType());
        String lockRemark = String.format("%s %s,%s", timeType.getDescription(), gradeDetailVO.getName(), gradeDetailVO.getCoachNames());
        List<Long> fieldPlanIdList = new ArrayList<>(fieldPlanList.size());
        FieldSyncMessage fieldSyncMessage = new FieldSyncMessage();
        for (FieldPlan fieldPlan : fieldPlanList) {
            if (!this.fieldPlanService.checkFieldByGrades(fieldPlan, grade.getId(), grade.getStartTime(), grade.getEndTime(), grade.getSharedVenue())) {
                log.error(String.format("班级%s锁场失败，场地时段%s被占用", grade.getName(), fieldPlan.getId()));
                throw new SportException("当前场地时段已被占用，请检查");
            }
            fieldPlan.setStatus(FieldPlanStatus.Occupy.getCode());
            fieldPlan.setGradeIds(SportCommonUtils.addString(fieldPlan.getGradeIds(), grade.getId().toString()));
            fieldPlan.setRemark("");
            fieldPlan.setLockRemark(SportCommonUtils.addString(fieldPlan.getLockRemark(), lockRemark, ";"));
            fieldPlan.setLockChannel(FieldPlanLockChannels.Current.getCode());
            this.fieldPlanService.updateById(fieldPlan);
            fieldPlanIdList.add(fieldPlan.getId());
            fieldSyncMessage.setStatus(fieldPlan.getStatus());
        }
        //场地状态变动，发送mq消息通知 用于同步锁场/解锁到第三方平台
        fieldSyncMessage.setChannel(FieldPlanLockChannels.Current.getCode());
        fieldSyncMessage.setFieldPlanIdList(fieldPlanIdList);
        mqMessageService.sendFieldSyncMessage(fieldSyncMessage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateGrade(GradeForm form) {
        if (CollectionUtils.isEmpty(form.getTeacherList())) {
            throw new SportException("老师不能为空!");
        }
        if (form.getGradeClassroomId() == null && CollectionUtils.isEmpty(form.getFieldIdList())) {
            throw new SportException("教室和场地不能都为空!");
        }
        // 权限校验 教练/老师 只能修改自己的班级
        if (OauthUtils.isOnlyTeacher()) {
            String gradeUserId = getUserIdByGradeId(form.getId());
            if (!gradeUserId.equals(OauthUtils.getCurrentUserId())) {
                throw new SportException("当前用户没有编辑此课程的权限");
            }
        }
        Grade updateGrade = null;
        if (form.getId() != null) {
            updateGrade = this.getById(form.getId());
        }
        Grade grade = new Grade();
        grade.setId(form.getId());
        BeanUtils.copyProperties(form, grade);
        if (CollectionUtils.isNotEmpty(form.getFieldIdList())) {
            grade.setFieldIds(form.getFieldIdList().stream().map(Object::toString).collect(Collectors.joining(",")));
        } else {
            grade.setFieldIds(null);
        }
        try {
            String dayStr = DateFormatUtils.format(form.getDay(), "yyyy-MM-dd");
            String startTime = form.getStartTime();
            String endTime = form.getEndTime();
            if (startTime.length() == 5) {
                startTime = startTime + ":00";
                endTime = endTime + ":00";
            }
            grade.setStartTime(DateUtils.parseDate(dayStr + " " + startTime, "yyyy-MM-dd HH:mm:ss"));
            grade.setEndTime(DateUtils.parseDate(dayStr + " " + endTime, "yyyy-MM-dd HH:mm:ss"));

            Date future = DateUtils.addDays(new Date(), 14);
            if (future.before(form.getDay())) {
                throw new SportException("只能修改14日内的时间!");
            }
            // 校验下时间是否重复
            if (getBaseMapper().checkGradeTime(grade) > 0) {
                throw new SportException("时间不能和已有班级时间冲突!");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!gradeCoachService.deleteUpdateCoach(form.getId(), form.getTeacherList().stream().map(BaseNameVO::getId).collect(Collectors.toList()))) {
            throw new SportException("更新失败!");
        }
        if (updateGrade != null) {
            this.cancelLockField(updateGrade);
        }
        updateById(grade);
        this.lockField(grade);
        //编辑班级，如果修改了班级结束时间，重新发送mq消息
        if (updateGrade != null && grade.getEndTime() != null &&  updateGrade.getEndTime().compareTo(grade.getEndTime()) != 0){
            Integer type = DelayMessageType.GRADE_FINISH.getCode();
            Long gradeId = updateGrade.getId();
            Date noticeTime = DateUtils.addSeconds(grade.getEndTime(), 1);
            Map<String, Object> msgMap = new HashMap<>();
            msgMap.put("gradeId", gradeId);
            msgMap.put("type", type);
            log.info("班级{}发送MQ消息，类型:{},完成时间:{}", gradeId, type, DateFormatUtils.format(noticeTime, "yyyy-MM-dd HH:mm:ss"));
            mqMessageService.sendDelayMsgByDate(msgMap, noticeTime);
        }
        return true;
    }

    @Override
    public boolean changeStatus(Long id, Integer status) {
        Grade grade = new Grade();
        grade.setId(id);
        grade.setStatus(status);
        return updateById(grade);
    }

    /**
     * 获取前一天过期的课程列表
     *
     * @return
     */
    private List<Grade> getExpiryGradeList() {
        String day = DateFormatUtils.format(DateUtils.addDays(new Date(), -1), "yyyy-MM-dd");
        return getBaseMapper().getGradeByDay(day);
    }

    @Override
    public List<Grade> getGradeByField(Long fieldId, String date) {
        return getBaseMapper().getGradeByField(fieldId, date);
    }

    @Override
    public boolean gradeAddNew() {
        Set<String> errorList = new HashSet<>();
        try {
            log.info("班级更新服务启动...");
            Date now = new Date();
            List<GradeTemplate> gradeTemplateList = gradeTemplateService.getUpdateTemplate();
            if (CollectionUtils.isEmpty(gradeTemplateList)) {
                log.info("没有需要更新课程模板");
                return false;
            }
            log.info("需要更新的课程模板:{}", gradeTemplateList.size());
            for (GradeTemplate gradeTemplate : gradeTemplateList) {
                log.info("班级模板:{}开始更新", gradeTemplate.getId());
//                Date now = null;
//                try {
//                    now = DateUtils.parseDate("2024-09-29", "yyyy-MM-dd");
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                Date future = DateUtils.addDays(now, 14);
                Date startTime = null;
                if (GradeTimeType.WEEK_DAY.equals(GradeTimeType.of(gradeTemplate.getTimeType()))) {
                    String weekDay = SportDateUtils.getDayOfWeek(future);
                    if (gradeTemplate.getDateList().contains(weekDay)) {
                        startTime = future;
                    }
                } else if (GradeTimeType.MONTH_DAY.equals(GradeTimeType.of(gradeTemplate.getTimeType()))) {
                    String day = DateFormatUtils.format(future, "dd");
                    if (gradeTemplate.getDateList().contains(day)) {
                        startTime = future;
                    }
                } else if (GradeTimeType.EVERY_DAY.equals(GradeTimeType.of(gradeTemplate.getTimeType()))) {
                    startTime = future;
                }
                if (startTime == null) {
                    log.warn("开始时间为空, 班级模板:{}不满足时间需求", gradeTemplate.getId());
                    continue;
                }
                String dayStr = DateFormatUtils.format(startTime, "yyyy-MM-dd");
                String startTimeStr = gradeTemplate.getStartTime().toString();
                String endTimeStr = gradeTemplate.getEndTime().toString();

                //判断模板是否跳过节假日
                if (gradeTemplate.getSkipHoliday() != null && gradeTemplate.getSkipHoliday()) {
                    if (specialDayService.isHoliday(dayStr)) {
                        // 放假 跳过
                        log.info("课程模板:{}跳过法定节假日", gradeTemplate.getId());
                        continue;
                    }
                }
                Long gradeId = null;
                try {
                    gradeId = gradeTemplateService.createFullGrade(gradeTemplate, dayStr, startTimeStr, endTimeStr);
                } catch (Exception e) {
                    errorList.add(gradeTemplate.getId());
                    log.error(e.getMessage(), e);
                }
                if (gradeId == null) {
                    continue;
                }
                List<GradeFixedUser> gradeFixedUserList = gradeFixedUserService.getByTemplateId(gradeTemplate.getId());
                if (CollectionUtils.isEmpty(gradeFixedUserList)) {
                    log.warn("班级模板:{}没有固定用户", gradeTemplate.getId());
                } else {
                    log.warn("班级模板:{}固定用户:{}", gradeTemplate.getId(), gradeFixedUserList.size());
                    for (GradeFixedUser gradeFixedUser : gradeFixedUserList) {
                        GradeEnrollUserForm form = new GradeEnrollUserForm();
                        form.setGradeId(gradeId);
                        form.setAppointmentType(0);
                        form.setUserId(gradeFixedUser.getUserId());
                        form.setUsername(gradeFixedUser.getUsername());
                        form.setPhone(gradeFixedUser.getPhone());
                        form.setChildName(gradeFixedUser.getChildName());
                        form.setChildAge(gradeFixedUser.getChildAge());
                        try {
                            gradeEnrollUserService.create(form);
                        } catch (Exception e) {
                            //TODO 2025 发送系统通知
                            log.error("自动约课失败:" + e);
                            e.printStackTrace();
                            if ("没有可用的核销券!".equals(e.getMessage())) {
                                log.warn("用户:{},没有课券,系统自动取消固定约课:{}", gradeFixedUser.getUserId(), gradeFixedUser.getGradeTemplateId());
                                gradeFixedUserService.removeById(gradeFixedUser.getId());
                            }
                        }
                    }
                }
            }
            log.info("班级模板任务执行完成");
            return true;
        } finally {
            //TODO 2025 luyao 发送系统通知 改为通用消息处理
            if (!errorList.isEmpty()){
                systemMonitorNotifyService.sendMonitorMail("班级模板任务执行失败通知", "班级模版id：" + StringUtils.join(errorList,"，"));
            }
        }
    }

    @Override
    public List<GradeUserItemVO> userList(UserGradeListForm form) {
        StoreConfig storeConfig = storeConfigService.getCompanyStoreConfig(form.getCompanyId(),"appointment_coach_limit");
        String coachUserId = null;
        if (storeConfig != null && Boolean.parseBoolean(storeConfig.getValue())){
            coachUserId = userInfoService.getUserIdByIdAndCompanyId(form.getUserId(), form.getCompanyId());
        }
        List<Long> gradleIdList = getBaseMapper().getUserAvailableGradeId(form.getUserId(), form.getCompanyId(),coachUserId);
        if (CollectionUtils.isEmpty(gradleIdList)) {
            return null;
        }
        List<GradeUserItemVO> gradeUserList = getBaseMapper().getByIdAndDate(gradleIdList, form.getDate());
        Map<Integer, Map<String, Long>> gradeEnrollUserNumber = getBaseMapper()
                .getGradeEnrollUserNumber(gradeUserList.stream().map(GradeUserItemVO::getId).collect(Collectors.toList()));
        for (GradeUserItemVO item : gradeUserList) {
            Map<String, Long> numberMap = gradeEnrollUserNumber.get(NumberUtils.toInt(item.getId() + ""));
            if (numberMap != null) {
                item.setEnrollNumber(Math.toIntExact(numberMap.get("enrollNumber")));
            }
        }
        return gradeUserList;
    }

    @Override
    public GradeUserDetailVO userDetail(Long id) {
        GradeUserDetailVO detail = getBaseMapper().userDetail(id);
        if (detail != null) {
            detail.getCoachList().forEach(item -> {
                item.setUrl(FileUtil.getImageFullUrl(item.getUrl()));
            });
        }
        return detail;
    }

    @Override
    public List<GradeUserDateVO> myAvailableDate(String userId, Long companyId) {
        return getAvailableDateByUserId(userId, companyId);
    }

    @Override
    public List<GradeUserDateVO> getAvailableDateByUserId(String userId, Long companyId) {
        StoreConfig storeConfig = storeConfigService.getCompanyStoreConfig(companyId,"appointment_coach_limit");
        String coachUserId = null;
        if (storeConfig != null && Boolean.parseBoolean(storeConfig.getValue())){
            coachUserId = userInfoService.getUserIdByIdAndCompanyId(userId, companyId);
        }

        List<Long> gradleIdList = getBaseMapper().getUserAvailableGradeId(userId, companyId, coachUserId);
        if (CollectionUtils.isEmpty(gradleIdList)) {
            return null;
        }
        List<GradeUserItemVO> gradeUserItemVOList = getBaseMapper().getByIdAndDate(gradleIdList, null);
        if (CollectionUtils.isNotEmpty(gradeUserItemVOList)) {
            Map<String, List<GradeUserItemVO>> dayGradeMap = gradeUserItemVOList.stream()
                    .peek(item -> item.setDay(DateFormatUtils.format(item.getStartTime(), "yyyy-MM-dd")))
                    .collect(Collectors.groupingBy(GradeUserItemVO::getDay));
            List<GradeUserDateVO> gradeUserDateVOList = new ArrayList<>();
            for (Map.Entry<String, List<GradeUserItemVO>> entry : dayGradeMap.entrySet()) {
                GradeUserDateVO item = new GradeUserDateVO();
                item.setDay(entry.getKey());
                item.setNumber(entry.getValue().size());
                gradeUserDateVOList.add(item);
            }
            gradeUserDateVOList = gradeUserDateVOList.stream().sorted(Comparator.comparing(GradeUserDateVO::getDay)).collect(Collectors.toList());
            return gradeUserDateVOList;
        }
        return null;
    }

    @Override
    public IPage<GradeVO> getByCoach(String userId, Integer type, int page, int size, String date) {
        return gradeCoachService.getGradeByCoachId(page, size, userId, type, date);
    }

    @Override
    public boolean miniNumberCheck(Long id) {
        Grade grade = getById(id);
        if (grade != null) {
            Date startTime = DateUtils.addHours(grade.getStartTime(), -2);
            Date now = new Date();
            if (Math.abs(now.getTime() - startTime.getTime()) < 10000) {
                List<GradeEnrollUser> enrollList = gradeEnrollUserService.enrollNumber(id);
                if (CollectionUtils.isEmpty(enrollList) || enrollList.size() < grade.getMinNumber()) {
                    log.warn("班级:{}报名人数低于:{}, 任务取消", id, grade.getMinNumber());
                    if (CollectionUtils.isNotEmpty(enrollList)) {
                        for (GradeEnrollUser enrollUser : enrollList) {
                            try {
                                gradeEnrollUserService.cancelGradeEnroll(enrollUser.getId(), enrollUser.getGradeId(),
                                        enrollUser.getUserId(), 1, "课程不满足最低" + grade.getMinNumber() + "人报名要求, 系统自动取消", null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    log.warn("班级:{}报名人数大于:{}, 任务继续", id, grade.getMinNumber());
                }
            } else {
                log.warn("班级:{}延迟消息差异大于10秒, 不进行最小人数检查", id);
            }
        }
        return false;
    }

    @Override
    public List<GradeVO> getByCoachId(List<Long> coachIdList) {
        return getBaseMapper().getByCoachId(coachIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(GradeCancelRecord gradeCancelRecord, String username) {
        log.warn("{}取消班级:{}, 任务取消", username, gradeCancelRecord.getGradeId());
        Grade grade = getById(gradeCancelRecord.getGradeId());
        if (grade == null) {
            throw new SportException("班级不存在");
        }
        UserVo userVo = userService.findByUsername(username);
        List<String> roles = userVo.getRoles();
        if (CollectionUtils.isEmpty(roles) || (!roles.stream().anyMatch(s -> s.startsWith("admin")) && !roles.contains("teacher"))) {
            throw new SportException("当前用户没有取消权限");
        }
        grade.setStatus(4);

        if (updateById(grade) && gradeCancelRecordService.create(userVo.getId(), gradeCancelRecord.getExplainReason(), gradeCancelRecord.getGradeId())) {
            this.cancelLockField(grade);
            //修复：取消班级审核未处理，班级结束了，导致审批同意时，课券未正常退还bug
            List<Integer> status = new ArrayList<>();
            status.add(1);
            status.add(3);
            List<GradeEnrollUser> enrollList = gradeEnrollUserService.enrollNumber(gradeCancelRecord.getGradeId(), status);
            if (CollectionUtils.isNotEmpty(enrollList)) {
                log.info("本次班级:{}报名人数:{}", grade.getId(), enrollList.size());
                for (GradeEnrollUser enrollUser : enrollList) {
                    try {
                        gradeEnrollUserService.cancelGradeEnroll(enrollUser.getId(),
                                enrollUser.getGradeId(), enrollUser.getUserId(), 2, userVo.getName() + "因" + gradeCancelRecord.getExplainReason() + "取消课程", gradeCancelRecord.getExplainReason(), gradeCancelRecord.getCompanyId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public GradeTimeRuleVO getGradeTimeRule(Long id) {
        return getBaseMapper().getGradeTimeRule(id);
    }

    @Override
    public IPage<GradeCancelRecordVo> getCancelGradeMessageByStoreId(CancelGradeMessageDTO dataDTO) {
        return gradeDao.getCancelGradeMessageByStoreId(new Page<>(dataDTO.getPage(), dataDTO.getSize()), dataDTO);
    }

    @Override
    public List<Grade> getCancelGradeByRange(Long storeId, Date start, Date end, String coachUserId) {
        return gradeDao.getCancelGradeByRange(storeId, start, end, coachUserId);
    }

    @Override
    public void checkGradeFinish() {
        Date now = new Date();
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Grade::getStatus, GradeStatus.NORMAL.getCode())
                // 小于或等于当前时间
                .le(Grade::getEndTime, now);
        List<Grade> gradeList = list(queryWrapper);
        if (CollectionUtils.isNotEmpty(gradeList)) {
            log.info("当前未完成的班级:{}个", gradeList.size());
            for (Grade grade : gradeList) {
                gradeEnrollUserService.gradeFinish(grade.getId());
            }
        } else {
            log.info("当前没有未完成的班级");
        }
    }

    @Override
    public Grade getByTemplateIdAndTime(String gradeTemplateId, Date startTime, Date endTime) {
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Grade::getGradeTemplateId, gradeTemplateId)
                .eq(Grade::getStartTime, startTime)
                .eq(Grade::getEndTime, endTime)
                .eq(Grade::getStatus, DataStatus.NORMAL.getCode());
        return getOne(queryWrapper);
    }

    @Override
    public Set<Long> getHasClassGradeNum(Long storeId, Date startTime, Date endTime) {
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Grade::getStoreId, storeId)
                .ge(Grade::getStartTime, startTime)
                .lt(Grade::getEndTime, endTime)
                .eq(Grade::getStatus, GradeStatus.FINISH.getCode());
        List<Grade> gradeList = list(queryWrapper);
        return gradeList.stream().map(Grade::getId).collect(Collectors.toSet());
    }

    @Override
    public Set<Long> getCoachFinishGradeId(String coachUserId, Long storeId, Date start, Date end) {
        return getBaseMapper().getCoachFinishGradeId(coachUserId, storeId, start, end);
    }

    @Override
    public String getUserIdByGradeId(Long gradeId) {
        return getBaseMapper().getUserIdByGradeId(gradeId);
    }

    @Override
    public Long getCompanyIdByGradeId(Long gradeId) {
        return getBaseMapper().getCompanyIdByGradeId(gradeId);
    }

    @Autowired
    private StoreService storeService;

    @Override
    public String getGradeNumber(String date, String storeName) {
        LoginVal currentUser = OauthUtils.getCurrentUser();
        if (currentUser == null) {
            return "用户未注册绑定，引导用户提供手机号码注册绑定";
        }
        if (StringUtils.isEmpty(storeName) || "线上趣数".equals(storeName)) {
            return "请告诉我你要查询的店铺名称";
        }
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Store::getName, storeName);
        Store store = storeService.getOne(queryWrapper);
        if (store == null) {
            return "店铺不存在, 请确认店铺名称是否正确";
        }
        try {
            Date startTime = DateUtils.parseDate(date + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            Date endTime = DateUtils.parseDate(date + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            QueryWrapper<Grade> gradeQueryWrapper = new QueryWrapper<>();
            gradeQueryWrapper.lambda().eq(Grade::getStoreId, store.getId())
                    .eq(Grade::getStartTime, startTime)
                    .eq(Grade::getEndTime, endTime);
            int count = count(gradeQueryWrapper);
            return date + "  共" + count + "个班级";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "未查询到班级信息";
    }

    @Override
    public List<Grade> getFutureGrade(String gradeTemplateId, Date endTime) {
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Grade::getGradeTemplateId, gradeTemplateId)
                .gt(Grade::getEndTime, endTime)
                .eq(Grade::getStatus, GradeStatus.NORMAL.getCode());
        return list(queryWrapper);
    }

    @Override
    public List<Grade> getClassroomGradeList(Long venueId, Date startTime, Date endTime) {
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Grade::getVenueId, venueId)
                .isNotNull(Grade::getGradeClassroomId)
                .in(Grade::getStatus, 1, 3)
                .ge(Grade::getStartTime, startTime)
                .lt(Grade::getEndTime, endTime);
        return list(queryWrapper);
    }

    @Override
    public List<StatisticsVo> getUnSignNumsByStoreId(Long storeId, Date startDate, Date endDate) {
        return this.baseMapper.getUnSignNumsByStoreId(storeId, startDate, endDate);
    }

    @Override
    public List<Long> getUsedClassroomId(Long venueId, List<Grade> timeList) {
        return getBaseMapper().getUsedClassroomId(venueId, timeList);
    }
}
