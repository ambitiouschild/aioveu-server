package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.ChargingChange;
import com.aioveu.entity.ChargingChargeOption;
import com.aioveu.service.ChargingChangeService;
import com.aioveu.service.ChargingChargeOptionService;
import com.aioveu.service.StoreChargingOptionService;
import com.aioveu.vo.StoreChargingOptionDetailVO;
import com.aioveu.vo.StoreChargingOptionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/store-charging-option")
public class StoreChargingOptionController {

    @Autowired
    private StoreChargingOptionService storeChargingOptionService;

    @Autowired
    private ChargingChargeOptionService chargingChargeOptionService;

    @Autowired
    private ChargingChangeService chargingChangeService;

    @GetMapping("/store/{storeId}")
    public List<StoreChargingOptionVO> getStoreChargingOption(@PathVariable Long storeId) {
        return storeChargingOptionService.getStoreChargingOption(storeId);
    }

    @GetMapping("/charge-list/{chargingCode}")
    public List<ChargingChargeOption> getChargeOptionByCode(@PathVariable String chargingCode) {
        return chargingChargeOptionService.getChargeOptionByCode(chargingCode);
    }

    @GetMapping("/detail")
    public StoreChargingOptionDetailVO getStoreChargingOptionDetail(@RequestParam String chargingCode, @RequestParam Long storeId) {
        return storeChargingOptionService.getStoreChargingOptionDetail(chargingCode, storeId);
    }

    @GetMapping("/change-list")
    public IPage<ChargingChange> getChangeList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                               @RequestParam(required = false, defaultValue = "10") Integer size,
                                               @RequestParam String chargingCode, @RequestParam Long storeId, @RequestParam(required = false) Integer changeType) {
        return chargingChangeService.getChangeList(storeId, chargingCode, changeType, page, size);
    }

}
