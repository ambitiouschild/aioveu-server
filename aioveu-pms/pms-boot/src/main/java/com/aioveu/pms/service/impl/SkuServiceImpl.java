package com.aioveu.pms.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import com.aioveu.pms.model.entity.PmsSpu;
import com.aioveu.pms.service.SpuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.pms.constant.ProductConstants;
import com.aioveu.pms.converter.SkuConverter;
import com.aioveu.pms.mapper.PmsSkuMapper;
import com.aioveu.pms.model.dto.LockSkuDTO;
import com.aioveu.pms.model.dto.SkuInfoDTO;
import com.aioveu.pms.model.entity.PmsSku;
import com.aioveu.pms.service.SkuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description: TODO 商品库存业务实现类
 * @Author: 雒世松
 * @Date: 2025/6/5 18:33
 * @param
 * @return:
 **/

@Service
@Slf4j
@RequiredArgsConstructor
public class SkuServiceImpl extends ServiceImpl<PmsSkuMapper, PmsSku> implements SkuService {

    private final RedisTemplate redisTemplate;
    private final SkuConverter skuConverter;

    @Lazy
    @Autowired
    private SpuService spuService;

    /**
     * 获取商品库存信息
     *
     * @param skuId SKU ID
     * @return 商品库存信息
     */
    @Override
    public SkuInfoDTO getSkuInfo(Long skuId) {
        return this.baseMapper.getSkuInfo(skuId);
    }

    /**
     * 获取商品库存信息列表
     *
     * @param skuIds SKU ID 列表
     * @return 商品库存信息列表
     */
    @Override
    public List<SkuInfoDTO> listSkuInfos(List<Long> skuIds) {
        List<PmsSku> list = this.list(new LambdaQueryWrapper<PmsSku>().in(PmsSku::getId, skuIds));
        if(list != null && list.size() > 0){
            List<Long> list1 = list.stream().map(PmsSku::getSpuId).distinct().toList();
            List<PmsSpu> pmsSpus = spuService.listByIds(list1);
            Map<Long,String> map = new HashMap<Long,String>();
            for (PmsSpu spus : pmsSpus) {
                map.put(spus.getId(),spus.getName());
            }

            List<SkuInfoDTO> temp = entity2SkuInfoDto(list,map);
            return temp;
        }
        return Collections.EMPTY_LIST;
    }

    private SkuInfoDTO entity2SkuInfoDto(PmsSku entity,Map<Long,String> map) {
        if ( entity == null ) {
            return null;
        }

        SkuInfoDTO skuInfoDTO = new SkuInfoDTO();

        skuInfoDTO.setId( entity.getId() );
        skuInfoDTO.setSkuSn( entity.getSkuSn() );
        skuInfoDTO.setSkuName( entity.getName() );
        skuInfoDTO.setPicUrl( entity.getPicUrl() );
        skuInfoDTO.setPrice( entity.getPrice() );
        skuInfoDTO.setStock( entity.getStock() );
        skuInfoDTO.setSpuName(map.get(entity.getSpuId()));

        return skuInfoDTO;
    }

    private List<SkuInfoDTO> entity2SkuInfoDto(List<PmsSku> list,Map<Long,String> map) {
        if ( list == null ) {
            return null;
        }

        List<SkuInfoDTO> list1 = new ArrayList<SkuInfoDTO>( list.size() );
        for ( PmsSku pmsSku : list ) {
            list1.add( entity2SkuInfoDto( pmsSku ,map) );
        }

        return list1;
    }

    /**
     * 校验并锁定库存
     *
     * @param orderToken  订单临时编号 (此时订单未创建)
     * @param lockSkuList 锁定商品库存列表
     * @return true/false
     */
    @Override
    @Transactional
    public boolean lockStock(String orderToken, List<LockSkuDTO> lockSkuList) {
        log.info("订单({})锁定商品库存：{}", orderToken, JSONUtil.toJsonStr(lockSkuList));
        Assert.isTrue(CollectionUtil.isNotEmpty(lockSkuList), "订单({})未包含任何商品", orderToken);

        // 校验库存数量是否足够以及锁定库存
        for (LockSkuDTO lockedSku : lockSkuList) {
            Integer quantity = lockedSku.getQuantity(); // 订单的商品数量
            // 库存足够
            boolean lockResult = this.update(new LambdaUpdateWrapper<PmsSku>()
                    .setSql("locked_stock = locked_stock + " + quantity) // 修改锁定商品数
                    .eq(PmsSku::getId, lockedSku.getSkuId())
                    .apply("stock - locked_stock >= {0}", quantity) // 剩余商品数 ≥ 订单商品数
            );
            Assert.isTrue(lockResult, "商品库存不足");
        }

        // 锁定的商品缓存至 Redis (后续使用：1.取消订单解锁库存；2：支付订单扣减库存)
        redisTemplate.opsForValue().set(ProductConstants.LOCKED_SKUS_PREFIX + orderToken, lockSkuList);
        return true;
    }

    /**
     * 解锁库存
     * <p>
     * 订单超时未支付，释放锁定的商品库存
     *
     * @param orderSn 订单号
     * @return true/false
     */
    @Override
    @Transactional
    public boolean unlockStock(String orderSn) {
        List<LockSkuDTO> lockedSkus = (List<LockSkuDTO>) redisTemplate.opsForValue().get(ProductConstants.LOCKED_SKUS_PREFIX + orderSn);
        log.info("释放订单({})锁定的商品库存:{}", orderSn, JSONUtil.toJsonStr(lockedSkus));

        // 库存已释放
        if (CollectionUtil.isEmpty(lockedSkus)) {
            return true;
        }

        // 解锁商品库存
        for (LockSkuDTO lockedSku : lockedSkus) {
            boolean unlockResult = this.update(new LambdaUpdateWrapper<PmsSku>()
                    .setSql("locked_stock = locked_stock - " + lockedSku.getQuantity())
                    .eq(PmsSku::getId, lockedSku.getSkuId())
            );
            Assert.isTrue(unlockResult, "解锁商品库存失败");
        }
        // 移除 redis 订单锁定的商品
        redisTemplate.delete(ProductConstants.LOCKED_SKUS_PREFIX + orderSn);
        return true;
    }

    /**
     * 扣减库存
     * <p>
     * 订单支付扣减商品库存和释放锁定库存
     *
     * @param orderSn 订单编号
     * @return ture/false
     */
    @Override
    @Transactional
    public boolean deductStock(String orderSn) {
        // 获取订单提交时锁定的商品
        List<LockSkuDTO> lockedSkus = (List<LockSkuDTO>) redisTemplate.opsForValue().get(ProductConstants.LOCKED_SKUS_PREFIX + orderSn);
        log.info("订单({})支付成功，扣减订单商品库存：{}", orderSn, JSONUtil.toJsonStr(lockedSkus));

        Assert.isTrue(CollectionUtil.isNotEmpty(lockedSkus), "扣减商品库存失败：订单({})未包含商品");

        for (LockSkuDTO lockedSku : lockedSkus) {
            boolean deductResult = this.update(new LambdaUpdateWrapper<PmsSku>()
                    .setSql("stock = stock - " + lockedSku.getQuantity())
                    .setSql("locked_stock = locked_stock - " + lockedSku.getQuantity())
                    .eq(PmsSku::getId, lockedSku.getSkuId())
            );
            Assert.isTrue(deductResult, "扣减商品库存失败");
        }

        // 移除订单锁定的商品
        redisTemplate.delete(ProductConstants.LOCKED_SKUS_PREFIX + orderSn);
        return true;
    }
}
