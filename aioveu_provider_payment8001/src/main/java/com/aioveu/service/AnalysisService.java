package com.aioveu.service;

import com.aioveu.form.StoreAnalysisForm;
import com.aioveu.vo.AnalysisCouponVO;
import com.aioveu.vo.AnalysisOrderVO;
import com.aioveu.vo.StoreAnalysisVO;
import com.aioveu.vo.StoreCheckCouponItemVO;

import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/7 0007 23:25
 */
public interface AnalysisService {


    /**
     * 店铺分析
     * @param storeId
     * @param start
     * @param end
     * @param coachUserId
     * @return
     */
    StoreAnalysisVO storeAnalysis(Long storeId, String start, String end, String coachUserId) throws Exception;


    /**
     * 获取卡券核销金额明细
     * @param form
     * @return
     */
    List<StoreCheckCouponItemVO> getStoreCheckedCouponList(StoreAnalysisForm form);

    /**
     * 订单明细
     * @param form
     * @return
     */
    List<AnalysisOrderVO> getOrderList(StoreAnalysisForm form);

    /**
     * 订单明细
     * @param form
     * @return
     */
    List<AnalysisOrderVO> getUnActiveOrderList(StoreAnalysisForm form);

    /**
     * 获取取消的课程券
     * @param form
     * @return
     */
    List<AnalysisCouponVO> getCancelGradeCoupon(StoreAnalysisForm form);

    /**
     * 查询门店满班率明细
     * 100%  几个
     * 20%   几个
     * 0%    几个
     * @param storeId
     * @param startStr
     * @param endStr
     * @param coachUserId
     * @param multiUserGrade 多人班级
     * @return
     */
    List<Map<String, Object>> getFullGradeRateList(Long storeId, String startStr, String endStr, String coachUserId ,boolean multiUserGrade);

    /**
     * 根据班级最多人数限制分组统计
     * 例如
     * 2人班  1个
     * 3人班  2个
     * @param storeId
     * @param startStr
     * @param endStr
     * @param coachUserId
     * @param multiUserGrade 多人班级
     * @return
     */
    List<Map<String, Object>> getCountGradeLimitGroup(Long storeId, String startStr, String endStr, String coachUserId ,boolean multiUserGrade);

    /**
     * 店铺分析
     * 获取门店的客户课券过期日期
     * @param storeId
     * @return
     */
    List<Map<String, Object>> storeChartUserCouponAnalysis(Long storeId);

}
