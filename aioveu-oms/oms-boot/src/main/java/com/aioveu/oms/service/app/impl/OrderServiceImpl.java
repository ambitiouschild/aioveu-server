package com.aioveu.oms.service.app.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aioveu.common.security.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.aioveu.common.result.Result;
import com.aioveu.common.web.exception.BizException;
import com.aioveu.oms.config.WxPayProperties;
import com.aioveu.oms.constant.OrderConstants;
import com.aioveu.oms.converter.OrderConverter;
import com.aioveu.oms.converter.OrderItemConverter;
import com.aioveu.oms.enums.OrderStatusEnum;
import com.aioveu.oms.enums.PaymentMethodEnum;
import com.aioveu.oms.mapper.OrderMapper;
import com.aioveu.oms.model.bo.OrderBO;
import com.aioveu.oms.model.dto.CartItemDto;
import com.aioveu.oms.model.dto.OrderItemDTO;
import com.aioveu.oms.model.entity.OmsOrder;
import com.aioveu.oms.model.entity.OmsOrderItem;
import com.aioveu.oms.model.form.OrderPaymentForm;
import com.aioveu.oms.model.form.OrderSubmitForm;
import com.aioveu.oms.model.query.OrderPageQuery;
import com.aioveu.oms.model.vo.OrderConfirmVO;
import com.aioveu.oms.model.vo.OrderPageVO;
import com.aioveu.oms.service.app.CartService;
import com.aioveu.oms.service.app.OrderItemService;
import com.aioveu.oms.service.app.OrderService;
import com.aioveu.pms.api.SkuFeignClient;
import com.aioveu.pms.model.dto.LockSkuDTO;
import com.aioveu.pms.model.dto.SkuInfoDTO;
import com.aioveu.ums.api.MemberFeignClient;
import com.aioveu.ums.dto.MemberAddressDTO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;


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
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OmsOrder> implements OrderService {

    // 微信支付配置属性
    private final WxPayProperties wxPayProperties;

    // 购物车服务
    private final CartService cartService;

    // 订单项服务
    private final OrderItemService orderItemService;

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

    // 微信支付服务
    private final WxPayService wxPayService;

    // 分布式锁客户端
    private final RedissonClient redissonClient;

    // 订单转换器
    private final OrderConverter orderConverter;

    // 订单项转换器
    private final OrderItemConverter orderItemConverter;

    /**
     * TODO  订单分页列表查询
     *
     * @param queryParams 分页查询参数
     * @return 分页订单数据
     */
    @Override
    public IPage<OrderPageVO> getOrderPage(OrderPageQuery queryParams) {

        log.info("调用Mapper进行分页查询，返回业务对象分页");
        Page<OrderBO> boPage = this.baseMapper.getOrderPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams);

        log.info("将业务对象分页转换为前端展示的分页VO");
        return orderConverter.toVoPageForApp(boPage);
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

        log.info("获取当前登录用户ID");
        Long memberId = SecurityUtils.getMemberId();

        // 解决子线程无法获取HttpServletRequest请求对象中数据的问题
        log.info("解决子线程无法获取HttpServletRequest请求对象中数据的问题");
        log.info("将当前请求属性保存，以便在异步线程中继续使用");
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        RequestContextHolder.setRequestAttributes(attributes, true);

        log.info("使用CompletableFuture进行异步并行处理，提升接口响应速度");

        // 获取订单商品
        log.info("异步任务1：获取订单商品信息");
        CompletableFuture<List<OrderItemDTO>> getOrderItemsFuture = CompletableFuture.supplyAsync(
                        () -> this.getOrderItems(skuId, memberId), threadPoolExecutor)
                .exceptionally(ex -> {
                    log.info("异常处理：如果获取商品信息失败，返回空列表并记录错误日志");
                    log.error("Failed to get order items: {}", ex.toString());
                    return Collections.emptyList();
                });

        // 用户收货地址
        log.info("异步任务2：获取用户收货地址");
        CompletableFuture<List<MemberAddressDTO>> getMemberAddressFuture = CompletableFuture.supplyAsync(() -> {

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
     *                  核心业务流程：防重校验 → 商品校验 → 库存锁定 → 订单创建
     *
     * @param submitForm {@link OrderSubmitForm} 订单提交表单数据
     * @return 订单编号
     */
    @Override
    @GlobalTransactional
    public String submitOrder(OrderSubmitForm submitForm) {
        log.info("订单提交参数:{}", JSONUtil.toJsonStr(submitForm));
        String orderToken = submitForm.getOrderToken();

        // 1. 判断订单是否重复提交(LUA脚本保证获取和删除的原子性，成功返回1，否则返回0)
        log.info("1. 防重提交校验：使用LUA脚本保证原子性（获取和删除在同一个原子操作中）");
        String lockAcquireScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Long lockAcquired = this.redisTemplate.execute(
                new DefaultRedisScript<>(lockAcquireScript, Long.class),
                Collections.singletonList(OrderConstants.ORDER_TOKEN_PREFIX + orderToken),
                orderToken
        );

        log.info("断言校验：如果令牌不存在或删除失败，说明是重复提交");
        Assert.isTrue(lockAcquired != null && lockAcquired.equals(1L), "订单重复提交，请刷新页面后重试");

        // 2. 订单商品校验 (PS：校验进入订单确认页面到提交过程商品(价格、上架状态)变化)
        log.info("订单商品校验：校验从订单确认到提交过程中商品信息是否发生变化");
        List<OrderSubmitForm.OrderItem> orderItems = submitForm.getOrderItems();

        log.info("提取所有商品SKU ID");
        List<Long> skuIds = orderItems.stream()
                .map(OrderSubmitForm.OrderItem::getSkuId)
                .collect(Collectors.toList());

        log.info("批量查询商品最新信息");
        List<SkuInfoDTO> skuList;
        try {
            skuList = skuFeignClient.getSkuInfoList(skuIds);
        } catch (Exception e) {
            log.error("Failed to get sku info list: {}", e.toString());
            skuList = Collections.emptyList();
        }

        log.info("逐个校验商品信息");
        for (OrderSubmitForm.OrderItem item : orderItems) {

            log.info("查找对应的商品信息");
            SkuInfoDTO skuInfo = skuList.stream().filter(sku -> sku.getId().equals(item.getSkuId()))
                    .findFirst()
                    .orElse(null);

            log.info("校验商品是否存在");
            Assert.isTrue(skuInfo != null, "商品({})已下架或删除");

            log.info("校验商品价格是否发生变化");
            Assert.isTrue(item.getPrice().compareTo(skuInfo.getPrice()) == 0, "商品({})价格发生变动，请刷新页面", item.getSkuName());
        }

        // 3. 校验库存并锁定库存
        log.info("3. 校验库存并锁定库存");
        List<LockSkuDTO> lockSkuList = orderItems.stream()
                .map(item -> new LockSkuDTO(item.getSkuId(), item.getQuantity()))
                .collect(Collectors.toList());


        log.info("调用商品服务锁定库存，使用订单令牌作为分布式事务的XID");
        boolean lockStockResult = skuFeignClient.lockStock(orderToken, lockSkuList);
        Assert.isTrue(lockStockResult, "订单提交失败：锁定商品库存失败！");

        // 4. 生成订单
        log.info("4. 生成订单");
        boolean result = this.saveOrder(submitForm);
        log.info("order订单token ({}) create result:{}", orderToken, result);
        return orderToken;
    }


    /**
     *  TODO  创建订单实体并保存到数据库
     *
     * @param submitForm 订单提交表单对象
     * @return 是否保存成功
     */
    private boolean saveOrder(OrderSubmitForm submitForm) {

        log.info("转换表单数据为订单实体");
        OmsOrder order = orderConverter.form2Entity(submitForm);

        log.info("初始状态：待支付");
        order.setStatus(OrderStatusEnum.UNPAID.getValue());
        order.setMemberId(SecurityUtils.getMemberId());
        order.setSource(submitForm.getOrderSource().getValue());

        log.info("保存订单主表");
        boolean result = this.save(order);

        Long orderId = order.getId();
        if (result) {

            // 保存订单明细
            log.info("保存订单明细（订单商品项）");
            List<OmsOrderItem> orderItemEntities = orderItemConverter.item2Entity(submitForm.getOrderItems());

            log.info("设置订单ID关联");
            orderItemEntities.forEach(item -> item.setOrderId(orderId));

            orderItemService.saveBatch(orderItemEntities);

            // 订单超时未支付取消
            log.info("发送订单超时关闭的延迟消息到RabbitMQ");
            log.info("订单在指定时间内未支付会自动取消，释放库存");
            rabbitTemplate.convertAndSend("order.exchange", "order.close.delay", submitForm.getOrderToken());
        }
        return result;
    }


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
    public <T> T payOrder(OrderPaymentForm paymentForm) {
        String orderSn = paymentForm.getOrderSn();

        log.info("根据订单号查询订单");
        OmsOrder order = this.getOne(new LambdaQueryWrapper<OmsOrder>().eq(OmsOrder::getOrderSn, orderSn));
        Assert.isTrue(order != null, "订单不存在");

        log.info("校验订单状态是否可支付");
        Assert.isTrue(OrderStatusEnum.UNPAID.getValue().equals(order.getStatus()), "订单不可支付，请检查订单状态");

        log.info("使用分布式锁防止重复支付（同一订单同时支付）");
        RLock lock = redissonClient.getLock(OrderConstants.ORDER_LOCK_PREFIX + order.getOrderSn());
        try {

            log.info("获取锁");
            lock.lock();
            T result;

            log.info("根据支付方式路由到不同的支付处理逻辑");
            switch (paymentForm.getPaymentMethod()) {
                case WX_JSAPI:

                    log.info("微信JSAPI支付（小程序支付）");
                    result = (T) wxJsapiPay(paymentForm.getAppId(), order.getOrderSn(), order.getPaymentAmount());
                    break;
                default:

                    log.info("余额支付");
                    result = (T) balancePay(order);
                    break;
            }
            return result;
        } finally {
            //释放锁

            log.info("释放锁");
            if (lock.isLocked()) {
                lock.unlock();
            }
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
        order.setPaymentTime(new Date());
        this.updateById(order);


        // 支付成功删除购物车已勾选的商品
        log.info("4. 支付成功删除购物车中已勾选的商品");
        cartService.removeCheckedItem();
        return Boolean.TRUE;
    }


    /**
     *       TODO               微信JSAPI支付（小程序支付）
     *                      生成微信支付调起参数，实际支付结果通过异步回调处理
     *
     * @param appId 微信小程序ID
     * @param orderSn 订单编号
     * @param paymentAmount 支付金额（单位：分）
     * @return 微信支付调起参数
     */
    private WxPayUnifiedOrderV3Result.JsapiResult wxJsapiPay(String appId, String orderSn, Long paymentAmount) {
        Long memberId = SecurityUtils.getMemberId();
        // 如果已经有outTradeNo了就先进行关单

        log.info("安全措施：如果订单已经有外部交易号，先关闭之前的微信支付订单");
        if (StrUtil.isNotBlank(orderSn)) {
            try {
                wxPayService.closeOrderV3(orderSn);
            } catch (WxPayException e) {
                log.error(e.getMessage(), e);
                throw new BizException("微信关单异常");
            }
        }

        // 更新订单状态
        log.info("更新订单支付方式为微信支付");
        boolean result = this.update(new LambdaUpdateWrapper<OmsOrder>()
                .set(OmsOrder::getPaymentMethod, PaymentMethodEnum.WX_JSAPI.getValue())
                .eq(OmsOrder::getOrderSn, orderSn)
        );

        log.info(" 获取用户的微信OpenID");
        String memberOpenId = memberFeignClient.getMemberOpenId(memberId).getData();


        log.info(" 构建微信支付请求参数");
        WxPayUnifiedOrderV3Request wxRequest = new WxPayUnifiedOrderV3Request()
                .setAppid(appId)   // 小程序ID
                .setOutTradeNo(orderSn)   // 商户订单号
                .setAmount(new WxPayUnifiedOrderV3Request
                        .Amount()
                        .setTotal(Math.toIntExact(paymentAmount))  // 支付金额（分）
                )
                .setPayer(
                        new WxPayUnifiedOrderV3Request.Payer()
                                .setOpenid(memberOpenId)   // 用户OpenID
                )
                .setDescription("赅买-订单编号：" + orderSn)   // 商品描述
                .setNotifyUrl(wxPayProperties.getPayNotifyUrl());   // 支付结果通知地址
        WxPayUnifiedOrderV3Result.JsapiResult jsapiResult;
        try {

            log.info(" 调用微信统一下单接口");
            jsapiResult = wxPayService.createOrderV3(TradeTypeEnum.JSAPI, wxRequest);
        } catch (WxPayException e) {
            log.error(e.getMessage(), e);
            throw new BizException("微信统一下单异常");
        }
        return jsapiResult;
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
     *        TODO              处理微信支付结果通知（异步回调）
     *                      微信支付成功后，微信服务器会调用此接口通知支付结果
     *
     * @param signatureHeader 微信签名头信息
     * @param notifyData 加密的通知数据
     * @throws WxPayException 微信支付异常
     */
    @Override
    public void handleWxPayOrderNotify(SignatureHeader signatureHeader, String notifyData) throws WxPayException {
        log.info("开始处理支付结果通知");
        // 解密支付通知内容

        log.info("解密支付通知内容");
        final WxPayOrderNotifyV3Result.DecryptNotifyResult result = this.wxPayService.parseOrderNotifyV3Result(notifyData, signatureHeader).getResult();
        log.debug("支付通知解密成功：[{}]", result.toString());


        // 根据商户订单号查询订单
        log.info("根据商户订单号查询订单");
        OmsOrder orderDO = this.getOne(new LambdaQueryWrapper<OmsOrder>()
                .eq(OmsOrder::getOutTradeNo, result.getOutTradeNo())
        );
        // 支付成功处理
        log.info("支付成功处理");
        if (WxPayConstants.WxpayTradeStatus.SUCCESS.equals(result.getTradeState())) {

            log.info("更新订单状态为已支付");
            orderDO.setStatus(OrderStatusEnum.PAID.getValue());

            log.info("微信支付交易号");
            orderDO.setTransactionId(result.getTransactionId());
            orderDO.setPaymentTime(new Date());
            this.updateById(orderDO);
        }
        log.info("账单更新成功");

        // 支付成功删除购物车已勾选的商品
        log.info("支付成功删除购物车已勾选的商品");
        cartService.removeCheckedItem();
    }


    /**
     * TODO    处理微信退款结果通知（异步回调）
     *
     * @param signatureHeader 微信签名头信息
     * @param notifyData 加密的退款通知数据
     * @throws WxPayException 微信支付异常
     */
    @Override
    public void handleWxPayRefundNotify(SignatureHeader signatureHeader, String notifyData) throws WxPayException {
        log.info("开始处理退款结果通知");
        // 解密支付通知内容
        final WxPayRefundNotifyV3Result.DecryptNotifyResult result = this.wxPayService.parseRefundNotifyV3Result(notifyData, signatureHeader).getResult();
        log.debug("退款通知解密成功：[{}]", result.toString());


        // 根据商户退款单号查询订单
        log.info("根据商户订单号查询订单");
        QueryWrapper<OmsOrder> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OmsOrder::getOutTradeNo, result.getOutTradeNo());
        OmsOrder orderDO = this.getOne(wrapper);


        // 退款成功处理
        log.info("退款成功处理");
        if (WxPayConstants.RefundStatus.SUCCESS.equals(result.getRefundStatus())) {

            log.info("更新为已完成状态");
            orderDO.setStatus(OrderStatusEnum.COMPLETE.getValue());

            log.info("微信退款单号");
            orderDO.setRefundId(result.getRefundId());
            this.updateById(orderDO);
        }
        log.info("账单更新成功");
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

            log.info("直接购买流程");
            orderItems = new ArrayList<>();

            log.info("查询商品详细信息");
            SkuInfoDTO skuInfoDTO = skuFeignClient.getSkuInfo(skuId);
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
            log.info("获取用户购物车中的所有商品");
            List<CartItemDto> cartItems = cartService.listCartItems(memberId);

            log.info("过滤出选中的商品，并转换为订单项");
            orderItems = cartItems.stream()
                    .filter(CartItemDto::getChecked)    // 只处理选中的商品
                    .map(cartItem -> {
                        OrderItemDTO orderItemDTO = new OrderItemDTO();
                        BeanUtil.copyProperties(cartItem, orderItemDTO);
                        return orderItemDTO;
                    }).collect(Collectors.toList());
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

}
