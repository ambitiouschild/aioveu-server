package com.aioveu.pms.aioveu05Sku.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aioveu.common.exception.BusinessException;
import com.aioveu.common.result.ResultCode;
import com.aioveu.pms.aioveu05Sku.converter.PmsSkuConverter;
import com.aioveu.pms.aioveu05Sku.model.form.PmsSkuForm;
import com.aioveu.pms.aioveu05Sku.model.query.PmsSkuQuery;
import com.aioveu.pms.aioveu05Sku.model.vo.PmsSkuVO;
import com.aioveu.pms.aioveu06Spu.model.entity.PmsSpu;
import com.aioveu.pms.aioveu06Spu.service.PmsSpuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.pms.aioveu06Spu.constant.ProductConstants;
import com.aioveu.pms.aioveu05Sku.mapper.PmsSkuMapper;
import com.aioveu.pms.model.dto.LockSkuDTO;
import com.aioveu.pms.model.dto.SkuInfoDTO;
import com.aioveu.pms.aioveu05Sku.model.entity.PmsSku;
import com.aioveu.pms.aioveu05Sku.service.PmsSkuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO 商品库存业务实现类
 *                      负责SKU库存的查询、锁定、解锁、扣减等核心业务逻辑
 *                      采用数据库+Redis双重机制保证库存操作的一致性和可靠性
 *                          核心业务流程解析
 *                          1. 库存锁定机制 (lockStock)
 *                              防超卖核心：使用stock - locked_stock >= quantity条件保证不会超卖
 *                              原子性操作：数据库更新操作的原子性防止并发问题
 *                              缓存备份：锁定信息同时存入Redis，用于后续操作
 *                          2. 库存状态转换
 *                              正常库存 → 锁定库存：用户下单时 (lockStock)
 *                              锁定库存 → 正常库存：订单取消时 (unlockStock)
 *                              锁定库存 → 已售库存：支付成功时 (deductStock)
 *                          关键技术实现
 *                          1. 数据库乐观锁
 *                              .apply("stock - locked_stock >= {0}", quantity)
 *                              在UPDATE的WHERE条件中校验库存，实现乐观锁
 *                              避免使用SELECT+UPDATE的非原子操作导致的超卖问题
 *                          2. Redis缓存应用
 *                              临时存储：存储锁定信息，避免重复查询数据库
 *                              状态跟踪：记录订单与SKU的锁定关系
 *                              异常恢复：系统重启后可根据Redis记录恢复库存状态
 *                          3. 事务管理
 *                              @Transactional保证多个库存操作的原子性
 *                              任一SKU操作失败时，整个事务回滚
 *                              与Redis操作结合，需要保证最终一致性
 *                          异常处理机制
 *                              1.参数校验：使用Hutool的Assert进行前置条件检查
 *                              2.操作验证：检查数据库更新结果，失败时立即回滚
 *                              3.日志记录：关键操作都有详细日志，便于问题排查
 *                              4.事务回滚：任一步骤失败都会回滚所有操作
 *                          性能与可靠性考虑
 *                              1.批量操作：支持批量SKU的库存操作
 *                              2.索引优化：SKU ID字段应有索引保证查询性能
 *                              3.缓存策略：Redis缓存减轻数据库压力
 *                              4.超时机制：Redis记录应设置过期时间，防止数据堆积
 *                  方法优化亮点
 *                  1.关联查询优化：
 *                      原方法只查询SKU信息，优化后关联查询SPU信息
 *                      通过distinct()去重避免重复查询相同SPU
 *                      使用Map建立映射关系，提高查找效率
 *                  2.数据完整性：
 *                      返回的DTO包含SPU名称，前端无需再次请求商品基本信息
 *                      适合购物车、订单确认等需要完整商品信息的场景
 *                  业务流程解析
 *                  1.批量查询阶段：
 *                      先查询SKU基础信息
 *                      再根据SKU中的SPU ID查询商品基本信息
 *                  3.数据关联阶段：
 *                      构建SPU ID到名称的映射表
 *                      为每个SKU注入对应的SPU名称
 *                  3.转换返回阶段：
 *                      实体对象转换为DTO对象
 *                      返回包含完整信息的数据列表
 *                  性能考虑
 *                  1.查询优化：
 *                      使用listByIds批量查询，减少数据库交互次数
 *                      通过一次查询获取所有相关SPU信息
 *                  2.内存效率：
 *                      使用Map实现O(1)时间复杂度的SPU名称查找
 *                      预分配List容量避免频繁扩容
 *                  3.空值处理：
 *                      对输入参数和查询结果进行空值检查
 *                      使用Collections.EMPTY_LIST返回空结果，节省内存
 *                  业务价值
 *                  1.减少前端请求：一次性返回SKU和SPU信息，前端无需额外请求商品详情
 *                  2.提升用户体验：购物车、订单页面直接显示完整商品名称
 *                  3.数据一致性：保证SKU显示的SPU名称与商品库一致
 * @Author: 雒世松
 * @Date: 2025/6/5 18:33
 * @param
 * @return:
 **/

