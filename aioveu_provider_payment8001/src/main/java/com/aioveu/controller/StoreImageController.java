package com.aioveu.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.StoreImage;
import com.aioveu.service.StoreImageService;
import com.aioveu.vo.StoreImageDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/api/v1/store-image")
@RestController
public class StoreImageController {

    private final StoreImageService storeImageService;

    @Autowired
    private StoreImageService storeImageImageService;


    public StoreImageController(StoreImageService storeImageService) {
        this.storeImageService = storeImageService;
    }

    @GetMapping("")
    public IPage<StoreImageDetailVO> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                          @RequestParam(required = false, defaultValue = "10") Integer size,
                                          @RequestParam Long storeId) {
        return storeImageImageService.getManagerAll(page, size, storeId);
    }

    @GetMapping("/{id}")
    public StoreImageDetailVO detail(@PathVariable Long id) {
        return storeImageImageService.managerDetail(id);
    }

    @PutMapping("")
    public boolean update(@Valid @RequestBody StoreImage storeImage) {
        return storeImageImageService.saveOrUpdate(storeImage);
    }

    @PostMapping("")
    public boolean create(@Valid @RequestBody StoreImage storeImage){
        return storeImageImageService.save(storeImage);
    }

    @DeleteMapping("/{id}")
    public boolean imageDelete(@PathVariable Long id) {
        return storeImageImageService.deleteImage(id);
    }





}
