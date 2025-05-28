package com.aioveu.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.WaterPoolSaleGroup;
import com.aioveu.service.WaterPoolSaleGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/api/v1/water_pool_sale_group")
@RestController
public class WaterPoolSaleGroupController {

    @Autowired
    private WaterPoolSaleGroupService waterPoolSaleGroupService;


    @GetMapping("")
    public IPage<WaterPoolSaleGroup> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "10") Integer size,
                                         @RequestParam Long storeId) {
        return waterPoolSaleGroupService.getByStoreId(page, size, storeId);
    }

    @PostMapping("")
    public Boolean create(@Valid @RequestBody WaterPoolSaleGroup waterPoolSaleGroup) {
        return waterPoolSaleGroupService.create(waterPoolSaleGroup);
    }

    @PutMapping("")
    public boolean update(@RequestBody WaterPoolSaleGroup waterPoolSaleGroup) {
        return waterPoolSaleGroupService.updGroupById(waterPoolSaleGroup);
    }

    @DeleteMapping("/{id}")
    public boolean groupDelete(@PathVariable Long id) {
        return waterPoolSaleGroupService.deleteGroup(id);
    }


}
