package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.auth.common.model.LoginVal;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.dto.GradeEnrollUserDTO;
import com.aioveu.entity.CouponTemplate;
import com.aioveu.entity.StoreVenue;
import com.aioveu.entity.UserWechatId;
import com.aioveu.entity.VenueField;
import com.aioveu.form.ActiveVenueFieldForm;
import com.aioveu.form.GradeTemplateForm;
import com.aioveu.form.StoreForm;
import com.aioveu.service.*;
import com.aioveu.tool.GradeEnrollUserNameTimeCancelTool;
import com.aioveu.tool.GradeEnrollUserTimeCancelTool;
import com.aioveu.tool.GradeTemplateCreateTool;
import com.aioveu.utils.SportDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GradeAiServiceImpl implements GradeAiService {

    @Autowired
    private StoreService storeService;

    @Autowired
    private UserCoachService userCoachService;

    @Autowired
    private CouponTemplateService couponTemplateService;

    @Autowired
    private StoreVenueService storeVenueService;

    @Autowired
    private VenueFieldService venueFieldService;

    @Autowired
    private UserWechatIdService userWechatIdService;

    @Autowired
    private GradeTemplateService gradeTemplateService;

    @Autowired
    private GradeEnrollUserService gradeEnrollUserService;

    @Override
    public String aiCreate(GradeTemplateCreateTool gradeTemplateCreateTool) {
        LoginVal currentUser = OauthUtils.getCurrentUser();
        if (currentUser == null) {
            return "用户未注册绑定，引导用户提供手机号码注册绑定";
        }
        String[] authorities = currentUser.getAuthorities();
        if (authorities !=null && Arrays.asList(authorities).contains("teacher")) {
            String userId = currentUser.getUserId();
            GradeTemplateForm form = new GradeTemplateForm();
            BeanUtils.copyProperties(gradeTemplateCreateTool, form);
            UserWechatId userWechatId = userWechatIdService.getByUserId(currentUser.getUserId());
            if (userWechatId == null) {
                return "用户未注册绑定，引导用户提供手机号码注册绑定";
            }
            Long comapnyId = userWechatId.getCompanyId();
            IPage<StoreForm> storePages = storeService.getAll(1, 10, comapnyId, gradeTemplateCreateTool.getStoreName(), 1);
            if (storePages.getTotal() == 0) {
                return "店铺不存在，请重新输入";
            } else if (storePages.getTotal() > 1) {
                return "店铺有" + storePages.getRecords().stream().map(StoreForm::getName).collect(Collectors.joining(",")) + "，请选择其中一个";
            }
            Long storeId = storePages.getRecords().get(0).getId();
            form.setStoreId(storeId);
            form.setExceed(false);
            form.setMinNumber(1);
            form.setGradeClassroomId(null);
            form.setGradeLevelId(null);
            form.setGradeAgeId(null);

            try {
                Date startDay = SportDateUtils.parseDate(gradeTemplateCreateTool.getStartDayTime());
                Date endDay = SportDateUtils.parseDate(gradeTemplateCreateTool.getEndDayTime());
                form.setStartDay(SportDateUtils.dayTime2Day(startDay));
                form.setEndDay(SportDateUtils.dayTime2Day(endDay));

                form.setStartTime(DateFormatUtils.format(startDay, "HH:mm:ss"));
                form.setEndTime(DateFormatUtils.format(endDay, "HH:mm:ss"));
                form.setRemark("智能管家代为创建");

                form.setTimeType(0);
                form.setSharedVenue(0);
                form.setSkipHoliday(false);

                CouponTemplate couponTemplate = couponTemplateService.findByName(gradeTemplateCreateTool.getCouponTemplateName());
                if (couponTemplate == null) {
                    return "课程券不存在，请重新输入";
                }
                form.setCouponTemplateIds(couponTemplate.getId() + "-" + gradeTemplateCreateTool.getClassHour());

                Long userCoachId = userCoachService.getStoreUserCoach(userId, storeId);
                form.setCoachIds("" + userCoachId);

                form.setDateList(SportDateUtils.getDateRange(form.getStartDay(), form.getEndDay()));
                form.setStatus(1);

                List<StoreVenue> storeVenueList = storeVenueService.getByName(gradeTemplateCreateTool.getVenueName(), storeId);
                if (storeVenueList.isEmpty()) {
                    return "场馆不存在，请重新输入";
                } else if (storeVenueList.size() > 1) {
                    return "场馆有" + storeVenueList.stream().map(StoreVenue::getName).collect(Collectors.joining(",")) + "，请选择其中一个";
                }
                form.setVenueId(storeVenueList.get(0).getId());

                // 设置场地
                ActiveVenueFieldForm activeVenueFieldForm = new ActiveVenueFieldForm();
                activeVenueFieldForm.setVenueId(form.getVenueId());
                activeVenueFieldForm.setStartDay(form.getStartDay());
                activeVenueFieldForm.setEndDay(form.getEndDay());
                activeVenueFieldForm.setStartTime(form.getStartTime());
                activeVenueFieldForm.setEndTime(form.getEndTime());
                activeVenueFieldForm.setTimeType(form.getTimeType());
                activeVenueFieldForm.setDateList(form.getDateList());
                activeVenueFieldForm.setSharedVenue(form.getSharedVenue());
                List<VenueField> activeVenueFieldList = venueFieldService.getActiveVenueFieldList(activeVenueFieldForm);
                Optional<VenueField> first = activeVenueFieldList.stream().filter(item -> !item.isDisable()).findFirst();
                if (!first.isPresent()) {
                    return "当前时间段场馆:" + gradeTemplateCreateTool.getVenueName() + " 没有可用场地，请修改其他时间段";
                }
                form.setFieldIdList(Collections.singletonList(first.get().getId()));
            } catch (ParseException e) {
                e.printStackTrace();
                return "时间格式错误，请重新输入";
            }
            try {
                if (gradeTemplateService.create(form)) {
                    return "创建成功";
                } else {
                    return "创建失败， 请稍后重试";
                }
            }catch (Exception e) {
                log.error("用户:{} AI创建班级失败:{}", userId, e.getMessage());
                e.printStackTrace();
                return e.getMessage();
            }

        } else {
            return "只有老师或者教练才可以创建班级，当前用户没有权限，请联系管理员配置权限";
        }
    }

    @Override
    public String aiUserTimeCancelEnroll(GradeEnrollUserTimeCancelTool gradeEnrollUserTimeCancelTool) {
        LoginVal currentUser = OauthUtils.getCurrentUser();
        if (currentUser == null) {
            return "用户未注册绑定，引导用户提供手机号码注册绑定";
        }
        try {
            List<GradeEnrollUserDTO> userEnrollList = gradeEnrollUserService.getUserEnroll4TimeRange(gradeEnrollUserTimeCancelTool.getStartDayTime(), gradeEnrollUserTimeCancelTool.getEndDayTime(), currentUser.getUserId(), null);
            return aiCancelUserEnroll(userEnrollList, currentUser.getUserId());
        }catch (Exception e) {
            log.error("用户:{} AI取消约课失败:{}", currentUser.getUserId(), e.getMessage());
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public String aiUserNameTimeCancelEnroll(GradeEnrollUserNameTimeCancelTool gradeEnrollUserNameTimeCancelTool) {
        LoginVal currentUser = OauthUtils.getCurrentUser();
        if (currentUser == null) {
            return "用户未注册绑定，引导用户提供手机号码注册绑定";
        }
        try {
            List<GradeEnrollUserDTO> userEnrollList = gradeEnrollUserService.getUserEnroll4TimeRange(gradeEnrollUserNameTimeCancelTool.getStartDayTime(), gradeEnrollUserNameTimeCancelTool.getEndDayTime(), currentUser.getUserId(), gradeEnrollUserNameTimeCancelTool.getGradeName());
            return aiCancelUserEnroll(userEnrollList, currentUser.getUserId());
        }catch (Exception e) {
            log.error("用户:{} 取消约课失败:{}", currentUser.getUserId(), e.getMessage());
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * AI用户根据时间取消约课调用
     * @param userEnrollList
     * @param userId
     * @return
     */
    private String aiCancelUserEnroll(List<GradeEnrollUserDTO> userEnrollList, String userId) {
        if (CollectionUtils.isEmpty(userEnrollList)) {
            return "当前时间未查询到用户有效的约课记录，问下用户其他时间是否有约课";
        } else if (userEnrollList.size() == 1) {
            if (gradeEnrollUserService.cancel(userEnrollList.get(0).getId(), userId)) {
                return "取消成功";
            } else {
                return "取消失败";
            }
        } else {
            return "查询到用户有多条约课记录:" + userEnrollList.stream().map(item -> SportDateUtils.getMergeTime(item.getStartTime(), item.getEndTime()) + " " + item.getName())
                    .collect(Collectors.joining(",")) + ", 引导用户选择具体时间对应的约课课程";
        }
    }
}
