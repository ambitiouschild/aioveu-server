package com.aioveu.pms.controller.app;

import com.aioveu.common.result.Result;
import com.aioveu.pms.model.vo.CategoryVO;
import com.aioveu.pms.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: TODO 商品分类控制器
 * @Author: 雒世松
 * @Date: 2025/6/5 18:29
 * @param
 * @return:
 **/

@Tag(name = "App-商品分类")
@RestController("appCategoryController")
@RequestMapping("/app-api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "分类列表")
    @GetMapping
    public Result list(@Parameter(name = "上级分类ID") Long parentId) {
        List<CategoryVO> list = categoryService.getCategoryList(parentId);
        return Result.success(list);
    }
}
