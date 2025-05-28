package com.aioveu.controller;

import com.alibaba.fastjson.JSON;
import com.aioveu.coupon.executor.ExecuteManager;
import com.aioveu.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h1>结算服务 Controller</h1>
 * @author: 雒世松
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/settlement")
public class SettlementController {

    /** 结算规则执行管理器 */
    @Autowired
    private ExecuteManager executeManager;


    /**
     * <h2>优惠券结算</h2>
     * 127.0.0.1:7003/coupon-settlement/settlement/compute
     * 127.0.0.1:9000/imooc/coupon-settlement/settlement/compute
     * */
    @PostMapping("/compute")
    public SettlementInfo computeRule(@RequestBody SettlementInfo settlement) {
        log.info("settlement: {}", JSON.toJSONString(settlement));
        return executeManager.computeRule(settlement);
    }
}
