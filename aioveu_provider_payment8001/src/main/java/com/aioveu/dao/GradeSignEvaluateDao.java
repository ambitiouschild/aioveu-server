package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.dto.GradeUserEvaluateDTO;
import com.aioveu.entity.GradeEnrollUser;
import com.aioveu.entity.GradeSignEvaluate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface GradeSignEvaluateDao extends BaseMapper<GradeSignEvaluate> {

    /**
     * 获取班级课评消息
     * @param id
     * @return
     */
    GradeUserEvaluateDTO getUserGradeEvaluate(Long id);

    /**
     * 获取课程未签到的用户报名信息
     * @param gradeId
     * @return
     */
    List<GradeEnrollUser> getUnSignUser(Long gradeId);

    /**
     * 传入课券id，获取课程中已签到的课券id
     * @param userCouponIds  课券id
     * @return
     */
    Set<Long> getSignUserCouponIds(Set<Long> userCouponIds);

}
