package com.aioveu.pms.aioveu05Sku.controller.admin;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pms.aioveu05Sku.model.entity.PmsSku;
import com.aioveu.pms.aioveu05Sku.model.form.PmsSkuForm;
import com.aioveu.pms.aioveu05Sku.model.query.PmsSkuQuery;
import com.aioveu.pms.aioveu05Sku.model.vo.PmsSkuVO;
import com.aioveu.pms.aioveu05Sku.service.PmsSkuService;
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
 * @Description: TODO Admin-商品SKU控制层
 * @Author: 雒世松
 * @Date: 2025/6/5 18:28
 * @param
 * @return:
 **/

@Tag(name = "Admin-商品SKU接口")
@RestController
//@RequestMapping("/api/v1/sku")
@RequestMapping("/api/v1/pms-sku")
@RequiredArgsConstructor
public class PmsSkuController {
    private final PmsSkuService pmsSkuService;

    @Operation(summary = "商品SKU详情")
    @GetMapping("/{skuId}")
    public Result getSkuDetail(
            @Parameter(name = "SkuId") @PathVariable Long skuId
    ) {
        PmsSku sku = pmsSkuService.getById(skuId);
        return Result.success(sku);
    }

    @Operation(summary = "修改SKU")
    @PutMapping(value = "/{skuId}")
    public Result updateSku(
            @Parameter(name = "SkuId") @PathVariable Long skuId,
            @RequestBody PmsSku sku
    ) {
        boolean result = pmsSkuService.updateById(sku);
        return Result.judge(result);
    }

    @Operation(summary = "商品库存分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsSku:pms-sku:query')")
    public PageResult<PmsSkuVO> getPmsSkuPage(PmsSkuQuery queryParams ) {
        IPage<PmsSkuVO> result = pmsSkuService.getPmsSkuPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增商品库存")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsSku:pms-sku:add')")
    public Result<Void> savePmsSku(@RequestBody @Valid PmsSkuForm formData ) {
        boolean result = pmsSkuService.savePmsSku(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取商品库存表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsSku:pms-sku:edit')")
    public Result<PmsSkuForm> getPmsSkuForm(
            @Parameter(description = "商品库存ID") @PathVariable Long id
    ) {
        PmsSkuForm formData = pmsSkuService.getPmsSkuFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改商品库存")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsSku:pms-sku:edit')")
    public Result<Void> updatePmsSku(
            @Parameter(description = "商品库存ID") @PathVariable Long id,
            @RequestBody @Validated PmsSkuForm formData
    ) {
        boolean result = pmsSkuService.updatePmsSku(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除商品库存")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsSku:pms-sku:delete')")
    public Result<Void> deletePmsSkus(
            @Parameter(description = "商品库存ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = pmsSkuService.deletePmsSkus(ids);
        return Result.judge(result);
    }
}
