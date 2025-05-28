package com.aioveu.controller;

import com.aioveu.entity.VenueField;
import com.aioveu.form.ActiveVenueFieldForm;
import com.aioveu.service.VenueFieldService;
import com.aioveu.vo.VenueFieldVO;
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
@RequestMapping("/api/v1/venue-field")
@RestController
public class VenueFieldController {

    @Autowired
    private VenueFieldService venueFieldService;

    @GetMapping("/{venueId}")
    public List<VenueFieldVO> fieldListByVenueId(@PathVariable Long venueId, @RequestParam Long companyId, @RequestParam String day) {
        return venueFieldService.getFieldByVenueId(venueId, companyId, day);
    }

    @PostMapping("/getActiveVenueFieldList")
    public List<VenueField> getActiveVenueFieldList(@RequestBody ActiveVenueFieldForm form) {
        return venueFieldService.getActiveVenueFieldList(form);
    }
}
