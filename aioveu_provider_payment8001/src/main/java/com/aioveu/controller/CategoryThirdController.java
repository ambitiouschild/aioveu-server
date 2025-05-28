package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.CategoryThird;
import com.aioveu.service.CategoryThirdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description
 * @author: xiaoyao
 * @date: 2025/09/28
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/categoryThird")
public class CategoryThirdController {

    @Autowired
    CategoryThirdService categoryThirdService;

    @GetMapping("/condition")
    public IPage<CategoryThird> condition(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) Integer parentId,
            @RequestParam(required = false) Integer companyId,
            @RequestParam(required = false) Integer storeId,
            @RequestParam(required = false) String name
    ) {
        return categoryThirdService.getCategoryThirdByCondition(page, size, parentId, companyId, storeId, name);
    }

    @PostMapping("")
    public Integer addCategory(@RequestBody CategoryThird dataDTO) {
        return categoryThirdService.addCategory(dataDTO);
    }

    @PutMapping("")
    public Integer modifyCategoryMessage(@RequestBody CategoryThird dataDTO) {
        return categoryThirdService.modifyCategoryMessage(dataDTO);
    }

    @DeleteMapping("/{id}")
    public Integer deleteCategory(@PathVariable long id) {
        return categoryThirdService.deleteCategory(id);
    }

}
