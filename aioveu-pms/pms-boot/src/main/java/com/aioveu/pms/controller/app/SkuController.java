package com.aioveu.pms.controller.app;

import com.aioveu.common.result.Result;
import com.aioveu.pms.model.dto.LockSkuDTO;
import com.aioveu.pms.model.dto.SkuInfoDTO;
import com.aioveu.pms.service.SkuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: TODO 商品库存
 * @Author: 雒世松
 * @Date: 2025/6/5 18:29
 * @param
 * @return:
 **/

@Tag(name  = "App-商品库存接口")
@RestController
@RequestMapping("/app-api/v1/skus")
@RequiredArgsConstructor
public class SkuController {

    private final SkuService skuService;

    @Operation(summary = "获取商品库存信息")
    @GetMapping("/{skuId}")
    public Result<SkuInfoDTO> getSkuInfo(
            @Parameter(name ="商品ID") @PathVariable Long skuId
    ) {
        SkuInfoDTO skuInfo = skuService.getSkuInfo(skuId);
        return Result.success(skuInfo);
    }

    @Operation(summary = "获取商品库存列表")
    @GetMapping
    public Result<List<SkuInfoDTO>> getSkuInfoList(
            @Parameter(name ="SKU ID 列表") @RequestParam List<Long> skuIds
    ) {
        List<SkuInfoDTO> skuInfos = skuService.listSkuInfos(skuIds);
        return Result.success(skuInfos);
    }

    @Operation(summary = "校验并锁定库存")
    @PutMapping("/lock")
    public Result<?> lockStock(
            @RequestParam String orderToken,
            @RequestBody List<LockSkuDTO> lockSkuList
    ) {
        boolean lockStockResult = skuService.lockStock(orderToken,lockSkuList);
        return Result.success(lockStockResult);
    }

    @Operation(summary = "解锁库存")
    @PutMapping("/unlock")
    public Result<?> unlockStock(String orderToken) {
        boolean result = skuService.unlockStock(orderToken);
        return Result.judge(result);
    }

    @Operation(summary = "扣减库存")
    @PutMapping("/deduct")
    public Result<?> deductStock(String orderSn) {
        boolean result = skuService.deductStock(orderSn);
        return Result.judge(result);
    }
}
