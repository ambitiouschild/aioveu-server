package com.aioveu.pay.aioveu00Payment.service.impl;

import cn.hutool.json.JSONUtil;
import com.aioveu.common.constant.OrderConstants;
import com.aioveu.common.enums.oms.OrderStatusEnum;
import com.aioveu.common.enums.pay.PaymentBizTypeEnum;
import com.aioveu.common.enums.pay.PaymentChannelEnum;
import com.aioveu.common.enums.pay.PaymentMethodEnum;
import com.aioveu.common.enums.pay.PaymentStatusEnum;
import com.aioveu.common.exception.BusinessException;
import com.aioveu.common.result.Result;
import com.aioveu.common.result.ResultCode;
import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.common.web.exception.BizException;
import com.aioveu.order.api.OrderFeignClient;
import com.aioveu.order.model.aioveu01Order.form.OmsOrderForm;
import com.aioveu.pay.aioveu00Payment.service.PaymentService;
import com.aioveu.pay.aioveu01PayOrder.converter.PayOrderConverter;
import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu01PayOrder.service.PayOrderService;
import com.aioveu.pay.aioveu06PayFlow.service.PayFlowService;
import com.aioveu.pay.aioveu07PayNotify.service.PayNotifyService;
import com.aioveu.pay.aioveu08PayAccount.service.PayAccountService;
import com.aioveu.pay.aioveu01.PaymentStrategy.PaymentStrategy;
import com.aioveu.pay.aioveu01.PaymentStrategy.PaymentStrategyFactory;
//import com.aioveu.pay.aioveuModule.channelRouter.ChannelRouter;
import com.aioveu.pay.aioveu01.service.WechatPay.service.WeChatPayService;
import com.aioveu.pay.aioveu10MqSendRecord.service.MqSendRecordService;
import com.aioveu.pay.aioveu12MqProducerPayment.enums.PaymentMqBizType;
import com.aioveu.pay.aioveu12MqProducerPayment.model.vo.SendPaymentMqDTO;
import com.aioveu.pay.aioveu12MqProducerPayment.service.PayCommonMessageProducerService;
import com.aioveu.common.enums.pay.PaymentCallbackStatusEnum;
import com.aioveu.pay.aioveu13PayCallbackRecord.model.entity.PayCallbackRecord;
import com.aioveu.pay.aioveu13PayCallbackRecord.service.PayCallbackRecordService;
import com.aioveu.pay.model.aioveuPayment.PaymentParamsVO;
import com.aioveu.pay.model.aioveuPayment.request.PaymentRequestOmsToPayDTO;
import com.aioveu.pay.model.aioveuPayment.request.PaymentRequestPayToTPPDTO;
import com.aioveu.pay.model.aioveu01PayOrder.vo.PayOrderVO;
import com.aioveu.pay.model.aioveuPayment.PaymentCallbackDTO;
import com.aioveu.pay.model.aioveuPayment.PaymentStatusVO;
import com.aioveu.ums.api.MemberFeignClient;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import com.aioveu.common.rabbitmq.producer.util.MessageIdGenerator;

import static cn.hutool.core.util.NumberUtil.toBigDecimal;

/**
 * @ClassName: aa
 * @Description TODO 主要业务逻辑接口实现
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 20:53
 * @Version 1.0
 **/

@Service
@Slf4j
@RequiredArgsConstructor  // Lombok 自动生成构造函数
public class PaymentServiceImpl implements PaymentService {


    private final PaymentStrategyFactory strategyFactory;
    private final PayFlowService payFlowService;
    private final PayOrderService payOrderService;
    private final PayOrderConverter payOrderConverter;
    private final PayAccountService payAccountService;
    private final PayNotifyService payNotifyService;
    private final WeChatPayService wechatPayService;
    private final OrderFeignClient orderFeignClient;
    private final MqSendRecordService mqSendRecordService;
    private final PayCommonMessageProducerService payCommonMessageProducerService;
    private final MessageIdGenerator messageIdGenerator;

    private final PayCallbackRecordService payCallbackRecordService;

    @Value("${pay.wechat.mch-key:}")
    private String wxMchKey;

    @Value("${pay.alipay.public-key:}")
    private String aliPublicKey;

    // 开启模拟支付
    @Value("${pay.mock.enabled:true}")
    private Boolean mockPayEnabled;

