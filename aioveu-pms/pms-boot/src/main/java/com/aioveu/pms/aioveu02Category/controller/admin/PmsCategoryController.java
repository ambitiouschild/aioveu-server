package com.aioveu.pms.aioveu02Category.controller.admin;

import com.aioveu.common.result.PageResult;
import com.aioveu.pms.aioveu02Category.model.form.PmsCategoryForm;
import com.aioveu.pms.aioveu02Category.model.query.PmsCategoryQuery;
import com.aioveu.pms.aioveu02Category.model.vo.PmsCategoryVO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.aioveu.common.result.Result;
import com.aioveu.common.web.model.Option;
import com.aioveu.pms.aioveu02Category.model.entity.PmsCategory;
import com.aioveu.pms.model.vo.CategoryVO;
import com.aioveu.pms.aioveu02Category.service.PmsCategoryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Description: TODO Admin-商品分类控制器
 * @Author: 雒世松
 * @Date: 2025/6/5 18:28
 * @param
 * @return:
 **/

@Tag(name = "Admin-商品分类接口")
@RestController
//@RequestMapping("/api/v1/categories")
@RequestMapping("/api/v1/pms-category")
@RequiredArgsConstructor
public class PmsCategoryController {

    private final PmsCategoryService pmsCategoryService;

    @Operation(summary = "商品分类列表")
    @GetMapping("/categories")
    public Result<List<CategoryVO>> getCategoryList() {
        List<CategoryVO> list = pmsCategoryService.getCategoryList(null);
        return Result.success(list);
    }

    @Operation(summary = "商品分类级联列表")
    @GetMapping("/options")
    public Result getCategoryOptions() {
        List<Option> list = pmsCategoryService.getCategoryOptions();
        return Result.success(list);
    }

    @Operation(summary = "商品分类详情")
    @GetMapping("/{id}")
    public Result detail(
            @Parameter(name = "商品分类ID") @PathVariable Long id
    ) {
        PmsCategory category = pmsCategoryService.getById(id);
        return Result.success(category);
    }

    @Operation(summary = "商品分类分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsCategory:pms-category:query')")
    public PageResult<PmsCategoryVO> getPmsCategoryPage(PmsCategoryQuery queryParams ) {
        IPage<PmsCategoryVO> result = pmsCategoryService.getPmsCategoryPage(queryParams);
        return PageResult.success(result);
    }



//    @Operation(summary = "新增商品分类")
//    @PostMapping
//    public Result addCategory(@RequestBody PmsCategory category) {
//        Long id = pmsCategoryService.saveCategory(category);
//        return Result.success(id);
//    }

    @Operation(summary = "新增商品分类")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsCategory:pms-category:add')")
    public Result<Void> savePmsCategory(@RequestBody @Valid PmsCategoryForm formData ) {
        boolean result = pmsCategoryService.savePmsCategory(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取商品分类表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsCategory:pms-category:edit')")
    public Result<PmsCategoryForm> getPmsCategoryForm(
            @Parameter(description = "商品分类ID") @PathVariable Long id
    ) {
        PmsCategoryForm formData = pmsCategoryService.getPmsCategoryFormData(id);
        return Result.success(formData);
    }


//    @Operation(summary = "修改商品分类")
//    @PutMapping(value = "/{id}")
//    public Result update(
//            @Parameter(name = "商品分类ID") @PathVariable Long id,
//            @RequestBody PmsCategory category
//    ) {
//        category.setId(id);
//        id = pmsCategoryService.saveCategory(category);
//        return Result.success(id);
//    }

    @Operation(summary = "修改商品分类")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsCategory:pms-category:edit')")
    public Result<Void> updatePmsCategory(
            @Parameter(description = "商品分类ID") @PathVariable Long id,
            @RequestBody @Validated PmsCategoryForm formData
    ) {
        boolean result = pmsCategoryService.updatePmsCategory(id, formData);
        return Result.judge(result);
    }


//    @Operation(summary = "删除商品分类")
//    @DeleteMapping("/{ids}")
//    @CacheEvict(value = "pms", key = "'categoryList'")
//    public Result delete(@PathVariable String ids) {
//        List<String> categoryIds = Arrays.asList(ids.split(","));
//        pmsCategoryService.remove(new LambdaQueryWrapper<PmsCategoryAttribute>().in(CollectionUtil.isNotEmpty(categoryIds),
//                PmsCategoryAttribute::getCategoryId, categoryIds));
//        boolean result = pmsCategoryService.removeByIds(categoryIds);
//        return Result.judge(result);
//    }

    @Operation(summary = "删除商品分类")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsCategory:pms-category:delete')")
    public Result<Void> deletePmsCategorys(
            @Parameter(description = "商品分类ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = pmsCategoryService.deletePmsCategorys(ids);
        return Result.judge(result);
    }



    @Operation(summary = "选择性修改商品分类")
    @PatchMapping(value = "/{id}")
//    @CacheEvict(value = "pms", key = "'categoryList'")
    public Result patch(@PathVariable Long id, @RequestBody PmsCategory category) {
        LambdaUpdateWrapper<PmsCategory> updateWrapper = new LambdaUpdateWrapper<PmsCategory>()
                .eq(PmsCategory::getId, id);
        updateWrapper.set(category.getVisible() != null, PmsCategory::getVisible, category.getVisible());
        boolean result = pmsCategoryService.update(updateWrapper);
        return Result.judge(result);
    }
}
