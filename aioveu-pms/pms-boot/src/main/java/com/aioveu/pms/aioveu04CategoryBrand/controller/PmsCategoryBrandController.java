package com.aioveu.pms.aioveu04CategoryBrand.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pms.aioveu04CategoryBrand.model.form.PmsCategoryBrandForm;
import com.aioveu.pms.aioveu04CategoryBrand.model.query.PmsCategoryBrandQuery;
import com.aioveu.pms.aioveu04CategoryBrand.model.vo.PmsCategoryBrandVO;
import com.aioveu.pms.aioveu04CategoryBrand.service.PmsCategoryBrandService;
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
 * @ClassName: PmsCategoryBrandController
 * @Description TODO 商品分类与品牌关联表前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/11 20:09
 * @Version 1.0
 **/

@Tag(name = "商品分类与品牌关联表接口")
@RestController
@RequestMapping("/api/v1/pms-category-brand")
@RequiredArgsConstructor
public class PmsCategoryBrandController {

    private final PmsCategoryBrandService pmsCategoryBrandService;

    @Operation(summary = "商品分类与品牌关联表分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsCategoryBrand:pms-category-brand:query')")
    public PageResult<PmsCategoryBrandVO> getPmsCategoryBrandPage(PmsCategoryBrandQuery queryParams ) {
        IPage<PmsCategoryBrandVO> result = pmsCategoryBrandService.getPmsCategoryBrandPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增商品分类与品牌关联表")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsCategoryBrand:pms-category-brand:add')")
    public Result<Void> savePmsCategoryBrand(@RequestBody @Valid PmsCategoryBrandForm formData ) {
        boolean result = pmsCategoryBrandService.savePmsCategoryBrand(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取商品分类与品牌关联表表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsCategoryBrand:pms-category-brand:edit')")
    public Result<PmsCategoryBrandForm> getPmsCategoryBrandForm(
            @Parameter(description = "商品分类与品牌关联表ID") @PathVariable Long id
    ) {
        PmsCategoryBrandForm formData = pmsCategoryBrandService.getPmsCategoryBrandFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改商品分类与品牌关联表")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsCategoryBrand:pms-category-brand:edit')")
    public Result<Void> updatePmsCategoryBrand(
            @Parameter(description = "商品分类与品牌关联表ID") @PathVariable Long id,
            @RequestBody @Validated PmsCategoryBrandForm formData
    ) {
        boolean result = pmsCategoryBrandService.updatePmsCategoryBrand(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除商品分类与品牌关联表")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsCategoryBrand:pms-category-brand:delete')")
    public Result<Void> deletePmsCategoryBrands(
            @Parameter(description = "商品分类与品牌关联表ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = pmsCategoryBrandService.deletePmsCategoryBrands(ids);
        return Result.judge(result);
    }
}