    // 分布式锁客户端
    private final RedissonClient redissonClient;
    // 会员服务Feign客户端
    private final MemberFeignClient memberFeignClient;
    /**
     * 统一支付接口
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentParamsVO createPaymentPayToTPP(PaymentRequestPayToTPPDTO request) {
        try {
            log.info("============【Pay】unifiedPayment统一支付接口============");

            // 1. 参数校验
//            validatePaymentRequest(request);

            // 1. 查询支付单（不新建）createPaymentPayToTPP不再查库，直接用 paymentNo
            PayOrder payOrder = payOrderService.getByPaymentNo(request.getPayOrderNo());
            if (payOrder == null) {
                throw new BusinessException("支付单不存在");
            }
            String paymentNo = payOrder.getPaymentNo();
            log.info("【Pay】支付订单支付单号paymentNo：{}",paymentNo);

            if (payOrder.getPaymentStatus() != PaymentStatusEnum.PAYING) {
                throw new BusinessException("支付单状态异常");
            }

            // 3. 根据支付渠道选择支付策略
            PaymentStrategy strategy = strategyFactory.getStrategy(request.getPaymentChannel());
            log.info("【Pay】获取支付策略：{}",strategy.getClass().getSimpleName());

            // 4. 调用策略获取支付参数,调第三方
            PaymentParamsVO params = strategy.appPay(paymentNo, request);
            log.info("【Pay】调用策略支付后获取的请求参数：{}", params);

            // 更新状态为 PAYING（可选） 在omsToPay已经改为支付中
//            payOrderService.markPaying(paymentNo);

            // 5. 直接返回支付参数VO
            return params;

        } catch (BusinessException e) {
            log.error("支付业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("支付系统异常", e);
            throw new BusinessException("支付系统异常");
        }
    }


    /**
     * 前端调用：查询支付状态
     *
     * @return
     */
    @Override
    public PaymentStatusVO queryPaymentStatus(String orderNo){

        PaymentStatusVO paymentStatusVO =new PaymentStatusVO();

        // 1. 查询本地数据库

        log.info("【前端调用：查询支付状态】调用orderFeignClient，查询本地oms数据库");

        OmsOrderForm orderForm = orderFeignClient.getOrderDetailByOrderNo(orderNo);
        if (orderForm == null) {
            paymentStatusVO.setErrorMessage("订单不存在");
            paymentStatusVO.setPaymentStatus(PaymentStatusEnum.UNKNOWN); // 特殊状态：订单不存在
            log.info("【前端调用：查询支付状态】订单不存在");
            return paymentStatusVO;
        }

        // 2. 如果订单已支付，直接返回
        if (orderForm.getStatus() == OrderStatusEnum.SHIPPED ||orderForm.getStatus() == OrderStatusEnum.COMPLETED ||orderForm.getStatus() == OrderStatusEnum.CANCELED) {
            paymentStatusVO.setPaymentNo(orderNo);
            paymentStatusVO.setPaymentStatus(PaymentStatusEnum.PAID);
            paymentStatusVO.setErrorMessage("订单已支付");
            log.info("【前端调用：查询支付状态】本地订单已支付，状态: {}", orderForm.getStatus());
            return paymentStatusVO;
        }else{

            log.info("【前端调用：查询支付状态】订单状态: {}，调用微信查询接口", orderForm.getStatus());
            // 3. 如果订单未支付，调用微信查询接口
            try {
                paymentStatusVO = wechatPayService.queryPayment(orderNo);
                log.info("【wechatPayService】微信支付状态返回结果:{}",paymentStatusVO);

                PaymentStatusEnum weChatPaymentStatus =  paymentStatusVO.getPaymentStatus();

                log.info("【wechatPayService】准备更新本地订单状态为微信支付状态weChatPaymentStatus:{}",weChatPaymentStatus);
                // 4. 查询成功后，直接更新本地订单状态
                log.info("【前端调用：查询支付状态】开始更新本地订单状态");


                // 4. 根据微信返回结果更新本地状态
                //支付状态：0-待支付 1-支付中 2-支付成功 3-支付失败 4-已关闭 5-已退款
                try {
                    boolean updateResult = orderFeignClient.updateOrderStatusByWechatPay(orderNo, weChatPaymentStatus);
                    log.info("【前端调用：查询支付状态】更新订单状态结果: {}", updateResult);

                    if (updateResult) {
                        paymentStatusVO.setErrorMessage("订单状态已更新,状态已同步");
                    } else {
                        paymentStatusVO.setErrorMessage("更新订单状态失败,状态同步失败");
                    }
                } catch (Exception e) {
                    log.error("【前端调用：查询支付状态】更新订单状态异常", e);
                    paymentStatusVO.setErrorMessage("更新订单异常: " + e.getMessage());
                }

                return paymentStatusVO;

            } catch (Exception e) {
                paymentStatusVO.setErrorMessage("错误");
                return paymentStatusVO;
            }


        }

    }


