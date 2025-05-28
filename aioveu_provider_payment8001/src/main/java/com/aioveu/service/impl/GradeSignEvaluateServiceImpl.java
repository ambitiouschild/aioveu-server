package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.dao.GradeSignEvaluateDao;
import com.aioveu.dto.GradeUserEvaluateDTO;
import com.aioveu.entity.Grade;
import com.aioveu.entity.GradeEnrollUser;
import com.aioveu.entity.GradeSignEvaluate;
import com.aioveu.enums.MsgOptionEnum;
import com.aioveu.exception.SportException;
import com.aioveu.service.*;
import com.aioveu.utils.SportDateUtils;
import com.aioveu.vo.user.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class GradeSignEvaluateServiceImpl extends ServiceImpl<GradeSignEvaluateDao, GradeSignEvaluate>  implements GradeSignEvaluateService {

    @Autowired
    private UserService userService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private GradeEnrollUserService gradeEnrollUserService;

    @Autowired
    private MQMessageService mqMessageService;

    @Override
    public boolean sign(Long gradeId, Long gradeEnrollUserId, String userId, String signUserId) {
        QueryWrapper<GradeSignEvaluate> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GradeSignEvaluate::getGradeId, gradeId)
                .eq(GradeSignEvaluate::getGradeEnrollUserId, gradeEnrollUserId);

        if (getOne(queryWrapper) != null) {
            throw new SportException("您已签到，不可重复签到!");
        }
        Grade grade = gradeService.getById(gradeId);
        if (!DateUtils.isSameDay(grade.getStartTime(), new Date())) {
            if (OauthUtils.getCurrentUserRoles().stream().anyMatch(s -> s.startsWith("admin"))) {
                log.info("管理员不做签到时间校验");
            } else {
                throw new SportException("非课程当天时间, 不可签到!");
            }
        }
        GradeSignEvaluate gradeSignEvaluate = new GradeSignEvaluate();
        gradeSignEvaluate.setGradeId(gradeId);
        gradeSignEvaluate.setGradeEnrollUserId(gradeEnrollUserId);
        gradeSignEvaluate.setUserId(userId);
        gradeSignEvaluate.setSignUserId(signUserId);
        return save(gradeSignEvaluate);
    }

    @Override
    public boolean evaluate(Long gradeEnrollUserId, String message, String evaluateUserId) {
        if (StringUtils.isEmpty(message)) {
            throw new SportException("点评内容不能为空!");
        }
        QueryWrapper<GradeSignEvaluate> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GradeSignEvaluate::getGradeEnrollUserId, gradeEnrollUserId);
        GradeSignEvaluate record = getOne(queryWrapper);
        if (record == null) {
            throw new SportException("您还未签到，不可点评!");
        }
        GradeSignEvaluate gradeSignEvaluate = new GradeSignEvaluate();
        gradeSignEvaluate.setId(record.getId());
        gradeSignEvaluate.setEvaluate(message);
        gradeSignEvaluate.setStatus(2);
        gradeSignEvaluate.setEvaluateUserId(evaluateUserId);

        if (updateById(gradeSignEvaluate)) {
            GradeEnrollUser gradeEnrollUser = gradeEnrollUserService.getById(gradeEnrollUserId);
            if (gradeEnrollUser.getStoreId() != null) {
                GradeUserEvaluateDTO userGradeEvaluate = getBaseMapper().getUserGradeEvaluate(gradeSignEvaluate.getId());
                Map<String, Object> msgMap = new HashMap<>();
                msgMap.put("gradeName", userGradeEvaluate.getGrade());
                msgMap.put("time", SportDateUtils.get2Day(userGradeEvaluate.getStartTime(), userGradeEvaluate.getEndTime()));
                msgMap.put("user", userGradeEvaluate.getUsername());
                String evaluate = "";
                if (userGradeEvaluate.getEvaluate().length() > 10) {
                    evaluate = userGradeEvaluate.getEvaluate().substring(0, 10) + "...";
                } else {
                    evaluate += userGradeEvaluate.getEvaluate();
                }
                msgMap.put("teacher", userGradeEvaluate.getTeacher() + ":" + evaluate);
                mqMessageService.sendNoticeMessage(msgMap, MsgOptionEnum.GRADE_EVALUATE_MSG.getCode(), gradeEnrollUser.getStoreId());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean unSignBack(Long gradeId, String username) {
        UserVo user = userService.findByUsername(username);
        List<String> roles = user.getRoles();
        if (CollectionUtils.isEmpty(roles) || !roles.stream().anyMatch(s -> s.startsWith("admin"))) {
            throw new SportException("当前用户没有操作权限！");
        }
        List<GradeEnrollUser> unSignUser = getBaseMapper().getUnSignUser(gradeId);
        if (CollectionUtils.isNotEmpty(unSignUser)) {
            log.info("本次课程:{}, 未签到用户:{}", gradeId, unSignUser.size());
            for (GradeEnrollUser gu : unSignUser) {
                gradeEnrollUserService.cancelGradeEnroll(gu.getId(), gradeId, gu.getUserId(),1, "系统取消",null);
            }
            return true;
        }
        return false;
    }

    @Override
    public int getGradeSignUserNum(Set<Long> gradeIdSet) {
        QueryWrapper<GradeSignEvaluate> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(GradeSignEvaluate::getGradeId, gradeIdSet)
                .in(GradeSignEvaluate::getStatus, 1, 2);
        return count(queryWrapper);
    }

    @Override
    public int getHasSignUserGradeNum(Set<Long> gradeIdSet) {
        QueryWrapper<GradeSignEvaluate> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(GradeSignEvaluate::getGradeId, gradeIdSet)
                .in(GradeSignEvaluate::getStatus, 1, 2);
        queryWrapper.select(" DISTINCT grade_id ");
        return count(queryWrapper);
    }

    @Override
    public int getGradeCommentUserNum(Set<Long> gradeIdSet) {
        QueryWrapper<GradeSignEvaluate> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(GradeSignEvaluate::getGradeId, gradeIdSet)
                .in(GradeSignEvaluate::getStatus, 3);
        return count(queryWrapper);
    }

    @Override
    public Set<Long> getSignUserCouponIds(Set<Long> userCouponIds) {
        return this.baseMapper.getSignUserCouponIds(userCouponIds);
    }
}
