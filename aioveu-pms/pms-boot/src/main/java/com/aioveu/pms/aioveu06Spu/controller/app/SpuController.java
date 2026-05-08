package com.aioveu.pms.aioveu06Spu.controller.app;

import com.aioveu.pms.aioveu06Spu.mapper.PmsSpuMapper;
import com.aioveu.pms.aioveu06Spu.model.entity.PmsSpu;
import com.aioveu.pms.aioveu06Spu.model.form.PmsSpuForm;
import com.aioveu.pms.aioveu06Spu.model.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pms.aioveu06Spu.model.query.PmsSpuQuery;
import com.aioveu.pms.aioveu06Spu.service.PmsSpuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Tag(name  = "App-商品接口")
@RestController
@RequestMapping("/app-api/v1/spu")
@RequiredArgsConstructor
public class SpuController {

    private final PmsSpuService pmsSpuService;

    private final PmsSpuMapper pmsSpuMapper;

    @Operation(summary = "商品分页列表")
    @GetMapping("/pages")
    public PageResult<SpuPageVO> listPagedSpuForApp(PmsSpuQuery queryParams) {

        log.info("✅ 接口被调用: /app-api/v1/spu/pages, query={}", queryParams);
        IPage<SpuPageVO> result = pmsSpuService.listPagedSpuForApp(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "获取商品详情")
    @GetMapping("/{spuId}")
    public Result<SpuDetailVO> getSpuDetail(
            @Parameter(name ="商品ID") @PathVariable Long spuId
    ) {
        SpuDetailVO spuDetailVO = pmsSpuService.getSpuDetailForApp(spuId);
        return Result.success(spuDetailVO);
    }

    @Operation(summary = "获取秒杀商品列表")
    @GetMapping("/seckilling")
    public Result<List<SeckillingSpuVO>> listSeckillingSpu() {
        List<SeckillingSpuVO> list = pmsSpuService.listSeckillingSpu();
        return Result.success(list);
    }


    @Operation(summary = "批量更新商品状态")
    @PostMapping("/batch/update-status")
    public Result batchUpdateStatus(@Validated @RequestBody BatchUpdateStatusDTO dto) {
        boolean result = pmsSpuService.batchUpdateStatus(dto.getSpuIds(), dto.getStatus());
        return Result.judge(result);
    }

    @Operation(summary = "批量上架商品")
    @PostMapping("/batch/shelf")
    public Result batchShelf(@RequestBody List<Long> spuIds) {
        boolean result = pmsSpuService.batchShelf(spuIds);
        return Result.judge(result);
    }

    @Operation(summary = "批量下架商品")
    @PostMapping("/batch/off-shelf")
    public Result batchOffShelf(@RequestBody List<Long> spuIds) {
        boolean result = pmsSpuService.batchOffShelf(spuIds);
        return Result.judge(result);
    }

    @Operation(summary = "批量删除商品")
    @PostMapping("/batch/remove")
    public Result batchRemove(@Valid @RequestBody BatchRemoveDTO dto) {
        boolean result = pmsSpuService.batchRemove(dto.getSpuIds());
        return Result.judge(result);
    }

    @Operation(summary = "\"批量物理删除商品（慎用）")
    @PostMapping("/batch/physical-remove")
    public Result batchPhysicalRemove(@RequestBody List<Long> spuIds) {
        try {
            // 参数验证
            if (spuIds == null || spuIds.isEmpty()) {
                return Result.failed("商品ID列表不能为空");
            }

            // 检查商品是否存在
            List<PmsSpu> existingSpus = pmsSpuService.listByIds(spuIds);
            List<Long> existingIds = existingSpus.stream()
                    .map(PmsSpu::getId)
                    .collect(Collectors.toList());

            if (existingIds.size() != spuIds.size()) {
                List<Long> notFoundIds = spuIds.stream()
                        .filter(id -> !existingIds.contains(id))
                        .collect(Collectors.toList());
                return Result.failed("部分商品不存在，ID：" + notFoundIds);
            }

            // 检查商品是否有关联数据（如订单）
            for (Long spuId : spuIds) {
                if (hasRelatedOrders(spuId)) {
                    return Result.failed("商品ID " + spuId + " 存在关联订单，无法删除");
                }
            }

            // 检查商品是否在售
            List<Long> onSaleSpuIds = existingSpus.stream()
                    .filter(spu -> spu.getStatus() == 1) // 上架状态
                    .map(PmsSpu::getId)
                    .collect(Collectors.toList());

            if (!onSaleSpuIds.isEmpty()) {
                return Result.failed("商品ID " + onSaleSpuIds + " 为上架状态，请先下架再删除");
            }

            // 执行批量删除
//            boolean result = pmsSpuService.batchPhysicalRemove(spuIds);
            int deletedCount = pmsSpuMapper.batchRemove(spuIds);

            if (deletedCount > 0) {
                // 同时删除关联的SKU数据
//                pmsSkuService.batchRemoveBySpuIds(spuIds);

                log.info("批量删除商品成功：spuIds={}, 删除数量={}", spuIds, deletedCount);
                return Result.success("成功删除 " + deletedCount + " 个商品");
            } else {
                return Result.failed("删除失败，可能商品已删除");
            }
        } catch (Exception e) {
            log.error("批量删除商品异常", e);
            return Result.failed("系统异常，请稍后重试");
        }

    }

    /**
     * 检查商品是否存在关联订单
     */
    private boolean hasRelatedOrders(Long spuId) {
        // 这里实现检查逻辑
        // 例如：查询订单项表，检查是否有此商品相关的订单
        // return orderService.existsBySpuId(spuId);
        return false; // 暂时返回false
    }

    @Operation(summary = "单个商品更新状态")
    @PostMapping("/update-status")
    public Result updateStatus(@RequestBody UpdateStatusDTO  dto)  {
        boolean result = pmsSpuService.batchUpdateStatus(List.of(dto.getId()), dto.getStatus());
        return Result.judge(result);
    }


    @Operation(summary = "删除商品")
    @DeleteMapping("/{ids}")
    public Result<Void> deletePmsSpus(
            @Parameter(description = "商品ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = pmsSpuService.deletePmsSpus(ids);
        return Result.judge(result);
    }

}
