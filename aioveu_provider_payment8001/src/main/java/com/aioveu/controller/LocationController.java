package com.aioveu.controller;

import com.aioveu.form.LocationRecordForm;
import com.aioveu.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping("/record")
    public Boolean record(@Valid @RequestBody LocationRecordForm form) {
        return locationService.record(form);
    }
}
