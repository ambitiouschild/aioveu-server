package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.service.CodeService;
import com.aioveu.service.OrderDetailService;
import com.aioveu.utils.SportDateUtils;
import com.aioveu.vo.BasicOrderVO;
import com.aioveu.vo.CheckOrderVO;
import com.aioveu.vo.OrderDetailVO;
import com.aioveu.vo.UserEnterVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.util.function.Tuple2;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/orderDetail")
public class OrderDetailController {

    @Autowired
    private CodeService codeService;

    @Autowired
    private OrderDetailService orderDetailService;


    @PostMapping("code")
    public CheckOrderVO checkCode(@RequestParam String userId, @RequestParam String code) {
        return codeService.checkCode(userId, code);
    }

    @GetMapping("/order/{orderId}")
    public OrderDetailVO detail(@PathVariable String orderId, @RequestParam String phone) {
        return orderDetailService.getOrderDetail(orderId, phone);
    }

    @GetMapping("enroll")
    public IPage<UserEnterVO> getEnrollUserList(@RequestParam Map<String, Object> param) {
        int page = NumberUtils.toInt(Optional.ofNullable(param.get("page")).orElse(1).toString());
        int pageSize = (NumberUtils.toInt(Optional.ofNullable(param.get("size")).orElse(10).toString()));
        return orderDetailService.getEnrollUserList(page, pageSize, Collections.singleton(Long.parseLong(param.get("productId")+"")), Long.parseLong(param.get("categoryId")+""));
    }

    @PostMapping("code-check/mine")
    public Boolean codeCheck(@RequestParam String orderId) {
        return codeService.checkCodeMine(orderId, OauthUtils.getCurrentUsername());
    }

    /**
     * 帮激活订单
     * @param orderId
     * @return
     */
    @PostMapping("code-check/helpActive")
    public Boolean helpActive(@RequestParam String orderId) {
        return codeService.helpActiveOrder(orderId);
    }

    @GetMapping("/basic-list")
    public IPage<BasicOrderVO> basicOrderList(@RequestParam Map<String, Object> param) throws Exception {
        int page = NumberUtils.toInt(Optional.ofNullable(param.get("page")).orElse(1).toString());
        int pageSize = (NumberUtils.toInt(Optional.ofNullable(param.get("size")).orElse(10).toString()));
        Tuple2<Date, Date> startAndEndDate = SportDateUtils.getStartAndEndDate(param);
        return orderDetailService.getFieldOrderDetailRangeAndStatus(page, pageSize, Long.parseLong(param.get("storeId").toString()), startAndEndDate.getT1(),
                startAndEndDate.getT2(), param.get("payType") + "",Integer.parseInt(param.get("status").toString()));
    }


}
