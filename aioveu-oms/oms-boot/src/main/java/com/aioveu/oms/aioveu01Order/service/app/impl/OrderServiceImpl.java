package com.aioveu.oms.aioveu01Order.service.app.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.aioveu.common.exception.BusinessException;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.ResultCode;
import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.oms.aioveu01Order.model.entity.OmsOrder;
import com.aioveu.oms.aioveu01Order.model.vo.*;
import com.aioveu.oms.aioveu01Order.service.app.OrderService;
import com.aioveu.oms.aioveu01Order.utils.OrderNoGenerator;
import com.aioveu.oms.aioveu02OrderItem.converter.OmsOrderItemConverter;
import com.aioveu.oms.aioveu03OrderDelivery.model.entity.OmsOrderDelivery;
import com.aioveu.oms.aioveu03OrderDelivery.service.OmsOrderDeliveryService;
import com.aioveu.pay.api.PayFeignClient;
import com.aioveu.pay.model.PaymentParamsVO;
import com.aioveu.pay.model.PaymentRequestDTO;
import com.alibaba.nacos.shaded.com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.alibaba.nacos.shaded.io.grpc.netty.shaded.io.netty.handler.codec.DateFormatter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyV3Result;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.aioveu.common.result.Result;
import com.aioveu.common.web.exception.BizException;
import com.aioveu.oms.aioveu01Order.constant.OrderConstants;
import com.aioveu.oms.aioveu01Order.converter.OmsOrderConverter;
import com.aioveu.oms.aioveu01Order.enums.OrderStatusEnum;
import com.aioveu.oms.aioveu01Order.enums.OrderSourceEnum;
import com.aioveu.oms.aioveu01Order.enums.PaymentMethodEnum;
import com.aioveu.oms.aioveu01Order.mapper.OmsOrderMapper;
import com.aioveu.oms.aioveu02OrderItem.model.vo.OrderItemDTO;
import com.aioveu.oms.aioveu02OrderItem.model.entity.OmsOrderItem;
import com.aioveu.oms.aioveu05OrderPay.model.form.OrderPaymentForm;
import com.aioveu.oms.aioveu05OrderPay.model.form.OrderSubmitForm;
import com.aioveu.oms.aioveu01Order.model.query.OrderPageQuery;
import com.aioveu.oms.aioveu01Order.service.CartService;
import com.aioveu.oms.aioveu02OrderItem.service.OmsOrderItemService;
import com.aioveu.pms.api.SkuFeignClient;
import com.aioveu.pms.model.dto.LockSkuDTO;
import com.aioveu.pms.model.dto.SkuInfoDTO;
import com.aioveu.ums.api.MemberFeignClient;
import com.aioveu.ums.dto.MemberAddressDTO;
import feign.FeignException;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.hutool.core.util.NumberUtil.toBigDecimal;



