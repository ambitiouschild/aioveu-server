package com.aioveu.pay.aioveu01PayOrder.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.enums.pay.PaymentBizTypeEnum;
import com.aioveu.common.enums.pay.PaymentStatusEnum;
import com.aioveu.common.exception.BusinessException;
import com.aioveu.common.web.exception.BizException;
import com.aioveu.order.api.OrderFeignClient;
import com.aioveu.pay.aioveu00Payment.Processor.Impl.BusinessProcessorComposite;
import com.aioveu.pay.aioveu01.service.WechatPay.service.WeChatPayService;
import com.aioveu.pay.aioveu01PayOrder.converter.PayOrderConverter;
import com.aioveu.pay.aioveu01PayOrder.mapper.PayOrderMapper;
import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu01PayOrder.model.query.PayOrderQuery;
import com.aioveu.pay.aioveu00Payment.Processor.BusinessProcessor;
import com.aioveu.pay.aioveu12MqProducerPayment.Publisher.PaymentEventPublisher;
import com.aioveu.pay.model.aioveu01PayOrder.vo.PayOrderVO;
import com.aioveu.pay.aioveu01PayOrder.service.PayOrderService;
import com.aioveu.pay.model.aioveuPayment.PaymentCallbackDTO;
import com.aioveu.pay.aioveu01PayOrder.utils.PayOrderNoGenerator;
import com.aioveu.common.enums.pay.PaymentCallbackStatusEnum;
import com.aioveu.pay.model.aioveu01PayOrder.form.PayOrderForm;
import com.aioveu.pay.model.aioveu01PayOrder.form.PayOrderCreateForm;
import com.aioveu.pay.model.aioveuPayment.PaymentStatusVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StringUtils;  // ✅ Spring 的 StringUtils
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: PayOrderServiceImpl
 * @Description TODO 支付订单服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 17:33
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements PayOrderService {

    private final PayOrderConverter payOrderConverter;


    private final PayOrderNoGenerator payOrderNoGenerator;

    private final OrderFeignClient orderFeignClient;
    // 创建 ObjectMapper 实例
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WeChatPayService wechatPayService;


    //Spring 会自动注入 唯一实现类（如果有多个再配合 @Qualifier）。
    @Resource
    private BusinessProcessorComposite businessProcessorComposite;

    /**
     * 获取支付订单分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PayOrderVO>} 支付订单分页列表
     */
    @Override
    public IPage<PayOrderVO> getPayOrderPage(PayOrderQuery queryParams) {
        Page<PayOrderVO> pageVO = this.baseMapper.getPayOrderPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取支付订单表单数据
     *
     * @param id 支付订单ID
     * @return 支付订单表单数据
     */
    @Override
    public PayOrderForm getPayOrderFormData(Long id) {
        PayOrder entity = this.getById(id);
        return payOrderConverter.toForm(entity);
    }

    /**
     * 新增支付订单
     *
     * @param formData 支付订单表单对象
     * @return 是否新增成功
     *
     * 正确写法（生产级 ✅）
     * 必须：用 orderNo做幂等
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePayOrder(PayOrderForm formData) {

        String orderNo = formData.getOrderNo();

        // 幂等校验（最关键）
        if (this.lambdaQuery()
                .eq(PayOrder::getOrderNo, orderNo)
                .exists()) {
            log.warn("支付订单已存在，orderNo={}", orderNo);
            return true;
        }

        // 生成支付单号
        formData.setPaymentNo(payOrderNoGenerator.generatePaymentNo());

        // 转换并保存
        PayOrder entity = payOrderConverter.toEntity(formData);
        return this.save(entity);
    }


    /**
     * 新增支付订单
     *
     * @param formData 支付订单表单对象
     * @return orderNo
     * 订单服务才能拿到 paymentNo
     * 正确写法（生产级 ✅）
     * 必须：用 orderNo做幂等
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createPayOrder(PayOrderCreateForm formData) {

        String orderSn = formData.getOrderSn();

        // 幂等校验（最关键）
        if (this.lambdaQuery()
                .eq(PayOrder::getOrderNo, orderSn)
                .exists()) {
            log.warn("支付订单已存在，orderSn={}", orderSn);
            return orderSn;
        }


        // 2️初始化支付单（Pay 主权）
        PayOrder entity = new PayOrder();
        // 生成支付单号
        entity.setPaymentNo(payOrderNoGenerator.generatePaymentNo());


        // 3️拷贝业务字段（OMS 主权）
        entity.setOrderNo(formData.getOrderSn());
        entity.setBizType(formData.getBizType());
        entity.setUserId(formData.getUserId());
        entity.setPaymentAmount(formData.getPaymentAmount());
        entity.setPaymentChannel(formData.getPaymentChannel()); // ✅ 不写死
        entity.setPaymentMethod(formData.getPaymentMethod());

        //Pay 在 PayOrderForm 中显式指定 bizType = ORDER
        // 4️ Pay 自己控制的字段

        entity.setPaymentStatus(PaymentStatusEnum.UNPAID);
        entity.setIsDeleted(0);
        entity.setNotifyStatus(0);
        entity.setNotifyCount(0);
        entity.setVersion(0);


        entity.setSubject("商品购买");
        entity.setBody("订单号：" + formData.getOrderSn());

        // 5️落库
        this.baseMapper.insert(entity);

        // 6️返回支付单号（不是 orderNo！）
        return entity.getPaymentNo();
    }


    /**
     * 更新支付订单
     *
     * @param id   支付订单ID
     * @param formData 支付订单表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePayOrder(Long id,PayOrderForm formData) {
        PayOrder entity = payOrderConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除支付订单
     *
     * @param ids 支付订单ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePayOrders(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的支付订单数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

    /**
     * 根据支付单号查询支付订单
     *
     * @param paymentNo 支付单号
     * @return PayOrder
     */
    @Override
    public PayOrder selectByPaymentNo(String paymentNo){

        PayOrder payOrder = this.getOne(
                new LambdaQueryWrapper<PayOrder>()
                        .eq(PayOrder::getPaymentNo, paymentNo)
        );

        log.info("根据支付单号查询支付订单:{}",payOrder);

        return payOrder;
    }

    /**
     *  更新订单状态
     *
     * @param order 支付单号,callback
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePayOrderStatusForCallBack(PayOrder order, PaymentCallbackDTO callback){

        PaymentCallbackStatusEnum status = callback.getStatus();
        // 设置状态
        if (status == PaymentCallbackStatusEnum.SUCCESS) {
            order.setPaymentStatus(PaymentStatusEnum.PAID);
            order.setPaymentTime(callback.getPaidTime());
            order.setThirdPaymentNo(callback.getThirdTransactionId());
            order.setErrorCode(null);
            order.setErrorMessage(null);
        } else {
            order.setPaymentStatus(PaymentStatusEnum.FAILED);
            order.setErrorCode(callback.getErrorCode());
            order.setErrorMessage(callback.getErrorMessage());
        }

        // 更新其他字段
//        order.setUpdateTime(new Date());
        order.setUpdateBy("system_callback");

        // 更新数据库

        // 记录状态变更日志
//        payOrderLogService.logStatusChange(order.getPaymentNo(),
//                PaymentStatusEnum.PENDING.getValue(),
//                order.getPaymentStatus(),
//                "回调通知");
        //update()
        return this.updateById(order);
    }


    /**
     *  处理业务逻辑
     *
     * @param order 支付单号,callback
     * @return Boolean
     */
    @Override
    public void handleBusinessLogic(PayOrder order, PaymentCallbackDTO callback) {

        if (!PaymentStatusEnum.PAID.equals(order.getPaymentStatus())) {
            return;  // 只有支付成功才处理业务
        }

        // 根据业务类型处理
        PaymentBizTypeEnum bizType = order.getBizType();
//        String bizId = order.getBizId();

        switch (bizType) {
            case ORDER_PAY:  // 订单支付
//                orderService.paySuccess(bizId, order);
                break;
            case RECHARGE:   // 充值
//                userBalanceService.recharge(bizId, order);
                break;
            case VIP_PAY:    // VIP购买
//                vipService.activateVip(bizId, order);
                break;
            default:
                log.warn("【业务处理】未知业务类型: {}", bizType);
        }

    }


    @Override
    public PayOrder getByPaymentNo(String paymentNo) {
        if (StringUtils.isEmpty(paymentNo)) {
            return null;
        }

        // 使用 MyBatis-Plus
        QueryWrapper<PayOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("payment_no", paymentNo);

        return this.baseMapper.selectOne(queryWrapper);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayOrderVO getPayOrderByOmsOrderSn(String orderSn) {

        if (StrUtil.isBlank(orderSn)) {
            return null;
        }

        PayOrder payOrder = this.baseMapper.selectOne(
                new LambdaQueryWrapper<PayOrder>()
                        .eq(PayOrder::getOrderNo, orderSn)
                        .last("limit 1")
        );
        // 转换并保存
        PayOrderVO payOrderVO = payOrderConverter.toVO(payOrder);

        // 1. 校验 outTradeNo
        if (payOrderVO == null) {
            throw new BusinessException("【PayOrder】payOrder不存在,orderSn:{}",orderSn);
        }

        // ✅ 永远用 orderNo 查
        return  payOrderVO;
    }


    /**
     * 根据omsOrderSn查询ThirdTransactionNo
     */
    @Override
    public String getThirdTransactionNoByOmsOrderSn(String omsOrderSn) {

        if (StrUtil.isBlank(omsOrderSn)) {
            return null;
        }

        //✅ 正确、健壮、可上线版本（推荐）
        return this.baseMapper.selectObjs(
                        new LambdaQueryWrapper<PayOrder>()
                                .eq(PayOrder::getOrderNo, omsOrderSn)
                                .select(PayOrder::getThirdTransactionNo)
                ).stream()
                .findFirst()
                .map(Object::toString)
                .orElse(null);
    }


    @Override
    @Transactional(readOnly = true)
    public PayOrder getByPaymentNoWithLock(String paymentNo) {
        if (StringUtils.isEmpty(paymentNo)) {
            return null;
        }

        // 使用 for update 锁
        return this.baseMapper.selectByPaymentNoWithLock(paymentNo);
    }

    @Override
    public boolean updatePaymentStatus(PayOrder payOrder, boolean success, Map<String, String> params) {
        if (payOrder == null) {
            log.error("更新支付状态失败：支付订单为空");
            return false;
        }

        try {
            // 1. 更新订单状态
            PaymentStatusEnum newStatus = success ? PaymentStatusEnum.PAID : PaymentStatusEnum.FAILED;
            payOrder.setPaymentStatus(newStatus);

            // 2. 更新支付完成时间
            if (success) {
                payOrder.setPaymentTime(LocalDateTime.now());
            }

            // 3. 设置微信支付相关信息
            if (params != null) {
                String transactionId = params.get("transaction_id");
                if (StringUtils.hasText(transactionId)) {
                    payOrder.setThirdTransactionNo(transactionId);  // ✅ 设置到 thirdTransactionNo
                }

                String outTradeNo = params.get("out_trade_no");
                if (StringUtils.hasText(outTradeNo)) {
                    // 确认 paymentNo 匹配
                    if (!outTradeNo.equals(payOrder.getPaymentNo())) {
                        log.warn("微信回调订单号不匹配: 回调={}, 数据库={}",
                                outTradeNo, payOrder.getPaymentNo());
                    }
                }

                // 3. 存储到 attachData
                Map<String, Object> attachData = parseAttachData(payOrder.getAttachData());

                // 添加微信回调参数
                attachData.put("wechat_transaction_id", transactionId);
                attachData.put("wechat_bank_type", params.get("bank_type"));
                attachData.put("wechat_openid", params.get("openid"));
                attachData.put("wechat_trade_type", params.get("trade_type"));
                attachData.put("wechat_cash_fee", params.get("cash_fee"));
                attachData.put("wechat_fee_type", params.get("fee_type"));
                attachData.put("wechat_settlement_total_fee", params.get("settlement_total_fee"));
                attachData.put("wechat_time_end", params.get("time_end"));
                attachData.put("wechat_result_code", params.get("result_code"));
                attachData.put("wechat_err_code", params.get("err_code"));
                attachData.put("wechat_err_code_des", params.get("err_code_des"));

                // 保存 attachData
                try {
                    payOrder.setAttachData(objectMapper.writeValueAsString(attachData));
                } catch (Exception e) {
                    log.warn("序列化附加数据失败", e);
                }

                // 设置支付完成时间（从微信回调）
                String timeEnd = params.get("time_end");
                if (StringUtils.hasText(timeEnd)) {
                    // 微信时间格式：20191201235959
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                    LocalDateTime paymentTime = LocalDateTime.parse(timeEnd, formatter);
                    payOrder.setPaymentTime(paymentTime);
                }
            }

            // 4. 更新版本号（乐观锁）  （推荐，用 MP 乐观锁注解）
//            if (payOrder.getVersion() != null) {
//                payOrder.setVersion(payOrder.getVersion() + 1);
//            }

            // 5. 执行更新
            int rows = this.baseMapper.updateById(payOrder);

            boolean updateSuccess = rows > 0;
            if (updateSuccess) {
                log.info("支付订单状态更新成功: paymentNo={}, 新状态={}",
                        payOrder.getPaymentNo(), newStatus);
            } else {
                log.error("支付订单状态更新失败: paymentNo={}, 影响行数={}",
                        payOrder.getPaymentNo(), rows);
            }

            return updateSuccess;

        } catch (Exception e) {
            log.error("更新支付订单状态异常: paymentNo={}",
                    payOrder.getPaymentNo(), e);
            return false;
        }
    }


    // 解析 attachData 的辅助方法
    private Map<String, Object> parseAttachData(String attachDataJson) {
        Map<String, Object> attachData = new HashMap<>();
        if (StringUtils.hasText(attachDataJson)) {
            try {
                attachData = objectMapper.readValue(attachDataJson,
                        new TypeReference<Map<String, Object>>() {});
            } catch (Exception e) {
                log.warn("解析附加数据失败: {}", attachDataJson, e);
            }
        }
        return attachData;
    }


    @Override
    public boolean updatePaymentStatus(String paymentNo, boolean success, Map<String, String> params) {
        if (StringUtils.isEmpty(paymentNo)) {
            return false;
        }

        // 先查询订单
        PayOrder payOrder = getByPaymentNo(paymentNo);
        if (payOrder == null) {
            log.error("更新支付状态失败：支付订单不存在: paymentNo={}", paymentNo);
            return false;
        }

        return updatePaymentStatus(payOrder, success, params);
    }


    /**
     * 将支付单从 UNPAID 推进到目标状态（如 PAYING）
     * 原子操作，并发安全
     */
    @Override
    public boolean updateStatusToTargetStatus(
            String paymentNo,
            PaymentStatusEnum fromStatus,
            PaymentStatusEnum targetStatus,
            Integer version
    ) {
        if (StringUtils.isEmpty(paymentNo)
                || fromStatus == null
                || targetStatus == null
                || version == null) {
            return false;
        }

        LambdaUpdateWrapper<PayOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                // 支付单号
                .eq(PayOrder::getPaymentNo, paymentNo)
                // 当前状态必须是 fromStatus
                .eq(PayOrder::getPaymentStatus, fromStatus)  //fromStatus 必须是“当前真实状态”，不能硬编码
                // ✅ 乐观锁
                .eq(PayOrder::getVersion, version)
                // 新状态
                .set(PayOrder::getPaymentStatus, targetStatus)
                // ✅ 状态变更时间
//                .set(PayOrder::getLastUpdateTime, LocalDateTime.now())
                // ✅ 乐观锁自增
                .setSql("version = version + 1");

        boolean updated = this.update(updateWrapper);

        if (!updated) {
            log.warn("支付单状态推进失败，可能已被其他线程处理，" +
                            "paymentNo={}, from={}, to={}",
                    paymentNo, fromStatus, targetStatus);
        }

        return updated;
    }


    /**
     * 前端轮询支付状态
     * ✅ 只查本地支付单
     * ✅ 必要时才调用微信
     * ✅ 不依赖订单状态反推支付状态
     */
    @Override
    public PaymentStatusVO queryPaymentStatusByPaymentNo(String paymentNo){

        PaymentStatusVO paymentStatusVO =new PaymentStatusVO();
        paymentStatusVO.setPaymentNo(paymentNo);
        // 1️查支付单（不是订单）
        PayOrder payOrder = this.selectByPaymentNo(paymentNo);
        if (payOrder == null) {
            paymentStatusVO.setErrorMessage("订单不存在");
            paymentStatusVO.setPaymentStatus(PaymentStatusEnum.UNKNOWN); // 特殊状态：订单不存在
            log.info("【前端调用：查询支付状态】订单不存在");
            return paymentStatusVO;
        }
        paymentStatusVO.setPaymentStatus(payOrder.getPaymentStatus());

        // 2. 如果订单已支付，直接返回
        if (paymentStatusVO.getPaymentStatus() == PaymentStatusEnum.PAID) {
            paymentStatusVO.setErrorMessage("订单已支付");
            return paymentStatusVO;
        }

        // 3️非终态，且距离上次查询超过 5 秒，才查微信
        if (needQueryWechat(payOrder)){

            try {
                //“回调说了算，查询只是确认，轮询只是安慰用户。”
                //只在“真的查了微信”时更新
                PaymentStatusVO wxResult = wechatPayService.queryPayment(paymentNo);
                log.info("【wechatPayService】微信支付状态返回结果wxResult:{}",wxResult);

                // 4️状态不一致才更新
                if (wxResult.getPaymentStatus() != paymentStatusVO.getPaymentStatus()) {
                    //同步支付状态 微信查询 → 视同“回调”

                    PayOrder update = new PayOrder();
                    update.setId(payOrder.getId());
                    update.setPaymentStatus(wxResult.getPaymentStatus());
                    update.setThirdTransactionNo(wxResult.getThirdPaymentNo());

                    if (wxResult.getPaymentStatus() == PaymentStatusEnum.PAID) {
                        update.setPaymentTime(wxResult.getPaymentTime()); // ✅
                    }

                    //微信查询 → 视同“回调”
                    this.updateById(update);
                     // ✅ 更新最后查询时间
                    this.updateLastQueryTime(paymentNo, LocalDateTime.now());

                    //✅ 只有“支付系统内部确认支付成功”时才发 MQ
                    //✅ MQ 是系统行为，不是用户行为触发的副作用

                    //✅ 轮询只负责“同步状态”
                    //✅ 不负责“推进业务”
                    //✅ 不负责“发 MQ”

                }
                paymentStatusVO.setPaymentStatus(wxResult.getPaymentStatus());
                paymentStatusVO.setErrorMessage("支付状态已同步");
            } catch (Exception e) {
                log.error("微信查询异常, paymentNo={}", paymentNo, e);
                paymentStatusVO.setErrorMessage("查询微信支付状态失败");
            }

        }

        return paymentStatusVO;
    }


    /*
    *
    * ✅ 前端轮询 2 秒一次
✅ 微信最多 5 秒查一次
✅ 微信不封你
✅ 用户体验不差
    * */
    private boolean needQueryWechat(PayOrder payOrder) {
        // 1. 已终态，不查
        if (PaymentStatusEnum.isTerminal(payOrder.getPaymentStatus())) {
            return false;
        }

        // 2. 从未查过，可以查
        if (payOrder.getLastQueryTime() == null) {
            return true;
        }

        // 3. 距上次查询超过 5 秒，才允许再查
        return payOrder.getLastQueryTime()
                .plusSeconds(5)
                .isBefore(LocalDateTime.now());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLocalPayAndOrderStatus(PayOrder payOrder, PaymentStatusVO wxResult) {

        // 1. 更新支付单
        boolean result = this.updateStatusByPaymentNo(
                payOrder.getPaymentNo(),
                wxResult.getPaymentStatus()
        );

//        // 2. 只更新订单支付状态，不改订单主状态
//        orderFeignClient.updatePaymentStatus(
//                payOrder.getOrderSn(),
//                wxResult.getPaymentStatus()
//        );

        return result;
    }


    /*
    * 更新最后微信查询时间
    * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLastQueryTime(String paymentNo, LocalDateTime lastQueryTime){

        LambdaUpdateWrapper<PayOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(PayOrder::getPaymentNo, paymentNo)
                .set(PayOrder::getLastQueryTime, lastQueryTime);

        boolean updated = this.update(updateWrapper);

        if (!updated) {
            log.warn("更新最后微信查询时间失败,paymentNo={}", paymentNo);
            throw new BizException("订单已在支付中或已完成");
        }
        return updated;

    }


    /**
     * 更新支付状态（幂等、安全）
     *
     * @param paymentNo    支付流水号
     * @param targetStatus 目标状态
     * @return true=更新成功，false=无需更新
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatusByPaymentNo(String paymentNo, PaymentStatusEnum targetStatus) {

        PayOrder payOrder = this.selectByPaymentNo(paymentNo);
        if (payOrder == null) {
            log.warn("支付单不存在, paymentNo={}", paymentNo);
            return false;
        }

        // ✅ 终态保护
        if (PaymentStatusEnum.isTerminal(payOrder.getPaymentStatus())) {

            log.info("支付单已是终态，跳过更新, paymentNo={}, currentStatus={}",
                    paymentNo, payOrder.getPaymentStatus());
            return false;
        }

        // ✅ 状态未变化
        if (payOrder.getPaymentStatus() == targetStatus) {
            return false;
        }

        boolean success = this.updateStatusToTargetStatus(
                paymentNo,
                payOrder.getPaymentStatus(), // ✅ 用当前状态  //fromStatus 必须是“当前真实状态”，不能硬编码
                targetStatus,
                payOrder.getVersion()
        );

        if (!success) {
            // ✅ 不抛异常，交给上层决定
            throw new BizException("订单已在支付中或已完成");
        }

        log.info("支付状态更新成功, paymentNo={}, {}→{}",
                paymentNo,
                payOrder.getPaymentStatus(),
                targetStatus.getCode());

        return true;
    }


}
