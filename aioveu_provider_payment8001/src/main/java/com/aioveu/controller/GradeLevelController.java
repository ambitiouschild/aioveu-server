package com.aioveu.controller;

import com.aioveu.entity.GradeLevel;
import com.aioveu.service.GradeLevelService;
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
@RequestMapping("/api/v1/grade-level")
public class GradeLevelController {

    @Autowired
    private GradeLevelService gradeLevelService;

    @GetMapping("/list/{storeId}")
    public List<IdNameVO> list(@PathVariable Long storeId) {
        return gradeLevelService.getByStoreId(storeId);
    }

    @GetMapping("/{id}")
    public GradeLevel detail(@PathVariable Long id) {
        return gradeLevelService.getById(id);
    }

    @PutMapping("")
    public boolean update(@RequestBody GradeLevel gradeLevel) {
        return gradeLevelService.updateById(gradeLevel);
    }

    @PostMapping("")
    public boolean create(@RequestBody GradeLevel gradeLevel) {
        return gradeLevelService.save(gradeLevel);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return gradeLevelService.removeById(id);
    }

    



}
