package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.dto.CancelGradeMessageDTO;
import com.aioveu.entity.Grade;
import com.aioveu.entity.GradeCancelRecord;
import com.aioveu.form.GradeForm;
import com.aioveu.form.UserGradeListForm;
import com.aioveu.vo.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface GradeService extends IService<Grade> {

    /**
     * 通过店铺id和 日期筛选班级
     *
     * @param storeId
     * @param date
     * @param status
     * @param page
     * @param size
     * @return
     */
    IPage<GradeVO> getByStoreId(Long storeId, String date, String status, Long coachId, String startTime, String endTime, Integer page, Integer size);

    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    GradeDetailVO detail(Long id);

    List<GradeDetailVO> getGradeDetailByIds(String ids);

    void cancelLockField(Grade grade);

    void lockField(Grade grade);

    /**
     * 更新单个班级
     *
     * @param form
     * @return
     */
    boolean updateGrade(GradeForm form);

    /**
     * 修改状态
     *
     * @param id
     * @param status
     * @return
     */
    boolean changeStatus(Long id, Integer status);

    List<Grade> getGradeByField(Long fieldId, String date);

    /**
     * 新增新的周期班级
     *
     * @return
     */
    boolean gradeAddNew();

    /**
     * 用户端的约课列表
     *
     * @param form
     * @return
     */
    List<GradeUserItemVO> userList(UserGradeListForm form);

    /**
     * 用户端课程详情
     *
     * @param id
     * @return
     */
    GradeUserDetailVO userDetail(Long id);

    /**
     * 获取用户端可约课的班级列表
     *
     * @param userId
     * @return
     */
    List<GradeUserDateVO> myAvailableDate(String userId, Long companyId);

    /**
     * 获取用户端可约课的班级列表
     *
     * @param userId
     * @return
     */
    List<GradeUserDateVO> getAvailableDateByUserId(String userId, Long companyId);

    /**
     * 获取教练的班级列表
     *
     * @param userId
     * @param type     类型 默认0 全部 1历史 2待开始 3已取消
     * @param page
     * @param size
     * @param date
     * @return
     */
    IPage<GradeVO> getByCoach(String userId, Integer type, int page, int size, String date);

    /**
     * 最小人数检查
     *
     * @param id
     * @return
     */
    boolean miniNumberCheck(Long id);

    /**
     * 根据查询id教练有没有课程
     *
     * @param coachIdList
     * @return
     */
    List<GradeVO> getByCoachId(List<Long> coachIdList);

    /**
     * 取消班级
     *
     * @param gradeCancelRecord
     * @param username
     * @return
     */
    boolean cancel(GradeCancelRecord gradeCancelRecord, String username);

    /**
     * 查询班级重复规则
     *
     * @param id
     * @return
     */
    GradeTimeRuleVO getGradeTimeRule(Long id);

    /**
     * 班级取消加入到统计中，所有班级取消的次数，点击能看到记录
     *
     * @param dataDTO
     * @return
     */
    IPage<GradeCancelRecordVo> getCancelGradeMessageByStoreId(CancelGradeMessageDTO dataDTO);

    /**
     * 获取取消班级数量
     *
     * @param storeId
     * @param start
     * @param end
     * @param coachUserId
     * @return
     */
    List<Grade> getCancelGradeByRange(Long storeId, Date start, Date end, String coachUserId);

    /**
     * 检查班级是否完成
     */
    void checkGradeFinish();

    /**
     * 根据时间查找班级
     *
     * @param gradeTemplateId
     * @param startTime
     * @param endTime
     * @return
     */
    Grade getByTemplateIdAndTime(String gradeTemplateId, Date startTime, Date endTime);

    /**
     * 获取店铺选定时间范围内已上课的班级数量
     *
     * @param storeId
     * @return
     */
    Set<Long> getHasClassGradeNum(Long storeId, Date startTime, Date endTime);

    /**
     * 获取教练已完成上课的课程id
     *
     * @param coachUserId
     * @param storeId
     * @param start
     * @param end
     * @return
     */
    Set<Long> getCoachFinishGradeId(String coachUserId, Long storeId, Date start, Date end);

    /**
     * 获取课程对应的教练或者老师id
     * @param gradeId
     * @return
     */
    String getUserIdByGradeId(Long gradeId);


    /**
     * 获取指定日期的班级数量
     * @param date
     * @param storeName
     * @return
     */
    String getGradeNumber(String date, String storeName);

    /**
     * 通过班级id获取公司id
     * @param gradeId
     * @return
     */
    Long getCompanyIdByGradeId(Long gradeId);

    /**
     * 获取同一个班级模板未来的班级
     * @param gradeTemplateId
     * @param endTime
     * @return
     */
    List<Grade> getFutureGrade(String gradeTemplateId, Date endTime);

    /**
     * 获取指定范围内教室课程
     * @param venueId
     * @param startTime
     * @param endTime
     * @return
     */
    List<Grade> getClassroomGradeList(Long venueId, Date startTime, Date endTime);


    /**
     * 门店课程未签到数量，统计每个教练未签到数
     * @param storeId
     * @return
     */
    List<StatisticsVo> getUnSignNumsByStoreId(Long storeId, Date startDate, Date endDate);

    /**
     * 获取符合给定时间范围内的班级id
     * @param venueId
     * @param timeList
     * @return
     */
    List<Long> getUsedClassroomId(Long venueId, List<Grade> timeList);

}
