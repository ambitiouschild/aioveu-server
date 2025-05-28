package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.ExerciseImage;
import com.aioveu.service.ExerciseImageService;
import com.aioveu.vo.ExerciseImageDetailVO;
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
@RequestMapping("/api/v1/exerciseImage")
@RestController
public class ExerciseImageController {

    private final ExerciseImageService exerciseImageService;

    @Autowired
    private ExerciseImageService exerciseImageImageService;

    public ExerciseImageController(ExerciseImageService exerciseImageService) {
        this.exerciseImageService = exerciseImageService;
    }

    @GetMapping("")
    public IPage<ExerciseImageDetailVO> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                             @RequestParam(required = false, defaultValue = "10") Integer size,
                                     @RequestParam Long exerciseId) {
        return exerciseImageService.getManagerAll(page, size, exerciseId);
    }

    @GetMapping("/{id}")
    public ExerciseImageDetailVO detail(@PathVariable Long id) {
        return exerciseImageService.managerDetail(id);
    }

    @PutMapping("")
    public boolean update(@Valid @RequestBody ExerciseImage exerciseImage) {
        return exerciseImageService.saveOrUpdate(exerciseImage);
    }

    @PostMapping("")
    public boolean create(@Valid @RequestBody ExerciseImage exerciseImage){
        return exerciseImageService.save(exerciseImage);
    }

    @DeleteMapping("/{id}")
    public boolean imageDelete(@PathVariable Long id) {
        return exerciseImageImageService.deleteImage(id);
    }



}