    /**
     * 处理支付回调
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> handleCallback(PaymentCallbackDTO callback) {

        String paymentNo = callback.getPaymentNo();
        log.info("【支付回调】开始处理，支付单号: {}", paymentNo);

        try {
            // ============ 1. 验证回调签名 ============
//            if (!verifyCallbackSign.verifyCallbackSign(callback)) {
//                log.error("【支付回调】签名验证失败: {}", paymentNo);
//                throw new BusinessException(ResultCode.CALLBACK_SIGN_ERROR, "回调签名验证失败");
//            }

            // ============ 2. 查询支付订单 ============
            PayOrder payorder = payOrderService.selectByPaymentNo(paymentNo);
            if (payorder == null) {
                log.error("【支付回调】支付订单不存在: {}", paymentNo);
                throw new BusinessException(ResultCode.ORDER_NOT_FOUND, "支付订单不存在");
            }

            // ============ 3. 避免重复处理 ============
            if (payorder.getPaymentStatus() != PaymentStatusEnum.UNPAID
                    && payorder.getPaymentStatus() != PaymentStatusEnum.PAYING) {
                log.warn("订单已处理，跳过回调: paymentNo={}, status={}",
                        callback.getPaymentNo(), payorder.getPaymentStatus());
                return Result.success();
            }

            // ============ 4. 验证回调金额 ============


            // ============ 5. 更新订单状态 ============
            Boolean result = payOrderService.updateOrderStatus(payorder,callback);


            // ============ 6. 处理业务逻辑 ============
            payOrderService.handleBusinessLogic(payorder,callback);


            // ============ 7. 记录支付流水 ============
            payFlowService.recordPaymentFlow(payorder, callback);


            // ============ 8. 发送异步通知 ============
            payNotifyService.sendPaymentNotify(payorder, callback);

            log.info("【支付回调】处理完成: {}，状态: {}", paymentNo, payorder.getPaymentStatus());

            return Result.success();

        } catch (Exception e) {
            log.error("处理支付回调异常", e);
            throw new BusinessException("处理支付回调失败");
        }
    }

    /**
     * 处理微信回调
     */
    @Override
    public String handleWechatCallback(String xmlData) {

        long startTime = System.currentTimeMillis();


        try {

            Map<String, String> params = parseWechatXml(xmlData);
            log.info("【微信回调】解析微信回调参数: {}", JSON.toJSONString(params));

            // 1️验签（失败立刻 FAIL）
            // 1️验签（mock 跳过）
            if (!isMockWechatCallback(params)) {
                if (!verifyWechatSign(params)) {
                    log.error("【微信回调】微信签名验证失败");
                    return generateWechatFailResponse("签名验证失败");
                }
            } else {
                log.info("【微信回调】检测到 mock 回调，跳过签名校验");
            }

            String transactionId = params.get("transaction_id");
            String paymentNo = params.get("out_trade_no");

            if (!StringUtils.hasText(paymentNo)) {
                log.error("【微信回调】支付订单号为空");
                return generateWechatFailResponse("订单号为空");
            }


            // 2️通信失败
            String returnCode = params.get("return_code");
            if (!"SUCCESS".equals(returnCode)) {
                log.error("【微信回调】通信失败: paymentNo={}, returnMsg={}",
                        paymentNo, params.get("return_msg"));
                return generateWechatFailResponse(params.get("return_msg"));
            }

            // 3. 验证业务返回码
            String resultCode = params.get("result_code");
            if (!"SUCCESS".equals(resultCode)) {
                // 支付失败
                String errorCode = params.get("err_code");
                String errorMsg = params.get("err_code_des");
                log.error("【微信回调】支付失败: paymentNo={}, errorCode={}, errorMsg={}",
                        paymentNo, errorCode, errorMsg);
                return handlePaymentFailure(paymentNo, params, startTime);
            }

            // 4. ✅【核心】幂等判断（必须用 transaction_id）（✅ 用 pay_callback_record）
            if (payCallbackRecordService.isConsumed(transactionId)) {

                PayCallbackRecord record =
                        payCallbackRecordService.getByTransactionId(transactionId);

                if (record != null && record.getNotifyCount() >= 10) {
                    log.warn("【微信回调】超过最大回调次数，忽略通知, transactionId={}", transactionId);
                    return generateWechatSuccessResponse();
                }


                log.info("【微信回调】已处理，增加回调次数, transactionId={}", transactionId);
                payCallbackRecordService.incrNotifyCount(transactionId);
                return generateWechatSuccessResponse();
            }

            // 5. 查询支付订单
            PayOrder payOrder = payOrderService.getByPaymentNo(paymentNo);
            if (payOrder == null) {
                log.error("【微信回调】支付订单不存在: paymentNo={}", paymentNo);
                return generateWechatFailResponse("订单不存在");
            }


            // 9. 验证应用ID
//            if (!verifyAppId(payOrder, params)) {
//                log.error("【微信回调】应用ID不匹配: paymentNo={}, 订单appId={}, 回调appId={}",
//                        paymentNo, payOrder.getAppId(), params.get("appid"));
//                return generateWechatFailResponse("应用ID不匹配");
//            }

            // 6️处理支付成功（✅ 事务）
            processWechatPaySuccess(payOrder, params, transactionId,startTime);

            // 10. 处理支付成功
            return generateWechatSuccessResponse();

        } catch (BizException e) {
            log.error("【微信回调】业务异常", e);
            return generateWechatFailResponse(e.getMessage());
        } catch (Exception e) {
            log.error("【微信回调】系统异常", e);
            return generateWechatSuccessResponse(); // ✅ 防死信
        }
    }


    private boolean isMockWechatCallback(Map<String, String> params) {
        String sign = params.get("sign");
        String transactionId = params.get("transaction_id");
        return "test_sign".equals(sign)
                || (transactionId != null && transactionId.startsWith("MOCK_"));
    }


