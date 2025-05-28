package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.City;
import com.aioveu.entity.VipCard;
import com.aioveu.service.CityService;
import com.aioveu.service.VipCardService;
import com.aioveu.vo.FieldPlanVO;
import com.aioveu.vo.IdNameVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/vipcard")
public class VipCardController {

    @Autowired
    private VipCardService vipCardService;

    @GetMapping("list/{pCategoryCode}")
    public List<VipCard> list(@PathVariable String pCategoryCode, @RequestParam Long companyId) {
        return vipCardService.getActiveList(pCategoryCode, companyId);
    }

    @GetMapping("alllist/{pCategoryCode}")
    public List<VipCard> alllist(@PathVariable String pCategoryCode, @RequestParam Long companyId) {
        return vipCardService.getAllList(pCategoryCode, companyId);
    }

    @GetMapping("/{vipCardId}")
    public VipCard detail(@PathVariable Long vipCardId) {
        return vipCardService.getById(vipCardId);
    }

    @PostMapping("")
    public void saveVipCard(@RequestBody VipCard vipCard) {
        vipCardService.saveVipCard(vipCard);
    }

    @PutMapping("/status")
    public boolean updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        return vipCardService.changeStatus(id, status);
    }

    @PostMapping("/get-field-vip-price")
    public BigDecimal getFieldVipPrice(@RequestParam Long vipCardId, @RequestParam List<Long> fieldPlanIds) {
        return vipCardService.getFieldVipPrice(vipCardId, fieldPlanIds);
    }
}
