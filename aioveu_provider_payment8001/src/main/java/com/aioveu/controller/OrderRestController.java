package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.constant.SportConstant;
import com.aioveu.form.*;
import com.aioveu.service.OrderRefundService;
import com.aioveu.service.OrderService;
import com.aioveu.utils.SportDateUtils;
import com.aioveu.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.util.function.Tuple2;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/14 0014 22:29
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/order")
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRefundService orderRefundService;

    @PostMapping("exercise")
    public WxMaPayVO exerciseOrder(@Valid OrderForm form) {
        return orderService.create(form);
    }

    @PutMapping("/{orderId}")
    public Boolean exerciseSuccess(@PathVariable String orderId) {
        return orderService.updateOrder(orderId, null);
    }

    /**
     * 根据公司id和商铺id，获取我的对应的订单信息
     * @param status
     * @param userId
     * @param companyId
     * @param storeId
     * @return
     */
    @GetMapping("/list/{status}")
    public List<OrderVO> list(@PathVariable Integer status,
                              @RequestParam String userId,
                              @RequestParam Long companyId,
                              @RequestParam(required = false) Long storeId) {
        return orderService.getList(status, companyId, userId, storeId);
    }

    @GetMapping("/manager")
    public IPage<OrderManagerVO> managerList(@RequestParam Map<String, Object> param) {
        return orderService.getManagerList(param);
    }

    @GetMapping("/manager/detail/{orderId}")
    public OrderManagerDetailVO detail(@PathVariable String orderId) {
        return orderService.detail(orderId);
    }

    @PostMapping("change")
    public Boolean change(@Valid @RequestBody OrderChangeForm form) {
        return orderService.updateOrder(form.getOrderId(), form.getPayFinishDate());
    }

    @PutMapping("/cancel/{orderId}")
    public Boolean cancelOrder(@PathVariable String orderId,
                               @RequestParam(required = false, defaultValue = "用户取消订单") String reason) {
        return orderService.cancelOrder(orderId, reason);
    }

    @PutMapping("/admin-cancel/{orderId}")
    public Boolean adminCancelOrder(@PathVariable String orderId) {
        return orderService.adminCancelOrder(orderId);
    }

    @GetMapping("/pay/{orderId}")
    public WxMaPayVO continuePay(@PathVariable String orderId) {
        return orderService.continuePay(orderId);
    }

    @PostMapping("/experiential")
    public IPage<UserInfoOrderVO> experientialList(@Valid @RequestBody StoreExperienceOrderForm form) {
        return orderService.getExperiential(form);
    }

    @GetMapping("/preview-agreement")
    public String previewAgreement(@RequestParam String userId, @RequestParam Long categoryId, @RequestParam(required = false) String productId) {
        return orderService.previewAgreement(userId, categoryId, productId);
    }

    @GetMapping("/preview-order-agreement")
    public String previewOrderAgreement(String orderId) {
        return orderService.previewOrderAgreement(orderId);
    }

    @GetMapping("/number/{storeId}")
    public OrderNumberVO getOrderNumber(@PathVariable Long storeId) {
        return orderService.getOrderNumber(storeId);
    }

    @PostMapping("field")
    public WxMaPayVO fieldOrder(@Valid FieldForm form) {
        return orderService.createField(form);
    }

    @PutMapping("field/{id}")
    public Boolean updateFieldOrder(@PathVariable String id) {
        return orderService.updateFieldOrder(id, null);
    }

    @GetMapping("/field/{id}")
    public FieldOrderDetailVO fieldOrderDetail(@PathVariable String id) {
        return orderService.fieldDetail(id);
    }

    @PostMapping("field-pay/{orderId}")
    public WxMaPayVO fieldOrderPay(@PathVariable String orderId) {
        return orderService.fieldPay(orderId, null);
    }

    /**
     * 查询【我的定场】信息
     * @param status 状态编码
     * @param userId  用户id
     * @param companyId  公司id
     * @param storeId  店铺id，非必传
     * @return  【我的定场】信息
     */
    @GetMapping("/fieldList/{status}")
    public List<FieldOrderVO> fieldList(@PathVariable Integer status,
                                        @RequestParam String userId,
                                        @RequestParam Long companyId,
                                        @RequestParam(required = false) Long storeId) {
        return orderService.getFieldOrderList(status, companyId, userId, storeId);
    }

    @GetMapping("/fieldManager")
    public IPage<FieldOrderManagerVO> fieldManager(@RequestParam Map<String, Object> param) {
        return orderService.getFieldManagerList(param);
    }

    @PostMapping("vip")
    public WxMaPayVO vipOrder(@Valid VipOrderForm form) {
        return orderService.createVipOrder(form);
    }

    @PutMapping("vip/{id}")
    public Boolean updateVipOrder(@PathVariable String id) {
        return orderService.updateVipOrder(id, null);
    }

    @GetMapping("/basic-list")
    public IPage<BasicOrderVO> basicOrderList(@RequestParam Map<String, Object> param) {
        return orderService.getBasicOrderList(param);
    }

    @GetMapping("/refund-list")
    public List<BasicRefundOrderVO> refundList(@RequestParam Map<String, Object> param) throws Exception {
        Tuple2<Date, Date> startAndEndDate = SportDateUtils.getStartAndEndDate(param);
        return orderRefundService.getDateRangeRefundOrder(Long.parseLong(param.get("storeId").toString()), startAndEndDate.getT1(), startAndEndDate.getT2(), SportConstant.FIELD_CATEGORY_CODE);
    }

}
