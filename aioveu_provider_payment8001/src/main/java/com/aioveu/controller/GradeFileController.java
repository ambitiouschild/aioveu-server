package com.aioveu.controller;

import com.aioveu.service.GradeFileService;
import com.aioveu.vo.SimpleFileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/grade-file")
public class GradeFileController {

    @Autowired
    private GradeFileService gradeFileService;

    @GetMapping("/list/{gradeId}")
    public List<SimpleFileVO> list(@PathVariable Long gradeId) {
        return gradeFileService.getByGradeId(gradeId);
    }

    @PostMapping("/{gradeId}")
    public boolean create(@PathVariable Long gradeId, MultipartFile file) throws Exception {
        return gradeFileService.upload(gradeId, file);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return gradeFileService.deleteById(id);
    }

    



}
