package com.aioveu.controller;

import com.aioveu.constant.SysDictConstant;
import com.aioveu.entity.MessageOption;
import com.aioveu.entity.Store;
import com.aioveu.entity.StoreConfig;
import com.aioveu.enums.MsgOptionEnum;
import com.aioveu.enums.StoreCoachTypeEnum;
import com.aioveu.service.*;
import com.aioveu.utils.DateUtil;
import com.aioveu.vo.StatisticsVo;
import com.aioveu.vo.StoreCoachVO;
import com.aioveu.vo.UserCouponTotalVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/scheduled-task")
public class ScheduledTaskController {

    @Autowired
    private CouponTemplateService couponTemplateService;

    @Autowired
    private UserReceiveCallService userReceiveCallService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private IUserCouponService iUserCouponService;

    @Autowired
    private ExerciseCountTemplateService exerciseCountTemplateService;

    @Autowired
    private FieldPlanService fieldPlanService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private StoreConfigService storeConfigService;

    @Autowired
    private MQMessageService mqMessageService;

    @Autowired
    private WxOpenAuthorizerService wxOpenAuthorizerService;

    @Autowired
    private MessageOptionService messageOptionService;

    @Autowired
    private StoreCoachService storeCoachService;

    @Autowired
    private StoreService storeService;

    /**
     * 自动清理30天未联系的用户 每天凌晨1点钟执行
     * @return
     */
    @GetMapping("/remove-30-day-un-contact")
    public boolean remove30DayUnContact() {
        return userReceiveCallService.autoRemove30DayUnContact();
    }

    /**
     * 新增新的周期班级 每天凌晨2点钟执行
     * @return
     */
    @GetMapping("/expiry-grade-add-new")
    public boolean expiryGradeAddNew() {
        return gradeService.gradeAddNew();
    }

    /**
     * 过期场地下架 增加新的场地 每天凌晨2点半钟执行
     * @return
     */
    @GetMapping("/expiry-field-flan-add-new")
    public boolean expiryFieldPlanAddNew() {
        return fieldPlanService.expiryFieldPlanAddNew();
    }

    /**
     * 计次活动模板定时更新 每天凌晨1点10分钟执行
     * @return
     */
    @GetMapping("/periodic-update")
    public boolean periodicUpdate() {
        return exerciseCountTemplateService.periodicUpdate();
    }

    /**
     * 每天检查未完成的班级 自动完成 每天4点钟
     * @return
     */
    @GetMapping("/check-grade-finish")
    public String checkGradeFinish() {
        log.info("班级自动完成检查开始");
        gradeService.checkGradeFinish();
        return "执行完成";
    }

    /**
     * 检查用户的优惠券是否过期 每天3点
     * @return
     */
    @GetMapping("/check-validity")
    public String checkValidity() {
        iUserCouponService.checkValidity();
        return "执行完成";
    }

    /**
     * 上课提醒 每周一8点钟
     * @return
     */
    @GetMapping("/every-week-grade-tips")
    public boolean everyWeekGradeTips() {
        return noticeService.everyWeekGradeTips();
    }

    /**
     * 优惠券模板过期检查 每天凌晨3点半检查
     * @return
     */
    @GetMapping("/offline-coupon-template")
    public String offlineCouponTemplate() {
        couponTemplateService.offlineCouponTemplate();
        return "执行完成";
    }