/**
 * @Description: TODO 订单业务实现类
 *                      核心功能：订单创建、支付、取消、删除等全生命周期管理
 *                      技术亮点：
 *                             - 分布式事务：使用Seata保证数据一致性
 *                             - 异步编程：使用CompletableFuture提升接口性能
 *                             - 分布式锁：使用Redisson防止重复支付
 *                             - 消息队列：使用RabbitMQ处理订单超时
 *                             - 防重提交：使用Redis Token机制
 *                        核心设计亮点总结：
 *                          🏗️ 架构设计
 *                              1.分层清晰：Controller → Service → Mapper，职责分离明确
 *                              2.异步优化：使用CompletableFuture并行处理IO密集型操作
 *                              3.分布式事务：Seata保证跨服务数据一致性
 *                          🔒 安全防护
 *                              1.防重提交：Redis Token + LUA脚本原子操作
 *                              2.分布式锁：Redisson防止重复支付
 *                              3.数据校验：订单提交前的全面业务校验
 *                          ⚡ 性能优化
 *                              1.批量操作：商品信息批量查询，避免N+1问题
 *                              2.异步处理：订单确认页面的并行数据加载
 *                              3.消息队列：订单超时使用延迟消息，避免轮询
 *                          🔄 业务流程
 *                              1.订单确认：商品校验 → 地址获取 → 令牌生成
 *                              2.订单提交：防重校验 → 库存锁定 → 订单创建 → 超时设置
 *                              3.订单支付：支付路由 → 分布式锁 → 状态更新 → 购物车清理
 *                          这个订单服务实现了一个完整的电商订单系统，涵盖了从订单创建到支付的完整业务流程，具有良好的可扩展性和容错性。
 * @Author: 雒世松
 * @Date: 2025/6/5 18:13
 * @param
 * @return:
 **/

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OmsOrderMapper, OmsOrder> implements OrderService {


    // 购物车服务
    private final CartService cartService;

    // 订单物流服务
    private final OmsOrderDeliveryService orderDeliveryService;

    // 订单项服务
    private final OmsOrderItemService orderItemService;

    // RabbitMQ消息模板，用于发送延迟消息（如订单超时关闭）
    private final RabbitTemplate rabbitTemplate;

    // Redis模板，用于防重提交和分布式锁
    private final StringRedisTemplate redisTemplate;

    // 线程池执行器，用于异步任务处理
    private final ThreadPoolExecutor threadPoolExecutor;

    // 会员服务Feign客户端
    private final MemberFeignClient memberFeignClient;

    // 商品服务Feign客户端
    private final SkuFeignClient skuFeignClient;


    // 添加支付微服务 Feign 客户端
    private final PayFeignClient payFeignClient;


    // 分布式锁客户端
    private final RedissonClient redissonClient;

    // 订单转换器
    private final OmsOrderConverter omsOrderConverter;

    // 订单项转换器
    private final OmsOrderItemConverter omsOrderItemConverter;

    private final OmsOrderItemService omsOrderItemService;

    private final OmsOrderDeliveryService omsOrderDeliveryService;



    // 开启模拟支付
    @Value("${pay.mock.enabled:true}")
    private Boolean mockPayEnabled;

    /**
     * TODO  订单分页列表查询
     *
     * @param queryParams 分页查询参数
     * @return 分页订单数据
     */
    @Override
    public IPage<OrderPageVO> getOrderPage(OrderPageQuery queryParams) {

        log.info("【Oms-Order】分页查询订单列表，查询参数：{}", queryParams);

        log.info("【Oms-Order】调用Mapper进行分页查询，返回业务对象分页");
        Page<OrderBO> boPage = this.baseMapper.getOrderPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams);

        // 2. 如果订单不为空，查询商品
        if (!CollectionUtils.isEmpty(boPage.getRecords())) {
            // 收集订单ID
            List<Long> orderIds = boPage.getRecords().stream()
                    .map(OrderBO::getId)
                    .collect(Collectors.toList());

            // 批量查询商品
            List<OmsOrderItem> allItems = omsOrderItemService.list(
                    new LambdaQueryWrapper<OmsOrderItem>().in(OmsOrderItem::getOrderId, orderIds));

            // 批量查询物流信息
            List<OmsOrderDelivery> allDeliveries = omsOrderDeliveryService.list(
                    new LambdaQueryWrapper<OmsOrderDelivery>()
                            .in(OmsOrderDelivery::getOrderId, orderIds)
                            .orderByDesc(OmsOrderDelivery::getCreateTime) // 按创建时间降序，取最新的一条
            );


            // 关键步骤：建立订单ID -> 商品列表的映射  OrderBO.OrderItem是内部类
            Map<Long, List<OrderBO.OrderItem>> itemsByOrderId = allItems
                    .stream()
                    .map(item -> {
                        OrderBO.OrderItem bo = new OrderBO.OrderItem();
                        bo.setId(item.getId());
                        bo.setOrderId(item.getOrderId());
                        bo.setSkuId(item.getSkuId());
                        bo.setSkuSn(item.getSkuSn());
                        bo.setSkuName(item.getSkuName());
                        bo.setSpuName(item.getSpuName());
                        bo.setPicUrl(item.getPicUrl());
                        bo.setPrice(item.getPrice());
                        bo.setQuantity(item.getQuantity());
                        bo.setTotalAmount(item.getTotalAmount());
                        return bo;
                    })
                    .collect(Collectors.groupingBy(OrderBO.OrderItem::getOrderId));

            // 建立订单ID -> 物流信息的映射（每个订单取最新的物流信息）
            Map<Long, OrderBO.OrderDelivery> deliveryByOrderId = allDeliveries
                    .stream()
                    .collect(Collectors.toMap(
                            OmsOrderDelivery::getOrderId,
                            delivery -> {
                                OrderBO.OrderDelivery deliveryBo = new OrderBO.OrderDelivery();
                                BeanUtils.copyProperties(delivery, deliveryBo);
                                return deliveryBo;
                            },
                            (oldDelivery, newDelivery) -> newDelivery // 如果同一个订单有多个物流记录，取最新的
                    ));



            // 3. 将商品设置到对应的订单中，并设置展示名
            boPage.getRecords().forEach(order -> {
                List<OrderBO.OrderItem> items = itemsByOrderId.getOrDefault(order.getId(),
                        Collections.emptyList());

                log.info("通过映射得到的商品各项: {}", items);
                // 设置商品列表
                order.setOrderItems(items);


                // 设置物流信息
                OrderBO.OrderDelivery delivery = deliveryByOrderId.get(order.getId());
                if (delivery != null) {
                    order.setOrderDelivery(delivery);
                    log.info("订单{}的物流信息: {}", order.getId(), delivery);
                } else {
                    // 如果没有物流信息，设置默认值或为空
                    log.info("订单{}暂无物流信息", order.getId());
                    order.setOrderDelivery(new OrderBO.OrderDelivery());
                }


                log.info("前端展示的订单信息: {}", order);


                // 设置订单展示名：取第一个商品的名称
                if (!items.isEmpty()) {
                    OrderBO.OrderItem firstItem = items.get(0);
                    log.info("第一个商品详情: {}", firstItem);
                    // 设置到订单的 spuName 字段
                    order.setSpuName(firstItem.getSpuName());
                    log.info("第一个商品spuName值: {}", firstItem.getSpuName());
                    // 也可以设置第一个商品的图片
                    order.setPicUrl(firstItem.getPicUrl());
                    log.info("第一个商品picUrl值: {}", firstItem.getPicUrl());
                } else {
                    // 如果没有商品，设置默认值
                    order.setSpuName("我的订单");
                    order.setPicUrl("https://minio.aioveu.com/aioveu/20251128/9dc40c944d044c8d8ae37b14a35b8b83.png");
                }
            });
        }


        Page<OrderPageVO> orderPageVO=  omsOrderConverter.toVoPageForApp(boPage);
        log.info("将业务对象分页转换为前端展示的分页VO:{}",orderPageVO);
        log.info("【Oms-Order】订单分页查询完成，共查询到{}条记录", orderPageVO.getTotal());


        return orderPageVO;
    }

    /**
     *  TODO        订单确认 → 进入创建订单页面
     *          获取购买商品明细、用户默认收货地址、防重提交唯一token
     *                  进入订单创建页面有两个入口：
     *                  1. 立即购买：传入skuId参数
     *                  2. 购物车结算：不传skuId参数
     *
     * @param skuId 商品ID(直接购买传值，购物车结算传null)
     * @return {@link OrderConfirmVO} 订单确认页面需要的数据
     */
    @Override
    public OrderConfirmVO confirmOrder(Long skuId) {


        Long memberId = SecurityUtils.getMemberId();
        log.info("【订单确认】开始处理，用户ID: {}, SKU ID: {}", memberId, skuId);


        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        RequestContextHolder.setRequestAttributes(attributes, true);
        // 解决子线程无法获取HttpServletRequest请求对象中数据的问题
        log.info("解决子线程无法获取HttpServletRequest请求对象中数据的问题");
        log.info("将当前请求属性保存，以便在异步线程中继续使用");
        log.info("【上下文保存】已保存请求上下文用于异步线程");

        log.info("使用CompletableFuture进行异步并行处理，提升接口响应速度");


        log.info("【异步任务1】开始获取订单商品");
        CompletableFuture<List<OrderItemDTO>> getOrderItemsFuture = CompletableFuture.supplyAsync(
                        () ->
                                this.getOrderItems(skuId, memberId),


                        threadPoolExecutor)
                .exceptionally(ex -> {
                    log.info("异常处理：如果获取商品信息失败，返回空列表并记录错误日志");
                    log.error("Failed to get order items: {}", ex.toString());
                    return Collections.emptyList();
                });


        // 用户收货地址
        log.info("异步任务2：获取用户收货地址");
        CompletableFuture<List<MemberAddressDTO>> getMemberAddressFuture = CompletableFuture.supplyAsync(() -> {


            //在异步线程中尝试访问已回收的请求对象
            //但在 Spring 的线程池中，请求上下文可能已经被清理了。

            // 关键：在异步线程中恢复请求上下文
            RequestContextHolder.setRequestAttributes(attributes, true);


            log.info("通过Feign客户端调用会员服务获取地址列表");
            Result<List<MemberAddressDTO>> getMemberAddressResult = memberFeignClient.listMemberAddresses(memberId);
            if (Result.isSuccess(getMemberAddressResult)) {
                return getMemberAddressResult.getData();
            }
            return null;
        }, threadPoolExecutor).exceptionally(ex -> {
            log.info("异常处理：地址获取失败时记录日志并返回空列表");
            log.error("Failed to get addresses for memberId {} : {}", memberId, ex.toString());
            return Collections.emptyList();
        });

        // 生成唯一令牌,防止重复提交(原理：提交会消耗令牌，令牌被消耗无法再次提交)
        log.info("异步任务3：生成防重提交令牌");
        log.info("生成唯一令牌,防止重复提交(原理：提交会消耗令牌，令牌被消耗无法再次提交)");
        CompletableFuture<String> generateOrderTokenFuture = CompletableFuture.supplyAsync(() -> {

            log.info("生成唯一的订单令牌，防止重复提交");
            String orderToken = this.generateTradeNo(memberId);


            log.info("将令牌存入Redis，设置过期时间（需要在提交时验证和删除）");
            redisTemplate.opsForValue().set(OrderConstants.ORDER_TOKEN_PREFIX + orderToken, orderToken);
            return orderToken;
        }, threadPoolExecutor).exceptionally(ex -> {

            log.info("异常处理：令牌生成失败记录日志");
            log.error("Failed to generate order token .");
            return null;
        });

        log.info("等待所有异步任务完成");
        CompletableFuture.allOf(getOrderItemsFuture, getMemberAddressFuture, generateOrderTokenFuture).join();

        log.info("等待所有异步任务完成，获取订单商品信息:{}" ,getOrderItemsFuture);
        log.info("等待所有异步任务完成，获取用户收货地址：{}" ,getMemberAddressFuture);
        log.info("等待所有异步任务完成，生成防重提交令牌：{}" ,generateOrderTokenFuture);

        log.info("构建返回结果");
        OrderConfirmVO orderConfirmVO = new OrderConfirmVO();
        log.info("订单商品列表");
        orderConfirmVO.setOrderItems(getOrderItemsFuture.join());  // 订单商品列表
        log.info("收货地址列表");
        orderConfirmVO.setAddresses(getMemberAddressFuture.join());   // 收货地址列表
        log.info("防重提交令牌");
        orderConfirmVO.setOrderToken(generateOrderTokenFuture.join());   // 防重提交令牌

        log.info("Order confirm response for skuId {}: {}", skuId, orderConfirmVO);
        return orderConfirmVO;
    }

    /**
     *      TODO            订单提交
     *                  核心业务流程：1. 参数校验 -> 2. 防重提交校验 → 3. 商品信息校验
     *                  → 4. 库存预检查  → 5. 锁定库存 -> 6. 创建订单 ->  7. 清理购物车
     *
     * @param submitForm {@link OrderSubmitForm} 订单提交表单数据
     * @return 订单编号
     */
    //orderService.submitOrder()方法中抛出的 BusinessException被捕获但没有正确传播到 Controller 层。
    @Override
    @GlobalTransactional(name = "submitOrder", rollbackFor = Exception.class, timeoutMills = 30000)
    // 确保捕获异常
    //需要检查事务传播行为
    //配置 Seata 不包装异常
    //seata 全局事务拦截器包装了你的业务异常，需要解开这个包装，让真正的 BusinessException能够被全局异常处理器捕获和处理。
    public String submitOrder(OrderSubmitForm submitForm) throws BusinessException{


        long startTime = System.currentTimeMillis();
        String orderSn = null;

        try {
            log.info("【订单提交】开始处理，订单令牌: {}", submitForm.getOrderToken());
            log.info("【订单提交】提交参数: {}", JSONUtil.toJsonStr(submitForm));

            String orderToken = submitForm.getOrderToken();
            List<OrderSubmitForm.OrderItem> orderItems = submitForm.getOrderItems();

            // ==================== 1. 参数校验 ====================
            validateSubmitForm(submitForm);

            // ==================== 2. 防重提交校验 ====================
            validateOrderToken(orderToken);

            // ==================== 3. 商品信息校验 ====================
            List<SkuInfoDTO> latestSkuInfos = validateAndGetSkuInfos(orderItems);

            // ==================== 4. 库存预检查 ====================
            preCheckStock(orderItems, latestSkuInfos);

            // ==================== 5. 锁定库存 ====================
            lockStockWithRetry(orderItems, orderToken, 2);

            // ==================== 6. 创建订单 ====================
            orderSn = createOrder(submitForm, orderItems, latestSkuInfos);

            // ==================== 7. 清理购物车 ====================
            clearCartItems(orderItems, submitForm.getMemberId());

            long duration = System.currentTimeMillis() - startTime;
            log.info("【订单提交】成功，订单号: {}, 耗时: {}ms", orderSn, duration);

            return orderSn;

        } catch (BusinessException e) {

            //1.第一个 catch (BusinessException e)捕获了业务异常
            //2.你 throw e重新抛出
            //3.但 Seata 的拦截器捕获了这个异常，把它包装成了 RuntimeException: try to proceed invocation error
            //4.这个包装后的 RuntimeException不会被 catch (BusinessException e)捕获
            //5.而是被 catch (Exception e)捕获
            //6.在 catch (Exception e)中，你检查 e.getCause() instanceof BusinessException是 true
            //7.你解包并重新抛出 throw be
            //8.但实际上，这个重新抛出的 be又会被 Seata 包装一次！
            //这是一个死循环。

            // 记录原始异常
            log.error("【订单提交】异常: ", e);

            // 直接抛出，不要做任何包装
            // Seata 会包装它，但我们会在 Controller 或全局异常处理器中解开
            throw e;  // 直接抛出，让 Seata 处理
            //但异常被 Seata 的 GlobalTransactionalInterceptor包装了
        } catch (Exception e) {

            // 如果是 Seata 包装的异常，需要解包
            if (e.getCause() instanceof BusinessException) {
                BusinessException be = (BusinessException) e.getCause();
                log.error("【订单提交】业务异常: {}", be.getMessage());
                throw be;
            }

            log.error("【订单提交】系统异常: ", e);
            throw new BusinessException("订单提交失败，请稍后重试", ResultCode.Order_submission_system_exception);
        }


    }


    /**
     * 参数校验
     */
    private void validateSubmitForm(OrderSubmitForm submitForm) {
        log.info("【参数校验】开始校验提交参数");

        Assert.notNull(submitForm, "订单提交参数不能为空");
//        Assert.hasLength(submitForm.getOrderToken(), "订单令牌不能为空");

        List<OrderSubmitForm.OrderItem> orderItems = submitForm.getOrderItems();
        Assert.notEmpty(orderItems, "订单商品不能为空");

//        Assert.notNull(submitForm.getMemberId(), "用户ID不能为空");
        Assert.notNull(submitForm.getShippingAddress(), "收货地址不能为空");
        Assert.notNull(submitForm.getPaymentAmount(), "支付金额不能为空");

        // 校验商品数量
        for (OrderSubmitForm.OrderItem item : orderItems) {
            Assert.notNull(item.getSkuId(), "商品ID不能为空");
            Assert.notNull(item.getQuantity(), "商品数量不能为空");
            Assert.isTrue(item.getQuantity() > 0, "商品数量必须大于0");
            Assert.notNull(item.getPrice(), "商品价格不能为空");

            //item.getPrice()返回的是 Long类型，而不是 BigDecimal类型
//            Assert.isTrue(item.getPrice().compareTo(BigDecimal.ZERO) > 0, "商品价格必须大于0");
            Assert.isTrue(item.getPrice() > 0, "商品价格必须大于0");

        }

        log.info("【参数校验】参数校验通过，商品数量: {}", orderItems.size());
    }

    /**
     * 防重提交校验
     */
    private void validateOrderToken(String orderToken) {
        log.info("【防重校验】开始校验订单令牌: {}", orderToken);

        // 使用LUA脚本保证原子性

        String lockAcquireScript =
                "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                        "    return redis.call('del', KEYS[1]) " +
                        "else " +
                        "    return 0 " +
                        "end";

        // 1. 判断订单是否重复提交(LUA脚本保证获取和删除的原子性，成功返回1，否则返回0)
        log.info("1. 防重提交校验：使用LUA脚本保证原子性（获取和删除在同一个原子操作中）");
        log.info("1. 判断订单是否重复提交(LUA脚本保证获取和删除的原子性，成功返回1，否则返回0");

        Long lockAcquired = this.redisTemplate.execute(
                new DefaultRedisScript<>(lockAcquireScript, Long.class),
                Collections.singletonList(OrderConstants.ORDER_TOKEN_PREFIX + orderToken),
                orderToken
        );

        if (lockAcquired == null || !lockAcquired.equals(1L)) {
            log.warn("【防重校验】如果令牌不存在或删除失败，说明订单重复提交，令牌: {}", orderToken);
//            Assert.isTrue(lockAcquired != null && lockAcquired.equals(1L), "订单重复提交，请刷新页面后重试");
            throw new BusinessException("订单重复提交，请刷新页面后重试");
        }

        log.info("【防重校验】令牌校验通过");
    }

    /**
     * 商品信息校验并获取最新信息
     */
    private List<SkuInfoDTO> validateAndGetSkuInfos(List<OrderSubmitForm.OrderItem> orderItems) {
        // 2. 订单商品校验 (PS：校验进入订单确认页面到提交过程商品(价格、上架状态)变化)
        log.info("【商品校验】：校验从订单确认到提交过程中商品信息是否发生变化");
        log.info("【商品校验】开始校验商品信息，商品数量: {}", orderItems.size());

        // 提取SKU ID
        List<Long> skuIds = orderItems.stream()
                .map(OrderSubmitForm.OrderItem::getSkuId)
                .distinct()
                .collect(Collectors.toList());

        log.info("【商品查询】批量查询商品信息，提取所有商品SKU IDs: {}", skuIds);

        // 批量查询商品最新信息
        List<SkuInfoDTO> skuList;
        try {

            skuList = skuFeignClient.getSkuInfoList(skuIds);

            if (skuList == null) {
                log.error("【商品查询】获取商品信息失败");
                throw new BusinessException("获取商品信息失败");
            }

        } catch (FeignException e) {
            log.error("【商品查询】Feign调用异常: {}", e.getMessage());
            throw new BusinessException("商品服务暂时不可用，请稍后重试");
        } catch (Exception e) {
            log.error("【商品查询】查询异常: {}", e.getMessage(), e);
            throw new BusinessException("商品信息获取失败");
        }

        // 构建SKU映射，方便查找
        Map<Long, SkuInfoDTO> skuMap = skuList.stream()
                .collect(Collectors.toMap(SkuInfoDTO::getId, Function.identity()));

        // 逐个校验商品
        for (OrderSubmitForm.OrderItem item : orderItems) {

            //查找对应的商品信息
//            SkuInfoDTO skuInfo = skuList.stream().filter(sku -> sku.getId().equals(item.getSkuId()))
//                    .findFirst()
//                    .orElse(null);

            SkuInfoDTO skuInfo = skuMap.get(item.getSkuId());

            if (skuInfo == null) {
                log.warn("【商品校验】商品不存在，SKU ID: {}", item.getSkuId());
                throw new BusinessException(String.format("商品【%s】已下架或删除", item.getSkuName()));
            }

//            // 检查商品状态
//            if (skuInfo.getStatus() != 1) {
//                log.warn("【商品校验】商品已下架，SKU ID: {}, 状态: {}", item.getSkuId(), skuInfo.getStatus());
//                throw new BusinessException(String.format("商品【%s】已下架", item.getSkuName()));
//            }

            // 检查商品价格
            if (item.getPrice().compareTo(skuInfo.getPrice()) != 0) {
                log.warn("【商品校验】商品价格变动，SKU ID: {}, 原价: {}, 现价: {}",
                        item.getSkuId(), item.getPrice(), skuInfo.getPrice());
                throw new BusinessException(String.format("商品【%s】价格已变动，请刷新页面", item.getSkuName()));
            }

            // 检查商品库存
            if (skuInfo.getStock() < item.getQuantity()) {
                log.warn("【商品校验】商品库存不足，SKU ID: {}, 库存: {}, 需要: {}",
                        item.getSkuId(), skuInfo.getStock(), item.getQuantity());
                throw new BusinessException(String.format("商品【%s】库存不足，当前库存%s件",
                        item.getSkuName(), skuInfo.getStock()));
            }

            log.info("【商品校验】商品 {} 校验通过，库存: {}/{}",
                    item.getSkuName(), skuInfo.getStock(), item.getQuantity());
        }

        log.info("【商品校验】所有商品校验通过");
        return skuList;
    }

    /**
     * 库存预检查
     */
    private void preCheckStock(List<OrderSubmitForm.OrderItem> orderItems, List<SkuInfoDTO> skuInfos) {
        log.info("【库存预检】开始库存预检查");

        Map<Long, Integer> skuStockMap = skuInfos.stream()
                .collect(Collectors.toMap(SkuInfoDTO::getId, SkuInfoDTO::getStock));

        for (OrderSubmitForm.OrderItem item : orderItems) {
            Integer stock = skuStockMap.get(item.getSkuId());
            if (stock == null) {
                log.warn("【库存预检】商品库存信息不存在，SKU ID: {}", item.getSkuId());
                throw new BusinessException("商品库存信息异常");
            }

            if (stock < item.getQuantity()) {
                log.warn("【库存预检】库存不足，SKU ID: {}, 需要: {}, 库存: {}",
                        item.getSkuId(), item.getQuantity(), stock);
                throw new BusinessException(String.format("商品【%s】库存不足，当前剩余%s件",
                        item.getSkuName(), stock));
            }
        }

        log.info("【库存预检】库存预检查通过");
    }

    /**
     * 带重试的库存锁定
     */
    private void lockStockWithRetry(List<OrderSubmitForm.OrderItem> orderItems, String orderToken, int maxRetries) {
        log.info("【库存锁定】开始锁定库存，重试次数: {}", maxRetries);

        List<LockSkuDTO> lockSkuList = orderItems.stream()
                .map(item -> new LockSkuDTO(item.getSkuId(), item.getQuantity()))
                .collect(Collectors.toList());

        int retryCount = 0;
        while (retryCount <= maxRetries) {
            try {
                log.info("【库存锁定】第{}次尝试锁定", retryCount + 1);

                Boolean result = skuFeignClient.lockStock(orderToken, lockSkuList);

                if (result) {
                    log.info("【库存锁定】库存锁定成功");
                    return;
                } else {
                    log.warn("【库存锁定】锁定失败: {}");

                    if (retryCount < maxRetries) {
                        retryCount++;
                        Thread.sleep(500L * retryCount); // 指数退避
                        continue;
                    }

                    throw new BusinessException("库存锁定失败: ");
                }

            } catch (FeignException e) {
                log.error("【库存锁定】Feign调用异常，状态码: {}, 消息: {}", e.status(), e.getMessage());

                if (e.status() == 400) {
                    // 400错误通常是库存不足
                    String errorMsg = "商品库存不足";
                    try {
                        // 尝试解析错误消息
                    } catch (Exception ignored) {
                    }
                    throw new BusinessException(errorMsg);
                } else if (e.status() == 503 || e.status() == 504) {
                    // 服务不可用，重试
                    if (retryCount < maxRetries) {
                        retryCount++;
                        log.warn("【库存锁定】服务暂时不可用，等待重试");
                        //Thread.sleep()方法会抛出 InterruptedException，需要进行异常处理
//                        Thread.sleep(1000L * retryCount);
                        continue;
                    }
                    throw new BusinessException("库存服务暂时不可用，请稍后重试");
                } else {
                    throw new BusinessException("库存锁定异常，请稍后重试");
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new BusinessException("库存锁定被中断");
            } catch (Exception e) {
                log.error("【库存锁定】系统异常: ", e);
                throw new BusinessException("库存锁定失败，请稍后重试");
            }
        }

        throw new BusinessException("库存锁定失败，已达到最大重试次数");
    }


    /**
     * 创建订单
     */
    private String createOrder(OrderSubmitForm submitForm,
                               List<OrderSubmitForm.OrderItem> orderItems,
                               List<SkuInfoDTO> skuInfos) {
        log.info("【创建订单】开始创建订单");

        try {

            // 构建订单实体
            OmsOrder order = new OmsOrder();

            Long memberId =  SecurityUtils.getMemberId();
            log.info("【创建订单】8.上下文获取会员ID: {}", memberId);

            // 生成订单号
//            String orderSn = OrderNoGenerator.generateOrderNo(submitForm.getMemberId());
            String orderSn = OrderNoGenerator.generateOrderNoRandom(memberId);
            log.info("【创建订单】2.生成订单号: {}", orderSn);

            // 订单总额
            Long totalAmount = calculateOrderAmount(orderItems);
            log.info("【创建订单】3.订单总额: {}", totalAmount);

            // 商品总数
            int  totalQuantity = orderItems.size();
            log.info("【创建订单】4.商品总数: {}", totalAmount);

            // 订单来源
            int source = OrderSourceEnum.APP.getValue();
            log.info("【创建订单】5.订单来源: {}", source);

            // 订单状态
            int  status = OrderStatusEnum.UNPAID.getValue();
            log.info("【创建订单】6.订单状态: {}", status);

            // 订单备注
            String  remark = submitForm.getRemark();
            log.info("【创建订单】7.订单备注: {}", remark);

            // 使用的优惠券
            Long  couponId = submitForm.getCouponId();
            log.info("【创建订单】9.使用的优惠券: {}", couponId);

            // 优惠券抵扣金额
            Long  couponAmount = submitForm.getCouponAmount() != null ? submitForm.getCouponAmount() : 0;
            log.info("【创建订单】10.优惠券抵扣金额: {}", couponAmount);

            // 运费金额
            Long  freightAmount = submitForm.getFreightAmount() != null ? submitForm.getFreightAmount() : 0;
            log.info("【创建订单】11.运费金额: {}", freightAmount);

            // 应付总额
            Long  paymentAmount = submitForm.getPaymentAmount() != null ? submitForm.getPaymentAmount() : 0;
            log.info("【创建订单】12.应付总额: {}", paymentAmount);

            // 支付时间
            LocalDateTime paymentTime= submitForm.getPaymentTime() != null ? submitForm.getPaymentTime() : LocalDateTime.now();
            log.info("【创建订单】13.支付时间: {}", paymentTime);

            // 支付方式
            Integer paymentMethod= submitForm.getPaymentMethod() != null ? submitForm.getPaymentMethod() : 1;
            log.info("【创建订单】14.支付方式: {}", paymentMethod);

            log.info("【创建订单】开始赋值===========");

            order.setOrderSn(orderSn);
//            order.setOrderToken(submitForm.getOrderToken());
            order.setTotalAmount(totalAmount);
            order.setTotalQuantity(totalQuantity);
            order.setSource(source);
            order.setStatus(status);
            order.setRemark(remark);
            order.setMemberId(memberId);
            order.setMemberId(memberId);
            order.setCouponId(couponId);
            order.setCouponAmount(couponAmount);
            order.setFreightAmount(freightAmount);
            order.setPaymentAmount(paymentAmount);
            order.setPaymentTime(paymentTime);
            order.setPaymentMethod(paymentMethod);


            //支付时间
            //支付方式
            //支付第三方商户订单号
//            order.setPaymentMethod(submitForm.getPaymentMethod());


            // 保存订单
            boolean saveResult = this.save(order);
            Assert.isTrue(saveResult, "订单保存失败");

            // 使用LambdaWrapper查询
            LambdaQueryWrapper<OmsOrder> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OmsOrder::getOrderSn, orderSn);

            OmsOrder omsOrder = this.getOne(queryWrapper);

            // 设置订单收货地址
            // 构建订单配送实体
            OmsOrderDelivery orderDelivery = new OmsOrderDelivery();
            OrderSubmitForm.ShippingAddress shippingAddress = submitForm.getShippingAddress();

            // 订单id
            Long orderId = omsOrder.getId();
            log.info("【创建订单物流】2.订单Id: {}", orderId);

            // 收货人姓名
            String consigneeName = shippingAddress.getConsigneeName();
            log.info("【创建订单物流】5.收货人姓名: {}", consigneeName);

            String receiverPhone = shippingAddress.getConsigneeMobile();
            log.info("【创建订单物流】6.收货人电话: {}", receiverPhone);

            String receiverProvince = shippingAddress.getProvince();
            log.info("【创建订单物流】7.收货人省份: {}", receiverProvince);

            String receiverCity = shippingAddress.getCity();
            log.info("【创建订单物流】8.收货人城市: {}", receiverCity);

            String receiverRegion = shippingAddress.getDistrict();
            log.info("【创建订单物流】9.收货人城市: {}", receiverRegion);

            String receiverDetailAddress = shippingAddress.getDetailAddress();
            log.info("【创建订单物流】10.收货人详细地址: {}", receiverDetailAddress);

            orderDelivery.setOrderId(orderId);
            orderDelivery.setReceiverName(consigneeName);
            orderDelivery.setReceiverPhone(receiverPhone);
            orderDelivery.setReceiverProvince(receiverProvince);
            orderDelivery.setReceiverCity(receiverCity);
            orderDelivery.setReceiverRegion(receiverRegion);
            orderDelivery.setReceiverDetailAddress(receiverDetailAddress);

            //保存物流信息
            boolean saveOrderDeliveryResult =orderDeliveryService.save(orderDelivery);
            log.info("【保存配送信息】成功：{}" , saveOrderDeliveryResult);

            // 保存订单商品
            saveOrderItems(order.getId(), orderItems, skuInfos);

            // 发送订单创建事件
//            sendOrderCreatedEvent(order);

            log.info("【创建订单】订单创建成功，订单号: {}", orderSn);
            return orderSn;

        } catch (Exception e) {
            log.error("【创建订单】创建失败: ", e);
            throw new BusinessException("订单创建失败，请稍后重试");
        }
    }

    /**
     * 计算订单金额  使用 reduce 方法
     */
    private Long calculateOrderAmount(List<OrderSubmitForm.OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> {
                    Long price = item.getPrice() != null ? item.getPrice() : 0L;
                    Integer quantity = item.getQuantity() != null ? item.getQuantity() : 0;
                    return price * quantity;
                })
                .reduce(0L, Long::sum);
    }

    /**
     * 保存订单商品
     */
    private void saveOrderItems(Long orderId,
                                List<OrderSubmitForm.OrderItem> orderItems,
                                List<SkuInfoDTO> skuInfos) {
        Map<Long, SkuInfoDTO> skuMap = skuInfos.stream()
                .collect(Collectors.toMap(SkuInfoDTO::getId, Function.identity()));

        log.info("保存订单明细（订单商品项）");
        List<OmsOrderItem> orderItemList = orderItems.stream()
                .map(item -> {
                    OmsOrderItem orderItem = new OmsOrderItem();
                    log.info("设置订单ID关联");
                    orderItem.setOrderId(orderId);
//                    orderItem.setOrderSn(OrderNoGenerator.generateOrderNo(null)); // 临时订单号
                    orderItem.setSpuName(item.getSpuName());

                    log.info("订单商品提交项的商品名：{}",item.getSpuName());
                    orderItem.setSkuId(item.getSkuId());
                    orderItem.setSkuName(item.getSkuName());

                    SkuInfoDTO skuInfo = skuMap.get(item.getSkuId());
                    if (skuInfo != null) {
                        orderItem.setPicUrl(skuInfo.getPicUrl());
                        orderItem.setSkuSn(skuInfo.getSkuSn());
                    }

                    orderItem.setPrice(item.getPrice());
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setTotalAmount(item.getPrice() * item.getQuantity());
//                    orderItem.setSkuSpec(item.getSpec() != null ? item.getSpec() : "");

                    return orderItem;
                })
                .collect(Collectors.toList());

        // 批量保存
        orderItemService.saveBatch(orderItemList);
        log.info("【创建订单】保存{}个订单商品", orderItemList.size());

    }


    /**
     * 清理购物车
     */
    private void clearCartItems(List<OrderSubmitForm.OrderItem> orderItems, Long memberId) {
        if (CollectionUtils.isEmpty(orderItems) || memberId == null) {
            return;
        }

        try {
            List<Long> skuIds = orderItems.stream()
                    .map(OrderSubmitForm.OrderItem::getSkuId)
                    .collect(Collectors.toList());

//            Result<Boolean> result = cartFeignClient.clearCheckedItems(memberId, skuIds);

            //移除购物车中被选中的商品
            Boolean result = cartService.removeCheckedItem();

            if (result) {
                log.info("【清理购物车】购物车清理成功");
            } else {
                log.warn("【清理购物车】购物车清理失败: {}");
            }

        } catch (Exception e) {
            log.error("【清理购物车】清理异常: ", e);
            // 这里不抛出异常，因为购物车清理失败不应该影响订单创建
        }
    }

