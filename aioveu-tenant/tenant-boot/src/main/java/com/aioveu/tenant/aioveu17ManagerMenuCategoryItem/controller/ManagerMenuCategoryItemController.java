package com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.form.ManagerMenuCategoryItemForm;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.query.ManagerMenuCategoryItemQuery;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.vo.ManagerMenuCategoryItemVo;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.service.ManagerMenuCategoryItemService;
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
 * @ClassName: ManagerMenuCategoryItemController
 * @Description TODO 管理系统工作台菜单项（多租户支持）前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/3 17:30
 * @Version 1.0
 **/
@Tag(name = "管理系统工作台菜单项（多租户支持）接口")
@RestController
@RequestMapping("/api/v1/manager-menu-category-item")
@RequiredArgsConstructor
public class ManagerMenuCategoryItemController {

    private final ManagerMenuCategoryItemService managerMenuCategoryItemService;

    @Operation(summary = "管理系统工作台菜单项（多租户支持）分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallManagerMenuCategoryItem:manager-menu-category-item:list')")
    public PageResult<ManagerMenuCategoryItemVo> getManagerMenuCategoryItemPage(ManagerMenuCategoryItemQuery queryParams ) {
        IPage<ManagerMenuCategoryItemVo> result = managerMenuCategoryItemService.getManagerMenuCategoryItemPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增管理系统工作台菜单项（多租户支持）")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallManagerMenuCategoryItem:manager-menu-category-item:create')")
    public Result<Void> saveManagerMenuCategoryItem(@RequestBody @Valid ManagerMenuCategoryItemForm formData ) {
        boolean result = managerMenuCategoryItemService.saveManagerMenuCategoryItem(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取管理系统工作台菜单项（多租户支持）表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallManagerMenuCategoryItem:manager-menu-category-item:update')")
    public Result<ManagerMenuCategoryItemForm> getManagerMenuCategoryItemForm(
            @Parameter(description = "管理系统工作台菜单项（多租户支持）ID") @PathVariable Long id
    ) {
        ManagerMenuCategoryItemForm formData = managerMenuCategoryItemService.getManagerMenuCategoryItemFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改管理系统工作台菜单项（多租户支持）")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallManagerMenuCategoryItem:manager-menu-category-item:update')")
    public Result<Void> updateManagerMenuCategoryItem(
            @Parameter(description = "管理系统工作台菜单项（多租户支持）ID") @PathVariable Long id,
            @RequestBody @Validated ManagerMenuCategoryItemForm formData
    ) {
        boolean result = managerMenuCategoryItemService.updateManagerMenuCategoryItem(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除管理系统工作台菜单项（多租户支持）")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallManagerMenuCategoryItem:manager-menu-category-item:delete')")
    public Result<Void> deleteManagerMenuCategoryItems(
            @Parameter(description = "管理系统工作台菜单项（多租户支持）ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = managerMenuCategoryItemService.deleteManagerMenuCategoryItems(ids);
        return Result.judge(result);
    }
}
