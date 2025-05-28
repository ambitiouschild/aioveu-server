package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.CheckRecordAccountConfig;
import com.aioveu.service.CheckRecordAccountConfigService;
import com.aioveu.vo.CheckRecordAccountConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单核销账号配置接口
 * @Author： yao
 * @Date： 2024/12/18 12:03
 * @Describe：
 */
@Slf4j
@RequestMapping("/api/v1/check-record-config")
@RestController
public class CheckRecordAccountConfigController {
    @Autowired
    CheckRecordAccountConfigService CheckRecordAccountConfigService;

    @GetMapping("")
    public IPage<CheckRecordAccountConfigVO> pageList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                   @RequestParam(required = false, defaultValue = "10") Integer size,
                                                   @RequestParam Long storeId) {
        return CheckRecordAccountConfigService.getPageList(page, size, storeId);
    }

    @PutMapping("/status")
    public boolean updateStatus(@RequestParam String id, @RequestParam Integer status) {
        return CheckRecordAccountConfigService.changeStatus(id, status);
    }

    @PostMapping("")
    public boolean save(@RequestBody CheckRecordAccountConfig config){
        return CheckRecordAccountConfigService.saveConfig(config);
    }
    @GetMapping("/{configId}")
    public CheckRecordAccountConfigVO detail(@PathVariable String configId) {
        return CheckRecordAccountConfigService.detail(configId);
    }
}
