package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.form.SyncDataAccountConfigForm;
import com.aioveu.service.SyncDataAccountConfigService;
import com.aioveu.vo.FieldPlanVO;
import com.aioveu.vo.SyncDataAccountConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 同步数据账号配置接口
 * @Author： yao
 * @Date： 2024/12/18 12:03
 * @Describe：
 */
@Slf4j
@RequestMapping("/api/v1/sync-data-account")
@RestController
public class SyncDataAccountConfigController {

    @Autowired
    SyncDataAccountConfigService syncDataAccountConfigService;

    @GetMapping("")
    public IPage<SyncDataAccountConfigVO> pageList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                   @RequestParam(required = false, defaultValue = "10") Integer size,
                                                   @RequestParam Long storeId) {
        return syncDataAccountConfigService.getPageList(page, size, storeId);
    }

    @PutMapping("/status")
    public boolean updateStatus(@RequestParam String id, @RequestParam Integer status) {
        return syncDataAccountConfigService.changeStatus(id, status);
    }

    @PostMapping("")
    public String save(@RequestBody @Valid SyncDataAccountConfigForm form){
        return syncDataAccountConfigService.saveConfig(form);
    }

    @GetMapping("/{configId}")
    public SyncDataAccountConfigVO detail(@PathVariable String configId) {
        return syncDataAccountConfigService.detail(configId);
    }

    @GetMapping("/login-status/{id}")
    public String loginStatus(@PathVariable String id) {
        return syncDataAccountConfigService.getLoginStatus(id);
    }

    @PutMapping("/login-code")
    public Boolean loginCode(@RequestParam String id, @RequestParam String code) {
        return syncDataAccountConfigService.setLoginCode(id, code);
    }

    /**
     * 全量手动同步，具体某个第三方平台全量同步到趣数
     * @param configId
     * @return
     */
    @GetMapping("/sync-data/{configId}")
    public boolean manualSyncData(@PathVariable String configId) {
        return syncDataAccountConfigService.syncFullPlatform2QsById(configId);
    }

    /**
     * 非全量同步 同步最新数据，点击首页浮标 第三方平台锁场订单 同步到趣数
     * 防止用户多次点击，增加redis校验，同一配置，只允许同一时间执行一次
     * @param storeId
     * @return
     */
    @GetMapping("/sync-field")
    public List<FieldPlanVO> latestSyncData(@RequestParam Long storeId) {
        return syncDataAccountConfigService.syncDataByStoreId(storeId);
    }

    /**
     * 全量同步 趣数锁场 同步到 指定第三方平台
     * @param storeId
     * @param platformCode
     * @return
     */
    @PutMapping("/sync-qs-platform")
    public boolean syncFullQs2Platform(@RequestParam Long storeId, @RequestParam String platformCode) {
        return syncDataAccountConfigService.syncFullQs2Platform(storeId, platformCode);
    }

    /**
     * 双向全量同步 第三方平台锁场同步到趣数 最后趣数同步到第三方平台 用户客户第一次上线做全量同步
     * @param storeId
     * @return
     */
    @PutMapping("/full-sync")
    public boolean firstFullSync(@RequestParam Long storeId) {
        return syncDataAccountConfigService.firstFullSync(storeId);
    }

    /**
     * 平台初始化
     * @param storeId
     * @return
     */
    @GetMapping("/init")
    public boolean init(@RequestParam Long storeId, @RequestParam String platformCode) {
        return syncDataAccountConfigService.init(storeId, platformCode);
    }

    @PutMapping("/sync-venue")
    public boolean syncVenue(@RequestParam Long storeId, @RequestParam String platformCode) {
        return syncDataAccountConfigService.syncVenue(storeId, platformCode);
    }

}
