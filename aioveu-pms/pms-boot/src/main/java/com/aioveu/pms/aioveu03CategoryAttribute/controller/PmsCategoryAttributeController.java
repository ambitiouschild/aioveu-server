package com.aioveu.pms.aioveu03CategoryAttribute.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pms.aioveu03CategoryAttribute.model.entity.PmsCategoryAttribute;
import com.aioveu.pms.aioveu03CategoryAttribute.model.form.PmsCategoryAttributeForm;
import com.aioveu.pms.aioveu03CategoryAttribute.model.query.PmsCategoryAttributeQuery;
import com.aioveu.pms.aioveu03CategoryAttribute.model.vo.PmsCategoryAttributeVO;
import com.aioveu.pms.aioveu03CategoryAttribute.service.PmsCategoryAttributeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
 * @ClassName: PmsCategoryAttributeController
 * @Description TODO  商品分类类型（规格，属性）前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/11 19:51
 * @Version 1.0
 **/

@Tag(name = "商品分类类型（规格，属性）接口")
@RestController
@RequestMapping("/api/v1/pms-category-attribute")
@RequiredArgsConstructor
public class PmsCategoryAttributeController {

    private final PmsCategoryAttributeService pmsCategoryAttributeService;

    @Operation(summary = "商品分类类型（规格，属性）分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsCategoryAttribute:pms-category-attribute:query')")
    public PageResult<PmsCategoryAttributeVO> getPmsCategoryAttributePage(PmsCategoryAttributeQuery queryParams ) {
        IPage<PmsCategoryAttributeVO> result = pmsCategoryAttributeService.getPmsCategoryAttributePage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary= "商品分类类型（规格，属性）列表")
    @GetMapping("/attributes")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsCategoryAttribute:pms-category-attribute:query')")
    public Result getAttributeList(
            @Parameter(name = "商品分类ID") Long categoryId,
            @Parameter(name = "类型（1：规格；2：属性）") Integer type
    ) {
        List<PmsCategoryAttribute> list = pmsCategoryAttributeService.list(new LambdaQueryWrapper<PmsCategoryAttribute>()
                .eq(categoryId != null, PmsCategoryAttribute::getCategoryId, categoryId)
                .eq(type != null, PmsCategoryAttribute::getType, type)
        );
        return Result.success(list);
    }

    @Operation(summary= "批量新增/修改")
    @PostMapping("/batch")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsCategoryAttribute:pms-category-attribute:add')")
    public Result saveBatch(@RequestBody PmsCategoryAttributeForm pmsCategoryAttributeForm) {
        boolean result = pmsCategoryAttributeService.saveBatch(pmsCategoryAttributeForm);
        return Result.judge(result);
    }

    @Operation(summary = "新增商品类型（规格，属性）")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsCategoryAttribute:pms-category-attribute:add')")
    public Result<Void> savePmsCategoryAttribute(@RequestBody @Valid PmsCategoryAttributeForm formData ) {
        boolean result = pmsCategoryAttributeService.savePmsCategoryAttribute(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取商品类型（规格，属性）表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsCategoryAttribute:pms-category-attribute:edit')")
    public Result<PmsCategoryAttributeForm> getPmsCategoryAttributeForm(
            @Parameter(description = "商品类型（规格，属性）ID") @PathVariable Long id
    ) {
        PmsCategoryAttributeForm formData = pmsCategoryAttributeService.getPmsCategoryAttributeFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改商品类型（规格，属性）")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsCategoryAttribute:pms-category-attribute:edit')")
    public Result<Void> updatePmsCategoryAttribute(
            @Parameter(description = "商品类型（规格，属性）ID") @PathVariable Long id,
            @RequestBody @Validated PmsCategoryAttributeForm formData
    ) {
        boolean result = pmsCategoryAttributeService.updatePmsCategoryAttribute(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除商品类型（规格，属性）")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsCategoryAttribute:pms-category-attribute:delete')")
    public Result<Void> deletePmsCategoryAttributes(
            @Parameter(description = "商品类型（规格，属性）ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = pmsCategoryAttributeService.deletePmsCategoryAttributes(ids);
        return Result.judge(result);
    }
}
