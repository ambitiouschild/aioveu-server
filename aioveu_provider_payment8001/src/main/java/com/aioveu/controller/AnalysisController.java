package com.aioveu.controller;

import com.aioveu.form.StoreAnalysisForm;
import com.aioveu.service.AnalysisService;
import com.aioveu.service.OrderService;
import com.aioveu.vo.AnalysisCouponVO;
import com.aioveu.vo.AnalysisOrderVO;
import com.aioveu.vo.StoreAnalysisVO;
import com.aioveu.vo.StoreCheckCouponItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/7 0007 23:13
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/store/{id}")
    public StoreAnalysisVO storeAnalysis(@PathVariable Long id, @RequestBody StoreAnalysisForm form) throws Exception {
        return analysisService.storeAnalysis(id, form.getStart(), form.getEnd(), form.getCoachUserId());
    }

    @PostMapping("/checked-amount")
    public List<StoreCheckCouponItemVO> checkedAmountList(@Valid @RequestBody StoreAnalysisForm form) throws Exception {
        return analysisService.getStoreCheckedCouponList(form);
    }

    @PostMapping("/order-list")
    public List<AnalysisOrderVO> orderList(@Valid @RequestBody StoreAnalysisForm form) throws Exception {
        return analysisService.getOrderList(form);
    }

    @PostMapping("/get-order-list")
    public List<AnalysisOrderVO> getOrderListById(@RequestBody StoreAnalysisForm form) {
        return orderService.getOrderListById(form.getOrderIdList());
    }

    @PostMapping("/un-active-order-list")
    public List<AnalysisOrderVO> unActiveOrderList(@Valid @RequestBody StoreAnalysisForm form) {
        return analysisService.getUnActiveOrderList(form);
    }

    @PostMapping("/cancel-grade-coupon")
    public List<AnalysisCouponVO> getCancelGradeCoupon(@Valid @RequestBody StoreAnalysisForm form) {
        return analysisService.getCancelGradeCoupon(form);
    }

    /**
     * 统计满班率
     * 满班率 = 满员班级数/班级总数
     * @param id
     * @param form
     * @return
     * @throws Exception
     */
    @PostMapping("/fullGradeRateList/{id}")
    public List<Map<String, Object>> getFullGradeRateList(@PathVariable Long id, @RequestBody StoreAnalysisForm form) throws Exception {
        return analysisService.getFullGradeRateList(id, form.getStart(), form.getEnd(), form.getCoachUserId(), form.isMultiUser());
    }

    /**
     * 统计班级，根据最多人数分组，得到x人班，有几个
     * 1人班  2个
     * 2人班  5个
     * 3人班  1个
     * @param id
     * @param form
     * @return
     * @throws Exception
     */
    @PostMapping("/gradeLimitGroup/{id}")
    public List<Map<String, Object>> gradeLimitGroup(@PathVariable Long id, @RequestBody StoreAnalysisForm form) throws Exception {
        return analysisService.getCountGradeLimitGroup(id, form.getStart(), form.getEnd(), form.getCoachUserId(), form.isMultiUser());
    }

    @GetMapping("/store-chart/user-coupon/{id}")
    public List<Map<String, Object>> storeChartUserCouponAnalysis(@PathVariable Long id) throws Exception {
        return analysisService.storeChartUserCouponAnalysis(id);
    }
}
