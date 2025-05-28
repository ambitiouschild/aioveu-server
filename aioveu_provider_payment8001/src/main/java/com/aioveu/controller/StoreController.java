package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.Store;
import com.aioveu.form.StoreBindUserPhoneForm;
import com.aioveu.form.StoreForm;
import com.aioveu.service.CodeService;
import com.aioveu.service.CompanyStoreUserService;
import com.aioveu.service.StoreService;
import com.aioveu.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/23 0023 16:00
 */
@Slf4j
@RequestMapping("/api/v1/store")
@RestController
public class StoreController {

    private final StoreService storeService;

    private CompanyStoreUserService companyStoreUserService;

    @Autowired
    private CodeService codeService;

    public StoreController(StoreService storeService, CompanyStoreUserService companyStoreUserService) {
        this.storeService = storeService;
        this.companyStoreUserService = companyStoreUserService;
    }

    @GetMapping("")
    public IPage<StoreForm> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                             @RequestParam(required = false, defaultValue = "10") Integer size,
                             @RequestParam(required = false) Integer status,
                             @RequestParam(required = false) Long companyId,
                             @RequestParam(required = false) String keyword) {
        return storeService.getAll(page, size, companyId, keyword, status);
    }

    @GetMapping("/{id}")
    public StoreForm detail(@PathVariable Long id) {
        return storeService.detail(id);
    }

    @PutMapping("")
    public boolean update(@Valid @RequestBody StoreForm store) {
        return storeService.createOrUpdate(store);
    }

    @PostMapping("")
    public boolean create(@Valid @RequestBody StoreForm store) {
       return storeService.createOrUpdate(store);
    }

    @PutMapping("/status")
    public boolean status(@RequestParam Long id, @RequestParam Integer status) {
        return storeService.changeStatus(id, status);
    }

    @GetMapping("mine/{userId}")
    public List<StoreSimpleVO> getStoreByUserId(@PathVariable String userId) {
        return companyStoreUserService.getStoreByUserId(userId);
    }

    /**
     * 小程序店铺列表
     *
     * @param param
     * @return
     */
    @PostMapping("/mini-list")
    public IPage<StoreMiniVO> miniList(@RequestParam Map<String, Object> param) throws Exception {
        return storeService.getMiniList(param);
    }

    /**
     * 小程序店铺详情
     * @return
     */
    @GetMapping("/mini-detail/{id}")
    public StoreMiniDetailVO miniDetail(@PathVariable Long id) {
        return storeService.getMiniDetail(id);
    }

    /**
     * 查询店铺名称
     *
     * @return
     */
    @GetMapping("/mini-name")
    public List<Store> miniName() {
        return storeService.getMiniName();
    }

    @GetMapping("/category/{id}")
    public List<CategoryBaseVO> getStoreCategory(@PathVariable Long id) {
        return storeService.getStoreCategory(id);
    }

    @GetMapping("/nearby")
    public List<NearbyStoreVO> nearby(@RequestParam Double longitude, @RequestParam Double latitude) {
        return storeService.getNearbyStore(longitude, latitude);
    }

    @GetMapping("/obtain")
    public List<UserPhoneVO> getStoreBindUserPhone(@RequestParam(required = false) Long companyId,@RequestParam(required = false)  Long storeId){
        return storeService.getStoreBindUserPhone(companyId,storeId);
    }
    @GetMapping("/get")
    public List<UserPhoneVO> getStoreBindUserPhoneOne(@RequestParam(required = false)  Long id){
        return storeService.getStoreBindUserPhoneOne(id);
    }

    @PutMapping("modify")
    public Boolean modifyStoreBindUserPhone(@RequestBody StoreBindUserPhoneForm storeBindUserPhoneForm){
        return storeService.modifyStoreBindUserPhone(storeBindUserPhoneForm);
    }
    @PostMapping("modify")
    public Boolean modifyStoreBindUser(@RequestBody StoreBindUserPhoneForm storeBindUserPhoneForm){
        return storeService.modifyStoreBindUserPhone(storeBindUserPhoneForm);
    }

    @DeleteMapping("/del/{id}")
    public Boolean deleteStoreBindUserPhone(@PathVariable Long id){
        return storeService.deleteStoreBindUserPhone(id);
    }

    @GetMapping("/store-code/{id}")
    public String toStoreCode(@PathVariable Long id) {
        return codeService.toStoreCode(id);
    }

    @GetMapping("/card-list/{id}")
    public IPage<StoreVipCardVO> storeCardList(@PathVariable Long id, @RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        return storeService.getStoreCardList(page, size, id);
    }

}
