package com.aioveu.controller;

import com.aioveu.entity.GradeAge;
import com.aioveu.service.GradeAgeService;
import com.aioveu.vo.IdNameVO;
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
@RestController
@RequestMapping("/api/v1/grade-age")
public class GradeAgeController {

    @Autowired
    private GradeAgeService gradeAgeService;

    @GetMapping("/list/{storeId}")
    public List<IdNameVO> list(@PathVariable Long storeId) {
        return gradeAgeService.getByStoreId(storeId);
    }

    @GetMapping("/{id}")
    public GradeAge detail(@PathVariable Long id) {
        return gradeAgeService.getById(id);
    }

    @PostMapping("")
    public boolean create(@RequestBody GradeAge gradeAge) {
        return gradeAgeService.save(gradeAge);
    }

    @PutMapping("")
    public boolean update(@RequestBody GradeAge gradeAge) {
        return gradeAgeService.updateById(gradeAge);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return gradeAgeService.removeById(id);
    }

    



}