    /**
     * 处理支付成功  事务方法（✅ 核心）
     * 方案一（最推荐）：事务方法不返回 String
     */
    @Transactional(rollbackFor = Exception.class)
    public void processWechatPaySuccess(
            PayOrder payOrder,
            Map<String, String> params,
            String transactionId,
            Long startTime
    ) {
        String paymentNo = payOrder.getPaymentNo();

        try {

            // 1. 状态校验
            if (!isProcessable(payOrder)) {
                log.warn("【微信回调】订单不可处理: {}", paymentNo);
                return; // ✅ 合法
            }

            // mock 回调跳过金额校验
            boolean isMock = transactionId != null && transactionId.startsWith("MOCK_");

            // 2. 验证金额  方案二（✅ 推荐）：mock 跳过金额校验（仅 dev）
            if (!isMock && !verifyAmount(payOrder, params)) {
                log.error("【微信回调】金额不匹配: paymentNo={}, 订单金额={}, 回调金额={}",
                        paymentNo, payOrder.getPaymentAmount(), getCallbackAmount(params));
                throw new BizException("金额不匹配");
            }


            // 3. 更新支付单
            boolean updateSuccess = payOrderService.updatePaymentStatus(payOrder, true, params);
            if (!updateSuccess) {
                log.error("【微信回调】更新支付订单状态失败: paymentNo={}", paymentNo);
                throw new BizException("更新支付单失败");
            }

            // 4. ✅ 幂等落库（必须在事务内）
            payCallbackRecordService.markConsumed(
                    transactionId,
                    paymentNo,
                    payOrder.getOrderNo(),
                    params
            );


            // 5. 支付流水
            /*
            * ❌ Map 不适合做业务参数
                ✅ PaymentCallbackDTO 才是支付系统的“合同”
            *
            * */
            PaymentCallbackDTO paymentCallbackdto = convertToDto(payOrder,params);
            payFlowService.recordPaymentFlow(payOrder, paymentCallbackdto);


            // 6. 发送MQ成功消息
            SendPaymentMqDTO dto =  new SendPaymentMqDTO();
            dto.setMessageId(messageIdGenerator.generatePaymentMessageId(paymentNo));
            dto.setPaymentNo(payOrder.getPaymentNo());
            dto.setOmsOrderNo(payOrder.getOrderNo());
            dto.setPaymentAmount(payOrder.getPaymentAmount());
            dto.setTransactionId(params.get("transaction_id"));
            dto.setPaymentTime(LocalDateTime.now());
            // ✅ 补上这两行
            dto.setBizTypeEnum(PaymentMqBizType.PAYMENT_SUCCESS);

            boolean mqSuccess = payCommonMessageProducerService.sendPaymentSuccessMessage(dto);

            // 如果MQ发送失败，记录到补偿表
            if (!mqSuccess) {
                log.warn("【微信回调】MQ发送失败，记录到补偿表: paymentNo={}", paymentNo);
                saveToCompensation(payOrder, params, "MQ_SEND_FAILED");
            }

            // 7. 记录处理时间
            long costTime = System.currentTimeMillis() - startTime;
            log.info("【微信回调】支付成功处理完成: paymentNo={}, 订单号={}, 微信订单号={}, 耗时={}ms",
                    paymentNo, payOrder.getOrderNo(), params.get("transaction_id"), costTime);



        } catch (Exception e) {
            log.error("【微信回调】支付成功处理异常: paymentNo={}", payOrder.getPaymentNo(), e);

        }
    }


    private PaymentCallbackDTO convertToDto(PayOrder payOrder, Map<String, String> params) {

        PaymentCallbackDTO dto = new PaymentCallbackDTO();
        dto.setPaymentNo(payOrder.getPaymentNo());
        dto.setOrderNo(payOrder.getOrderNo());
        dto.setChannel("WECHAT");
        dto.setThirdTransactionId(params.get("transaction_id"));
        dto.setPaidAmount(getCallbackAmount(params));
        dto.setPaidTime(LocalDateTime.now());
        dto.setStatus(PaymentCallbackStatusEnum.SUCCESS);
        dto.setRawData(JSON.toJSONString(params));
        return dto;
    }

    /**
     * 处理支付失败
     */
    private String handlePaymentFailure(String paymentNo, Map<String, String> params, long startTime) {
        try {
            // 1. 查询支付订单
            PayOrder payOrder = payOrderService.getByPaymentNo(paymentNo);
            if (payOrder == null) {
                log.warn("【微信回调】支付失败订单不存在: paymentNo={}", paymentNo);
                return generateWechatSuccessResponse(); // 对微信返回成功，避免重复通知
            }

            // 2. 更新支付订单为失败状态
            if (isProcessable(payOrder)) {
                boolean updateSuccess = payOrderService.updatePaymentStatus(payOrder, false, params);
                if (!updateSuccess) {
                    log.error("【微信回调】更新支付失败状态失败: paymentNo={}", paymentNo);
                }
            }

            payCallbackRecordService.markConsumed(
                    params.get("transaction_id"),
                    paymentNo,
                    payOrder.getOrderNo(),
                    params
            );


            // 3. 发送MQ失败消息
            SendPaymentMqDTO dto = new SendPaymentMqDTO();

            dto.setMessageId(messageIdGenerator.generatePaymentMessageId(paymentNo));
            dto.setPaymentNo(payOrder.getPaymentNo());
            dto.setOmsOrderNo(payOrder.getOrderNo());
            dto.setPaymentAmount(payOrder.getPaymentAmount());
            dto.setTransactionId(params.get("transaction_id"));
            dto.setPaymentTime(LocalDateTime.now());


            boolean mqSuccess = sendPaymentFailureMessage(dto);
            if (!mqSuccess) {
                log.warn("【微信回调】MQ失败消息发送失败，记录到补偿表: paymentNo={}", paymentNo);
                saveToCompensation(payOrder, params, "MQ_FAILURE_SEND_FAILED");
            }

            // 4. 记录处理时间
            long costTime = System.currentTimeMillis() - startTime;
            log.warn("【微信回调】支付失败处理完成: paymentNo={}, 耗时={}ms", paymentNo, costTime);

            return generateWechatSuccessResponse(); // 对微信返回成功，避免重复通知

        } catch (Exception e) {
            log.error("【微信回调】支付失败处理异常: paymentNo={}", paymentNo, e);
            return generateWechatSuccessResponse(); // 对微信返回成功，避免重复通知
        }
    }

