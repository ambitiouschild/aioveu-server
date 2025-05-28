package com.aioveu.controller;


import com.aioveu.entity.CoachCertificate;
import com.aioveu.service.CoachCertificateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/coach-certificate")
public class CoachCertificateController {

    @Autowired
    private CoachCertificateService coachCertificateService;


    @GetMapping("")
    public List<CoachCertificate> list(@RequestParam Long coachId) {
        return coachCertificateService.getByCoachId(coachId);
    }

    @PostMapping("")
    public Boolean create(@RequestBody List<CoachCertificate> coachCertificates) {
        return coachCertificateService.batchAdd(coachCertificates);
    }

    @DeleteMapping("/{id}")
    public boolean deleteCertificate(@PathVariable Long id) {
        return coachCertificateService.deleteCertificate(id);
    }


}