//    /**
//     * 发送订单创建事件
//     */
//    private void sendOrderCreatedEvent(OmsOrder order) {
//        try {
//            OrderCreatedEvent event = new OrderCreatedEvent();
//            event.setOrderId(order.getId());
//            event.setOrderSn(order.getOrderSn());
//            event.setMemberId(order.getMemberId());
//            event.setTotalAmount(order.getTotalAmount());
//            event.setCreateTime(LocalDateTime.now());
//
//            applicationEventPublisher.publishEvent(event);
//            log.info("【事件发布】订单创建事件已发布，订单号: {}", order.getOrderSn());
//
//        } catch (Exception e) {
//            log.error("【事件发布】发布订单创建事件失败: ", e);
//        }
//    }


    /**
     *         TODO             订单支付
     *                      支持多种支付方式：微信支付、余额支付
     *                      支付流程：
     *                      - 余额支付：立即扣减余额、库存，更新订单状态
     *                      - 微信支付：生成支付参数，实际处理在支付回调中
     *
     * @param paymentForm 支付表单数据
     * @return 支付结果（微信支付返回调起参数，余额支付返回布尔值）
     */
    @Override
    @GlobalTransactional
//    public <T> T payOrder(OrderPaymentForm paymentForm) {

    public Object payOrder(OrderPaymentForm paymentForm) {

        String orderSn = paymentForm.getOrderSn();
        PaymentMethodEnum paymentMethod  = paymentForm.getPaymentMethod();
        Long paymentAmount = paymentForm.getPaymentAmount();

        log.info("【支付】开始处理，订单号: {}, 支付方式: {}, 支付金额: {},模拟模式: {}",
                orderSn, paymentMethod, paymentAmount, mockPayEnabled);

        // 1. 验证支付金额
        if (paymentAmount == null || paymentAmount <= 0) {
            log.error("【支付】支付金额无效: {}", paymentAmount);
            throw new BizException("支付金额必须大于0");
        }

        // 2. 验证订单金额
        log.info("根据订单号查询订单");
        OmsOrder order = this.getOne(new LambdaQueryWrapper<OmsOrder>().eq(OmsOrder::getOrderSn, orderSn));
        Assert.isTrue(order != null, "订单不存在");


        //
        Long orderPaymentAmount = order.getPaymentAmount();
        if (orderPaymentAmount == null || orderPaymentAmount <= 0) {
            log.error("【支付】订单金额异常: {}", orderPaymentAmount);
            throw new BizException("订单金额异常");
        }

        // 5. 比较金额  你在验证金额时，应该用原始的分进行比较，而不是转换后的元：
        //保持单位为分进行比较（推荐）
        if (!orderPaymentAmount.equals(paymentAmount)) {
            // 只用于显示，不用于比较
            double orderAmountYuan = orderPaymentAmount / 100.00;  // 121.5
            double requestAmountYuan = paymentAmount / 100.00;           // 121.5

            log.error("【支付】金额不匹配，订单金额: {}，请求金额: {}",
                    orderAmountYuan, requestAmountYuan);
            throw new BizException(String.format("支付金额不匹配，订单金额: ¥%.2f",
                    orderPaymentAmount / 100.0));
        }

        log.info("校验订单状态是否可支付");
        Assert.isTrue(OrderStatusEnum.UNPAID.getValue().equals(order.getStatus()), "订单不可支付，请检查订单状态");

        // 2. 检查支付方式
        if (!isValidPaymentMethod(paymentMethod)) {
            throw new BizException("不支持的支付方式: " + paymentMethod);
        }


        log.info("使用分布式锁防止重复支付（同一订单同时支付）");
        RLock lock = redissonClient.getLock(OrderConstants.ORDER_LOCK_PREFIX + order.getOrderSn());

        log.info("获取锁");
        lock.lock();

        // 继续支付流程...

        return processRealPayment(paymentForm, paymentMethod, order, lock);


        // 3. 判断使用模拟支付还是真实支付
//        if (Boolean.TRUE.equals(mockPayEnabled) && mockPayService.isMockEnabled()) {
//            log.info("【支付】使用模拟支付");
//            return processMockPayment(orderSn, paymentMethod, paymentAmount);
//        } else {
//            log.info("【支付】使用真实支付");
//            return processRealPayment(paymentForm, paymentMethod, order, lock);
//        }


    }


    /**
     * 处理真实支付
     */
    private Result<PaymentParamsVO> processRealPayment(OrderPaymentForm paymentForm,
                                      PaymentMethodEnum paymentMethod,
                                      OmsOrder order,
                                      RLock lock) {
        // 原有的真实支付逻辑
        // 这里可以留空或抛出异常，提示需要配置真实支付

        try {

            log.info("根据支付方式路由到不同的支付处理逻辑");

            String appId=paymentForm.getAppId();
            String orderSn =   order.getOrderSn();

            Long memberId = SecurityUtils.getMemberId();

            // 7. 获取用户的微信OpenID
            log.info("【会员微服务】获取用户OpenID，会员ID: {}", memberId);
            Result<String> openIdResult = memberFeignClient.getOpenIdByMemberId(memberId);

            String openId = openIdResult.getData();
            log.info("【会员微服务】用户OpenID获取成功: {}", openId);


            // 1. 构建支付请求
            PaymentRequestDTO paymentRequest = buildPaymentRequest(order, paymentMethod, memberId, openId);
            log.info("【支付微服务】Pay微服务后端createPayment需求参数PaymentRequestDTO: {}", JSONUtil.toJsonStr(paymentRequest));

            // 2. 调用支付微服务

            Result<PaymentParamsVO>  paymentParamsVO = payFeignClient.createPayment(paymentRequest);
            log.info("【支付微服务】调用支付微服务payFeignClient，获取前端调用第三方支付所需的支付参数PaymentParamsVO:{}",paymentParamsVO.getData());


            if (paymentParamsVO == null) {
                throw new BizException("支付服务返回空结果");
            }


            return paymentParamsVO;
        } finally {
            //释放锁

            log.info("释放锁");
            if (lock.isLocked()) {
                lock.unlock();
            }
        }

//        throw new BizException("真实支付功能未配置，请启用模拟支付或配置真实支付参数");
    }


    /**
     * 构建支付请求
     */
    private PaymentRequestDTO buildPaymentRequest(OmsOrder order, PaymentMethodEnum paymentMethod,
                                                  Long memberId, String openId) {

        Long PaymentAmount = order.getPaymentAmount();

        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setUserId(memberId);
        request.setBizType("ORDER");
        request.setOrderNo(order.getOrderSn());
        request.setAmount(toBigDecimal(PaymentAmount));

        request.setSubject("商品购买");
        request.setBody("订单号：" + order.getOrderSn());

        log.info("【构建支付请求】支付方式:{}", paymentMethod);

        // 根据支付方式设置参数
        switch (paymentMethod) {
            case MOCK:
                request.setChannel("MOCK");
                request.setPayType("JSAPI");
                request.setOpenId(openId);
                break;
            case WX_JSAPI:
                request.setChannel("WECHAT");
                request.setPayType("JSAPI");
                request.setOpenId(openId);
                break;
            case ALIPAY:
                request.setChannel("ALIPAY");
                request.setPayType("APP");
                break;
            case BALANCE:
                request.setChannel("BALANCE");
                request.setPayType("BALANCE");
                break;
            case WX_APP:
                request.setChannel("WECHAT");
                request.setPayType("APP");
                break;
            default:
                throw new BizException("不支持的支付方式: " + paymentMethod);
        }

        // 额外参数
        Map<String, Object> extraParams = new HashMap<>();
        extraParams.put("appId", getAppIdByMethod(paymentMethod));
        extraParams.put("memberId", memberId);
        extraParams.put("orderId", order.getId());
        request.setExtraParams(extraParams);

        return request;
    }

    /**
     * 根据支付方式获取AppId
     */
    private String getAppIdByMethod(PaymentMethodEnum paymentMethod) {
        // 这里可以从配置文件中读取
        switch (paymentMethod) {
            case WX_JSAPI:
            case WX_APP:
                return "wx1234567890abcdef"; // 替换为实际的微信AppId
            case ALIPAY:
                return "2021000118691234";   // 替换为实际的支付宝AppId
            default:
                return "";
        }
    }

    /**
     * 检查支付方式是否有效
     */
    private boolean isValidPaymentMethod(PaymentMethodEnum paymentMethod) {

        return paymentMethod != null && paymentMethod != PaymentMethodEnum.UNKNOWN;
    }

    /**
     * 支付后更新订单
     */
    private void updateOrderAfterPayment(String orderSn, PaymentMethodEnum paymentMethod, boolean success) {
        if (success) {
            // 支付成功，更新订单状态
            this.update(new LambdaUpdateWrapper<OmsOrder>()
                    .set(OmsOrder::getStatus, 2)  // 2=待发货
                    .set(OmsOrder::getPaymentMethod, paymentMethod.getValue())
                    .set(OmsOrder::getPaymentTime, new Date())
                    .eq(OmsOrder::getOrderSn, orderSn));

            log.info("【支付】订单支付成功，订单号: {}", orderSn);
        } else {
            // 支付失败，记录日志
            log.warn("【支付】订单支付失败，订单号: {}", orderSn);
        }
    }


    /**
     *          TODO            余额支付处理
     *                      业务流程：扣减余额 → 扣减库存 → 更新订单状态 → 清理购物车
     *
     * @param order 订单实体
     * @return 支付是否成功
     */
    private Boolean balancePay(OmsOrder order) {
        // 扣减余额

        log.info("1. 扣减用户余额");
        Long memberId = order.getMemberId();
        Long payAmount = order.getPaymentAmount();
        Result<?> deductBalanceResult = memberFeignClient.deductBalance(memberId, payAmount);
        Assert.isTrue(Result.isSuccess(deductBalanceResult), "扣减账户余额失败");

        // 扣减库存
        log.info("2. 扣减商品库存");
        skuFeignClient.deductStock(order.getOrderSn());

        // 更新订单状态
        log.info("3. 更新订单状态为已支付");
        order.setStatus(OrderStatusEnum.PAID.getValue());
        order.setPaymentMethod(PaymentMethodEnum.BALANCE.getValue());
        order.setPaymentTime(LocalDateTime.now());
        this.updateById(order);


        // 支付成功删除购物车已勾选的商品
        log.info("4. 支付成功删除购物车中已勾选的商品");
        cartService.removeCheckedItem();
        return Boolean.TRUE;
    }


    /**
     *       TODO     支付成功后续操作
     *
     */
    private Result aftrtPay(String appId, String orderSn, Long paymentAmount) {
        return null;

    }

    /**
     *            TODO              关闭未支付订单
     *                          通常由定时任务或用户手动触发
     *
     * @param orderSn 订单编号
     * @return 是否关闭成功
     */
    @Override
    public boolean closeOrder(String orderSn) {


        log.info(" 只关闭待支付状态的订单");
        return this.update(new LambdaUpdateWrapper<OmsOrder>()
                .eq(OmsOrder::getOrderSn, orderSn)
                .eq(OmsOrder::getStatus, OrderStatusEnum.UNPAID.getValue())
                .set(OmsOrder::getStatus, OrderStatusEnum.CANCELED.getValue())   // 更新为已取消状态
        );
    }

    /**
     *      TODO                    删除订单
     *                          只有已取消或待支付的订单可以删除
     *
     * @param orderId 订单ID
     * @return 是否删除成功
     */
    @Override
    public boolean deleteOrder(Long orderId) {

        log.info(" 查询订单是否存在");
        OmsOrder order = this.getById(orderId);
        Assert.isTrue(order != null, "删除失败,订单不存在！");

        log.info(" 校验订单状态：只有已取消或待支付的订单可以删除");
        Assert.isTrue(
                OrderStatusEnum.CANCELED.getValue().equals(order.getStatus())
                        || OrderStatusEnum.UNPAID.getValue().equals(order.getStatus())
                ,
                "当前状态订单不能删除"
        );

        log.info(" 物理删除订单");
        return this.removeById(orderId);
    }








    /**
     *           TODO               获取订单的商品明细信息
     *                          根据不同的下单方式获取商品信息：
     *                          1. 直接购买：传入skuId，数量为1
     *                          2. 购物车结算：不传skuId，获取购物车中选中的商品
     *
     * @param skuId 直接购买的商品ID，购物车结算时为null
     * @param memberId 用户ID
     * @return 订单商品明细列表
     */
    private List<OrderItemDTO> getOrderItems(Long skuId, Long memberId) {
        List<OrderItemDTO> orderItems;
        if (skuId != null) {  // 直接购买


            orderItems = new ArrayList<>();
            log.info("直接购买流程");


            SkuInfoDTO skuInfoDTO = skuFeignClient.getSkuInfo(skuId);
            log.info("查询商品详细信息:{}", skuInfoDTO);

            OrderItemDTO orderItemDTO = new OrderItemDTO();
            orderItemDTO.setSkuId(skuId);


            log.info("拷贝商品属性到订单项");
            BeanUtil.copyProperties(skuInfoDTO, orderItemDTO);
            orderItemDTO.setSkuId(skuInfoDTO.getId());

            log.info("直接购买商品的数量固定为1");
            orderItemDTO.setQuantity(1); // 直接购买商品的数量为1
            orderItems.add(orderItemDTO);
        } else { // 购物车结算

            log.info("购物车结算流程");

            List<CartItemDto> cartItems = cartService.listCartItems(memberId);
            log.info("获取用户购物车中的所有商品cartItems:{}",cartItems);

            orderItems = cartItems.stream()
                    .filter(CartItemDto::getChecked)    // 只处理选中的商品
                    .map(cartItem -> {
                        OrderItemDTO orderItemDTO = new OrderItemDTO();

                        //传递购买数量
                        orderItemDTO.setQuantity(cartItem.getCount());

                        BeanUtil.copyProperties(cartItem, orderItemDTO);
                        return orderItemDTO;
                    }).collect(Collectors.toList());
            log.info("过滤出选中的商品，并转换为订单项orderItems:{}",orderItems);
        }

        return orderItems;
    }

    /**
     *     TODO         生成商户订单号（防重复）
     *              订单号生成规则：时间戳(13位) + 3位随机数 + 用户ID后5位
     *              总长度：13 + 3 + 5 = 21位
     *              设计思路参考美团点评订单号生成方案，保证唯一性和可读性
     *
     * @param memberId 会员ID
     * @return 唯一的订单编号
     */
    private String generateTradeNo(Long memberId) {
        // 用户id前补零保证五位，对超出五位的保留后五位

        log.info("用户id前补零保证五位，对超出五位的保留后五位");
        String userIdFilledZero = String.format("%05d", memberId);
        String fiveDigitsUserId = userIdFilledZero.substring(userIdFilledZero.length() - 5);
        // 在前面加上wxo（wx order）等前缀是为了人工可以快速分辨订单号是下单还是退款、来自哪家支付机构等
        // 将时间戳+3位随机数+五位id组成商户订单号，规则参考自<a href="https://tech.meituan.com/2016/11/18/dianping-order-db-sharding.html">大众点评</a>

        // 订单号组成：时间戳 + 3位随机数 + 用户ID后5位
        // 优点：
        // 1. 时间戳保证趋势递增，利于数据库分页查询
        // 2. 随机数防止同一毫秒内的冲突
        // 3. 用户ID后5位便于人工识别订单归属
        log.info("订单号组成：时间戳 + 3位随机数 + 用户ID后5位");
        log.info("1. 时间戳保证趋势递增，利于数据库分页查询");
        log.info("2. 随机数防止同一毫秒内的冲突");
        log.info("3. 用户ID后5位便于人工识别订单归属");
        return System.currentTimeMillis() + RandomUtil.randomNumbers(3) + fiveDigitsUserId;
    }


    //-------------------------------------------------------------

    /**
     * 获取订单统计信息
     */
    @Override
    public Map<String, Object>  getOrderStatistics(OrderPageQuery queryParams) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 查询各状态订单数量
            Map<Integer, Map<String, Object>> statusCountsMap =
                    this.baseMapper.getOrderStatusCounts(queryParams);

            // 2. 初始化统计结果
            Map<String, Integer> statusCounts = new HashMap<>();
            statusCounts.put("-1", 0);  // 全部

            // 3. 转换结果格式
            int total = 0;
            for (Map.Entry<Integer, Map<String, Object>> entry : statusCountsMap.entrySet()) {
                Integer status = entry.getKey();
                Map<String, Object> countMap = entry.getValue();
                Integer count = ((Number) countMap.get("count")).intValue();

                // 状态值转换为字符串
                statusCounts.put(status.toString(), count);
                total += count;
            }

            // 4. 设置总数
            statusCounts.put("-1", total);

            // 5. 查询今日数据
            Integer todayOrderCount = getTodayOrderCount(queryParams);
            Integer pendingCount = getPendingOrderCount(queryParams);
            Long todayIncome = getTodayIncome(queryParams);

            // 6. 返回结果
            result.put("statusCounts", statusCounts);
            result.put("todayOrderCount", todayOrderCount);
            result.put("pendingCount", pendingCount);
            result.put("todayIncome", todayIncome);
            result.put("total", total);

        } catch (Exception e) {
            log.error("查询订单统计失败", e);
            throw new BusinessException("统计查询失败");
        }

        return result;
    }

    /**
     * 查询今日订单数量
     */
    private Integer getTodayOrderCount(OrderPageQuery queryParams) {
        // 创建今天的查询条件
        OrderPageQuery todayQuery = new OrderPageQuery();
        BeanUtils.copyProperties(queryParams, todayQuery);

        // 设置今天的时间范围
        LocalDate today = LocalDate.now();
        todayQuery.setStartTime(today.atStartOfDay()); // 今天 00:00:00
        todayQuery.setEndTime(today.atTime(23, 59, 59));  // 今天 23:59:59

        return this.baseMapper.selectCountByQuery(todayQuery);
    }

    /**
     * 查询待处理订单数量
     */
    private Integer getPendingOrderCount(OrderPageQuery queryParams) {
        // 待处理订单：待付款 + 待发货
        OrderPageQuery pendingQuery = new OrderPageQuery();
        BeanUtils.copyProperties(queryParams, pendingQuery);

        // 重置状态，查询特定状态
        pendingQuery.setStatus(null);

        return this.baseMapper.selectCountByCondition(
                pendingQuery,
                Arrays.asList(0, 1)  // 待付款(0)和待发货(1)
        );
    }

    /**
     * 查询今日收入
     */
    private Long getTodayIncome(OrderPageQuery queryParams) {
        OrderPageQuery todayQuery = new OrderPageQuery();
        BeanUtils.copyProperties(queryParams, todayQuery);

        LocalDate today = LocalDate.now();
        todayQuery.setStartTime(today.atStartOfDay()); // 今天 00:00:00
        todayQuery.setEndTime(today.atTime(23, 59, 59));  // 今天 23:59:59

        return this.baseMapper.selectTodayIncome(todayQuery);
    }

    //-------------------------------------------------------------

    // 创建专门的线程池用于并行查询
    private static final ExecutorService queryExecutor = Executors.newFixedThreadPool(
            5,
            new ThreadFactoryBuilder()
                    .setNameFormat("order-query-thread-%d")
                    .build()
    );

    /**
     * 获取订单分页和统计信息（业务编排）
     */
    @Override
    public OrderPageWithStatsVO getOrderPageWithStatistics(OrderPageQuery queryParams) {
        log.info("开始查询订单分页和统计信息，参数：{}", queryParams);
        OrderPageWithStatsVO result = new OrderPageWithStatsVO();

        // 并行查询分页数据和统计信息
        CompletableFuture<IPage<OrderPageVO>> pageFuture =
                CompletableFuture.supplyAsync(() ->
                        {
                            log.info("开始查询订单分页数据");
                            return this.getOrderPage(queryParams);
                        }, queryExecutor)
                        .exceptionally(e -> {
                        log.error("查询订单分页数据失败", e);
                        return null;
                });

        CompletableFuture<Map<String, Object>> statsFuture =
                CompletableFuture.supplyAsync(() ->
                        {
                            log.info("开始查询订单统计信息");
                            return this.getOrderStatistics(queryParams);
                        }, queryExecutor)
                        .exceptionally(e -> {
                        log.error("查询订单统计信息失败", e);
                        return new HashMap<>();
                });

        // 等待结果
        CompletableFuture.allOf(pageFuture, statsFuture)
                .completeOnTimeout(null, 10, TimeUnit.SECONDS)
                .join();

        try {
            IPage<OrderPageVO> pageResult = pageFuture.get(5, TimeUnit.SECONDS);
            Map<String, Object> statistics = statsFuture.get(5, TimeUnit.SECONDS);

            if (pageResult == null) {
                log.error("订单分页查询结果为null");
                throw new BusinessException("订单查询失败");
            }

            // 设置分页信息
            result.setList(pageResult.getRecords());
            result.setPageNum(pageResult.getCurrent());
            result.setPageSize(pageResult.getSize());
            result.setTotal(pageResult.getTotal());
            result.setPages(pageResult.getPages());
            result.setHasNextPage(pageResult.getCurrent() < pageResult.getPages());

            // 设置统计信息
            if (statistics != null) {
                result.setStatusCounts((Map<String, Integer>) statistics.get("statusCounts"));
                result.setTodayOrderCount(((Number) statistics.getOrDefault("todayOrderCount", 0)).intValue());
                result.setPendingCount(((Number) statistics.getOrDefault("pendingCount", 0)).intValue());
                result.setTodayIncome(((Number) statistics.getOrDefault("todayIncome", 0L)).longValue());
            }

            log.info("订单分页和统计信息查询成功，共{}条记录", result.getTotal());

        } catch (Exception e) {
            log.error("查询订单分页和统计信息失败", e);
            throw new BusinessException("查询失败");
        }

        return result;
    }

}
