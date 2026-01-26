package com.aioveu.oms.aioveu01Order.service.impl;

import com.aioveu.common.result.ResultCode;
import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.common.web.exception.BizException;
import com.aioveu.oms.aioveu01Order.constant.OrderConstants;
import com.aioveu.oms.aioveu01Order.converter.CartConverter;
import com.aioveu.oms.aioveu01Order.model.vo.CartItemDto;
import com.aioveu.oms.aioveu01Order.model.vo.CartItemVo;
import com.aioveu.oms.aioveu01Order.service.CartService;
import com.aioveu.pms.api.SkuFeignClient;
import com.aioveu.pms.model.dto.SkuInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


/**
 *            TODO              购物车模块
 *                          使用Redis的Hash结构存储购物车数据：
 *                          - Key格式：MEMBER_CART_PREFIX + memberId（如：cart:member:123）
 *                          - Hash结构：
 *                            - field（字段）：skuId（商品ID）
 *                            - value（值）：CartItemDto对象（购物车商品信息）
 *                          优势：
 *                          - 天然支持按用户隔离购物车数据
 *                          - Hash结构适合存储对象，方便单个商品的操作
 *                          - 利用Redis内存操作，性能高效
 *                          核心设计要点说明：
 *                                  1. 数据结构设计
 *                                      Key: cart:member:{memberId}（按用户隔离）
 *                                      Value: Hash结构，field为skuId，value为CartItemDto序列化对象
 *                                  2. 线程安全考虑
 *                                      每个用户独立购物车，避免并发冲突
 *                                      Redis操作是原子性的，保证数据一致性
 *                                  3. 性能优化
 *                                      使用BoundHashOperations减少重复的key操作
 *                                      批量操作时直接遍历values，避免多次网络请求
 *                                  4. 异常处理
 *                                      用户认证失败统一抛出TOKEN_INVALID异常
 *                                      商品不存在时记录日志但继续执行（可根据业务需求调整）
 *                                  5. 扩展性
 *                                      易于添加新字段到CartItemDto
 *                                      FeignClient设计便于后续微服务拆分
 *                                  这样的设计既保证了购物车基本功能的实现，又为后续功能扩展留下了空间。
 * <p>
 * 核心技术：BoundHashOperations
 * 数据格式：
 * -- key <--> 商品列表
 * ---- hKey:value <--> skuId 商品1
 * ---- hKey:value <--> 商品2
 * ---- hKey:value <--> 商品3
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    // Redis操作模板，用于执行Redis命令
    private final RedisTemplate redisTemplate;

    // 商品服务Feign客户端，用于获取商品信息
    private final SkuFeignClient skuFeignService;

    // 购物车转换器，用于DTO对象转换
    private final CartConverter cartConverter;

//    private final CartItemMapper cartItemMapper;  // 新增：数据库Mapper

    /**
     *   TODO       获取用户购物车中的所有商品列表
     *
     * @param memberId 用户ID
     * @return 购物车商品列表，如果用户ID为空返回空列表
     */
    @Override
    public List<CartItemVo> listCartItemsVo(Long memberId) {

        log.info("检查用户ID是否有效");
        if (memberId != null) {

            log.info("获取该用户的购物车Hash操作对象");
            BoundHashOperations cartHashOperations = getCartHashOperations(memberId);


            //把购物车里的购物车所有对象全部取出
            log.info("获取Hash中所有的值（即所有购物车商品）");
            List<CartItemDto> cartItems = cartHashOperations.values();
            if(cartItems == null || cartItems.size() <= 0){
                return Collections.emptyList();
            }
            // 取出购物车里所有的商品id
            log.info("取出购物车里所有的商品id");
            // 使用Stream API进行数据处理：
            // 1. map(CartItemDto::getSkuId)：从CartItemDto中提取skuId
            // 2. distinct()：去重，避免重复查询同一商品
            // 3. toList()：转换为List集合
            List<Long> skuIds = cartItems.stream().map(CartItemDto::getSkuId).distinct().toList();

            //把这些商品信息全部查询出来，为了获取到最新的商品名称也好，商品价格也好 反正我现在是要获取到购物车里的所有商品的最新信息
            log.info("把这些商品信息全部查询出来，为了获取到最新的商品名称也好，商品价格也好 反正我现在是要获取到购物车里的所有商品的最新信息");
            // 通过Feign客户端批量调用商品服务，获取所有商品的最新信息
            // 这样做的好处：
            // 1. 避免多次网络请求（N+1查询问题）
            // 2. 确保显示的商品信息（价格、名称等）是最新的
            List<SkuInfoDTO> skuInfoList = skuFeignService.getSkuInfoList(skuIds);

            //把skuInfoList转为Map。key是id value是SkuInfoDTO
            log.info("将商品列表转换为Map结构，提高后续查找效率");
            log.info("把skuInfoList转为Map。key是id value是SkuInfoDTO");
            log.info("使用HashMap实现O(1)时间复杂度的查找，替代原来的O(n)线性查找");
            Map<Long,SkuInfoDTO> skuInfoMap = new HashMap<Long,SkuInfoDTO>();

            log.info("这个方法很好地展示了如何通过算法优化来提升系统性能，特别是在处理购物车这种可能包含大量商品的场景下");
            for (SkuInfoDTO skuInfoDTO : skuInfoList) {
                log.info("以商品ID作为键，商品信息DTO作为值存入Map");
                skuInfoMap .put(skuInfoDTO.getId(),skuInfoDTO);
            }

            //构建该方法需要的返回值，
            log.info("开始构建前端需要的VO（View Object）对象列表");
            log.info("构建该方法需要的返回值");


            log.info("预分配足够容量的ArrayList，避免扩容带来的性能开销");
            List<CartItemVo> cartItemVoList = new ArrayList<>(cartItems.size());


            log.info("遍历购物车中的每个商品，转换为前端展示用的VO对象");
            for (CartItemDto cartItem : cartItems) {
                // 创建新的VO对象实例
                CartItemVo vo = new CartItemVo();

                // 设置基本属性：商品SKU ID
                vo.setSkuId(cartItem.getSkuId());

                // 注释掉的旧代码：使用线性查找，时间复杂度O(n)
                log.info("注释掉的旧代码：使用线性查找，时间复杂度O(n)");
//                for (SkuInfoDTO skuInfoDTO : skuInfoList) {
//                    if(Objects.equals(skuInfoDTO.getId(), cartItem.getSkuId())){
//                        //TODO  需要组合一个完整的CartItemVo
//                        vo.setSpuName(skuInfoDTO.getSkuName());
//                        vo.setImageUrl(skuInfoDTO.getPicUrl());
//                        vo.setPrice(skuInfoDTO.getPrice());
//                        break;
//                    }
//                }
// 优化后的代码：使用Map查找，时间复杂度O(1) - 这就是"LeetCode 第一题"的优化思想

                //LeetCode 第一题
                log.info("优化后的代码：使用Map查找，时间复杂度O(1) - 这就是\"LeetCode 第一题\"的优化思想");
                SkuInfoDTO skuInfoDTO = skuInfoMap.get(cartItem.getSkuId());

                // TODO: 需要处理skuInfoDTO可能为null的情况（NPE问题）
                //TODO  skuInfoDTO  NPE 的问题
                log.info("设置商品详细信息到VO对象中");
                vo.setSkuName(skuInfoDTO.getSkuName());   // 商品SKU名称
                vo.setSpuName(skuInfoDTO.getSpuName());   // 商品SPU名称
                vo.setPicUrl(skuInfoDTO.getPicUrl());   // 商品图片URL
                vo.setPrice(skuInfoDTO.getPrice());     // 商品最新价格
                vo.setCount(cartItem.getCount());       // 购物车中的商品数量（来自Redis）
                vo.setChecked(cartItem.getChecked());     // 商品选中状态（来自Redis）
                vo.setStock(skuInfoDTO.getStock());       // ✅ 关键：设置库存

                log.info("将构建好的VO对象添加到结果列表");
                cartItemVoList.add(vo);
            }
            log.info("返回构建完成的前端展示数据:{}", cartItemVoList);
            return cartItemVoList;
        }

        log.info("用户ID为空时，返回空列表");
        return Collections.EMPTY_LIST;
    }

    /**
     *   TODO       获取用户购物车中的所有商品列表
     *
     * @param memberId 用户ID
     * @return 购物车商品列表，如果用户ID为空返回空列表
     */
    @Override
    public List<CartItemDto> listCartItems(Long memberId) {

        log.info("检查用户ID是否有效");
        if (memberId != null) {

            log.info("获取该用户的购物车Hash操作对象");
            BoundHashOperations cartHashOperations = getCartHashOperations(memberId);


            //把购物车里的购物车所有对象全部取出

            List<CartItemDto> cartItems = cartHashOperations.values();
            log.info("获取Hash中所有的值（即所有购物车商品）：{}",cartItems);
            return cartItems;
        }
        log.info("用户ID为空时返回空列表，避免空指针异常");
        return Collections.EMPTY_LIST;
    }

    /**
     * TODO  清空当前登录用户的购物车（删除整个购物车Key）
     *
     * @return 总是返回true，表示操作成功
     */
    @Override
    public boolean deleteCart() {

        log.info("构建购物车在Redis中的Key：cart:member:{memberId}");
        String key = OrderConstants.MEMBER_CART_PREFIX + SecurityUtils.getMemberId();

        log.info("直接删除整个购物车Key");
        redisTemplate.delete(key);
        return true;
    }

    /**
     * TODO   添加商品到购物车
     *
     * @param skuId 商品SKU ID
     * @param count 添加商品到购物车商品数量
     * @return 添加成功返回true
     * @throws BizException 如果商品信息获取失败会抛出异常
     */
    @Override
    public boolean addCartItem(Long skuId , Integer count) {

        log.info("添加商品到购物车，skuId: {}, 数量: {}", skuId, count);

        // 1. 参数验证
        if (skuId == null || skuId <= 0) {
            log.error("商品SKU ID无效: {}", skuId);
            throw new BizException("商品ID不能为空");
        }

        if (count == null || count <= 0) {
            // 默认数量为1
            count = 1;
            log.info("数量为空或无效，使用默认值: 1");
        }
        //2. 获取当前登录用户ID
        log.info("从安全工具类获取当前登录用户ID");
        Long memberId = SecurityUtils.getMemberId();

        if (memberId == null) {
            log.error("用户未登录，无法添加购物车");
            throw new BizException(ResultCode.TOKEN_INVALID);
        }

        // 3. 获取商品信息
        log.info("通过商品服务获取商品详细信息");
        SkuInfoDTO skuInfo = skuFeignService.getSkuInfo(skuId);
        if (skuInfo == null) {
            log.error("商品不存在，skuId: {}", skuId);
            throw new BizException("商品不存在");
        }

        // 验证价格不为空
        if (skuInfo.getPrice() == null) {
            log.error("商品价格为空，skuId: {}", skuId);
            throw new BizException("商品价格信息异常");
        }

        // 4. 验证库存
        if (count > skuInfo.getStock()) {
            log.error("商品库存不足，skuId: {}, 库存: {}, 请求数量: {}",
                    skuId, skuInfo.getStock(), count);
            throw new BizException("商品库存不足，仅剩 " + skuInfo.getStock() + " 件");
        }

        // 5. 获取购物车操作对象
        log.info("获取该用户的购物车Hash操作对象");
        BoundHashOperations<String, String, CartItemDto> cartHashOperations = getCartHashOperations(memberId);

        log.info("使用skuId作为Hash的字段名");
        String hKey = String.valueOf(skuId);

        // 6. 尝试从购物车中获取已存在的商品
        log.info("尝试从购物车中获取已存在的商品");
        CartItemDto cartItem = cartHashOperations.get(hKey);

        if (cartItem != null) {

            // 购物车已存在该商品，更新商品数量
            log.info("情况1：购物车中已存在该商品");
            log.info("商品数量+1（每次点击\"加入购物车\"数量增加1）");
            // 计算新的总数量
            int newCount = cartItem.getCount() + count;

            // 再次验证库存（考虑已存在的数量）
            if (newCount > skuInfo.getStock()) {
                log.error("总数量超过库存，skuId: {}, 现有: {}, 新增: {}, 库存: {}",
                        skuId, cartItem.getCount(), count, skuInfo.getStock());
                throw new BizException("添加后总数量超过库存，最多可再添加 " +
                        (skuInfo.getStock() - cartItem.getCount()) + " 件");
            }

            // 更新数量和选中状态
            cartItem.setCount(newCount);
            log.info("设置商品为选中状态");
            cartItem.setChecked(true);

            // 确保价格不为空
            Long newPrice = skuInfo.getPrice();
            if (newPrice == null) {
                newPrice = cartItem.getPrice() != null ? cartItem.getPrice() : 0;
            }

            // 更新价格（保持最新价格）
            cartItem.setPrice(newPrice);   // 确保设置价格
            cartItem.setSkuName(skuInfo.getSkuName());
            cartItem.setPicUrl(skuInfo.getPicUrl());
            // ✅ 关键：更新库存信息
            cartItem.setStock(skuInfo.getStock());

            log.info("购物车已存在商品，更新数量: skuId={}, 原数量={}, 新增={}, 新总数={}",
                    skuId, cartItem.getCount() - count, count, newCount);

        } else {

            // 购物车中不存在该商品，新增商品到购物车
            log.info("情况2：购物车中不存在该商品，需要新增");


                log.info("将商品信息转换为购物车商品对象");
                cartItem = cartConverter.sku2CartItem(skuInfo);

                log.info("设置用户选择的数量");
                cartItem.setCount(count);



                log.info("默认设置为选中状态");
                cartItem.setChecked(true);

                // 确保价格不为空
                if (cartItem.getPrice() == null) {
                    Long price = skuInfo.getPrice();
                    if (price == null) {
                        price = 0L;  // 设置默认价格
                        log.warn("商品价格为空，使用默认价格0，skuId: {}", skuId);
                    }
                    cartItem.setPrice(price);
                }

                // ✅ 关键：设置库存
                cartItem.setStock(skuInfo.getStock());

                // 设置加入时间（用于后续价格过期判断）
                cartItem.setAddTime(LocalDateTime.now());

            log.debug("新增商品到购物车: skuId={}, 商品名称={}, 数量={}, 价格={}",
                    skuId, cartItem.getSkuName(), count, cartItem.getPrice());

        }

        // 7. 保存到Redis购物车
        log.info("将商品信息保存到Redis购物车中");

        if (cartItem.getPrice() == null) {
            log.warn("购物车商品价格为空，设置默认价格0，skuId: {}", skuId);
            cartItem.setPrice(0L);
        }

        cartHashOperations.put(hKey, cartItem);
        log.debug("商品skuId:{}已添加到用户{}的购物车", skuId, memberId);


        // 8. 安全地记录操作日志
        try {
            Long price = cartItem.getPrice() != null ? cartItem.getPrice() : 0;
            Integer itemCount = cartItem.getCount() != null ? cartItem.getCount() : 0;
            Long totalPrice = price.longValue() * itemCount;

            log.info("商品已添加到购物车: 用户ID={}, skuId={}, 商品名称={}, 数量={}, 单价={}, 总价={}",
                    memberId, skuId, cartItem.getSkuName(), itemCount, price, totalPrice);
        } catch (Exception e) {
            log.error("记录购物车日志失败: {}", e.getMessage());
        }

        return true;
    }

    /**
     *        TODO          更新购物车中商品的信息（数量、选中状态）
     *
     * @param cartItem 包含更新信息的购物车商品对象
     * @return 更新成功返回true
     * @throws BizException 用户未登录或token无效时抛出异常
     */
    @Override
    public boolean updateCartItem(CartItemDto cartItem) {
        Long memberId;
        try {

            log.info("获取当前登录用户ID，可能抛出认证异常");
            memberId = SecurityUtils.getMemberId();
        } catch (Exception e) {

            log.info("用户未登录或token无效，抛出业务异常");
            throw new BizException(ResultCode.TOKEN_INVALID);
        }

        log.info("获取用户购物车操作对象");
        BoundHashOperations cartHashOperations = getCartHashOperations(memberId);

        log.info("使用skuId构建Hash字段名");
        String hKey = cartItem.getSkuId() + "";

        log.info("检查购物车中是否存在该商品");
        if (cartHashOperations.get(hKey) != null) {

            log.info("获取购物车中已存在的商品信息");
            CartItemDto cacheCartItem = (CartItemDto) cartHashOperations.get(hKey);

            log.info("只更新传入的非空字段（部分更新）");
            if (cartItem.getChecked() != null) {
                cacheCartItem.setChecked(cartItem.getChecked());
            }
            if (cartItem.getCount() != null) {
                cacheCartItem.setCount(cartItem.getCount());
            }

            log.info("将更新后的商品信息保存回Redis");
            cartHashOperations.put(hKey, cacheCartItem);

            log.debug("更新购物车商品，skuId:{}，数量:{}，选中状态:{}",
                    cartItem.getSkuId(), cacheCartItem.getCount(), cacheCartItem.getChecked());
        }else {
            log.warn("尝试更新不存在的购物车商品，skuId:{}", cartItem.getSkuId());
        }

        return true;
    }

    /**
     * TODO  从购物车中移除指定商品
     *
     * @param skuId 要移除的商品SKU ID
     * @return 移除成功返回true
     * @throws BizException 用户未登录或token无效时抛出异常
     */
    @Override
    public boolean removeCartItem(Long skuId) {
        Long memberId;
        try {
            memberId = SecurityUtils.getMemberId();
        } catch (Exception e) {
            throw new BizException(ResultCode.TOKEN_INVALID);
        }
        BoundHashOperations cartHashOperations = getCartHashOperations(memberId);
        String hKey = skuId + "";

        log.info("直接从Hash中删除该商品字段");
        cartHashOperations.delete(hKey);

        log.debug("从用户{}购物车中移除商品skuId:{}", memberId, skuId);
        return true;
    }


    /**
     * TODO   设置购物车中所有商品的选中状态（全选/取消全选）
     *
     * @param checked 选中状态：true全选，false取消全选
     * @return 操作成功返回true
     * @throws BizException 用户未登录或token无效时抛出异常
     */
    @Override
    public boolean checkAll(boolean checked) {
        Long memberId;
        try {
            memberId = SecurityUtils.getMemberId();
        } catch (Exception e) {
            throw new BizException(ResultCode.TOKEN_INVALID);
        }
        BoundHashOperations cartHashOperations = getCartHashOperations(memberId);

        log.info("遍历购物车中的所有商品");
        for (Object value : cartHashOperations.values()) {
            CartItemDto cartItem = (CartItemDto) value;

            // 更新每个商品的选中状态
            cartItem.setChecked(checked);
            String hKey = cartItem.getSkuId() + "";

            // 将更新后的商品信息保存回Redis
            cartHashOperations.put(hKey, cartItem);
        }

        log.debug("设置用户{}购物车全部商品选中状态为:{}", memberId, checked);
        return true;
    }


    /**
     * TODO   移除购物车中所有选中的商品
     *          典型应用场景：用户支付成功后，清除已购买的商品
     *
     * @return 移除成功返回true
     * @throws BizException 用户未登录或token无效时抛出异常
     */
    @Override
    public boolean removeCheckedItem() {
        Long memberId = SecurityUtils.getMemberId();
        if (memberId == null) {
            throw new BizException(ResultCode.TOKEN_INVALID);
        }
        BoundHashOperations cartHashOperations = getCartHashOperations(memberId);

        log.info("遍历购物车中的所有商品");
        for (Object value : cartHashOperations.values()) {
            CartItemDto cartItem = (CartItemDto) value;

            // 只删除被选中的商品
            if (cartItem.getChecked()) {
                cartHashOperations.delete(cartItem.getSkuId() + "");
                log.debug("移除用户{}购物车中选中的商品skuId:{}", memberId, cartItem.getSkuId());
            }
        }
        return true;
    }

    /**
     *       TODO           获取第一层，即某个用户的购物车
     *                  获取指定用户的购物车Redis操作对象
     *                   您这是纯Redis存储的购物车实现
     *
     * @param memberId 用户ID
     * @return BoundHashOperations对象，用于操作该用户的购物车Hash
     */
    private BoundHashOperations getCartHashOperations(Long memberId) {

        // 所有数据都存在Redis

        log.info("构建购物车在Redis中的Key，格式如：cart:member:123");
        String cartKey = OrderConstants.MEMBER_CART_PREFIX + memberId;

        log.info("获取绑定到特定Key的Hash操作对象，方便后续操作");
        BoundHashOperations operations = redisTemplate.boundHashOps(cartKey);
        return operations;

        // Redis中的数据结构
//        Key: "cart:member:123"
//        Type: Hash
//        Field: "785" (skuId)
//                Value: CartItemDto JSON对象
    }
}
