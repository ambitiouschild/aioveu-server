package com.aioveu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.aioveu.entity.Poster;
import com.aioveu.service.PosterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/poster")
public class PosterController {

    @Autowired
    private PosterService posterService;

    @GetMapping("")
    public Poster get(@RequestParam(required = false) Long id) {
        if (id != null) {
            return posterService.getById(id);
        }
        return posterService.getOne(new QueryWrapper<Poster>()
                .eq("status", 1));
    }
}
