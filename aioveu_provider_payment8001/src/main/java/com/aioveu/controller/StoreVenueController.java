package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.StoreVenue;
import com.aioveu.service.StoreVenueService;
import com.aioveu.vo.StoreVenueItemVO;
import com.aioveu.vo.StoreVenueVO;
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
@RequestMapping("/api/v1/store-venue")
@RestController
public class StoreVenueController {

    @Autowired
    private StoreVenueService storeVenueService;

    @GetMapping("/{storeId}")
    public IPage<StoreVenueItemVO> storeList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                             @RequestParam(required = false, defaultValue = "10") Integer size,
                                             @RequestParam(required = false) Integer status,
                                             @PathVariable Long storeId) {
        return storeVenueService.getByStoreId(page, size, status, storeId);
    }

    @GetMapping("getDetail/{venueId}")
    public StoreVenueVO getDetail(@PathVariable Long venueId) {
        return storeVenueService.getDetail(venueId);
    }

    @PostMapping("")
    public void saveStoreVenue(@RequestBody StoreVenueVO storeVenueVO) {
        storeVenueService.saveStoreVenue(storeVenueVO);
    }

    @PutMapping("/status")
    public boolean updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        return storeVenueService.changeStatus(id, status);
    }

}
