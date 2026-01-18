package com.aioveu.pms.aioveu06Spu.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pms.aioveu06Spu.model.form.PmsSpuForm;
import com.aioveu.pms.aioveu06Spu.model.query.PmsSpuQuery;
import com.aioveu.pms.aioveu06Spu.model.vo.PmsSpuDetailVO;
import com.aioveu.pms.aioveu06Spu.model.vo.PmsSpuVO;
import com.aioveu.pms.aioveu06Spu.service.PmsSpuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: TODO Admin-商品控制层
 * @Author: 雒世松
 * @Date: 2025/6/5 18:29
 * @param
 * @return:
 **/

@Tag(name = "Admin-商品SPU接口")
@RestController
//@RequestMapping("/api/v1/spu")
@RequestMapping("/api/v1/pms-spu")
@AllArgsConstructor
public class PmsSpuController {

    private PmsSpuService pmsSpuService;

//    @Operation(summary = "商品分页列表")
//    @GetMapping("/page")
//    public PageResult listPagedSpu(PmsSpuQuery queryParams) {
//        IPage<PmsSpuVO> result = pmsSpuService.listPagedSpu(queryParams);
//        return PageResult.success(result);
//    }

    @Operation(summary = "商品分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsSpu:pms-spu:query')")
    public PageResult<PmsSpuVO> getPmsSpuPage(PmsSpuQuery queryParams ) {
        IPage<PmsSpuVO> result = pmsSpuService.getPmsSpuPage(queryParams);
        return PageResult.success(result);
    }


    @Operation(summary = "商品详情")
    @GetMapping("/{id}/detail")
    public Result detail(@Parameter(name = "商品ID") @PathVariable Long id) {
        PmsSpuDetailVO pmsSpuDetailVO = pmsSpuService.getSpuDetail(id);
        return Result.success(pmsSpuDetailVO);
    }

    @Operation(summary = "新增商品")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsSpu:pms-spu:add')")
    public Result addSpu(@RequestBody PmsSpuForm formData) {
        boolean result = pmsSpuService.addSpu(formData);
        return Result.judge(result);
    }

//    @Operation(summary = "新增商品")
//    @PostMapping
//    @PreAuthorize("@ss.hasPerm('aioveuMallPmsSpu:pms-spu:add')")
//    public Result<Void> savePmsSpu(@RequestBody @Valid PmsSpuForm formData ) {
//        boolean result = pmsSpuService.savePmsSpu(formData);
//        return Result.judge(result);
//    }

    @Operation(summary = "获取商品表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsSpu:pms-spu:edit')")
    public Result<PmsSpuForm> getPmsSpuForm(
            @Parameter(description = "商品ID") @PathVariable Long id
    ) {
        PmsSpuForm formData = pmsSpuService.getPmsSpuFormData(id);
        return Result.success(formData);
    }

//    @Operation(summary = "修改商品")
//    @PutMapping(value = "/{id}")
//    public Result updateSpuById(
//            @Parameter(name = "商品ID") @PathVariable Long id,
//            @RequestBody PmsSpuForm formData
//    ) {
//        boolean result = pmsSpuService.updateSpuById(id, formData);
//        return Result.judge(result);
//    }

    @Operation(summary = "修改商品")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsSpu:pms-spu:edit')")
    public Result<Void> updatePmsSpu(
            @Parameter(description = "商品ID") @PathVariable Long id,
            @RequestBody @Validated PmsSpuForm formData
    ) {
        boolean result = pmsSpuService.updatePmsSpu(id, formData);
        return Result.judge(result);
    }

//    @Operation(summary = "删除商品")
//    @DeleteMapping("/{ids}")
//    public Result delete(
//            @Parameter(name = "商品ID,多个以英文逗号(,)分隔") @PathVariable String ids
//    ) {
//        boolean result = pmsSpuService.removeBySpuIds(ids);
//        return Result.judge(result);
//    }

    @Operation(summary = "删除商品")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPmsSpu:pms-spu:delete')")
    public Result<Void> deletePmsSpus(
            @Parameter(description = "商品ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = pmsSpuService.deletePmsSpus(ids);
        return Result.judge(result);
    }

}
