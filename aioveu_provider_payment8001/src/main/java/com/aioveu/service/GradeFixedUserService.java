package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.GradeFixedUser;
import com.aioveu.form.GradeEnrollUserForm;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface GradeFixedUserService extends IService<GradeFixedUser> {

    /**
     * 固定用户
     * @param form
     * @param gradeTemplateId
     * @return
     */
    boolean fixedUser(GradeEnrollUserForm form, String gradeTemplateId);

    /**
     * 查询固定用户数
     * @param gradeTemplateId
     * @return
     */
    Integer fixedUserCount(String gradeTemplateId);

    /**
     * 取消固定约课
     * @param id
     * @param username
     * @return
     */
    boolean cancelFixed(Long id, String username);

    /**
     * 判断用户是否已固定
     * @param userId
     * @param gradeTemplateId
     * @return
     */
    boolean hasFixed(String userId, String childName, String gradeTemplateId);

    /**
     * 根据班级模板id查找固定的用户列表
     * @param gradeTemplateId
     * @return
     */
    List<GradeFixedUser> getByTemplateId(String gradeTemplateId);

    /**
     * 取消固定
     * @param userId
     * @param templateId
     * @return
     */
    boolean cancelFixedByUsernameAndTemplate(String userId, String childName, String templateId);

}
