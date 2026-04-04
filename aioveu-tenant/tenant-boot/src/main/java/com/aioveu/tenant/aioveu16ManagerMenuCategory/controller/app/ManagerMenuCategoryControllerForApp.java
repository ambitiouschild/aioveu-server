package com.aioveu.tenant.aioveu16ManagerMenuCategory.controller.app;

import com.aioveu.common.result.Result;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.form.ManagerMenuCategoryForm;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.vo.ManagerMenuCategoryWithItemsVO;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.service.ManagerMenuCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName: ManagerMenuCategoryControllerForApp
 * @Description TODO 管理端菜单分类（多租户）前端控制层forApp
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/3 21:24
 * @Version 1.0
 **/

@Tag(name = "小程序管理端菜单分类（多租户）接口")
@RestController
@RequestMapping("/app-api/v1/manager-menu-category")
@RequiredArgsConstructor
public class ManagerMenuCategoryControllerForApp {


    private final ManagerMenuCategoryService managerMenuCategoryService;


    /**
     * 获取用户的工作台菜单（包含分类和菜单项）
     */

    @Operation(summary = "获取用户的工作台菜单（包含分类和菜单项）")
    @GetMapping("/categories-with-items")
    public Result<List<ManagerMenuCategoryWithItemsVO>> getWorkbenchCategoriesWithItems() {
        List<ManagerMenuCategoryWithItemsVO> categories = managerMenuCategoryService.getManagerMenuCategoriesWithItems();
        return Result.success(categories);
    }


}
