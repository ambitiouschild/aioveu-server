package com.aioveu.dao;

import com.aioveu.dto.AnalysisGradeFieldPlanDto;
import com.aioveu.entity.Order;
import com.aioveu.vo.AnalysisOrderVO;
import com.aioveu.vo.StoreCheckCouponItemVO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface AnalysisDao {


    /**
     * 通过优惠券查询核销明细
     * @param idList
     * @return
     */
    List<StoreCheckCouponItemVO> getStoreCheckedCouponByOrder(List<Long> idList);

    /**
     * 获取指定时间范围内用户取消约课的课券总金额
     * @param storeId
     * @param start
     * @param end
     * @return
     */
    BigDecimal getUserCancelGradeCouponAmount(Long storeId, Date start, Date end);

    /**
     * 获取指定时间范围内用户取消约课的总数
     * @param storeId
     * @param start
     * @param end
     * @return
     */
    Integer getUserCancelGradeCount(Long storeId, Date start, Date end);

    /**
     * 获取班级取消对应的用户数量
     * @param idList
     * @return
     */
    Integer getGradeCancelUserCountByGrade(List<Long> idList);

    /**
     * 统计门店满班率明细
     * 满班率 = 满员班级数/班级总数
     * 100%  几个
     * 20%   几个
     * 0%    几个
     * @param storeId
     * @param start
     * @param end
     * @param coachUserId
     * @param status
     * @param multiUserGrade 多人班级
     * @return
     */
    List<Map<String, Object>> getFullGradeRateList(Long storeId, Date start, Date end, String coachUserId, List<Integer> status, boolean multiUserGrade);

    /**
     * 根据班级最多人数限制分组统计
     * 例如
     * 2人班  1个
     * 3人班  2个
     * @param storeId  门店id
     * @param start    开始时间
     * @param end      结束时间
     * @param coachUserId   教练id
     * @param status   班级状态
     * @return
     */
    List<Map<String, Object>> getCountGradeLimitGroup(Long storeId, Date start, Date end, String coachUserId, List<Integer> status, boolean multiUserGrade);


    /**
     * 获取教练班级使用场地总时长
     * @param storeId
     * @param start
     * @param end
     * @param coachUserId
     * @return
     */
    List<AnalysisGradeFieldPlanDto> getGradeUsedFieldTotal(Long storeId, Date start, Date end, String coachUserId);


    /**
     * 店铺分析
     * 获取门店的客户课券过期日期
     * @param storeId
     * @return
     */
    List<Map<String, Object>> storeChartUserCouponAnalysis(Long storeId);

    /**
     * 获取订单订单，门店场馆的订单信息
     * @param storeId
     * @param start
     * @param end
     * @return
     */
    List<AnalysisOrderVO> getStoreVenueFieldOrderList(Long storeId, Date start, Date end,List<Integer> statusList);

    /**
     * 获取订场订单，场馆用户是否新签用户
     *
     * @param storeId
     * @param venueId
     * @param start
     * @return
     */
    Order getVenueNewSignatureFieldOrder(Long storeId, Long venueId, String userId, Date start,List<Integer> statusList);

    /**
     * 手动核销券统计数量与金额
     * @param storeId
     * @param start
     * @param end
     * @param coachUserId
     * @return
     */
    Map<String, Object> manualUsedCouponVerifyStatistics(Long storeId, Date start, Date end, String coachUserId);
}
