package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.dto.CancelGradeMessageDTO;
import com.aioveu.entity.Grade;
import com.aioveu.vo.*;
import org.apache.ibatis.annotations.MapKey;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface GradeDao extends BaseMapper<Grade> {

    /**
     * 根据店铺id查找数据
     *
     * @param page
     * @param storeId
     * @param date
     * @param status
     * @param experienceCategoryId
     * @return
     */
    IPage<GradeVO> getByStoreId(IPage<GradeVO> page, Long storeId, String date, String status, Long experienceCategoryId, Long coachId, String startTime, String endTime);

    /**
     * 获取用户可以约课的课程id
     *
     * @param userId
     * @param coachUserId 客户约课教练归属
     * @return
     */
    List<Long> getUserAvailableGradeId(String userId, Long companyId, String coachUserId);

    /**
     * 根据id集合和时间获取班级列表
     *
     * @param gradeIdList
     * @param date
     * @return
     */
    List<GradeUserItemVO> getByIdAndDate(List<Long> gradeIdList, String date);

    /**
     * 用户端的课程详情
     *
     * @param id
     * @return
     */
    GradeUserDetailVO userDetail(Long id);

    /**
     * 获取指定日期的班级
     *
     * @param day
     * @return
     */
    List<Grade> getGradeByDay(String day);

    /**
     * 订场锁场，获取指定日期的班级
     * @param fieldId
     * @param date
     * @return
     */
    List<Grade> getGradeByField(Long fieldId, String date);

    /**
     * 检查时间是否冲突
     *
     * @param grade
     * @return
     */
    int checkGradeTime(Grade grade);

    /**
     * 获取对应班级的报名人数
     *
     * @param gradeIdList
     * @return
     */
    @MapKey("gradeId")
    Map<Integer, Map<String, Long>> getGradeEnrollUserNumber(List<Long> gradeIdList);


    /**
     * 查询教练有没有课程
     *
     * @param coachIdList
     * @return
     */
    List<GradeVO> getByCoachId(List<Long> coachIdList);

    /**
     * 查询班级重复规则
     *
     * @param id
     * @return
     */
    GradeTimeRuleVO getGradeTimeRule(Long id);

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    GradeDetailVO getGradeDetailById(Long id);

    List<GradeDetailVO> getGradeDetailByIds(String ids);

    /**
     * 班级取消加入到统计中，所有班级取消的次数，点击能看到记录
     */
    IPage<GradeCancelRecordVo> getCancelGradeMessageByStoreId(IPage<GradeCancelRecordVo> page, CancelGradeMessageDTO dataDTO);

    /**
     * 获取指定时间店铺取消的课程集合
     *
     * @param storeId
     * @param start
     * @param end
     * @param coachUserId
     * @return
     */
    List<Grade> getCancelGradeByRange(Long storeId, Date start, Date end, String coachUserId);

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
     * 通过班级id获取公司id
     * @param gradeId
     * @return
     */
    Long getCompanyIdByGradeId(Long gradeId);

    /**
     * 查询门店课程未签到数量，统计每个教练未签到数
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
