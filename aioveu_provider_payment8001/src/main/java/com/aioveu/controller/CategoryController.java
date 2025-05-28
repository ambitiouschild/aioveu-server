package com.aioveu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.Category;
import com.aioveu.service.CategoryService;
import com.aioveu.vo.CategoryBaseVO;
import com.aioveu.vo.CategoryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public List<Category> list() {
        return categoryService.list(new QueryWrapper<Category>()
                .eq("status", 1)
                .orderByAsc("priority")
        );
    }

    @GetMapping("/{code}")
    public List<CategoryBaseVO> getByCode(@PathVariable String code) {
        return categoryService.getCategoryListByCode(code);
    }

    @GetMapping("/getTreeCategory")
    public List<Category> getTreeCategory() {
        return categoryService.getTreeCategory();
    }

    @GetMapping("/selCondition")
    public IPage<CategoryVo> selCondition(@RequestParam(required = false) Integer parentId,
                                           @RequestParam(required = false) Integer id,
                                           @RequestParam(required = false, defaultValue = "1") int page,
                                           @RequestParam(required = false, defaultValue = "10") int size) {
        return categoryService.selCondition(page,size,parentId,id);
    }

    @PostMapping("")
    public Integer addCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }

    @PutMapping("")
    public Integer modifyCategoryMessage(@RequestBody Category dataDTO){
        return categoryService.modifyCategoryMessage(dataDTO);
    }

    @DeleteMapping("/{id}")
    public Integer deleteCategory(@PathVariable long id){
        return categoryService.deleteCategory(id);
    }

}
