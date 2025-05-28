package com.aioveu.controller;

import com.aioveu.entity.StoreConfig;
import com.aioveu.service.StoreConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/23 0023 16:00
 */
@RestController
@RequestMapping("/api/v1/store-config")
public class StoreConfigController {

    @Autowired
    private StoreConfigService storeConfigService;

    @GetMapping("/{storeId}/{dictCode}")
    public StoreConfig getByDictCodeAndStoreId(@PathVariable String dictCode, @PathVariable Long storeId) {
        return storeConfigService.getStoreConfig(dictCode, storeId);
    }

    @GetMapping("/category")
    public List<StoreConfig> getStoreConfigList(@RequestParam Long storeId, @RequestParam String categoryCode) {
        return storeConfigService.getStoreConfigList(storeId, categoryCode);
    }

    @PostMapping("")
    public StoreConfig saveOneSysDict(@Valid @RequestBody StoreConfig storeConfig) {
        return storeConfigService.saveOrUpdateStoreConfig(storeConfig);
    }
}
