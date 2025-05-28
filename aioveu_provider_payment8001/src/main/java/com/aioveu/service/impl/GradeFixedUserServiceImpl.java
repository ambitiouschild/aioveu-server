package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.GradeFixedUserDao;
import com.aioveu.entity.GradeFixedUser;
import com.aioveu.exception.SportException;
import com.aioveu.form.GradeEnrollUserForm;
import com.aioveu.service.GradeFixedUserService;
import com.aioveu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class GradeFixedUserServiceImpl extends ServiceImpl<GradeFixedUserDao, GradeFixedUser> implements GradeFixedUserService {

    @Autowired
    private UserService userService;

    @Override
    public boolean fixedUser(GradeEnrollUserForm form, String gradeTemplateId) {
        GradeFixedUser gradeFixedUser = new GradeFixedUser();
        BeanUtils.copyProperties(form, gradeFixedUser);
        gradeFixedUser.setGradeTemplateId(gradeTemplateId);
        return save(gradeFixedUser);
    }

    @Override
    public Integer fixedUserCount(String gradeTemplateId) {
        QueryWrapper<GradeFixedUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GradeFixedUser::getGradeTemplateId, gradeTemplateId);
        return count(queryWrapper);
    }

    @Override
    public boolean cancelFixed(Long id, String userId) {
        GradeFixedUser gradeFixedUser = getById(id);
        if (gradeFixedUser == null) {
            throw new SportException("固定预约不存在!");
        }
        if (!gradeFixedUser.getUserId().equals(userId + "")) {
            throw new SportException("当前用户没有操作权限!");
        }
        return removeById(id);
    }

    @Override
    public boolean hasFixed(String userId, String childName, String gradeTemplateId) {
        List<GradeFixedUser> gradeFixedUserList = getFixedByUserIdAndTemplate(userId, childName, gradeTemplateId);
        return CollectionUtils.isNotEmpty(gradeFixedUserList);
    }

    @Override
    public List<GradeFixedUser> getByTemplateId(String gradeTemplateId) {
        QueryWrapper<GradeFixedUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GradeFixedUser::getGradeTemplateId, gradeTemplateId);
        return list(queryWrapper);
    }

    private List<GradeFixedUser> getFixedByUserIdAndTemplate(String userId, String childName, String templateId) {
        QueryWrapper<GradeFixedUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GradeFixedUser::getUserId, userId)
                .eq(GradeFixedUser::getChildName, childName)
                .eq(GradeFixedUser::getGradeTemplateId, templateId);
        return list(queryWrapper);
    }

    @Override
    public boolean cancelFixedByUsernameAndTemplate(String userId, String childName, String templateId) {
        List<GradeFixedUser> gradeFixedUserList = getFixedByUserIdAndTemplate(userId, childName, templateId);
        if (CollectionUtils.isNotEmpty(gradeFixedUserList)) {
            return cancelFixed(gradeFixedUserList.get(0).getId(), userId);
        }
        return false;
    }
}
