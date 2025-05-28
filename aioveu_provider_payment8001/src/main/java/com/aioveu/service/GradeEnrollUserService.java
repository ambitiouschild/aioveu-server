package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.dto.GradeEnrollUserDTO;
import com.aioveu.dto.GradeWeekEnrollUserDTO;
import com.aioveu.entity.GradeEnrollUser;
import com.aioveu.entity.GradeUserCoupon;
import com.aioveu.entity.UserCoupon;
import com.aioveu.form.GradeEnrollUserForm;
import com.aioveu.form.HelpCancelGradeEnrollUserForm;
import com.aioveu.form.HelpGradeEnrollUserForm;
import com.aioveu.vo.AnalysisCouponVO;
import com.aioveu.vo.GradeEnrollUserDetailVO;
import com.aioveu.vo.GradeEnrollUserItemVO;
import com.aioveu.vo.GradeEnrollUserSimpleVO;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Description: TODO
 * @Author: 雒世松
 * @Date: 2025/5/29 0:27
 * @param null
 * @return:
 **/


public interface GradeEnrollUserService extends IService<GradeEnrollUser> {

    /**
     * 约课
     * @param form
     * @return
     */
    boolean create(GradeEnrollUserForm form);

    /**
     * 帮助约课
     * @param form
     * @return
     */
    boolean helpEnroll(HelpGradeEnrollUserForm form);

    /**
     * 我的约课列表
     * @param userId
     * @param status
     * @return
     */
    List<GradeEnrollUserItemVO> list(String userId, Integer status, Long companyId, Long storeId);

    List<GradeUserCoupon> getGradeCoupon(String userId, Long gradeId);

    /**
     * 详情
     * @param id
     * @return
     */
    GradeEnrollUserDetailVO detail(Long id);

    /**
     * 用户手动取消约课
     * @param id
     * @param userId
     * @return
     */
    boolean cancel(Long id, String userId);

    /**
     * 取消用户约课
     * @param id
     * @param gradeId
     * @param userId
     * @param type 0 用户取消 1 系统取消 2 老师帮取消
     * @param reason
     * @param gradeCancelOption
     * @param companyId
     * @return
     */
    boolean cancelGradeEnroll(Long id, Long gradeId, String userId, Integer type, String reason, String gradeCancelOption, Long companyId);

    /**
     * 批量保存约课优惠券
     * @param list
     * @return
     */
    boolean batchSaveGradeCouponList(List<GradeUserCoupon> list);

    /**
     * 取消当前约课和固定约课
     * @param id
     * @return
     */
    boolean cancelAll(Long id);

    /**
     * 获取班级的报名人数
     * @param gradeId
     * @return
     */
    List<GradeEnrollUser> enrollNumber(Long gradeId);

    /**
     * 获取班级的报名人数
     * @param gradeId
     * @param status
     * @return
     */
    List<GradeEnrollUser> enrollNumber(Long gradeId, List<Integer> status);

    /**
     * 系统取消
     * @param id
     * @param gradeId
     * @param userId
     * @param type 0 用户取消 1 系统取消 2 商户取消
     * @param reason
     * @return
     */
    boolean cancelGradeEnroll(Long id, Long gradeId, String userId, Integer type, String reason, String gradeCancelOption);

    /**
     * 获取班级的报名学生列表
     * @param gradeId
     * @param userId
     * @return
     */
    List<GradeEnrollUserSimpleVO> getByGradeId(Long gradeId, String userId);

    /**
     * 报名签到
     * @param id
     * @param userId
     * @return
     */
    boolean sign(Long id, String userId);

    /**
     * 点评
     * @param id
     * @param message
     * @param userId
     * @return
     */
    boolean evaluate(Long id, String message, String userId);

    /**
     * 获取班级的签到学生列表
     * @param gradeId
     * @return
     */
    List<GradeEnrollUserSimpleVO> getEvaluateListByGradeId(Long gradeId);

    /**
     * 班级结束
     * @param id
     */
    void gradeFinish(Long id);

    /**
     * 获取指定店铺范围内核销的优惠券列表
     * @param start
     * @param end
     * @param storeId
     * @param coachUserId
     * @return
     */
    List<UserCoupon> getGradeUsedCouponList(Date start, Date end, Long storeId, String coachUserId);

    /**
     * 获取指定店铺未核销的课程全部优惠券列表
     * @param storeId
     * @return
     */
    List<UserCoupon> getStoreUnUsedCouponList(Long storeId);

    /**
     * 获取指定班级指定范围内的优惠券列表
     * @param start
     * @param end
     * @param gradeList
     * @return
     */
    List<UserCoupon> getUsedCouponListByGradeId(Date start, Date end, List<Long> gradeList);

    /**
     * 获取分析的班级取消优惠券
     * @param start
     * @param end
     * @param gradeList
     * @return
     */
    List<AnalysisCouponVO> getAnalysisCancelGradeCoupon(Date start, Date end, List<Long> gradeList);

    Date getLastClassTime(String userId);

    /**
     * 同步用户优惠券
     */
    void syncGradeCoupon();

    /**
     * 帮助取消约课
     * @param form
     * @return
     */
    boolean helpCancelEnroll(HelpCancelGradeEnrollUserForm form);

    /**
     * 获取教练核销课券对应金额
     * @param start
     * @param end
     * @param storeId
     * @param coachId
     * @return
     */
    List<UserCoupon> getCoachVerifyList(Date start, Date end, Long storeId, Long coachId);

    /**
     * 获取指定时间范围内预约信息
     * @param start
     * @param end
     * @return
     */
    List<GradeWeekEnrollUserDTO> getTimeRangeEnrollList(Date start, Date end);

    /**
     * 获取取消课程的时间
     * @param storeId
     * @return
     */
    int getCancelGradeMinute(Long storeId);


    /**
     * 修改报名childName
     * @param id
     * @param childName
     * @return
     */
    boolean editChildName(Long id,String childName);

    /**
     * 根据时间范围获取用户的约课信息
     * @param start
     * @param end
     * @param userId
     * @param name
     * @return
     */
    List<GradeEnrollUserDTO> getUserEnroll4TimeRange(String start, String end, String userId, String name);


    /**
     * 根据班级id集合，获取约课人数，取消的不算
     * @param gradeIdSet
     * @return
     */
    int getGradeEnrollUserNum(Set<Long> gradeIdSet);

}
