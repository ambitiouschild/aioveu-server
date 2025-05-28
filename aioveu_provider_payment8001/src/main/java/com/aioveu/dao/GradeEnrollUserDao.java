package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.dto.GradeEnrollUserDTO;
import com.aioveu.dto.GradeWeekEnrollUserDTO;
import com.aioveu.entity.GradeEnrollUser;
import com.aioveu.entity.GradeUserCoupon;
import com.aioveu.entity.UserCoupon;
import com.aioveu.vo.AnalysisCouponVO;
import com.aioveu.vo.GradeEnrollUserDetailVO;
import com.aioveu.vo.GradeEnrollUserItemVO;
import com.aioveu.vo.GradeEnrollUserSimpleVO;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface GradeEnrollUserDao extends BaseMapper<GradeEnrollUser> {

    /**
     * 获取用户的约课信息
     * 根据公司id、商铺id
     * @param userId
     * @param status
     * @return
     */
    List<GradeEnrollUserItemVO> getByUserId(String userId, Integer status, Long companyId, Long storeId);

    /**
     * 查询预约详情
     * @param id
     * @return
     */
    GradeEnrollUserDetailVO detail(Long id);

    /**
     * 批量保存约课优惠券
     * @param list
     * @return
     */
    int batchSaveGradeCouponList(List<GradeUserCoupon> list);

    /**
     * 根据用户班级报名id删除
     * 解决用户重复报名，一个签到，一个未签到，导致【未签到退还课券】会将所有课券全部退还bug
     * 加上gradeEnrollUserId报名id，只退还未签到的课券。
     * @param gradeEnrollUserId
     * @param userId
     * @param gradeId
     * @return
     */
    int deleteByGradeEnrollId(Long gradeEnrollUserId, String userId, Long gradeId);

    /**
     * 查询约课优惠券
     * @param userId
     * @param gradeId
     * @return
     */
    List<GradeUserCoupon> getGradeCoupon(String userId, Long gradeId);

    /**
     * 根据用户班级报名id查询对应消费的券
     * 解决用户重复报名问题
     * @param gradeEnrollUserId
     * @param userId
     * @param gradeId
     * @return
     */
    List<GradeUserCoupon> getGradeCouponByGradeEnrollId(Long gradeEnrollUserId, String userId, Long gradeId);

    /**
     * 获取班级的报名用户
     * @param gradeId
     * @return
     */
    List<GradeEnrollUserSimpleVO> getByGradeId(Long gradeId);

    /**
     * 获取班级的签到用户列表
     * @param gradeId
     * @return
     */
    List<GradeEnrollUserSimpleVO> getEvaluateListByGradeId(Long gradeId);

    /**
     * 获取班级相关订单
     * @param gradeId
     * @return
     */
    List<String> getGradeOrderId(Long gradeId);

    /**
     * 查询指定店铺时间范围内的课程优惠券列表
     * @param start
     * @param end
     * @param storeId
     * @param coachUserId
     * @return
     */
    List<UserCoupon> getGradeUsedCouponList(Date start, Date end, Long storeId, String coachUserId);

    /**
     * 查询指定店铺未核销的全部优惠券列表
     * @param storeId
     * @return
     */
    List<UserCoupon> getStoreUnUsedCouponList(Long storeId);

    /**
     * 获取指定班级的优惠券列表
     * @param gradeList
     * @return
     */
    List<UserCoupon> getUsedCouponListByGradeId(List<Long> gradeList);


    /**
     * 获取分析的班级取消优惠券
     * @param start
     * @param end
     * @param gradeList
     * @return
     */
    List<AnalysisCouponVO> getAnalysisCancelGradeCoupon(Date start, Date end, List<Long> gradeList);

    /**
     * 获取总共的约课用户
     * @return
     */
    List<String> getGradeEnrollUserIdList();

    /**
     * 获取约课对应的订单id
     * @param gradeId
     * @param userId
     * @return
     */
    String getGradeCouponOrderId(Long gradeId, String userId);

    /**
     * 获取最后上课时间
     * @param userId
     * @return
     */
    Date getLastClassTime(String userId);


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
     * 根据时间范围获取用户的约课信息
     * @param start
     * @param end
     * @param userId
     * @param name
     * @return
     */
    List<GradeEnrollUserDTO> getUserEnroll4TimeRange(String start, String end, String userId, String name);

}
