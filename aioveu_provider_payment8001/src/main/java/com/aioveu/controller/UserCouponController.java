package com.aioveu.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.entity.UserCoupon;
import com.aioveu.form.CouponConvertForm;
import com.aioveu.form.OrderUserCouponForm;
import com.aioveu.form.SettlementForm;
import com.aioveu.service.IUserCouponService;
import com.aioveu.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * @description 用户服务
 * @author: 雒世松
 * @date: 2025/2/1 0001 16:39
 */
@Slf4j
@RequestMapping("/api/v1/user-coupon")
@RestController
public class UserCouponController {

    /**
     * 用户服务接口
     */
    private final IUserCouponService userCouponService;

    public UserCouponController(IUserCouponService userCouponService) {
        this.userCouponService = userCouponService;
    }

    @PostMapping("")
    public List<Long> receive(@RequestParam String userId, @RequestParam Long couponTemplateId) {
        return userCouponService.acquireByCouponTemplateId(userId, couponTemplateId, null, new BigDecimal(0), null, null, true);
    }

    /**
     * 根据优惠券状态查询用户优惠券记录
     * @param userId
     * @param status
     * @return
     */
    @GetMapping("/coupons")
    public List<UserCoupon> findCouponByStatus(
            @RequestParam("userId") String userId,
            @RequestParam("status") Integer status)  {
        log.info("Find Coupons By Status: {}, {}", userId, status);
        return userCouponService.findUserCouponsByStatus(userId, status);
    }

    @GetMapping("/status-list")
    public List<UserCouponVO> findByStatus(@RequestParam("userId") String userId,
                                           @RequestParam("companyId") Long companyId,
                                           @RequestParam("status") Integer status,
                                           @RequestParam(value = "productLineId", required = false) Integer productLineId,
                                           @RequestParam(value = "categoryId", required = false) Long categoryId,
                                           @RequestParam(value = "productId", required = false) Long productId) {
        return userCouponService.findByStatus(userId, companyId, status, categoryId, productId, productLineId);
    }

    @GetMapping("/claim-coupons")
    public boolean claimCoupons(@RequestParam Long id) {
        return userCouponService.claimCoupons(id, OauthUtils.getCurrentUsername());
    }

    @GetMapping("detail/{id}")
    public UserCouponDetailVO detail(@PathVariable Long id) {
        return userCouponService.detail(id);
    }

    /**
     * 根据用户id查找当前可以领取的优惠券模板
     * @param userId
     * @return
     */
    @GetMapping("/template")
    public List<CouponTemplateSDK> findAvailableTemplate(
            @RequestParam("userId") String userId) {
        log.info("Find Available Template: {}", userId);
        return userCouponService.findAvailableTemplate(userId);
    }

    /**
     * 用户领取优惠券
     * @param request
     * @return
     */
    @PostMapping("/acquire/template")
    public List<UserCoupon> acquireTemplate(@RequestBody AcquireTemplateRequest request){
        log.info("Acquire Template: {}", JSON.toJSONString(request));
        return userCouponService.acquireTemplate(request);
    }

    /**
     * 用户结算优惠券
     * @param settlementForm
     * @return
     */
    @PostMapping("/pre-settlement")
    public SettlementResultVO settlement(@RequestBody SettlementForm settlementForm) {
        log.info("Settlement: {}", JSON.toJSONString(settlementForm));
        return userCouponService.preSettlement(settlementForm);
    }

    @GetMapping("/use-expire")
    public List<UserCouponVO> useExpire(@RequestParam("userId") String userId) {
        return userCouponService.useExpire(userId);
    }

    @PostMapping("/code")
    public Boolean receive(@RequestParam String code) {
        return userCouponService.receiveShareCouponByCode(OauthUtils.getCurrentUsername(), code);
    }

    @PostMapping("/order")
    public Boolean orderCouponAdd(@Valid @RequestBody OrderUserCouponForm form) {
        return userCouponService.orderCouponAdd(form);
    }

    @GetMapping("/order")
    public List<OrderCouponVO> orderCoupon(@RequestParam(required = false) String phone) {
        return userCouponService.getOrderCoupon(phone);
    }

    @GetMapping("/order/{orderId}")
    public List<UserCouponVO> orderCouponList(@PathVariable String orderId) {
        return userCouponService.getOrderCouponList(orderId);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return userCouponService.removeById(id);
    }

    /**
     * 管理端，管理端，客户列表-学员列表数据查询
     * 用户优惠券、班级教练明细
     * @param page
     * @param size
     * @param phone
     * @param userName
     * @param templateId
     * @param status
     * @param countFrom
     * @param countTo
     * @param orderStatus
     * @param orderStatusDesc
     * @param storeId
     * @param coachId
     * @return
     */
    @GetMapping("total-list")
    public IPage<UserCouponTotalVO> totalList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                              @RequestParam(required = false, defaultValue = "10") Integer size,
                                              @RequestParam(required = false) String phone,
                                              @RequestParam(required = false) String userName,
                                              @RequestParam(required = false) Long templateId,
                                              @RequestParam(required = false) Integer status,
                                              @RequestParam(required = false) Integer countFrom,
                                              @RequestParam(required = false) Integer countTo,
                                              @RequestParam(required = false) Integer orderStatus,
                                              @RequestParam(required = false) String orderStatusDesc,
                                              @RequestParam Long storeId,
                                              @RequestParam(required = false) String coachId) {
        return userCouponService.getTotalList(page, size, phone, userName, templateId, status, countFrom, countTo, storeId, orderStatus, orderStatusDesc,coachId);
    }

    @PostMapping("/send-exercise")
    public boolean sendByExercise(@RequestParam String userId, @RequestParam Long exerciseId) {
        return userCouponService.sendByExercise(userId, exerciseId);
    }

    @PostMapping("/expire-validity")
    public boolean expireValidity(@RequestParam String userId, @RequestParam Long couponId, @RequestParam int couponType, @RequestParam String day, @RequestParam Integer num, @RequestParam(required = false) Integer changeStatus, @RequestParam(required = false) String userCouponIds) {
        return userCouponService.setExpireValidity(userId, couponId, couponType, day, num, changeStatus, userCouponIds);
    }

    @PutMapping("/back")
    public boolean back(@RequestParam Long userCouponId, @RequestParam(required = false) Long gradeId, @RequestParam(required = false, defaultValue = "0") Integer extendDay) {
        return userCouponService.couponBack(userCouponId, gradeId, extendDay);
    }

    @PostMapping("/convert")
    public boolean couponConvert(@Valid @RequestBody CouponConvertForm form) {
        return userCouponService.couponConvert(form);
    }

}