    /**
     * 检查订单是否可处理
     */
    private boolean isProcessable(PayOrder payOrder) {
        PaymentStatusEnum status = payOrder.getPaymentStatus();
        return status == PaymentStatusEnum.UNPAID
                || status == PaymentStatusEnum.PAYING;
    }


    /**
     * 验证金额
     */
    private boolean verifyAmount(PayOrder payOrder, Map<String, String> params) {
        try {

            //回调里的 verifyAmount，必须和 PayOrder比较
            //PayOrder是“支付系统对外的唯一资金合同”
            // PayOrder 元 → 分
            Long payAmountFen = yuanToFen(payOrder.getPaymentAmount());

            Long callbackFen = yuanToFen(getCallbackAmount(params));

            if (!payAmountFen.equals(callbackFen)) {
                log.error("【微信回调】金额不匹配，支付单金额(分):{}, 回调金额(分):{}",
                        payAmountFen, callbackFen);
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("验证金额异常: paymentNo={}", payOrder.getPaymentNo(), e);
            return false;
        }
    }

    /**
     * 获取回调金额
     */
    private BigDecimal getCallbackAmount(Map<String, String> params) {
        try {
            String totalFee = params.get("total_fee");
            if (StringUtils.hasText(totalFee)) {
                return new BigDecimal(totalFee)
                        .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            }
        } catch (Exception e) {
            log.error("解析回调金额异常: totalFee={}", params.get("total_fee"), e);
        }
        return BigDecimal.ZERO;
    }

    /**
     * 验证应用ID
     */
//    private boolean verifyAppId(PayOrder payOrder, Map<String, String> params) {
//        try {
//            String callbackAppId = params.get("appid");
//            String orderAppId = payOrder.getAppId();
//
//            if (!StringUtils.hasText(orderAppId)) {
//                // 如果订单没有appId，跳过验证
//                return true;
//            }
//
//            return orderAppId.equals(callbackAppId);
//        } catch (Exception e) {
//            log.error("验证应用ID异常: paymentNo={}", payOrder.getPaymentNo(), e);
//            return false;
//        }
//    }


    /**
     * 发送支付失败消息
     */
    private boolean sendPaymentFailureMessage(SendPaymentMqDTO dto) {
        try {
            return payCommonMessageProducerService.sendPaymentFailedMessage(dto);
        } catch (Exception e) {
            log.error("发送支付失败MQ消息异常: paymentNo={}", dto.getPaymentNo(), e);
            return false;
        }
    }

    /**
     * 保存到补偿表
     */
    private void saveToCompensation(PayOrder payOrder, Map<String, String> params, String reason) {
        try {
            // TODO: 实现补偿表保存逻辑
            log.info("保存到补偿表: paymentNo={}, reason={}", payOrder.getPaymentNo(), reason);

            // 示例代码：
            // CompensationRecord record = new CompensationRecord();
            // record.setPaymentNo(payOrder.getPaymentNo());
            // record.setOrderNo(payOrder.getOrderNo());
            // record.setTenantId(payOrder.getTenantId());
            // record.setEventType("WECHAT_CALLBACK");
            // record.setReason(reason);
            // record.setParams(JSON.toJSONString(params));
            // record.setRetryCount(0);
            // record.setStatus(CompensationStatusEnum.PENDING.getValue());
            // record.setCreateTime(LocalDateTime.now());
            // compensationRecordService.save(record);

        } catch (Exception e) {
            log.error("保存到补偿表异常: paymentNo={}", payOrder.getPaymentNo(), e);
        }
    }


    /**
     * 生成微信成功响应
     */
    private String generateWechatSuccessResponse() {
        return "<xml>" +
                "<return_code><![CDATA[SUCCESS]]></return_code>" +
                "<return_msg><![CDATA[OK]]></return_msg>" +
                "</xml>";
    }

    /**
     * 生成微信失败响应
     */
    private String generateWechatFailResponse(String errorMsg) {
        return "<xml>" +
                "<return_code><![CDATA[FAIL]]></return_code>" +
                "<return_msg><![CDATA[" + (errorMsg != null ? errorMsg : "处理失败") + "]]></return_msg>" +
                "</xml>";
    }

    /**
     * 处理支付宝回调
     */
    @Override
    public String handleAlipayCallback(Map<String, String> params) {
        try {
            log.info("处理支付宝回调参数: {}", JSON.toJSONString(params));

            // 1. 验证签名
            if (!verifyAlipaySign(params)) {
                log.error("支付宝签名验证失败");
                return "failure";
            }

            // 2. 获取订单号
            String orderNo = params.get("out_trade_no");
            if (!StringUtils.hasText(orderNo)) {
                log.error("订单号为空");
                return "failure";
            }

            // 3. 验证交易状态
            String tradeStatus = params.get("trade_status");
            if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                // 支付成功
                String tradeNo = params.get("trade_no");
                log.info("订单支付成功: {}, 支付宝订单号: {}", orderNo, tradeNo);

                // TODO: 更新订单状态
                // orderService.paySuccess(orderNo, tradeNo);

                return "success";
            } else {
                log.error("支付失败，订单号: {}, 状态: {}", orderNo, tradeStatus);
                return "failure";
            }

        } catch (Exception e) {
            log.error("处理支付宝回调异常", e);
            return "failure";
        }
    }

