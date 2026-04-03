package com.aioveu.tenant.aioveu16ManagerMenuCategory.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.form.ManagerMenuCategoryForm;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.query.ManagerMenuCategoryQuery;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.vo.ManagerMenuCategoryVo;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.service.ManagerMenuCategoryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: ManagerMenuCategoryController
 * @Description TODO 管理端菜单分类（多租户）前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/3 17:15
 * @Version 1.0
 **/
@Tag(name = "管理端菜单分类（多租户）接口")
@RestController
@RequestMapping("/api/v1/manager-menu-category")
@RequiredArgsConstructor
public class ManagerMenuCategoryController {

    private final ManagerMenuCategoryService managerMenuCategoryService;

    @Operation(summary = "管理端菜单分类（多租户）分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallManagerMenuCategory:manager-menu-category:list')")
    public PageResult<ManagerMenuCategoryVo> getManagerMenuCategoryPage(ManagerMenuCategoryQuery queryParams ) {
        IPage<ManagerMenuCategoryVo> result = managerMenuCategoryService.getManagerMenuCategoryPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增管理端菜单分类（多租户）")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallManagerMenuCategory:manager-menu-category:create')")
    public Result<Void> saveManagerMenuCategory(@RequestBody @Valid ManagerMenuCategoryForm formData ) {
        boolean result = managerMenuCategoryService.saveManagerMenuCategory(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取管理端菜单分类（多租户）表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallManagerMenuCategory:manager-menu-category:update')")
    public Result<ManagerMenuCategoryForm> getManagerMenuCategoryForm(
            @Parameter(description = "管理端菜单分类（多租户）ID") @PathVariable Long id
    ) {
        ManagerMenuCategoryForm formData = managerMenuCategoryService.getManagerMenuCategoryFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改管理端菜单分类（多租户）")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallManagerMenuCategory:manager-menu-category:update')")
    public Result<Void> updateManagerMenuCategory(
            @Parameter(description = "管理端菜单分类（多租户）ID") @PathVariable Long id,
            @RequestBody @Validated ManagerMenuCategoryForm formData
    ) {
        boolean result = managerMenuCategoryService.updateManagerMenuCategory(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除管理端菜单分类（多租户）")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallManagerMenuCategory:manager-menu-category:delete')")
    public Result<Void> deleteManagerMenuCategorys(
            @Parameter(description = "管理端菜单分类（多租户）ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = managerMenuCategoryService.deleteManagerMenuCategorys(ids);
        return Result.judge(result);
    }
}