@Service
@Slf4j
@RequiredArgsConstructor   // Lombok注解：为所有final字段生成构造函数，实现依赖注入
public class PmsSkuServiceImpl extends ServiceImpl<PmsSkuMapper, PmsSku> implements PmsSkuService {


    // Redis操作模板，用于缓存锁定库存信息
    private final RedisTemplate redisTemplate;

    // SKU对象转换器，用于实体与DTO之间的转换
    private final PmsSkuConverter pmsSkuConverter;

    @Lazy
    @Autowired
    private PmsSpuService pmsSpuService;

    /**
     *   TODO               获取单个商品SKU的详细信息
     *                  包括库存数量、锁定库存、价格等完整信息
     *
     * @param skuId SKU唯一标识ID
     * @return SkuInfoDTO SKU详细信息数据传输对象
     */
    @Override
    public SkuInfoDTO getSkuInfo(Long skuId) {

        log.info("使用LambdaQueryWrapper获取SKU详细信息，skuId: {}", skuId);

        if (skuId == null) {
            log.warn("skuId不能为空");
            throw new BusinessException("SKU ID不能为空");
        }

        try {
            // 使用LambdaWrapper查询
            LambdaQueryWrapper<PmsSku> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PmsSku::getId, skuId);
//                    .eq(PmsSku::getDeleted, 0)  // 未删除
//                    .eq(PmsSku::getStatus, 1);  // 上架状态

            PmsSku pmsSku = this.getOne(queryWrapper);

            if (pmsSku == null) {
                log.warn("未找到对应的SKU信息，skuId: {}", skuId);
                throw new BusinessException( "商品不存在或已下架");
            }

            SkuInfoDTO skuInfo = pmsSkuConverter.entity2SkuInfoDto(pmsSku);
            log.info("转换器转化为 skuInfo {}", skuInfo);

            // 使用LambdaWrapper查询
            LambdaQueryWrapper<PmsSpu> querySpuWrapper = new LambdaQueryWrapper<>();
            querySpuWrapper.eq(PmsSpu::getId, pmsSku.getSpuId());

            PmsSpu pmsSpu = pmsSpuService.getOne(querySpuWrapper);

//            设置商品信息
            skuInfo.setSpuName(pmsSpu.getName());

            log.info("成功获取SKU信息，skuId: {}, spuName: {}, skuName: {}", skuId, skuInfo.getSpuName()  ,skuInfo.getSkuName());

            return skuInfo;

        } catch (Exception e) {
            log.error("获取SKU信息失败，skuId: {}", skuId, e);
            throw new BusinessException("获取商品信息失败");
        }

    }

    /**
     *   TODO           批量获取多个SKU的库存信息列表
     *              适用于购物车、订单确认等需要批量查询SKU信息的场景
     *              方法优化：增加了SPU信息的关联查询，返回更完整的商品信息
     *
     * @param skuIds SKU ID列表
     * @return List<SkuInfoDTO> SKU信息列表 包含SPU信息的SKU信息列表
     */
    @Override
    public List<SkuInfoDTO> listSkuInfos(List<Long> skuIds) {

        log.info("1. 根据SKU ID列表批量查询SKU基础信息");
        log.info("使用LambdaQueryWrapper构建查询条件，查询指定ID列表的SKU数据");
        List<PmsSku> list = this.list(new LambdaQueryWrapper<PmsSku>().in(PmsSku::getId, skuIds));

        log.info("2. 校验查询结果，如果存在数据则进行进一步处理");
        if(list != null && list.size() > 0){

            log.info("3. 提取所有不重复的SPU ID列表");
            log.info("通过stream操作获取SKU列表中所有的SPU ID，并去重");
            List<Long> spuIds  = list.stream()
                    .map(PmsSku::getSpuId).    // 提取每个SKU的SPU ID
                    distinct()  // 去除重复的SPU ID
                    .toList();   // 转换为List


            log.info("4. 批量查询对应的SPU信息（商品基本信息）");
            log.info("根据SPU ID列表查询所有相关的商品基本信息");
            List<PmsSpu> pmsSpus = pmsSpuService.listByIds(spuIds);

            log.info("5. 构建SPU ID到SPU名称的映射Map，便于后续快速查找");
            log.info("创建HashMap，key为SPU ID，value为SPU名称");
            Map<Long,String> spuNameMap  = new HashMap<Long,String>();

            for (PmsSpu spus : pmsSpus) {
                // 将SPU信息存入Map，建立ID与名称的对应关系
                spuNameMap .put(spus.getId(),spus.getName());
            }

            log.info("6. 将SKU实体列表转换为DTO列表，并注入SPU名称信息");
            log.info("调用转换方法，传入SKU列表和SPU名称映射Map");
            List<SkuInfoDTO> temp = entity2SkuInfoDto(list,spuNameMap);
            return temp;
        }

        log.info("7. 如果查询结果为空，返回空列表（使用Collections.EMPTY_LIST避免创建新对象）");
        return Collections.EMPTY_LIST;
    }

    /**
     *  TODO        将单个SKU实体对象转换为SKU信息DTO对象
     *          私有转换方法，用于对象属性的映射和转换
     *
     * @param entity SKU实体对象
//     * @param spuNameMap SPU名称映射表（key: SPU ID, value: SPU名称）
     * @return SkuInfoDTO 转换后的SKU信息数据传输对象
     */
    private SkuInfoDTO entity2SkuInfoDto(PmsSku entity,Map<Long,String> map) {

        log.info("参数空值检查，如果实体为空则返回null");
        if ( entity == null ) {
            return null;
        }

        log.info("创建SKU信息DTO对象");
        SkuInfoDTO skuInfoDTO = new SkuInfoDTO();


        log.info("属性映射：将实体对象属性值设置到DTO对象中");
        skuInfoDTO.setId( entity.getId() );   // 设置SKU ID
        skuInfoDTO.setSkuSn( entity.getSkuSn() );  // 设置SKU编码
        skuInfoDTO.setSkuName( entity.getName() );  // 设置SKU名称
        skuInfoDTO.setPicUrl( entity.getPicUrl() );   // 设置SKU图片URL
        skuInfoDTO.setPrice( entity.getPrice() );  // 设置SKU价格
        skuInfoDTO.setStock( entity.getStock() );     // 设置SKU库存数量

        log.info("从SPU名称映射表中获取对应的SPU名称并设置");
        log.info("通过SKU的SPU ID在映射表中查找对应的SPU名称");
        skuInfoDTO.setSpuName(map.get(entity.getSpuId()));

        return skuInfoDTO;
    }

    /**
     *     TODO     将SKU实体对象列表批量转换为SKU信息DTO列表
     *          私有方法，用于处理列表数据的批量转换
     *
     * @param list SKU实体对象列表
//     * @param spuNameMap SPU名称映射表（key: SPU ID, value: SPU名称）
     * @return List<SkuInfoDTO> 转换后的SKU信息DTO列表
     */
    private List<SkuInfoDTO> entity2SkuInfoDto(List<PmsSku> list,Map<Long,String> map) {

        log.info("参数空值检查，如果输入列表为空则返回null");
        if ( list == null ) {
            return null;
        }

        log.info("创建结果列表，预分配足够容量以提高性能");
        List<SkuInfoDTO> resultList  = new ArrayList<SkuInfoDTO>( list.size() );

        log.info("遍历SKU实体列表，逐个转换为DTO对象");
        for ( PmsSku pmsSku : list ) {
            // 调用单个对象转换方法，并添加到结果列表
            resultList .add( entity2SkuInfoDto( pmsSku ,map) );
        }

        return resultList ;
    }

    /**
     *      TODO            校验库存并锁定库存数量
     *                  核心业务流程：用户下单时预占库存，防止超卖
     *                  采用数据库乐观锁机制保证库存操作的原子性
     *
     * @param orderToken  订单临时编号（订单创建前的唯一标识）
     * @param lockSkuList 需要锁定的SKU列表及数量
     * @return boolean 锁定是否成功
     *
     * @Transactional 注解保证方法在事务中执行，出现异常时回滚
     */
    @Override
    @Transactional(rollbackFor = {BusinessException.class, Exception.class})  // 明确指定 添加事务注解的回滚规则
    public boolean lockStock(String orderToken, List<LockSkuDTO> lockSkuList) {

        log.info("记录锁定库存操作日志，便于问题排查");
        log.info("【库存锁定】开始锁定库存，订单令牌: {}, 锁定列表: {}", orderToken, JSONUtil.toJsonStr(lockSkuList));


        Assert.isTrue(CollectionUtil.isNotEmpty(lockSkuList), "订单({})未包含任何商品", orderToken);
        log.info("【库存锁定】参数校验通过，需要锁定商品数量: {}", lockSkuList.size());



        // 先查询商品信息，记录当前库存状态
        Map<Long, PmsSku> skuMap = new HashMap<>();
        for (LockSkuDTO lockedSku : lockSkuList) {
            Long skuId = lockedSku.getSkuId();
            PmsSku sku = this.getById(skuId);

            if (sku == null) {
                log.error("【库存锁定】商品不存在，SKU ID: {}", skuId);
                throw new BusinessException("商品不存在");
            }

            // 获取锁定库存，处理null值
            Integer lockedStock = sku.getLockedStock();
            if (lockedStock == null) {
                sku.setLockedStock(0);
                lockedStock = 0;
            }

            // 获取库存，处理null值
            Integer stock = sku.getLockedStock();
            if (stock == null) {
                sku.setStock(0);
                stock = 0;
            }

            //更新处理的锁定库存
            this.updateById(sku);

            skuMap.put(skuId, sku);

            // 记录当前库存状态
            log.info("【库存锁定】商品详情 - ID: {}, 名称: {}, 总库存: {}, 锁定库存: {}, 可用库存: {}, 需要锁定: {}",
                    skuId, sku.getName(), stock, lockedStock,
                    sku.getStock() - sku.getLockedStock(), lockedSku.getQuantity());
        }



        // 校验库存数量是否足够以及锁定库存
        log.info("遍历需要锁定的每个SKU，逐个进行库存校验和锁定");
        for (LockSkuDTO lockedSku : lockSkuList) {

            Long skuId = lockedSku.getSkuId();
            // 获取当前SKU需要锁定的数量
            Integer quantity = lockedSku.getQuantity(); // 订单的商品数量
            PmsSku sku = skuMap.get(skuId);

            // 计算当前可用库存
            int currentLockedStock = sku.getLockedStock() != null ? sku.getLockedStock() : 0;
            int currentStock = sku.getStock() != null ? sku.getStock() : 0;
            int availableStock = currentStock - currentLockedStock;

            // 检查库存数据一致性
            if (availableStock < 0) {
                log.error("【库存锁定】库存数据异常，SKU ID: {}, 总库存: {}, 锁定库存: {}, 可用库存为负数: {}",
                        skuId, currentStock, currentLockedStock, availableStock);

                // 自动修复：将锁定库存重置为不超过总库存
                int fixedLockedStock = Math.min(currentLockedStock, currentStock);

                log.warn("【库存锁定】自动修复库存数据，SKU ID: {}，原锁定库存: {}，修复为: {}",
                        skuId, currentLockedStock, fixedLockedStock);

                PmsSku updateSku = new PmsSku();
                updateSku.setId(skuId);
                updateSku.setLockedStock(fixedLockedStock);

                this.updateById(updateSku);

                // 重新计算可用库存
                availableStock = currentStock - fixedLockedStock;
            }

                // 检查库存是否足够
                if (availableStock < quantity) {
                    log.error("【库存锁定】库存不足，SKU ID: {}, 商品: {}, 需要: {}, 可用: {}, 总库存: {}, 已锁定: {}",
                            skuId, sku.getName(), quantity, availableStock, currentStock, currentLockedStock);
                    throw new BusinessException(ResultCode.INSUFFICIENT_STOCK,String.format("商品【%s】库存不足，当前可用%s件",
                            sku.getName(), availableStock));
                }

            // 执行库存锁定
            log.info("【库存锁定】执行库存锁定，SKU ID: {}, 锁定数量: {}", skuId, quantity);


            // 库存足够
            // 使用数据库乐观锁机制更新库存
            // 核心逻辑：在增加锁定库存的同时，校验实际可用库存是否足够
            boolean lockResult = this.update(new LambdaUpdateWrapper<PmsSku>()

                    // 锁定库存增加：locked_stock = locked_stock + quantity
                    .setSql("locked_stock = locked_stock + " + quantity) // 修改锁定商品数

                    // 条件：匹配指定SKU ID
                    .eq(PmsSku::getId, lockedSku.getSkuId())

                    // 关键条件：可用库存（stock - locked_stock）必须大于等于要锁定的数量
                    // 这个条件保证了不会超卖，是防止库存超卖的核心逻辑
                    .apply("stock - locked_stock >= {0}", quantity) // 剩余商品数 ≥ 订单商品数
            );

            if (!lockResult) {
                // 如果更新失败，可能是并发问题，重新检查
                PmsSku newSku = this.getById(skuId);
                int newLockedStock = newSku.getLockedStock() != null ? newSku.getLockedStock() : 0;
                int newStock = newSku.getStock() != null ? newSku.getStock() : 0;
                int newAvailableStock = newStock - newLockedStock;

                if (newAvailableStock < quantity) {
                    log.error("【库存锁定】并发库存不足，SKU ID: {}, 需要: {}, 新可用: {}",
                            skuId, quantity, newAvailableStock);
                    throw new BusinessException(String.format("商品【%s】库存不足，当前可用%s件",
                            newSku.getName(), newAvailableStock));
                } else {
                    // 重试一次
                    log.info("【库存锁定】重试锁定库存，SKU ID: {}", skuId);
                    lockResult = this.update(new LambdaUpdateWrapper<PmsSku>()
                            .setSql("locked_stock = locked_stock + " + quantity)
                            .eq(PmsSku::getId, skuId)
                            .apply("stock - locked_stock >= {0}", quantity)
                    );

                    if (!lockResult) {
                        log.error("【库存锁定】最终锁定失败，SKU ID: {}", skuId);
                        throw new BusinessException("库存锁定失败");
                    }
                }
            }

//            // 保存库存锁定记录
//            saveLockRecord(orderToken, sku, quantity, currentLockedStock, currentLockedStock + quantity);

            log.info("【库存锁定】SKU ID: {} 锁定成功，锁定数量: {}", skuId, quantity);
            log.info("如果更新失败（影响行数为0），说明库存不足，抛出异常并回滚事务");
            Assert.isTrue(lockResult, "商品库存不足");
        }

        // 锁定的商品缓存至 Redis (后续使用：1.取消订单解锁库存；2：支付订单扣减库存)
        // 将锁定的SKU信息缓存到Redis中，用于后续的解锁或扣减操作
        // 缓存键格式：locked_skus:{orderToken}
        log.info("锁定的商品缓存至 Redis (后续使用：1.取消订单解锁库存；2：支付订单扣减库存)");
        log.info("将锁定的SKU信息缓存到Redis中，用于后续的解锁或扣减操作");
        log.info("缓存键格式：locked_skus:{orderToken}");
        try {
            redisTemplate.opsForValue().set(
                    ProductConstants.LOCKED_SKUS_PREFIX + orderToken,
                    lockSkuList,
                    30,  // 30分钟过期
                    TimeUnit.MINUTES
            );
            log.info("【库存锁定】Redis缓存成功");
        } catch (Exception e) {
            log.error("【库存锁定】Redis缓存失败: ", e);
            // 这里不抛出异常，因为Redis缓存失败不应该影响主流程
        }

        log.info("【库存锁定】所有商品库存锁定成功，订单令牌: {}", orderToken);

        return true;
    }