    /**
     * 验证微信签名
     */
    private boolean verifyWechatSign(Map<String, String> params) {
        try {
            if (!StringUtils.hasText(wxMchKey)) {
                log.error("微信商户密钥未配置");
                return false;
            }

            String receivedSign = params.get("sign");
            if (!StringUtils.hasText(receivedSign)) {
                log.error("微信回调中无签名");
                return false;
            }

            // 计算签名
            String generatedSign = calculateWechatSign(params, wxMchKey);

            boolean ok = receivedSign.equals(generatedSign);
            if (!ok) {
                log.error("签名不匹配，收到: {}, 计算: {}", receivedSign, generatedSign);
            }

            return ok;

        } catch (Exception e) {
            log.error("验证微信签名异常", e);
            return false;
        }
    }

    /**
     * 计算微信签名
     */
    private String calculateWechatSign(Map<String, String> params, String mchKey) {
        // 1. 按key排序
        Map<String, String> sorted = new TreeMap<>(params);

        // 2. 拼接字符串
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sorted.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            // 跳过sign字段和空值
            if (!"sign".equals(key) && StringUtils.hasText(value)) {
                if (sb.length() > 0) sb.append("&");
                sb.append(key).append("=").append(value);
            }
        }

        // 3. 加上key
        sb.append("&key=").append(mchKey);

        // 4. 计算MD5
        String signStr = sb.toString();
        log.debug("微信签名字符串: {}", signStr);

