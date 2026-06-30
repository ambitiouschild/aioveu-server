package com.aioveu.pms.api;

import com.aioveu.common.result.Result;
import com.aioveu.feign.config.FeignDecoderConfig;
import com.aioveu.pms.model.dto.LockSkuDTO;
import com.aioveu.pms.model.dto.SkuInfoDTO;
import com.aioveu.pms.model.vo.SeckillingSpuVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(value = "aioveu-tenant-pms", contextId = "sku", configuration = {FeignDecoderConfig.class})
public interface SkuFeignClient {

    /**
     * 获取商品库存信息
     */
    @GetMapping("/aioveu/api/v8/app/pms/skus/{skuId}")
    SkuInfoDTO getSkuInfo(@PathVariable Long skuId);

    /**
     * 获取商品库存信息列表
     *
     * @param skuIds SKU ID 列表
     * @return 商品库存信息列表
     */
    @GetMapping("/aioveu/api/v8/app/pms/skus")
    List<SkuInfoDTO> getSkuInfoList(@RequestParam List<Long> skuIds);

    /**
     * 锁定商品库存
     */
    @PutMapping("/aioveu/api/v8/app/pms/skus/lock")
    boolean lockStock(@RequestParam String orderToken, @RequestBody List<LockSkuDTO> lockSkuList);

    /**
     * 解锁商品库存
     */
    @PutMapping("/aioveu/api/v8/app/pms/skus/unlock")
    boolean unlockStock(@RequestParam String orderSn);

    /**
     * 扣减订单商品库存
     * <p>
     * 扣减指定订单商品的库存数量。
     *
     * @param orderSn 订单编号
     * @return 扣减库存结果
     */
    @PutMapping("/aioveu/api/v8/app/pms/skus/deduct")
    boolean deductStock(@RequestParam String orderSn);

    @Operation(summary = "获取秒杀商品列表")
    @GetMapping("/aioveu/api/v8/app/pms/spu/seckilling")
    Result<List<SeckillingSpuVO>> listSeckillingSpu();
}
