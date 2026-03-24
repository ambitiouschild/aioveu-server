package com.aioveu.pms.api;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.common.web.config.FeignDecoderConfig;
import com.aioveu.pms.model.dto.LockSkuDTO;
import com.aioveu.pms.model.dto.SkuInfoDTO;
import com.aioveu.pms.model.query.PmsSpuQuery;
import com.aioveu.pms.model.vo.CategoryVO;
import com.aioveu.pms.model.vo.SeckillingSpuVO;
import com.aioveu.pms.model.vo.SpuDetailVO;
import com.aioveu.pms.model.vo.SpuPageVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(value = "aioveu-tenant-pms", contextId = "pms", configuration = {FeignDecoderConfig.class})
public interface PmsFeignClient {

    @Operation(summary = "获取秒杀商品列表")
    @GetMapping("/app-api/v1/spu/seckilling")
    List<SeckillingSpuVO> listSeckillingSpu(
            @RequestHeader("X-Tenant-Id") Long tenantId
    );

    @Operation(summary = "分类列表")
    @GetMapping("/app-api/v1/categories")
    List<CategoryVO> list( // 添加 required=false
            @Parameter(name = "上级分类ID")  @RequestParam(value = "parentId", required = false) Long parentId,
            @RequestHeader("X-Tenant-Id") Long tenantId
    );

    @Operation(summary = "获取商品详情")
    @GetMapping("/app-api/v1/spu/{spuId}")
    SpuDetailVO getSpuDetail(
            @Parameter(name ="商品ID") @PathVariable Long spuId,
            @RequestHeader("X-Tenant-Id") Long tenantId
    );



}
