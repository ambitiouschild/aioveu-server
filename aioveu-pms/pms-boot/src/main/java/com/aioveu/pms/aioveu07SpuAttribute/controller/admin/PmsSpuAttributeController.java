package com.aioveu.pms.aioveu07SpuAttribute.controller.admin;

import com.aioveu.common.result.PageResult;
import com.aioveu.pms.aioveu03CategoryAttribute.service.PmsCategoryAttributeService;
import com.aioveu.pms.aioveu07SpuAttribute.model.form.PmsSpuAttributeForm;
import com.aioveu.pms.aioveu07SpuAttribute.model.query.PmsSpuAttributeQuery;
import com.aioveu.pms.aioveu07SpuAttribute.model.vo.PmsSpuAttributeVO;
import com.aioveu.pms.aioveu07SpuAttribute.service.PmsSpuAttributeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.aioveu.common.result.Result;
import com.aioveu.pms.aioveu03CategoryAttribute.model.entity.PmsCategoryAttribute;
import com.aioveu.pms.aioveu03CategoryAttribute.model.form.PmsCategoryAttributeForm;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: TODO Admin-商品属性控制器
 * @Author: 雒世松
 * @Date: 2025/6/5 18:28
 * @param
 * @return:
 **/

@Tag(name = "Admin-商品类型（属性/规格）接口")
@RestController
//@RequestMapping("/api/v1/attributes")
@RequestMapping("/api/v1/pms-spu-attribute")
@Slf4j
@AllArgsConstructor
public class PmsSpuAttributeController {

    private PmsCategoryAttributeService pmsCategoryAttributeService;
    private final PmsSpuAttributeService pmsSpuAttributeService;

    @Operation(summary = "商品类型（属性/规格）分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsSpuAttribute:pms-spu-attribute:query')")
    public PageResult<PmsSpuAttributeVO> getPmsSpuAttributePage(PmsSpuAttributeQuery queryParams ) {
        IPage<PmsSpuAttributeVO> result = pmsSpuAttributeService.getPmsSpuAttributePage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增商品类型（属性/规格）")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsSpuAttribute:pms-spu-attribute:add')")
    public Result<Void> savePmsSpuAttribute(@RequestBody @Valid PmsSpuAttributeForm formData ) {
        boolean result = pmsSpuAttributeService.savePmsSpuAttribute(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取商品类型（属性/规格）表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsSpuAttribute:pms-spu-attribute:edit')")
    public Result<PmsSpuAttributeForm> getPmsSpuAttributeForm(
            @Parameter(description = "商品类型（属性/规格）ID") @PathVariable Long id
    ) {
        PmsSpuAttributeForm formData = pmsSpuAttributeService.getPmsSpuAttributeFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改商品类型（属性/规格）")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsSpuAttribute:pms-spu-attribute:edit')")
    public Result<Void> updatePmsSpuAttribute(
            @Parameter(description = "商品类型（属性/规格）ID") @PathVariable Long id,
            @RequestBody @Validated PmsSpuAttributeForm formData
    ) {
        boolean result = pmsSpuAttributeService.updatePmsSpuAttribute(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除商品类型（属性/规格）")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsSpuAttribute:pms-spu-attribute:delete')")
    public Result<Void> deletePmsSpuAttributes(
            @Parameter(description = "商品类型（属性/规格）ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = pmsSpuAttributeService.deletePmsSpuAttributes(ids);
        return Result.judge(result);
    }

    @Operation(summary= "属性列表")
    @GetMapping
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
    public Result saveBatch(@RequestBody PmsCategoryAttributeForm pmsCategoryAttributeForm) {
        boolean result = pmsCategoryAttributeService.saveBatch(pmsCategoryAttributeForm);
        return Result.judge(result);
    }
}