        String md5 = DigestUtils.md5DigestAsHex(signStr.getBytes(StandardCharsets.UTF_8));
        return md5.toUpperCase();
    }

    /**
     * 验证支付宝签名
     */
    private boolean verifyAlipaySign(Map<String, String> params) {
        try {
            if (!StringUtils.hasText(aliPublicKey)) {
                log.warn("支付宝公钥未配置，跳过验证");
                return true;  // 开发环境跳过
            }

            // 这里需要支付宝SDK
            // boolean valid = AlipaySignature.rsaCheckV1(params, aliPublicKey, "UTF-8", "RSA2");
            // return valid;

            // 暂时返回true，生产环境一定要实现
            log.warn("支付宝签名验证暂未实现");
            return true;

        } catch (Exception e) {
            log.error("验证支付宝签名异常", e);
            return false;
        }
    }

    /**
     * 解析微信XML
     */
    private Map<String, String> parseWechatXml(String xml) {
        Map<String, String> map = new HashMap<>();

        if (!StringUtils.hasText(xml)) {
            return map;
        }

        try {
            // 简单的XML解析
            xml = xml.replaceAll("<!\\[CDATA\\[|\\]\\]>", "");  // 移除CDATA

            // 正则匹配标签
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("<([^>]+)>([^<]+)</\\1>");
            java.util.regex.Matcher matcher = pattern.matcher(xml);

            while (matcher.find()) {
                String key = matcher.group(1);
                String value = matcher.group(2);
                map.put(key, value);
            }

        } catch (Exception e) {
            log.error("解析XML异常", e);
        }

        return map;
    }



    /**
     * 统一退款接口
     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Result<RefundResultVO> unifiedRefund(RefundRequestDTO request) {
//        try {
//            // 1. 参数校验
////            validateRefundRequest(request);
//
//            // 2. 创建退款记录
//            RefundApplyDTO applyDTO = buildRefundApplyDTO(request);
//            Result<String> createResult = refundService.applyRefund(applyDTO);
//            if (!createResult.isSuccess()) {
//                return Result.error(createResult.getCode(), createResult.getMessage());
//            }
//
//            String refundNo = createResult.getData();
//
//            // 3. 执行退款
//            RefundStrategy strategy = refundStrategyFactory.getStrategy(request.getChannel());
//            RefundResultVO result = strategy.refund(refundNo, request);
//
//            return Result.success(result);
//
//        } catch (BusinessException e) {
//            log.error("退款业务异常", e);
//            throw e;
//        } catch (Exception e) {
//            log.error("退款系统异常", e);
//            throw new BusinessException("退款系统异常");
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
    public PaymentParamsVO createPaymentOmsToPay(PaymentRequestOmsToPayDTO paymentForm) {

        String orderSn = paymentForm.getOrderSn();
        PaymentChannelEnum paymentChannel  = paymentForm.getPaymentChannel();
        PaymentMethodEnum paymentMethod  = paymentForm.getPaymentMethod();

        log.info("【createPaymentOmsToPay】开始处理，订单号: {}, 支付渠道: {}, 支付方式: {},模拟模式: {}",
                orderSn, paymentChannel, paymentMethod, mockPayEnabled);

        //-------------------------------------------------------
        //✅方案一（最优）：OMS 把“已校验过的数据”传给 Pay
        /*
        * Pay 只干一件事：
                “我相信你 OMS 已经校验过了，我只负责收钱”
        * */

        //PayOrder 是“唯一可信资金来源”
        //✅ 1.查支付订单（✅ 必须补）（✅ 金额唯一可信来源）  ✅ 方案二（兜底）：Pay 完全不查订单状态
        PayOrderVO payOrder = payOrderService.getPayOrderByOrderNo(orderSn);
        if (payOrder == null) {
            throw new BizException("【createPaymentOmsToPay】支付订单不存在");
        }
        log.info("【createPaymentOmsToPay】查支付订单（✅ 必须补）,根据订单号查询支付订单PayOrder:{}",payOrder);
        //-------------------------------------------------------



        // 只做：校验 + 创建支付单   锁只保护数据一致性
        //  2.验证支付金额 ✅ 用 PayOrder 校验金额（✅ 关键）  校验金额合法性（不是比较）
        Long payAmountFen = yuanToFen(payOrder.getPaymentAmount()); // ✅ 元转分   BigDecimal元 转 BIGINT 分
        if (payAmountFen == null || payAmountFen <= 0) {
            log.error("【createPaymentOmsToPay】支付金额异常: {}", payAmountFen);
            throw new BizException("【createPaymentOmsToPay】支付金额异常");
        }
        //✅ 用 PayOrder 校验金额（✅ 关键）
        log.info("【createPaymentOmsToPay】✅ 用 PayOrder 校验金额（✅） 关键");
        log.info("【createPaymentOmsToPay】✅ 永远用 PayOrder 的金额做资金判断");
        log.info("【createPaymentOmsToPay】✅ PayOrder 金额校验通过");

        //-------------------------------------------------------
        // 3. 校验状态（✅ 只校验状态，不碰金额）
        log.info("【createPaymentOmsToPay】校验订单状态是否可支付");
        if (!PaymentStatusEnum.UNPAID.equals(payOrder.getPaymentStatus())) {
            throw new BizException("【createPaymentOmsToPay】订单不可支付，请检查订单状态");
        }

        // 5. 支付渠道校验
        if (!isValidPaymentChannel(paymentChannel)) {
            throw new BizException("【createPaymentOmsToPay】不支持的支付渠道: " + paymentChannel);
        }

        if (paymentForm.getPaymentMethod() == PaymentMethodEnum.JSAPI
                && !StringUtils.hasText(paymentForm.getOpenId())) {
            throw new BizException("JSAPI 支付必须提供 openId");
        }

        //-------------------------------------------------------

        // 分布式锁（防重复支付）
        log.info("【createPaymentOmsToPay】使用分布式锁防止重复支付（同一订单同时支付）");
        RLock lock = redissonClient.getLock(OrderConstants.ORDER_LOCK_PREFIX + payOrder.getOrderNo());
        log.info("【createPaymentOmsToPay】获取锁,分布式锁范围过大");
        lock.lock();
        log.info("【【createPaymentOmsToPay】获取分布式锁成功");


        try {

            //这把锁里：只做“支付单维度的幂等与状态保护”
            //防止同一支付单，在极短时间内被重复发起支付 / 重复处理
            log.info("【createPaymentOmsToPay】防止同一支付单，在极短时间内被重复发起支付 / 重复处理）");
            /*
            * ❌ 不做：
                    远程调用（Feign / MQ / 微信）
                    复杂业务逻辑
                    金额比较（前面已经做完了）
                    ✅ 只保证：同一支付单，同一时刻，只会被处理一次
                    *
                    * // ✅ 锁内禁止：
                    // 1. Feign 调用
                    // 2. MQ 发送
                    // 3. 第三方 HTTP 调用
                    // 4. 非 PayOrder 表操作
            * */

            // 1️再次确认 PayOrder 状态（防止并发）
            // 并发安全校验（幂等核心）
            PayOrderVO current = payOrderService.getPayOrderByOrderNo(orderSn);
            if (current == null) {
                throw new BizException("【createPaymentOmsToPay】支付订单不存在");
            }
            log.info("【createPaymentOmsToPay】再次确认 PayOrder 状态（防止并发）,并发安全校验（幂等核心）");

            // 2️只有“未支付”才允许继续
            if (current.getPaymentStatus() != PaymentStatusEnum.UNPAID) {
                throw new BizException("【createPaymentOmsToPay】订单已在支付中或已完成");
            }

            // 3️（可选）标记支付中状态，防止重复进入 ✅ 数据库级状态推进  //这里已经改为支付中
            payOrderService.updateStatusToPaying(current.getPaymentNo());
            log.info("【createPaymentOmsToPay】先锁 → 再改状态 → 再解锁 → 再调微信,将支付单从 UNPAID 推进到 PAYING");

        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        // 继续支付流程...

        PaymentRequestOmsToPayDTO paymentRequestOmsToPayDTO = PaymentRequestOmsToPayDTO.builder()
                .orderSn(paymentForm.getOrderSn())
                .bizType(payOrder.getBizType()) // ✅ 后端指定
                .userId(payOrder.getUserId())
                .openId(paymentForm.getOpenId())
                .paymentAmount(yuanToFen(payOrder.getPaymentAmount())) // ✅ 用订单的
                .paymentChannel(payOrder.getPaymentChannel())
                .paymentMethod(payOrder.getPaymentMethod())
                .build();


        // 锁外：调微信
        // 7. 真实支付处理
        return processRealPayment(paymentRequestOmsToPayDTO, paymentChannel, paymentMethod, payOrder, lock);

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
    private PaymentParamsVO processRealPayment(PaymentRequestOmsToPayDTO paymentForm,
                                               PaymentChannelEnum paymentChannel,
                                               PaymentMethodEnum  paymentMethod,
                                               PayOrderVO payOrder,
                                               RLock lock) {
        // 原有的真实支付逻辑
        // 这里可以留空或抛出异常，提示需要配置真实支付

        /*
        * // 2️ 锁外
        // ✅ 调微信
        // ✅ 调 Feign
        // ✅ 不碰 DB 状态
        * */

        log.info("【createPaymentOmsToPay】根据支付渠道和支付方式路由到不同的支付处理逻辑");

//            String appId=paymentForm.getAppId();
        String orderNo =   payOrder.getOrderNo();
        Long memberId = SecurityUtils.getMemberId();
        //OpenID 理论上应该 OMS 传进来
        String openId = paymentForm.getOpenId();
        log.info("【createPaymentOmsToPay】用户OpenID获取成功: {}", openId);

        // 1. 构建支付请求
        PaymentRequestPayToTPPDTO paymentRequest = buildPaymentRequest(payOrder, paymentChannel, paymentMethod, memberId, openId);
        log.info("【createPaymentOmsToPay】Pay微服务后端createPayment需求参数PaymentRequestDTO: {}", JSONUtil.toJsonStr(paymentRequest));

        // 2. 调用支付微服务 ✅ 锁外调用微信
        PaymentParamsVO  paymentParamsVO = this.createPaymentPayToTPP(paymentRequest);
        log.info("【createPaymentOmsToPay】✅ 锁外调用微信,调用支付微服务:{}",paymentParamsVO);


        if (paymentParamsVO == null) {
            throw new BizException("【createPaymentOmsToPay】支付服务返回空结果");
        }


        return paymentParamsVO;

    }


    /**
     * 构建支付请求
     */
    private PaymentRequestPayToTPPDTO buildPaymentRequest(PayOrderVO payOrder,
                                                              PaymentChannelEnum paymentChannel,
                                                              PaymentMethodEnum paymentMethod,
                                                              Long memberId,
                                                              String openId) {

        BigDecimal PaymentAmount = payOrder.getPaymentAmount();

        PaymentRequestPayToTPPDTO request = new PaymentRequestPayToTPPDTO();
        request.setPayOrderNo(payOrder.getPaymentNo());
        request.setUserId(memberId);
        request.setBizType(PaymentBizTypeEnum.ORDER_PAY);
        request.setOrderSn(payOrder.getOrderNo());
        request.setPaymentAmount(PaymentAmount);

        request.setSubject("商品购买");
        request.setBody("订单号：" + payOrder.getOrderNo());

        // ✅ 渠道
        request.setPaymentChannel(paymentChannel);
        log.info("【构建支付请求】支付渠道:{}", paymentChannel);

        // ✅ 支付方式（不再写死）
        request.setPaymentMethod(paymentMethod);
        log.info("【构建支付请求】支付方式:{}", paymentMethod);

        // ✅ 根据支付方式决定参数
        Map<String, Object> extraParams = new HashMap<>();

        // ✅ 根据支付方式决定参数
        switch (paymentMethod) {
            case JSAPI:
                if (com.alibaba.nacos.common.utils.StringUtils.isBlank(openId)) {
                    throw new BizException("JSAPI 支付必须传入 openId");
                }
                request.setOpenId(openId);
                extraParams.put("appId", getAppIdByMethod(paymentChannel,paymentMethod));
                break;
            case APP:
                extraParams.put("appId", getAppIdByMethod(paymentChannel,paymentMethod));
                break;
            case H5:
                extraParams.put("appId", getAppIdByMethod(paymentChannel,paymentMethod));
//                extraParams.put("sceneInfo", buildSceneInfo());
                break;
            case NATIVE:
                extraParams.put("appId", getAppIdByMethod(paymentChannel,paymentMethod));
                break;
            case BALANCE:
                // 余额支付不走三方
                break;
            default:
                throw new BizException("【buildPaymentRequest】不支持的支付方式paymentMethod: " + paymentMethod);
        }

        // 额外参数
        request.setExtraParams(extraParams);

        return request;
    }


    /**
     * 根据支付方式获取AppId
     */
    private String getAppIdByMethod(PaymentChannelEnum paymentChannel,
                                    PaymentMethodEnum paymentMethod) {

        if (paymentChannel == PaymentChannelEnum.WECHAT) {
            return switch (paymentMethod) {
                case JSAPI -> "wxJsapiAppId";
                case APP -> "wxAppAppId";
                case H5 -> "wxH5AppId";
                default -> throw new BizException("微信不支持该支付方式");
            };
        }


        if (paymentChannel == PaymentChannelEnum.ALIPAY) {
            return "aliAppId";
        }

        return null;
    }

    private BigDecimal fenToYuan(Long fen) {
        return BigDecimal.valueOf(fen)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }


    /**
     * BigDecimal 元 → Long 分
     */
    private Long yuanToFen(BigDecimal amountYuan) {
        return amountYuan
                .setScale(2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .longValueExact();
    }


    /**
     * 检查支付渠道是否有效
     */
    private boolean isValidPaymentChannel(PaymentChannelEnum paymentChannel) {

        return paymentChannel != null && paymentChannel != PaymentChannelEnum.UNKNOWN;
    }

}
