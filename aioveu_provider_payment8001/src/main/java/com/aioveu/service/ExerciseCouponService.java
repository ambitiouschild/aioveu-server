package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.ExerciseCoupon;
import com.aioveu.vo.ExerciseCouponSimpleVO;
import com.aioveu.vo.ExerciseCouponVO;
import com.aioveu.vo.TemplateRule;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface ExerciseCouponService extends IService<ExerciseCoupon> {

    /**
     * 根据活动id赠送用户优惠券
     * @param exerciseId
     * @param userId
     * @param categoryId
     * @param orderId
     * @return
     */
    Map<String, String> sendUserCouponByExerciseId(Long exerciseId, String userId, Long categoryId, String orderId);

    /**
     * 领取优惠券并发送小程序通知消息
     * @param exerciseId
     * @param userId
     * @param categoryId
     * @param orderId
     * @param orderName
     */
    void sendAndNoticeUserCouponByExerciseId(Long exerciseId, String userId, Long categoryId, String orderId, String orderName);

    /**
     * 批量保存
     * @param exerciseId
     * @param categoryId
     * @param couponTemplateIds
     * @return
     */
    boolean batchSave(Long exerciseId, Long categoryId, List<Long> couponTemplateIds);

    /**
     * 获取列表
     * @param page
     * @param size
     * @param exerciseId
     * @return
     */
    IPage<ExerciseCouponVO> getManagerAll(int page, int size, Long exerciseId);

    /**
     * 根据优惠券id删除数据
     * @param id
     * @return
     */
    boolean deleteExercise(Long id);

    /**
     * 获取活动的优惠券配置
     * @param exerciseCoupon
     * @return
     */
    TemplateRule.Expiration getCouponExpiration(ExerciseCoupon exerciseCoupon);

    /**
     * 批量保存
     * @param exerciseCouponFormList
     * @return
     */
    boolean batchSave(List<ExerciseCoupon> exerciseCouponFormList);

    /**
     * 根据活动id删除对应的优惠券模板
     * @param exerciseId
     * @return
     */
    boolean deleteByExerciseId(Long exerciseId);

    /**
     * 通过课程id获取优惠券
     * @param exerciseId
     * @return
     */
    List<ExerciseCouponSimpleVO> getByExerciseId(Long exerciseId);


    /**
     * 送优惠券
     * @param couponTemplateId
     * @param count
     * @param userId
     * @param exerciseId
     * @param categoryId
     * @param amount
     * @param orderId
     */
    void sendCoupon(Long couponTemplateId, Integer count, String userId, Long exerciseId,
                    Long categoryId, BigDecimal amount, String orderId);

    /**
     * 根据活动id赠送相应的优惠券
     * @param exerciseId
     * @param userId
     * @param categoryId
     * @return
     */
    boolean sendCouponByExercise(Long exerciseId, String userId, Long categoryId);
}
