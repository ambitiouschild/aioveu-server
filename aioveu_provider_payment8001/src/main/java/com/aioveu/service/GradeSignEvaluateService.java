package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.GradeSignEvaluate;

import java.util.Set;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface GradeSignEvaluateService extends IService<GradeSignEvaluate> {

    /**
     * 签到
     * @param gradeId
     * @param gradeEnrollUserId
     * @param userId
     * @param signUserId
     * @return
     */
    boolean sign(Long gradeId, Long gradeEnrollUserId, String userId, String signUserId);

    /**
     * 点评
     * @param gradeEnrollUserId
     * @param message
     * @param evaluateUserId
     * @return
     */
    boolean evaluate(Long gradeEnrollUserId, String message, String evaluateUserId);

    /**
     * 班级未签到用户优惠券退还
     * @param gradeId
     * @param username
     * @return
     */
    boolean unSignBack(Long gradeId, String username);

    /**
     * 获取已上课班级签到人数
     * @param gradeIdSet
     * @return
     */
    int getGradeSignUserNum(Set<Long> gradeIdSet);

    /**
     * 获取已上课班级评论人数
     * @param gradeIdSet
     * @return
     */
    int getGradeCommentUserNum(Set<Long> gradeIdSet);

    /**
     * 获取有签到的班级数量
     * @param gradeIdSet
     * @return
     */
    int getHasSignUserGradeNum(Set<Long> gradeIdSet);

    /**
     * 传入课券id，获取课程中已签到的课券id
     * @param userCouponIds  课券id
     * @return
     */
    Set<Long> getSignUserCouponIds(Set<Long> userCouponIds);

}
