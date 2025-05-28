package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.auth.common.model.LoginVal;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.constant.PhoneCodeConstant;
import com.aioveu.constant.SysDictConstant;
import com.aioveu.dao.GradeEnrollUserDao;
import com.aioveu.dto.GradeEnrollUserDTO;
import com.aioveu.dto.GradeWeekEnrollUserDTO;
import com.aioveu.entity.*;
import com.aioveu.enums.*;
import com.aioveu.exception.SportException;
import com.aioveu.form.GradeEnrollUserForm;
import com.aioveu.form.HelpCancelGradeEnrollUserForm;
import com.aioveu.form.HelpGradeEnrollUserForm;
import com.aioveu.service.*;
import com.aioveu.utils.JacksonUtils;
import com.aioveu.utils.SportDateUtils;
import com.aioveu.utils.StringValidatorUtils;
import com.aioveu.vo.AnalysisCouponVO;
import com.aioveu.vo.GradeEnrollUserDetailVO;
import com.aioveu.vo.GradeEnrollUserItemVO;
import com.aioveu.vo.GradeEnrollUserSimpleVO;
import com.aioveu.vo.user.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class GradeEnrollUserServiceImpl extends ServiceImpl<GradeEnrollUserDao, GradeEnrollUser> implements GradeEnrollUserService {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private GradeFixedUserService gradeFixedUserService;

    @Autowired
    private IUserCouponService iUserCouponService;

    @Autowired
    private GradeTemplateService gradeTemplateService;

    @Autowired
    private UserService userService;

    @Autowired
    private GradeSignEvaluateService gradeSignEvaluateService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserOpenIdService userOpenIdService;

    @Autowired
    private GradeCoachService gradeCoachService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private StoreConfigService storeConfigService;

    @Autowired
    private GradeCancelOptionsService gradeCancelOptionsService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private MQMessageService mqMessageService;

    @Autowired
    private GradeUserCouponService gradeUserCouponService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private UserCoachService userCoachService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(GradeEnrollUserForm form) {
        log.info("约课:" + JacksonUtils.obj2Json(form));
        if (form.getChildAge() == null) {
            throw new SportException("孩子年龄不能为空!");
        }
        //校验姓名合法性
        if(StringUtils.isBlank(form.getChildName())
                || StringUtils.isBlank(form.getUsername())){
            throw new SportException("请输入姓名!");
        }
        Grade grade = gradeService.getById(form.getGradeId());
        if (grade == null) {
            throw new SportException(form.getGradeId() + "班级不存在");
        }
        if (form.isHistoryAppointment() && (OauthUtils.isAdmin() || OauthUtils.isSuperAdmin())) {
            log.info("历史约课 不校验课程状态");
        } else {
            if (grade.getStatus() != 1) {
                log.warn("班级不可约:{}, 状态:{}", grade.getId(), grade.getStatus());
                throw new SportException("该班级已不可约!");
            }
            if (DateUtils.addMinutes(grade.getStartTime(), 15).before(new Date())) {
                throw new SportException("该班级预约时间已过!");
            }
        }

        form.setChildName(form.getChildName().trim());
        form.setUsername(form.getUsername().trim());

        // 预约重复检查
        QueryWrapper<GradeEnrollUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GradeEnrollUser::getGradeId, form.getGradeId())
                .eq(GradeEnrollUser::getUserId, form.getUserId())
                .eq(GradeEnrollUser::getUsername, form.getUsername())
                .eq(GradeEnrollUser::getChildName, form.getChildName())
                .eq(GradeEnrollUser::getStatus, GradeStatus.NORMAL.getCode());
        if (count(queryWrapper) > 0) {
            throw new SportException(form.getChildName() + "已预约该班级, 请勿重复预约");
        }

        // 用户归属教练 和 班级对应教练 校验
        Company company = companyService.getOneByStoreId(grade.getStoreId());
        if (company == null) {
            throw new SportException("店铺数据异常!");
        }
        StoreConfig storeConfig = storeConfigService.getCompanyStoreConfig(company.getId(),"appointment_coach_limit");
        if (storeConfig != null && Boolean.parseBoolean(storeConfig.getValue())){
            String phone = OauthUtils.getCurrentUser().getPhone();
            String belongUserId = userInfoService.getUserIdByPhoneAndCompanyId(phone, company.getId());
            if (StringUtils.isNotEmpty(belongUserId)) {
                String gradeCoachUserId = gradeService.getUserIdByGradeId(form.getGradeId());
                if (!belongUserId.equals(gradeCoachUserId)) {
                    throw new SportException("当前用户不能预约该班级所属教练的课程!");
                }
            }
        }

        // 人数限制检查
        List<GradeEnrollUser> enrollList = enrollNumber(form.getGradeId());
        if (CollectionUtils.isNotEmpty(enrollList) && enrollList.size() >= grade.getLimitNumber()) {
            throw new SportException("当前课程已约满!");
        }
        GradeTemplate gradeTemplate = gradeTemplateService.getById(grade.getGradeTemplateId());
        GradeEnrollUser enrollUser = new GradeEnrollUser();
        BeanUtils.copyProperties(form, enrollUser);
        enrollUser.setStoreId(grade.getStoreId());
        enrollUser.setCompanyId(company.getId());

        String subtitle = "";
        List<Grade> futureGradeList = null;
        if (form.getFixed() != null && form.getFixed()) {
            // 查询用户是否已固定报名
            if (!gradeFixedUserService.hasFixed(form.getUserId(), form.getChildName(), grade.getGradeTemplateId())) {
                Integer fixedCount = gradeFixedUserService.fixedUserCount(grade.getGradeTemplateId());
                if (gradeTemplate.getLimitNumber() != null && fixedCount > gradeTemplate.getLimitNumber()) {
                    log.error("固定人数已满:" + JacksonUtils.obj2Json(form));
                    throw new SportException("该班级固定人数已满, 暂不可固定!");
                }
                if (!gradeFixedUserService.fixedUser(form, grade.getGradeTemplateId())) {
                    log.error("预约固定失败:" + JacksonUtils.obj2Json(form));
                    throw new SportException("预约失败, 请稍后重试!");
                }
            } else {
                log.warn("用户:{},已固定:{}", form.getUserId(), grade.getGradeTemplateId());
            }
            subtitle = "(已固定)";

            futureGradeList = gradeService.getFutureGrade(grade.getGradeTemplateId(), grade.getEndTime());
        }

        if (!save(enrollUser)) {
            log.error("预约失败:" + JacksonUtils.obj2Json(form));
            throw new SportException("预约失败, 请稍后重试!");
        }

        // 减优惠券
        if (!iUserCouponService.gradeCheckCode(form.getUserId(), gradeTemplate.getClassHour(), grade.getId(), grade.getStoreId(),enrollUser.getId())) {
            throw new SportException("预约失败, 请稍后重试!");
        }
        // 检查剩余优惠券 如果全部使用完 将订单设置为完成
        iUserCouponService.gradeCouponAllUsed(form.getGradeId());
        String coachUserId = userCoachService.getCoachUserIdByGradeId(form.getGradeId());
        if (StringUtils.isNotEmpty(coachUserId)) {
            mqMessageService.sendUserAttributeModifyMessage(form.getPhone(), coachUserId, 1, grade.getStoreId(), "用户约课系统");
        } else {
            log.warn("班级:{}未查找到教练信息, 不更新教练归属", form.getGradeId());
        }
        // 更新私海池中用户的信息
        userInfoService.updateUserAndChildNameAndAge(form.getPhone(), form.getUsername(), form.getChildName(), form.getChildAge(), company.getId());
        // 发送约课成功消息通知
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("gradeName", grade.getName() + subtitle);
        msgMap.put("user", form.getChildName());
        msgMap.put("gradeTime", SportDateUtils.get2Day(grade.getStartTime(), grade.getEndTime()));
        msgMap.put("teacher", gradeCoachService.getByGradeId(grade.getId()).stream().map(StoreCoach::getName).collect(Collectors.joining(",")));
        mqMessageService.sendNoticeMessage(msgMap, MsgOptionEnum.USER_GRADE_ENROLL_SUCCESS.getCode(), grade.getStoreId());

        // 对于固定 如果还有其他约课 继续约
        if (CollectionUtils.isNotEmpty(futureGradeList)) {
            // 发送约课信息
            for (Grade futureGrade : futureGradeList) {
                // 对于固定报名的，需要从当前时间继续往后查询是否还有未报名的课程，有的话，自动继续报名
                GradeEnrollUserForm enrollUserForm = new GradeEnrollUserForm();
                BeanUtils.copyProperties(form, enrollUserForm);
                enrollUserForm.setFixed(false);
                enrollUserForm.setGradeId(futureGrade.getId());
                //MQ 异步约课
                mqMessageService.gradeEnroll(enrollUserForm);
            }
        }
        return true;
    }

    @Override
    public boolean helpEnroll(HelpGradeEnrollUserForm form) {
        Grade grade = gradeService.getById(form.getGradeId());
        StoreConfig storeConfig = storeConfigService.getStoreConfig("GRADE_HELP_NOT_PHONE_CODE", grade.getStoreId());
        if (storeConfig != null && Boolean.parseBoolean(storeConfig.getValue())) {
            log.info("店铺:{}已配置帮约课无需用户验证码验证", grade.getStoreId());
        } else {
            String code = noticeService.getCode(form.getPhone(), PhoneCodeConstant.CODE_TYPE_ENROLL_GRADE);
            if (StringUtils.isEmpty(code)) {
                throw new SportException("验证码已过期!");
            }else if (!code.equals(form.getCode())) {
                throw new SportException("验证码错误!");
            }
        }
        GradeEnrollUserForm gradeEnrollUserForm = new GradeEnrollUserForm();
        BeanUtils.copyProperties(form, gradeEnrollUserForm);
        //TODO 2025 事务不生效 需要修改
        boolean result = create(gradeEnrollUserForm);
        Store store = storeService.getById(grade.getStoreId());
        sendOperateLogMessage("帮约课", 0, JacksonUtils.obj2Json(form), store.getCompanyId(), grade.getStoreId(), OauthUtils.getCurrentUsername() + "帮用户约课", grade.getId() + "");
        return result;
    }

    /**
     * 操作日志
     * @param name
     * @param operateType
     * @param params
     * @param companyId
     * @param storeId
     * @param detail
     * @param productId
     */
    private void sendOperateLogMessage(String name, Integer operateType, String params, Long companyId, Long storeId, String detail, String productId) {
        LoginVal currentUser = OauthUtils.getCurrentUser();
        OperateLog operateLog = new OperateLog();
        operateLog.setName(name);
        operateLog.setOperateType(operateType);
        operateLog.setOperateTime(new Date());
        operateLog.setUserId(currentUser.getUserId());
        if (currentUser.getNickname() != null) {
            operateLog.setUsername(currentUser.getNickname());
        } else {
            operateLog.setUsername(currentUser.getUsername());
        }
        operateLog.setParams(params);
        operateLog.setRoleCode(String.join(",", OauthUtils.getCurrentUserRoles()));
        operateLog.setCompanyId(companyId);
        operateLog.setStoreId(storeId);
        operateLog.setDetail(detail);
        operateLog.setProductId(productId);
        operateLog.setCategoryCode(OperateLogCategoryEnum.GRADE.getCode());
        mqMessageService.sendOperateLogMessage(operateLog);
    }

    /**
     * 获取班级的报名人数
     * @param gradeId
     * @return
     */
    @Override
    public List<GradeEnrollUser> enrollNumber(Long gradeId) {
        QueryWrapper<GradeEnrollUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GradeEnrollUser::getGradeId, gradeId)
                .eq(GradeEnrollUser::getStatus, DataStatus.NORMAL.getCode());
        return list(queryWrapper);
    }

    @Override
    public List<GradeEnrollUser> enrollNumber(Long gradeId, List<Integer> status) {
        QueryWrapper<GradeEnrollUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GradeEnrollUser::getGradeId, gradeId)
                .in(GradeEnrollUser::getStatus, status);
        return list(queryWrapper);
    }

    @Override
    public List<GradeEnrollUserItemVO> list(String userId, Integer status, Long companyId, Long storeId) {
        return getBaseMapper().getByUserId(userId, status, companyId, storeId);
    }

    @Override
    public List<GradeUserCoupon> getGradeCoupon(String userId, Long gradeId) {
        return getBaseMapper().getGradeCoupon(userId, gradeId);
    }

    @Override
    public GradeEnrollUserDetailVO detail(Long id) {
        return getBaseMapper().detail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(Long id, String userId) {
        GradeEnrollUser gradeEnrollUser = getById(id);
        if (gradeEnrollUser == null) {
            throw new SportException("预约不存在!");
        }
        if (!gradeEnrollUser.getUserId().equals(userId)) {
            throw new SportException("当前用户没有操作权限!");
        }
        cancelCheck(gradeEnrollUser.getGradeId());
        //获取公司id
        Long companyId = gradeService.getCompanyIdByGradeId(gradeEnrollUser.getGradeId());
        boolean result = cancelGradeEnroll(id, gradeEnrollUser.getGradeId(), gradeEnrollUser.getUserId(), 0, "用户手动取消",null, companyId);
        sendOperateLogMessage("用户取消约课", 1, null, gradeEnrollUser.getCompanyId(), gradeEnrollUser.getStoreId(), "用户取消" + gradeEnrollUser.getUsername() + "的约课", gradeEnrollUser.getId() + "");
        return result;
    }

    /**
     * 取消课程检查校验
     * @param gradeId
     */
    private void cancelCheck(Long gradeId) {
        Grade grade = gradeService.getById(gradeId);
        if (grade == null) {
            throw new SportException("班级不存在!");
        }
        int value = getCancelGradeMinute(grade.getStoreId());

        Date cancelDate = DateUtils.addMinutes(new Date(), value);
        if (cancelDate.after(grade.getStartTime())) {
            throw new SportException(String.format("请提前%s分钟进行取消!", value));
        }
    }

    @Override
    public int getCancelGradeMinute(Long storeId) {
        // 默认三十分钟前
        int defaultCancelDate = 30;
        return Optional.ofNullable(storeConfigService.getStoreConfig(SysDictConstant.CANCEL_APPOINTMENT_CLASSES_TIME, storeId))
                .map(StoreConfig::getValue)
                .map(m -> NumberUtils.toInt(m, defaultCancelDate))
                .orElse(defaultCancelDate);
    }

    @Override
    public boolean cancelGradeEnroll(Long id, Long gradeId, String userId, Integer type, String reason, String gradeCancelOption) {
        return cancelGradeEnroll(id, gradeId, userId, type, reason,gradeCancelOption, null);
    }

    @Override
    public boolean cancelGradeEnroll(Long id, Long gradeId, String userId, Integer type, String reason, String gradeCancelOption, Long companyId) {
        log.info("取消:{}, 班级:{}, 用户:{}的报名, 类型:{}", id, gradeId, userId, type);
        // 取消约课 退还使用的优惠券
        List<GradeUserCoupon> gradeUserCouponList = getBaseMapper().getGradeCouponByGradeEnrollId(id, userId, gradeId);
        int couponExtendDays = 0;
        if(!StringUtils.isEmpty(gradeCancelOption)) {
            GradeCancelOptions gradeCancelOptionsByName = gradeCancelOptionsService.getGradeCancelOptionsByName(companyId, gradeCancelOption);
            if (gradeCancelOptionsByName != null) {
                couponExtendDays = gradeCancelOptionsByName.getCouponExtendDays();
            }
        }
        iUserCouponService.batchBackCoupon(gradeUserCouponList.stream().map(item -> {
            CouponChangeRecord cr = new CouponChangeRecord();
            cr.setGradeId(item.getGradeId());
            cr.setUserCouponId(item.getUserCouponId());
            return cr;
        }).collect(Collectors.toList()), couponExtendDays);
        // 删除约课的优惠券关联表
        getBaseMapper().deleteByGradeEnrollId(id, userId, gradeId);

        // 更新报名记录状态
        GradeEnrollUser gradeEnrollUser = new GradeEnrollUser();
        gradeEnrollUser.setId(id);
        if (type == 0) {
            //用户手动取消约课
            gradeEnrollUser.setStatus(2);
        } else if (type == 1) {
            gradeEnrollUser.setStatus(4);
        } else {
            gradeEnrollUser.setStatus(8);
        }

        GradeEnrollUser gu = getById(id);

        // 约课取消 订阅号通知
        Grade grade = gradeService.getById(gradeId);
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("gradeName", grade.getName());
        msgMap.put("gradeTime", SportDateUtils.get2Day(grade.getStartTime(), grade.getEndTime()));
        msgMap.put("user", gu.getChildName());
        msgMap.put("teacher", gradeCoachService.getByGradeId(grade.getId()).stream().map(StoreCoach::getName).collect(Collectors.joining(",")));
        if (reason.length() > 10) {
            msgMap.put("reason", reason.substring(0, 10) + "...");
        } else {
            msgMap.put("reason", reason + " " + "课程券已退回账户");
        }

        GradeTemplate gradeTemplate = gradeTemplateService.getById(grade.getGradeTemplateId());
        List<UserCoupon> userCoupons = userCouponService.getByUserIdAndTemplateId(userId, Arrays.stream(gradeTemplate.getCouponTemplateIds().split(",")).collect(Collectors.toList()));
        long couponNum = gradeUserCouponList.stream().filter(t -> t.getStatus().equals(1)).count();
        if (type == 0) {
            // 用户取消约课的，发送小程序消息
            msgMap.put("miniReason", reason);
            msgMap.put("tips", String.format("返还%s张课券，剩余课券%s张", couponNum, userCoupons.size()));

            // 用户自己取消的，发送小程序的通知
            Company company = companyService.getById(companyId);
            if(company != null) {
                String userOpenId = userOpenIdService.getByAppIdAndUserId(userId, company.getMiniAppId(), false);
                if (StringUtils.isNotEmpty(userOpenId)) {
                    msgMap.put("miniOpenId", userOpenId);
                }
            }
        } else {
            Store store = storeService.getById(grade.getStoreId());
            UserVo userVO = userService.getByUserId(userId);
            msgMap.put("smsTime", DateFormatUtils.format(grade.getStartTime(), "yyyy-MM-dd HH:mm"));
            msgMap.put("smsReason", reason);
            msgMap.put("storeName", store.getName());
            msgMap.put("couponNum", couponNum);
            msgMap.put("day", couponExtendDays);
            msgMap.put("remainNum", userCoupons.size());
            msgMap.put("phone", userVO.getPhone());
        }
        mqMessageService.sendNoticeMessage(msgMap, MsgOptionEnum.USER_GRADE_ENROLL_CANCEL.getCode(), grade.getStoreId());
        return updateById(gradeEnrollUser);
    }

    @Override
    public boolean batchSaveGradeCouponList(List<GradeUserCoupon> list) {
        return getBaseMapper().batchSaveGradeCouponList(list) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelAll(Long id) {
        GradeEnrollUser gradeEnrollUser = getById(id);
        if (gradeEnrollUser == null) {
            throw new SportException("预约不存在!");
        }
        Grade grade = gradeService.getById(gradeEnrollUser.getGradeId());
        String userId = OauthUtils.getCurrentUserId();
        if (cancel(id, userId) && gradeFixedUserService.cancelFixedByUsernameAndTemplate(userId, gradeEnrollUser.getChildName(), grade.getGradeTemplateId())) {
            return true;
        }
        throw new SportException("操作失败, 请稍后重试！");
    }

    @Override
    public List<GradeEnrollUserSimpleVO> getByGradeId(Long gradeId, String userId) {
        List<GradeEnrollUserSimpleVO> gradeEnrollUserList = getBaseMapper().getByGradeId(gradeId);
        if (CollectionUtils.isNotEmpty(gradeEnrollUserList) && userId != null) {
            List<String> roleList = roleUserService.getByUserId(userId);
            if (CollectionUtils.isNotEmpty(roleList) && roleList.stream().anyMatch(s -> s.startsWith("admin"))) {

            } else {
                for (GradeEnrollUserSimpleVO item : gradeEnrollUserList) {
                    String phone = item.getPhone();
                    item.setPhone(phone.substring(0, 3) + "****" + phone.substring(7));
                }
            }
        }
        return gradeEnrollUserList;
    }

    @Override
    public List<GradeEnrollUserSimpleVO> getEvaluateListByGradeId(Long gradeId) {
        return getBaseMapper().getEvaluateListByGradeId(gradeId);
    }

    @Override
    public boolean sign(Long id, String userId) {
        GradeEnrollUser gradeEnrollUser = getById(id);
        if (gradeEnrollUser == null) {
            throw new SportException("未查询到报名记录, 请检查!");
        }
        return gradeSignEvaluateService.sign(gradeEnrollUser.getGradeId(), gradeEnrollUser.getId(), gradeEnrollUser.getUserId(), userId);
    }

    @Override
    public boolean evaluate(Long id, String message, String userId) {
        GradeEnrollUser gradeEnrollUser = getById(id);
        if (gradeEnrollUser == null) {
            throw new SportException("未查询到报名记录, 请检查!");
        }
        return gradeSignEvaluateService.evaluate(gradeEnrollUser.getId(), message, userId);
    }

    @Override
    public void gradeFinish(Long id) {
        Grade grade = gradeService.getById(id);
        if (grade == null) {
            log.error("班级:{}不存在", id);
            return;
        }
        if (grade.getStatus() != 1) {
            log.error("班级:{}状态异常:{}", id, grade.getStatus());
            return;
        }
        // date1 > date2 返回 true
        if (grade.getEndTime().after(new Date())) {
            // 11:30 11:20
            log.error("班级:{}还未到完成时间, 更新消息发送时间错误", id);
            return;
        }
        // 班级状态更新为完成
        Grade gd = new Grade();
        gd.setId(id);
        gd.setStatus(GradeStatus.FINISH.getCode());
        gradeService.updateById(gd);

        QueryWrapper<GradeEnrollUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GradeEnrollUser::getGradeId, id)
                .eq(GradeEnrollUser::getStatus, DataStatus.NORMAL.getCode());
        List<GradeEnrollUser> gradeEnrollUserList = list(queryWrapper);
        if (CollectionUtils.isNotEmpty(gradeEnrollUserList)) {
            log.info("班级:{}, 报名人数:{}标记为完成", id, gradeEnrollUserList.size());
            // 更新课程报名状态为完成
            List<GradeEnrollUser> upList = new ArrayList<>(gradeEnrollUserList);
            for (GradeEnrollUser item : gradeEnrollUserList) {
                GradeEnrollUser gu = new GradeEnrollUser();
                gu.setId(item.getId());
                gu.setStatus(3);
                upList.add(gu);
            }
            updateBatchById(upList);
            // 更新课券状态
            gradeUserCouponService.gradeUserCoupon2Use(id);
            // 更新订单状态
            List<String> orderIdList = getBaseMapper().getGradeOrderId(id);
            if (CollectionUtils.isNotEmpty(orderIdList)){
                log.info("班级:{} 待更新订单:{}", id, orderIdList.size());
                orderService.toUsed(orderIdList);
            }
        } else {
            log.info("班级:{}没有需要设置为完成状态的报名记录", id);
        }
    }

    @Override
    public List<UserCoupon> getGradeUsedCouponList(Date start, Date end, Long storeId, String coachUserId) {
        return getBaseMapper().getGradeUsedCouponList(start, end, storeId, coachUserId);
    }

    @Override
    public List<UserCoupon> getStoreUnUsedCouponList(Long storeId) {
        return getBaseMapper().getStoreUnUsedCouponList(storeId);
    }

    @Override
    public Date getLastClassTime(String userId) {
        return getBaseMapper().getLastClassTime(userId);
    }

    @Autowired
    private IUserCouponService userCouponService;

    @Override
    public void syncGradeCoupon() {
        List<String> userIdList = getBaseMapper().getGradeEnrollUserIdList();
//        List<String> userIdList = Arrays.asList("d20fd57786e2f186df178e9e0fd6cac1");
        log.info("总共约课用户:{}", userIdList.size());
        for (String userId : userIdList) {
            log.info("开始处理:{}", userId);
            QueryWrapper<GradeEnrollUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(GradeEnrollUser::getUserId, userId)
                    .in(GradeEnrollUser::getStatus, 1, 3);
            List<GradeEnrollUser> usedGradeList = list(queryWrapper);
            log.info("用户:{}, 已约课:{}", userId, usedGradeList.size());
            if (CollectionUtils.isEmpty(usedGradeList)) {
                log.warn("用户:{}, 未约课", userId);
                continue;
            }
            QueryWrapper<UserCoupon> ucQuery = new QueryWrapper<>();
            ucQuery.lambda().eq(UserCoupon::getUserId, userId)
                    .eq(UserCoupon::getStatus, 3);
            List<UserCoupon> usedUcList = userCouponService.list(ucQuery);
            log.info("用户:{}, 已使用的券:{}", userId, usedUcList.size());
            int buUc = usedGradeList.size() - usedUcList.size();
            log.warn("用户:{}, 需要补券:{}", userId, buUc);
            if (buUc > 0) {
                QueryWrapper<UserCoupon> usableQuery = new QueryWrapper<>();
                usableQuery.lambda().eq(UserCoupon::getUserId, userId)
                        .eq(UserCoupon::getStatus, 1);
                List<UserCoupon> usableUcList = userCouponService.list(usableQuery);
                log.info("用户:{}, 未使用的券:{}", userId, usableUcList.size());
                if (usableUcList.size() < buUc) {
                    log.error("用户:{}, 未使用的券数量不够", userId);
                    continue;
                }
                List<UserCoupon> buUcList = usableUcList.subList(0, buUc);
                List<UserCoupon> buUpdateList = buUcList.stream().map(item -> {
                    UserCoupon updateUc = new UserCoupon();
                    updateUc.setId(item.getId());
                    updateUc.setStatus(UserCouponStatus.USED);
                    return updateUc;
                }).collect(Collectors.toList());
                if (userCouponService.updateBatchById(buUpdateList)) {
                    log.info("用户:{}, 优惠券同步完毕", userId);
                }
            } else {
                log.warn("用户:{}, 不需要补券", userId);
            }
        }
    }

    @Override
    public List<UserCoupon> getUsedCouponListByGradeId(Date start, Date end, List<Long> gradeList) {
        return getBaseMapper().getUsedCouponListByGradeId(gradeList);
    }

    @Override
    public List<AnalysisCouponVO> getAnalysisCancelGradeCoupon(Date start, Date end, List<Long> gradeList) {
        return getBaseMapper().getAnalysisCancelGradeCoupon(start, end, gradeList);
    }

    @Override
    public boolean helpCancelEnroll(HelpCancelGradeEnrollUserForm form) {
        GradeEnrollUser gradeEnrollUser = getById(form.getGradeEnrollUserId());
        StoreConfig storeConfig = storeConfigService.getStoreConfig("GRADE_HELP_CANCEL_NOT_PHONE_CODE", gradeEnrollUser.getStoreId());
        if (storeConfig != null && Boolean.parseBoolean(storeConfig.getValue())) {
            log.info("店铺:{}已配置帮取消课程无需用户验证码验证", gradeEnrollUser.getStoreId());
        } else {
            String code = noticeService.getCode(form.getPhone(), PhoneCodeConstant.CODE_TYPE_CANCEL_GRADE);
            if (StringUtils.isEmpty(code)) {
                throw new SportException("验证码已过期!");
            }else if (!code.equals(form.getCode())) {
                throw new SportException("验证码错误!");
            }
        }
        cancelCheck(gradeEnrollUser.getGradeId());

        if(form.getType() == 1 || form.getType() == 3) {
            cancelGradeEnroll(form.getGradeEnrollUserId(), gradeEnrollUser.getGradeId(),
                    gradeEnrollUser.getUserId(), 2, "老师/教练帮忙取消", null);
        }
        if(form.getType() == 2 || form.getType() == 3) {
            User user = userService.getByUserPhone(form.getPhone());
            gradeFixedUserService.cancelFixed(form.getFixedId(), user.getId());
        }
        sendOperateLogMessage("帮取消约课", 1, JacksonUtils.obj2Json(form), gradeEnrollUser.getCompanyId(), gradeEnrollUser.getStoreId(), "教练/老师帮取消" + gradeEnrollUser.getUsername() + "的约课", gradeEnrollUser.getId() + "");
        return true;
    }

    @Override
    public List<UserCoupon> getCoachVerifyList(Date start, Date end, Long storeId, Long coachId) {
        return getBaseMapper().getCoachVerifyList(start, end, storeId, coachId);
    }

    @Override
    public List<GradeWeekEnrollUserDTO> getTimeRangeEnrollList(Date start, Date end) {
        return getBaseMapper().getTimeRangeEnrollList(start, end);
    }

    @Override
    public boolean editChildName(Long id, String childName) {
        if (StringUtils.isBlank(childName)){
            throw new SportException("姓名不能为空，请重新输入!");
        }
        if( !StringValidatorUtils.validateName(childName.trim())){
            throw new SportException("姓名只支持中英文，请重新输入!");
        }
        GradeEnrollUser gradeEnrollUser = new GradeEnrollUser();
        gradeEnrollUser.setId(id);
        gradeEnrollUser.setChildName(childName.trim());

        // 修改报名孩子姓名 同步更新用户表中的孩子姓名
        GradeEnrollUser eu = getById(id);
        if (eu != null) {
            userInfoService.updateChildNameByPhoneAndCompanyId(eu.getPhone(), eu.getCompanyId(), childName);
        }
        return updateById(gradeEnrollUser);
    }

    @Override
    public List<GradeEnrollUserDTO> getUserEnroll4TimeRange(String start, String end, String userId, String name) {
        return getBaseMapper().getUserEnroll4TimeRange(start, end, userId, name);
    }

    @Override
    public int getGradeEnrollUserNum(Set<Long> gradeIdSet) {
        QueryWrapper<GradeEnrollUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(GradeEnrollUser::getGradeId, gradeIdSet)
                .in(GradeEnrollUser::getStatus, 1, 3);
        return count(queryWrapper);
    }
}
