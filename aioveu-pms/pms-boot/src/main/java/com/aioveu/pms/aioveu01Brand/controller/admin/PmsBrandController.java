package com.aioveu.pms.aioveu01Brand.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.aioveu.pms.aioveu01Brand.model.form.PmsBrandForm;
import com.aioveu.pms.aioveu01Brand.model.vo.PmsBrandVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pms.aioveu01Brand.model.entity.PmsBrand;
import com.aioveu.pms.aioveu01Brand.model.query.PmsBrandQuery;
import com.aioveu.pms.aioveu01Brand.service.PmsBrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: TODO 商品品牌前端控制层
 * @Author: 雒世松
 * @Date: 2025/6/5 18:28
 * @param
 * @return:
 **/

@Tag(name = "Admin-商品品牌接口")
@RestController
//@RequestMapping("/api/v1/brands")
@RequestMapping("/api/v1/pms-brand")
@RequiredArgsConstructor
public class PmsBrandController {

    private final PmsBrandService pmsBrandService;

//    @Operation(summary = "品牌分页列表")
//    @GetMapping("/page")
//    public PageResult getBrandPage(PmsBrandQuery queryParams) {
//
//        // 查询参数
//        int pageNum = queryParams.getPageNum();
//        int pageSize = queryParams.getPageSize();
//        String keywords = queryParams.getKeywords();
//
//        // 分页查询
//        Page<PmsBrand> page = pmsBrandService.page(new Page<>(pageNum, pageSize),
//                new LambdaQueryWrapper<PmsBrand>().like(StrUtil.isNotBlank(keywords), PmsBrand::getName, keywords)
//                        .orderByDesc(PmsBrand::getCreateTime));
//        return PageResult.success(page);
//    }

    @Operation(summary = "品牌列表")
    @GetMapping
    public Result getBrandList() {
        List<PmsBrand> list = pmsBrandService.list(new LambdaQueryWrapper<PmsBrand>()
                .select(PmsBrand::getId, PmsBrand::getName));
        return Result.success(list);
    }

    @Operation(summary = "品牌详情")
    @GetMapping("/{id}")
    public Result getBrandList(@PathVariable Integer id) {
        PmsBrand brand = pmsBrandService.getById(id);
        return Result.success(brand);
    }

//    @Operation(summary = "新增品牌")
//    @PostMapping
//    public Result addBrand(@RequestBody PmsBrand brand) {
//        boolean status = pmsBrandService.save(brand);
//        return Result.judge(status);
//    }

//    @Operation(summary = "修改品牌")
//    @PutMapping(value = "/{id}")
//    public Result updateBrand(
//            @PathVariable Long id,
//            @RequestBody PmsBrand brand) {
//        boolean status = pmsBrandService.updateById(brand);
//        return Result.judge(status);
//    }

//    @Operation(summary = "删除品牌")
//    @DeleteMapping("/{ids}")
//    public Result deleteBrands(@Parameter(name = "品牌ID，多个以英文逗号(,)分割") @PathVariable("ids") String ids) {
//        boolean status = pmsBrandService.removeByIds(Arrays.asList(ids.split(",")));
//        return Result.judge(status);
//    }


    @Operation(summary = "商品品牌分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsBrand:pms-brand:query')")
    public PageResult<PmsBrandVO> getPmsBrandPage(PmsBrandQuery queryParams ) {
        IPage<PmsBrandVO> result = pmsBrandService.getPmsBrandPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增商品品牌")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsBrand:pms-brand:add')")
    public Result<Void> savePmsBrand(@RequestBody @Valid PmsBrandForm formData ) {
        boolean result = pmsBrandService.savePmsBrand(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取商品品牌表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsBrand:pms-brand:edit')")
    public Result<PmsBrandForm> getPmsBrandForm(
            @Parameter(description = "商品品牌ID") @PathVariable Long id
    ) {
        PmsBrandForm formData = pmsBrandService.getPmsBrandFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改商品品牌")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsBrand:pms-brand:edit')")
    public Result<Void> updatePmsBrand(
            @Parameter(description = "商品品牌ID") @PathVariable Long id,
            @RequestBody @Validated PmsBrandForm formData
    ) {
        boolean result = pmsBrandService.updatePmsBrand(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除商品品牌")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsBrand:pms-brand:delete')")
    public Result<Void> deletePmsBrands(
            @Parameter(description = "商品品牌ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = pmsBrandService.deletePmsBrands(ids);
        return Result.judge(result);
    }
}
