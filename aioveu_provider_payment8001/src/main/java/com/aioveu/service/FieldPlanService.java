package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.dto.FieldBookUserDTO;
import com.aioveu.dto.FieldPlanDTO;
import com.aioveu.entity.FieldBookUser;
import com.aioveu.entity.FieldPlan;
import com.aioveu.entity.Grade;
import com.aioveu.vo.FieldPlanVO;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public interface FieldPlanService extends IService<FieldPlan> {

    IPage<FieldPlanVO> listByCondition(int page, int size, Long storeId, Long venueId, Long fieldId, String date, Integer status);

    FieldPlan getByTemplateIdAndTime(Long fieldId, Date day, Time startTime, Time endTime);

    /**
     * 根据订单id查找预订场地信息
     *
     * @param orderId
     * @return
     */
    List<FieldBookUserDTO> getFieldBookUserDetail(String orderId);

    List<FieldPlanDTO> getByUser(String userId, Long companyId, Integer status);

    List<FieldBookUser> getFieldBookUserByNotUsed(String userId, Long venueId);

    boolean transferLockToFieldPlan();

    /**
     * 将过期的场地下架 创建新的场地
     * @return
     */
    boolean expiryFieldPlanAddNew();

    void saveFieldPlan(FieldPlanVO fieldPlan);

    /**
     * 批量锁场或者解锁
     * @param id
     * @param remark
     * @param status
     */
    void changeStatus(List<Long> id, String remark, Integer status);

    List<FieldPlan> getFieldByGrade(Grade grade);

    List<FieldPlan> getFieldByGrade(String fieldIds, String day, String timeFrom, String timeTo);

    List<FieldPlan> getFieldByGradeId(Long gradeId);

    boolean checkFieldByGrades(FieldPlan fieldPlan, Long gradeId, Date dateFrom, Date dateTo, Integer sharedVenue);

    /**
     * 第三方平台订场同步到趣数
     * @param fieldPlanList
     * @return
     */
    List<FieldPlanVO> syncThirdPlatform2Qs(List<FieldPlanVO> fieldPlanList);

    /**
     * 根据场地计划id，获取场地计划、场地信息、场馆信息
     * @param id
     * @return
     */
    FieldPlanDTO getFiledNameById(Long id);

    /**
     * 获取指定场馆指定日期的订场计划
     * @param venueId
     * @param day
     * @return
     */
    List<FieldPlan> getByVenueIdAndDay(Long venueId, String day);

    /**
     * 标记过期场地
     * @param day
     * @return
     */
    boolean markExpireFieldPlan(String day);
    List<FieldPlanDTO> getFieldLockedList4BookDay(Long storeId, int fieldBookDay, String platformCode, List<Long> venueIdList);
}
