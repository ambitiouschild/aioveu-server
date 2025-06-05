package com.aioveu.oms.service.app.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
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
import com.aioveu.common.security.util.SecurityUtils;
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
import com.aioveu.api.SkuFeignClient;
import com.aioveu.model.dto.LockSkuDTO;
import com.aioveu.model.dto.SkuInfoDTO;
import com.aioveu.api.MemberFeignClient;
import com.aioveu.dto.MemberAddressDTO;
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
 * @Author: 雒世松
 * @Date: 2025/6/5 18:13
 * @param
 * @return:
 **/

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OmsOrder> implements OrderService {

    private final WxPayProperties wxPayProperties;
    private final CartService cartService;
    private final OrderItemService orderItemService;
    private final RabbitTemplate rabbitTemplate;
    private final StringRedisTemplate redisTemplate;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final MemberFeignClient memberFeignClient;
    private final SkuFeignClient skuFeignClient;
    private final WxPayService wxPayService;
    private final RedissonClient redissonClient;
    private final OrderConverter orderConverter;
    private final OrderItemConverter orderItemConverter;

    /**
     * 订单分页列表
     */
    @Override
    public IPage<OrderPageVO> getOrderPage(OrderPageQuery queryParams) {
        Page<OrderBO> boPage = this.baseMapper.getOrderPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams);
        return orderConverter.toVoPageForApp(boPage);
    }

    /**
     * 订单确认 → 进入创建订单页面
     * <p>
     * 获取购买商品明细、用户默认收货地址、防重提交唯一token
     * 进入订单创建页面有两个入口，1：立即购买；2：购物车结算
     *
     * @param skuId 商品ID(直接购买传值)
     * @return {@link OrderConfirmVO}
     */
    @Override
    public OrderConfirmVO confirmOrder(Long skuId) {

        Long memberId = SecurityUtils.getMemberId();

        // 解决子线程无法获取HttpServletRequest请求对象中数据的问题
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        RequestContextHolder.setRequestAttributes(attributes, true);

        // 获取订单商品
        CompletableFuture<List<OrderItemDTO>> getOrderItemsFuture = CompletableFuture.supplyAsync(
                        () -> this.getOrderItems(skuId, memberId), threadPoolExecutor)
                .exceptionally(ex -> {
                    log.error("Failed to get order items: {}", ex.toString());
                    return Collections.emptyList();
                });

        // 用户收货地址
        CompletableFuture<List<MemberAddressDTO>> getMemberAddressFuture = CompletableFuture.supplyAsync(() -> {
            Result<List<MemberAddressDTO>> getMemberAddressResult = memberFeignClient.listMemberAddresses(memberId);
            if (Result.isSuccess(getMemberAddressResult)) {
                return getMemberAddressResult.getData();
            }
            return null;
        }, threadPoolExecutor).exceptionally(ex -> {
            log.error("Failed to get addresses for memberId {} : {}", memberId, ex.toString());
            return Collections.emptyList();
        });

        // 生成唯一令牌,防止重复提交(原理：提交会消耗令牌，令牌被消耗无法再次提交)
        CompletableFuture<String> generateOrderTokenFuture = CompletableFuture.supplyAsync(() -> {
            String orderToken = this.generateTradeNo(memberId);
            redisTemplate.opsForValue().set(OrderConstants.ORDER_TOKEN_PREFIX + orderToken, orderToken);
            return orderToken;
        }, threadPoolExecutor).exceptionally(ex -> {
            log.error("Failed to generate order token .");
            return null;
        });

        CompletableFuture.allOf(getOrderItemsFuture, getMemberAddressFuture, generateOrderTokenFuture).join();
        OrderConfirmVO orderConfirmVO = new OrderConfirmVO();
        orderConfirmVO.setOrderItems(getOrderItemsFuture.join());
        orderConfirmVO.setAddresses(getMemberAddressFuture.join());
        orderConfirmVO.setOrderToken(generateOrderTokenFuture.join());

        log.info("Order confirm response for skuId {}: {}", skuId, orderConfirmVO);
        return orderConfirmVO;
    }

    /**
     * 订单提交
     *
     * @param submitForm {@link OrderSubmitForm}
     * @return 订单编号
     */
    @Override
    @GlobalTransactional
    public String submitOrder(OrderSubmitForm submitForm) {
        log.info("订单提交参数:{}", JSONUtil.toJsonStr(submitForm));
        String orderToken = submitForm.getOrderToken();

        // 1. 判断订单是否重复提交(LUA脚本保证获取和删除的原子性，成功返回1，否则返回0)
        String lockAcquireScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Long lockAcquired = this.redisTemplate.execute(
                new DefaultRedisScript<>(lockAcquireScript, Long.class),
                Collections.singletonList(OrderConstants.ORDER_TOKEN_PREFIX + orderToken),
                orderToken
        );
        Assert.isTrue(lockAcquired != null && lockAcquired.equals(1L), "订单重复提交，请刷新页面后重试");

        // 2. 订单商品校验 (PS：校验进入订单确认页面到提交过程商品(价格、上架状态)变化)
        List<OrderSubmitForm.OrderItem> orderItems = submitForm.getOrderItems();
        List<Long> skuIds = orderItems.stream()
                .map(OrderSubmitForm.OrderItem::getSkuId)
                .collect(Collectors.toList());
        List<SkuInfoDTO> skuList;
        try {
            skuList = skuFeignClient.getSkuInfoList(skuIds);
        } catch (Exception e) {
            log.error("Failed to get sku info list: {}", e.toString());
            skuList = Collections.emptyList();
        }
        for (OrderSubmitForm.OrderItem item : orderItems) {
            SkuInfoDTO skuInfo = skuList.stream().filter(sku -> sku.getId().equals(item.getSkuId()))
                    .findFirst()
                    .orElse(null);
            Assert.isTrue(skuInfo != null, "商品({})已下架或删除");
            Assert.isTrue(item.getPrice().compareTo(skuInfo.getPrice()) == 0, "商品({})价格发生变动，请刷新页面", item.getSkuName());
        }

        // 3. 校验库存并锁定库存
        List<LockSkuDTO> lockSkuList = orderItems.stream()
                .map(item -> new LockSkuDTO(item.getSkuId(), item.getQuantity()))
                .collect(Collectors.toList());

        boolean lockStockResult = skuFeignClient.lockStock(orderToken, lockSkuList);
        Assert.isTrue(lockStockResult, "订单提交失败：锁定商品库存失败！");

        // 4. 生成订单
        boolean result = this.saveOrder(submitForm);
        log.info("order ({}) create result:{}", orderToken, result);
        return orderToken;
    }


    /**
     * 创建订单
     *
     * @param submitForm 订单提交表单对象
     * @return
     */
    private boolean saveOrder(OrderSubmitForm submitForm) {
        OmsOrder order = orderConverter.form2Entity(submitForm);
        order.setStatus(OrderStatusEnum.UNPAID.getValue());
        order.setMemberId(SecurityUtils.getMemberId());
        order.setSource(submitForm.getOrderSource().getValue());
        boolean result = this.save(order);

        Long orderId = order.getId();
        if (result) {

            // 保存订单明细
            List<OmsOrderItem> orderItemEntities = orderItemConverter.item2Entity(submitForm.getOrderItems());
            orderItemEntities.forEach(item -> item.setOrderId(orderId));

            orderItemService.saveBatch(orderItemEntities);

            // 订单超时未支付取消
            rabbitTemplate.convertAndSend("order.exchange", "order.close.delay", submitForm.getOrderToken());
        }
        return result;
    }


    /**
     * 订单支付
     * <p>
     * 余额支付：库存、余额、订单处理
     * 微信支付：生成微信支付调起参数，订单、库存、余额处理在支付回调
     */
    @Override
    @GlobalTransactional
    public <T> T payOrder(OrderPaymentForm paymentForm) {
        String orderSn = paymentForm.getOrderSn();
        OmsOrder order = this.getOne(new LambdaQueryWrapper<OmsOrder>().eq(OmsOrder::getOrderSn, orderSn));
        Assert.isTrue(order != null, "订单不存在");

        Assert.isTrue(OrderStatusEnum.UNPAID.getValue().equals(order.getStatus()), "订单不可支付，请检查订单状态");

        RLock lock = redissonClient.getLock(OrderConstants.ORDER_LOCK_PREFIX + order.getOrderSn());
        try {
            lock.lock();
            T result;
            switch (paymentForm.getPaymentMethod()) {
                case WX_JSAPI:
                    result = (T) wxJsapiPay(paymentForm.getAppId(), order.getOrderSn(), order.getPaymentAmount());
                    break;
                default:
                    result = (T) balancePay(order);
                    break;
            }
            return result;
        } finally {
            //释放锁
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
    }


    /**
     * 余额支付
     *
     * @param order
     * @return
     */
    private Boolean balancePay(OmsOrder order) {
        // 扣减余额
        Long memberId = order.getMemberId();
        Long payAmount = order.getPaymentAmount();
        Result<?> deductBalanceResult = memberFeignClient.deductBalance(memberId, payAmount);
        Assert.isTrue(Result.isSuccess(deductBalanceResult), "扣减账户余额失败");

        // 扣减库存
        skuFeignClient.deductStock(order.getOrderSn());

        // 更新订单状态
        order.setStatus(OrderStatusEnum.PAID.getValue());
        order.setPaymentMethod(PaymentMethodEnum.BALANCE.getValue());
        order.setPaymentTime(new Date());
        this.updateById(order);
        // 支付成功删除购物车已勾选的商品
        cartService.removeCheckedItem();
        return Boolean.TRUE;
    }


    /**
     * 微信支付调起
     *
     * @param appId         微信小程序ID
     * @param orderSn       订单编号
     * @param paymentAmount 支付金额
     * @return 微信支付调起参数
     */
    private WxPayUnifiedOrderV3Result.JsapiResult wxJsapiPay(String appId, String orderSn, Long paymentAmount) {
        Long memberId = SecurityUtils.getMemberId();
        // 如果已经有outTradeNo了就先进行关单
        if (StrUtil.isNotBlank(orderSn)) {
            try {
                wxPayService.closeOrderV3(orderSn);
            } catch (WxPayException e) {
                log.error(e.getMessage(), e);
                throw new BizException("微信关单异常");
            }
        }

        // 更新订单状态
        boolean result = this.update(new LambdaUpdateWrapper<OmsOrder>()
                .set(OmsOrder::getPaymentMethod, PaymentMethodEnum.WX_JSAPI.getValue())
                .eq(OmsOrder::getOrderSn, orderSn)
        );

        String memberOpenId = memberFeignClient.getMemberOpenId(memberId).getData();

        WxPayUnifiedOrderV3Request wxRequest = new WxPayUnifiedOrderV3Request()
                .setAppid(appId)
                .setOutTradeNo(orderSn)
                .setAmount(new WxPayUnifiedOrderV3Request
                        .Amount()
                        .setTotal(Math.toIntExact(paymentAmount))
                )
                .setPayer(
                        new WxPayUnifiedOrderV3Request.Payer()
                                .setOpenid(memberOpenId)
                )
                .setDescription("赅买-订单编号：" + orderSn)
                .setNotifyUrl(wxPayProperties.getPayNotifyUrl());
        WxPayUnifiedOrderV3Result.JsapiResult jsapiResult;
        try {
            jsapiResult = wxPayService.createOrderV3(TradeTypeEnum.JSAPI, wxRequest);
        } catch (WxPayException e) {
            log.error(e.getMessage(), e);
            throw new BizException("微信统一下单异常");
        }
        return jsapiResult;
    }

    /**
     * 关闭订单
     *
     * @param orderSn 订单编号
     * @return 是否成功 true|false
     */
    @Override
    public boolean closeOrder(String orderSn) {

        return this.update(new LambdaUpdateWrapper<OmsOrder>()
                .eq(OmsOrder::getOrderSn, orderSn)
                .eq(OmsOrder::getStatus, OrderStatusEnum.UNPAID.getValue())
                .set(OmsOrder::getStatus, OrderStatusEnum.CANCELED.getValue())
        );
    }

    /**
     * 删除订单
     *
     * @param orderId 订单ID
     * @return true/false
     */
    @Override
    public boolean deleteOrder(Long orderId) {
        OmsOrder order = this.getById(orderId);
        Assert.isTrue(order != null, "删除失败,订单不存在！");

        Assert.isTrue(
                OrderStatusEnum.CANCELED.getValue().equals(order.getStatus())
                        || OrderStatusEnum.UNPAID.getValue().equals(order.getStatus())
                ,
                "当前状态订单不能删除"
        );

        return this.removeById(orderId);
    }

    @Override
    public void handleWxPayOrderNotify(SignatureHeader signatureHeader, String notifyData) throws WxPayException {
        log.info("开始处理支付结果通知");
        // 解密支付通知内容
        final WxPayOrderNotifyV3Result.DecryptNotifyResult result = this.wxPayService.parseOrderNotifyV3Result(notifyData, signatureHeader).getResult();
        log.debug("支付通知解密成功：[{}]", result.toString());
        // 根据商户订单号查询订单
        OmsOrder orderDO = this.getOne(new LambdaQueryWrapper<OmsOrder>()
                .eq(OmsOrder::getOutTradeNo, result.getOutTradeNo())
        );
        // 支付成功处理
        if (WxPayConstants.WxpayTradeStatus.SUCCESS.equals(result.getTradeState())) {
            orderDO.setStatus(OrderStatusEnum.PAID.getValue());
            orderDO.setTransactionId(result.getTransactionId());
            orderDO.setPaymentTime(new Date());
            this.updateById(orderDO);
        }
        log.info("账单更新成功");
        // 支付成功删除购物车已勾选的商品
        cartService.removeCheckedItem();
    }

    @Override
    public void handleWxPayRefundNotify(SignatureHeader signatureHeader, String notifyData) throws WxPayException {
        log.info("开始处理退款结果通知");
        // 解密支付通知内容
        final WxPayRefundNotifyV3Result.DecryptNotifyResult result = this.wxPayService.parseRefundNotifyV3Result(notifyData, signatureHeader).getResult();
        log.debug("退款通知解密成功：[{}]", result.toString());
        // 根据商户退款单号查询订单
        QueryWrapper<OmsOrder> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OmsOrder::getOutTradeNo, result.getOutTradeNo());
        OmsOrder orderDO = this.getOne(wrapper);
        // 退款成功处理
        if (WxPayConstants.RefundStatus.SUCCESS.equals(result.getRefundStatus())) {
            orderDO.setStatus(OrderStatusEnum.COMPLETE.getValue());
            orderDO.setRefundId(result.getRefundId());
            this.updateById(orderDO);
        }
        log.info("账单更新成功");
    }


    /**
     * 获取订单的商品明细信息
     * <p>
     * 创建订单两种方式，1：直接购买；2：购物车结算
     *
     * @param skuId 直接购买必有值，购物车结算必没值
     * @return
     */
    private List<OrderItemDTO> getOrderItems(Long skuId, Long memberId) {
        List<OrderItemDTO> orderItems;
        if (skuId != null) {  // 直接购买
            orderItems = new ArrayList<>();
            SkuInfoDTO skuInfoDTO = skuFeignClient.getSkuInfo(skuId);
            OrderItemDTO orderItemDTO = new OrderItemDTO();
            orderItemDTO.setSkuId(skuId);
            BeanUtil.copyProperties(skuInfoDTO, orderItemDTO);
            orderItemDTO.setSkuId(skuInfoDTO.getId());
            orderItemDTO.setQuantity(1); // 直接购买商品的数量为1
            orderItems.add(orderItemDTO);
        } else { // 购物车结算
            List<CartItemDto> cartItems = cartService.listCartItems(memberId);
            orderItems = cartItems.stream()
                    .filter(CartItemDto::getChecked)
                    .map(cartItem -> {
                        OrderItemDTO orderItemDTO = new OrderItemDTO();
                        BeanUtil.copyProperties(cartItem, orderItemDTO);
                        return orderItemDTO;
                    }).collect(Collectors.toList());
        }
        return orderItems;
    }

    /**
     * 生成商户订单号
     *
     * @param memberId 会员ID
     * @return
     */
    private String generateTradeNo(Long memberId) {
        // 用户id前补零保证五位，对超出五位的保留后五位
        String userIdFilledZero = String.format("%05d", memberId);
        String fiveDigitsUserId = userIdFilledZero.substring(userIdFilledZero.length() - 5);
        // 在前面加上wxo（wx order）等前缀是为了人工可以快速分辨订单号是下单还是退款、来自哪家支付机构等
        // 将时间戳+3位随机数+五位id组成商户订单号，规则参考自<a href="https://tech.meituan.com/2016/11/18/dianping-order-db-sharding.html">大众点评</a>
        return System.currentTimeMillis() + RandomUtil.randomNumbers(3) + fiveDigitsUserId;
    }

}
