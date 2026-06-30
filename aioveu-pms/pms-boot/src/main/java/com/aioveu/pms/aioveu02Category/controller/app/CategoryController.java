package com.aioveu.pms.aioveu02Category.controller.app;

import com.aioveu.common.result.Result;
import com.aioveu.pms.model.vo.CategoryVO;
import com.aioveu.pms.aioveu02Category.service.PmsCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO 商品分类控制器
 * @Author: 雒世松
 * @Date: 2025/6/5 18:29
 * @param
 * @return:
 **/

@Slf4j
@Tag(name = "App-商品分类")
@RestController("appCategoryController")
@RequestMapping("/aioveu/api/v8/app/pms/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final PmsCategoryService pmsCategoryService;

    @Operation(summary = "分类列表")
    @GetMapping("/goodsCategories")
    public Result list(@Parameter(name = "上级分类ID") Long parentId) {

        List<CategoryVO> list = pmsCategoryService.getCategoryListForApp(parentId);

        log.info("【auth-app-goodsCategories】根据tenantI过滤获取商品:{}",list);
        return Result.success(list);
    }

}
