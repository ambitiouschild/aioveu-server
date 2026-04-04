package com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.controller.admin;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.model.form.ManagerMenuHomeBannerForm;
import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.model.query.ManagerMenuHomeBannerQuery;
import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.model.vo.ManagerMenuHomeBannerVo;
import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.service.ManagerMenuHomeBannerService;
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
 * @ClassName: ManagerMenuHomeBannerController
 * @Description TODO 管理端app首页滚播栏前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/4 15:44
 * @Version 1.0
 **/
@Tag(name = "管理端app首页滚播栏接口")
@RestController
@RequestMapping("/api/v1/manager-menu-home-banner")
@RequiredArgsConstructor
public class ManagerMenuHomeBannerController {

    private final ManagerMenuHomeBannerService managerMenuHomeBannerService;

    @Operation(summary = "管理端app首页滚播栏分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallManagerMenuHomeBanner:manager-menu-home-banner:list')")
    public PageResult<ManagerMenuHomeBannerVo> getManagerMenuHomeBannerPage(ManagerMenuHomeBannerQuery queryParams ) {
        IPage<ManagerMenuHomeBannerVo> result = managerMenuHomeBannerService.getManagerMenuHomeBannerPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增管理端app首页滚播栏")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallManagerMenuHomeBanner:manager-menu-home-banner:create')")
    public Result<Void> saveManagerMenuHomeBanner(@RequestBody @Valid ManagerMenuHomeBannerForm formData ) {
        boolean result = managerMenuHomeBannerService.saveManagerMenuHomeBanner(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取管理端app首页滚播栏表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallManagerMenuHomeBanner:manager-menu-home-banner:update')")
    public Result<ManagerMenuHomeBannerForm> getManagerMenuHomeBannerForm(
            @Parameter(description = "管理端app首页滚播栏ID") @PathVariable Long id
    ) {
        ManagerMenuHomeBannerForm formData = managerMenuHomeBannerService.getManagerMenuHomeBannerFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改管理端app首页滚播栏")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallManagerMenuHomeBanner:manager-menu-home-banner:update')")
    public Result<Void> updateManagerMenuHomeBanner(
            @Parameter(description = "管理端app首页滚播栏ID") @PathVariable Long id,
            @RequestBody @Validated ManagerMenuHomeBannerForm formData
    ) {
        boolean result = managerMenuHomeBannerService.updateManagerMenuHomeBanner(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除管理端app首页滚播栏")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallManagerMenuHomeBanner:manager-menu-home-banner:delete')")
    public Result<Void> deleteManagerMenuHomeBanners(
            @Parameter(description = "管理端app首页滚播栏ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = managerMenuHomeBannerService.deleteManagerMenuHomeBanners(ids);
        return Result.judge(result);
    }
}
