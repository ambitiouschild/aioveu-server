package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.Agreement;
import com.aioveu.service.AgreementService;
import com.aioveu.vo.AgreementDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/23 0023 16:00
 */
@Slf4j
@RequestMapping("/api/v1/agreement")
@RestController
public class AgreementController {

    private final AgreementService agreementService;

    @Autowired
    private AgreementService agreementImageService;

    public AgreementController(AgreementService agreementService) {
        this.agreementService = agreementService;
    }

    @GetMapping("")
    public IPage<AgreementDetailVO> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                     @RequestParam(required = false, defaultValue = "10") Integer size,
                                     @RequestParam Long companyId) {
        return agreementService.getManagerAll(page, size, companyId);
    }

    @GetMapping("/{id}")
    public AgreementDetailVO detail(@PathVariable Long id) {
        return agreementService.managerDetail(id);
    }

    @PutMapping("")
    public boolean update(@Valid @RequestBody Agreement agreement) {
        return agreementService.saveOrUpdate(agreement);
    }

    @PostMapping("")
    public boolean create(@Valid @RequestBody Agreement agreement){
        return agreementService.save(agreement);
    }

    @DeleteMapping("/{id}")
    public boolean imageDelete(@PathVariable Long id) {
        return agreementImageService.deleteImage(id);
    }



}