    /**
     * 每周提醒一次，配置提醒的课券阈值，不低于1，课券数低于阈值就发送提醒，在商家端经营提醒里面能看到提醒记录，课券数量为0不提醒
     * 0<课券数<=阈值
     */
    @GetMapping("/coupon-num-notice")
    public String couponNumBelowThresholdNotice() {
        List<StoreConfig> storeConfigs = storeConfigService.getAllStoreDictByCode(SysDictConstant.COUPONS_BELOW_THRESHOLD);
        String finishStr = "执行完成";
        if (CollectionUtils.isEmpty(storeConfigs)){
            log.info("课券数不足通知未配置");
            return finishStr;
        }
        for( StoreConfig storeConfig : storeConfigs){
            Integer value = 0;
            try {
                value = Integer.valueOf(storeConfig.getValue());
                if (value < 1) {
                    log.info("课券数提醒阈值小于1:{}", storeConfig.getId());
                    continue;
                }
            } catch (Exception e) {
                log.error("课券数提醒阈值配置不规范{},{}：", storeConfig.getId(), storeConfig.getValue());
                continue;
            }
            //获取用户课券数小于阈值的list
            List<UserCouponTotalVO> userCouponTotalVOS = iUserCouponService.queryCouponsBelowThreshold(storeConfig.getStoreId(), value);
            if (CollectionUtils.isEmpty(userCouponTotalVOS)){
                log.info("门店：{},未没有客户课券数低于阈值：{}", storeConfig.getStoreId(), value);
                return finishStr;
            }
            List<String> userIds = userCouponTotalVOS.stream().map(UserCouponTotalVO::getUserId).collect(Collectors.toList());
            //获取用户教练、销售、约课姓名
            List<UserCouponTotalVO> nameInfos = iUserCouponService.queryUserReceiveCallByUserIds(userCouponTotalVOS.get(0).getCompanyId(), storeConfig.getStoreId(), userIds);

            userCouponTotalVOS.stream().forEach(item -> {
                UserCouponTotalVO vo = nameInfos.stream().filter(p -> p.getUserId().equals(item.getUserId())).findFirst().orElse(null);
                String seaPoolCoachName = StringUtils.isBlank(vo.getSeaPoolCoachName()) ? "无" : vo.getSeaPoolCoachName();
                String seaPoolSaleName = StringUtils.isBlank(vo.getSeaPoolSaleName()) ? "无" : vo.getSeaPoolSaleName();
                String name = StringUtils.isBlank(vo.getName()) ? "无" : vo.getName();

                Map<String, Object> msgMap = new HashMap<>();
                msgMap.put("storeId", storeConfig.getStoreId());
                msgMap.put("userId",item.getUserId());
                msgMap.put("userName", name + " " + item.getPhone());
                msgMap.put("couponNum", item.getCouponNum());
                msgMap.put("coachName", seaPoolCoachName);
                msgMap.put("saleName", seaPoolSaleName);
                mqMessageService.sendNoticeMessage(msgMap, MsgOptionEnum.USER_COUPON_LESS.getCode(), storeConfig.getStoreId());
            });
        }
        return finishStr;
    }

    /**
     * 微信开放平台，刷新授权authorizer_access_token，authorizer_access_token有2个小时有效期，如果过期后如果不刷新，则接口调用不通，需要重新授权
     * 每隔10分钟查询一次是否有即将过期的授权令牌，即将过期，则刷新令牌
     * 当前时间-令牌时间 <= 令牌有效时间-10*60
     *
     */
    @GetMapping("/wx-open/refresh-auth-token")
    public String refreshAuthToken() {
        wxOpenAuthorizerService.refreshAuthToken(null,null);
        return "success";
    }

    /**
     * 每天晚上20点统计当天未签到的数量，并已服务号消息形式，发送给对应教练
     * @return
     * @throws ParseException
     */
    @GetMapping("/unsign-notice")
    public String unSignNotification() throws ParseException {
        log.info("教练未签到结果统计开始");
        List<MessageOption> list = messageOptionService.getListByCode(MsgOptionEnum.UN_SIGN_MSG.getCode());
        if (CollectionUtils.isEmpty(list)){
            return "执行完成";
        }
        Date startDate = DateUtil.getDayByString(DateUtil.getToday());
        Date endDate = DateUtil.getDayByString(DateUtil.getTomorrowDay());

        for (int i = 0; i < list.size(); i++) {
            List<StoreCoachVO> coachList = storeCoachService.getListByStoreId(list.get(i).getStoreId(), StoreCoachTypeEnum.COACH_TYPE.getCode());
            if (CollectionUtils.isEmpty(coachList)){
                continue;
            }
            Map<Long, StoreCoachVO> storeCoachVOMap = coachList.stream().collect(Collectors.toMap(StoreCoachVO::getId, User -> User));

            List<StatisticsVo> totalMap = gradeService.getUnSignNumsByStoreId(list.get(i).getStoreId(), startDate, endDate);
            if (totalMap == null || totalMap.isEmpty()){
                continue;
            }
            Store store = storeService.getById(list.get(i).getStoreId());
            for (StatisticsVo statisticsVo : totalMap){
                if (storeCoachVOMap.containsKey(statisticsVo.getId())){
                    StoreCoachVO storeCoachVO = storeCoachVOMap.get(statisticsVo.getId());
                    Map<String, Object> msgMap = new HashMap<>();
                    msgMap.put("storeId", list.get(i).getStoreId());
                    msgMap.put("storeName", store.getName());
                    msgMap.put("userId",storeCoachVO.getUserId());
                    msgMap.put("coachName", storeCoachVO.getName());
                    msgMap.put("unSignNum", statisticsVo.getTotal() +"人");
                    mqMessageService.sendNoticeMessage(msgMap, MsgOptionEnum.UN_SIGN_MSG.getCode(), list.get(i).getStoreId());
                }
            }
        }
        log.info("教练未签到结果统计结束");
        return "执行完成";
    }
}
