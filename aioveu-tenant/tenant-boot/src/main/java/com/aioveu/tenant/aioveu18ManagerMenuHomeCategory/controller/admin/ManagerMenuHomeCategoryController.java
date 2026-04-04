package com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.controller.admin;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.model.form.ManagerMenuHomeCategoryForm;
import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.model.query.ManagerMenuHomeCategoryQuery;
import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.model.vo.ManagerMenuHomeCategoryVo;
import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.service.ManagerMenuHomeCategoryService;
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
 * @ClassName: ManagerMenuHomeCategoryController
 * @Description TODO 管理端app首页分类配置前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/4 13:45
 * @Version 1.0
 **/
@Tag(name = "管理端app首页分类配置接口")
@RestController
@RequestMapping("/api/v1/manager-menu-home-category")
@RequiredArgsConstructor
public class ManagerMenuHomeCategoryController {

    private final ManagerMenuHomeCategoryService managerMenuHomeCategoryService;

    @Operation(summary = "管理端app首页分类配置分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallManagerMenuHomeCategory:manager-menu-home-category:list')")
    public PageResult<ManagerMenuHomeCategoryVo> getManagerMenuHomeCategoryPage(ManagerMenuHomeCategoryQuery queryParams ) {
        IPage<ManagerMenuHomeCategoryVo> result = managerMenuHomeCategoryService.getManagerMenuHomeCategoryPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增管理端app首页分类配置")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallManagerMenuHomeCategory:manager-menu-home-category:create')")
    public Result<Void> saveManagerMenuHomeCategory(@RequestBody @Valid ManagerMenuHomeCategoryForm formData ) {
        boolean result = managerMenuHomeCategoryService.saveManagerMenuHomeCategory(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取管理端app首页分类配置表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallManagerMenuHomeCategory:manager-menu-home-category:update')")
    public Result<ManagerMenuHomeCategoryForm> getManagerMenuHomeCategoryForm(
            @Parameter(description = "管理端app首页分类配置ID") @PathVariable Long id
    ) {
        ManagerMenuHomeCategoryForm formData = managerMenuHomeCategoryService.getManagerMenuHomeCategoryFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改管理端app首页分类配置")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallManagerMenuHomeCategory:manager-menu-home-category:update')")
    public Result<Void> updateManagerMenuHomeCategory(
            @Parameter(description = "管理端app首页分类配置ID") @PathVariable Long id,
            @RequestBody @Validated ManagerMenuHomeCategoryForm formData
    ) {
        boolean result = managerMenuHomeCategoryService.updateManagerMenuHomeCategory(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除管理端app首页分类配置")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallManagerMenuHomeCategory:manager-menu-home-category:delete')")
    public Result<Void> deleteManagerMenuHomeCategorys(
            @Parameter(description = "管理端app首页分类配置ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = managerMenuHomeCategoryService.deleteManagerMenuHomeCategorys(ids);
        return Result.judge(result);
    }
}