//    /**
//     * 保存库存锁定记录
//     */
//    private void saveLockRecord(String orderToken, PmsSku sku, Integer quantity,
//                                Integer beforeLocked, Integer afterLocked) {
//        try {
//            StockLockRecord record = new StockLockRecord();
//            record.setOrderToken(orderToken);
//            record.setSkuId(sku.getId());
//            record.setSkuName(sku.getSkuName());
//            record.setQuantity(quantity);
//            record.setBeforeStock(sku.getStock());
//            record.setBeforeLockedStock(beforeLocked);
//            record.setAfterLockedStock(afterLocked);
//            record.setCreateTime(new Date());
//
//            // 保存到数据库
//            stockLockRecordMapper.insert(record);
//
//            log.debug("【库存记录】保存锁定记录成功，SKU ID: {}, 锁定数量: {}", sku.getId(), quantity);
//
//        } catch (Exception e) {
//            log.error("【库存记录】保存锁定记录失败: ", e);
//            // 这里不抛出异常，因为记录失败不影响主业务
//        }
//    }




    /**
     *     TODO             解锁已锁定的库存
     *                  应用场景：订单超时未支付、用户主动取消订单等
     *                  将预占的库存释放回可用库存中
     *
     * @param orderSn 订单编号
     * @return boolean 解锁是否成功
     */
    @Override
    @Transactional
    public boolean unlockStock(String orderSn) {

        log.info("从Redis中获取该订单锁定的SKU信息");
        List<LockSkuDTO> lockedSkus = (List<LockSkuDTO>) redisTemplate.opsForValue()
                .get(ProductConstants.LOCKED_SKUS_PREFIX + orderSn);


        log.info("释放订单({})锁定的商品库存:{}", orderSn, JSONUtil.toJsonStr(lockedSkus));

        // 库存已释放
        log.info("如果Redis中不存在锁定记录，说明库存已经被释放，直接返回成功");
        if (CollectionUtil.isEmpty(lockedSkus)) {
            return true;
        }

        // 解锁商品库存
        log.info("遍历所有锁定的SKU，逐个释放锁定库存");
        for (LockSkuDTO lockedSku : lockedSkus) {

            // 减少锁定库存数量：locked_stock = locked_stock - quantity
            boolean unlockResult = this.update(new LambdaUpdateWrapper<PmsSku>()
                    .setSql("locked_stock = locked_stock - " + lockedSku.getQuantity())
                    .eq(PmsSku::getId, lockedSku.getSkuId())
            );

            log.info("如果解锁失败，抛出异常回滚事务");
            Assert.isTrue(unlockResult, "解锁商品库存失败");
        }
        // 移除 redis 订单锁定的商品
        log.info("解锁成功后，从Redis中删除对应的锁定记录");
        redisTemplate.delete(ProductConstants.LOCKED_SKUS_PREFIX + orderSn);
        return true;
    }

    /**
     *       TODO           扣减实际库存
     *                  应用场景：订单支付成功后，将锁定的库存实际扣除
     *                  同时减少总库存和锁定库存
     *
     * @param orderSn 订单编号
     * @return boolean 扣减是否成功
     */
    @Override
    @Transactional
    public boolean deductStock(String orderSn) {


        // 获取订单提交时锁定的商品
        log.info("从Redis中获取订单锁定的SKU信息");
        List<LockSkuDTO> lockedSkus = (List<LockSkuDTO>) redisTemplate.opsForValue()
                .get(ProductConstants.LOCKED_SKUS_PREFIX + orderSn);


        log.info("订单({})支付成功，扣减订单商品库存：{}", orderSn, JSONUtil.toJsonStr(lockedSkus));

        log.info("参数校验：锁定记录必须存在");
        Assert.isTrue(CollectionUtil.isNotEmpty(lockedSkus), "扣减商品库存失败：订单({})未包含商品");

        log.info("遍历所有SKU，执行库存扣减操作");
        for (LockSkuDTO lockedSku : lockedSkus) {

            // 同时扣减总库存和锁定库存
            // stock = stock - quantity（减少实际库存）
            // locked_stock = locked_stock - quantity（释放锁定库存）
            boolean deductResult = this.update(new LambdaUpdateWrapper<PmsSku>()
                    .setSql("stock = stock - " + lockedSku.getQuantity())
                    .setSql("locked_stock = locked_stock - " + lockedSku.getQuantity())
                    .eq(PmsSku::getId, lockedSku.getSkuId())
            );
            log.info("扣减失败时抛出异常回滚");
            Assert.isTrue(deductResult, "扣减商品库存失败");
        }

        // 移除订单锁定的商品
        log.info("扣减成功后，清除Redis中的锁定记录");
        redisTemplate.delete(ProductConstants.LOCKED_SKUS_PREFIX + orderSn);
        return true;
    }

    /**
     * 获取商品库存分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PmsSkuVO>} 商品库存分页列表
     */
    @Override
    public IPage<PmsSkuVO> getPmsSkuPage(PmsSkuQuery queryParams) {
        Page<PmsSkuVO> pageVO = this.baseMapper.getPmsSkuPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取商品库存表单数据
     *
     * @param id 商品库存ID
     * @return 商品库存表单数据
     */
    @Override
    public PmsSkuForm getPmsSkuFormData(Long id) {
        PmsSku entity = this.getById(id);
        return pmsSkuConverter.toForm(entity);
    }

    /**
     * 新增商品库存
     *
     * @param formData 商品库存表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePmsSku(PmsSkuForm formData) {
        PmsSku entity = pmsSkuConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新商品库存
     *
     * @param id   商品库存ID
     * @param formData 商品库存表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePmsSku(Long id,PmsSkuForm formData) {
        PmsSku entity = pmsSkuConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除商品库存
     *
     * @param ids 商品库存ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePmsSkus(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的商品库存数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
