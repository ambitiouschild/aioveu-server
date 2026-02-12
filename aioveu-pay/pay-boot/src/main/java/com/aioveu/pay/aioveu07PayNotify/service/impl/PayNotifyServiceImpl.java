package com.aioveu.pay.aioveu07PayNotify.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu07PayNotify.converter.PayNotifyConverter;
import com.aioveu.pay.aioveu07PayNotify.mapper.PayNotifyMapper;
import com.aioveu.pay.aioveu07PayNotify.model.entity.PayNotify;
import com.aioveu.pay.aioveu07PayNotify.model.form.PayNotifyForm;
import com.aioveu.pay.aioveu07PayNotify.model.query.PayNotifyQuery;
import com.aioveu.pay.aioveu07PayNotify.model.vo.PayNotifyDTO;
import com.aioveu.pay.aioveu07PayNotify.model.vo.PayNotifyVO;
import com.aioveu.pay.aioveu07PayNotify.service.PayNotifyService;
import com.aioveu.pay.aioveuModule.model.vo.PaymentCallbackDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyV3Result;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: PayNotifyServiceImpl
 * @Description TODO 支付通知服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:00
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class PayNotifyServiceImpl extends ServiceImpl<PayNotifyMapper, PayNotify> implements PayNotifyService {

    private final PayNotifyConverter payNotifyConverter;

    /**
     * 获取支付通知分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PayNotifyVO>} 支付通知分页列表
     */
    @Override
    public IPage<PayNotifyVO> getPayNotifyPage(PayNotifyQuery queryParams) {
        Page<PayNotifyVO> pageVO = this.baseMapper.getPayNotifyPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取支付通知表单数据
     *
     * @param id 支付通知ID
     * @return 支付通知表单数据
     */
    @Override
    public PayNotifyForm getPayNotifyFormData(Long id) {
        PayNotify entity = this.getById(id);
        return payNotifyConverter.toForm(entity);
    }

    /**
     * 新增支付通知
     *
     * @param formData 支付通知表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePayNotify(PayNotifyForm formData) {
        PayNotify entity = payNotifyConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新支付通知
     *
     * @param id   支付通知ID
     * @param formData 支付通知表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePayNotify(Long id,PayNotifyForm formData) {
        PayNotify entity = payNotifyConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除支付通知
     *
     * @param ids 支付通知ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePayNotifys(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的支付通知数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

    /**
     * 发送异步通知
     *
     * @param order
     * @return Result
     */
    @Override
    public Result<Void> sendPaymentNotify(PayOrder order, PaymentCallbackDTO callback) {
        // 构建通知DTO
        PayNotifyDTO notifyDTO = PayNotifyDTO.builder()
                .paymentNo(order.getPaymentNo())
//                .orderNo(order.getOrderNo())
//                .bizType(order.getBizType())
//                .bizId(order.getBizId())
//                .channel(order.getPaymentChannel())
//                .amount(order.getAmount())
//                .status(order.getPaymentStatus())
//                .payTime(order.getPaymentTime())
//                .thirdPaymentNo(order.getThirdPaymentNo())
//                .attach(order.getAttach())
                .build();

        // 发送MQ消息
        Message<PayNotifyDTO> message = MessageBuilder.withPayload(notifyDTO)
                .setHeader("retry_count", 0)
                .setHeader("max_retry", 3)
                .build();

//        // 发送到消息队列
//        mqTemplate.send("pay-notify-topic", message);
//
//        // 或者调用HTTP通知
//        payNotifyService.sendPaymentNotify(notifyDTO);

        return Result.success();
    }

    /**
     * 发送异步通知
     */
//    @Override
//    public Result<Void> sendNotify(PayNotifyDTO notifyDTO) {
//        PayNotify notify = buildPayNotify(notifyDTO);
//        notifyMapper.insert(notify);
//
//        // 异步发送通知
//        notifyExecutor.execute(() -> {
//            executeNotify(notify);
//        });
//
//        return Result.success();
//    }

//    /**
//     * 执行通知
//     */
//    private void executeNotify(PayNotify notify) {
//        int retryCount = 0;
//        boolean success = false;
//
//        while (retryCount < notify.getMaxNotifyCount() && !success) {
//            try {
//                // 发送HTTP请求
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_JSON);
//
//                HttpEntity<String> request = new HttpEntity<>(notify.getRequestData(), headers);
//                ResponseEntity<String> response = restTemplate.postForEntity(
//                        notify.getNotifyUrl(),
//                        request,
//                        String.class
//                );
//
//                // 处理响应
//                if (response.getStatusCode().is2xxSuccessful()) {
//                    String responseBody = response.getBody();
//                    if (isSuccessResponse(responseBody)) {
//                        success = true;
//                        notify.setNotifyStatus(NotifyStatus.SUCCESS.getCode());
//                        notify.setSuccessTime(new Date());
//                        notify.setResponseData(responseBody);
//                    }
//                }
//
//            } catch (Exception e) {
//                log.error("通知发送失败: notifyNo={}, retryCount={}",
//                        notify.getNotifyNo(), retryCount, e);
//            }
//
//            if (!success) {
//                retryCount++;
//                notify.setNotifyCount(retryCount);
//                notify.setLastNotifyTime(new Date());
//                notify.setNextNotifyTime(calculateNextNotifyTime(retryCount));
//                notify.setErrorMessage("通知失败，重试次数: " + retryCount);
//
//                // 等待后重试
//                if (retryCount < notify.getMaxNotifyCount()) {
//                    try {
//                        Thread.sleep(getRetryInterval(retryCount));
//                    } catch (InterruptedException ie) {
//                        Thread.currentThread().interrupt();
//                        break;
//                    }
//                }
//            }
//
//            notifyMapper.updateById(notify);
//        }
//
//        if (!success) {
//            notify.setNotifyStatus(NotifyStatus.FAILED.getCode());
//            notify.setErrorMessage("达到最大重试次数，通知失败");
//            notifyMapper.updateById(notify);
//        }
//    }

//    /**
//     * 重试失败通知
//     */
//    @Override
//    @Scheduled(fixedDelay = 300000) // 每5分钟执行一次
//    public void retryFailedNotify() {
//        List<PayNotify> failedNotifies = notifyMapper.selectFailedNotifies();
//
//        for (PayNotify notify : failedNotifies) {
//            if (notify.getNextNotifyTime().before(new Date())) {
//                notifyExecutor.execute(() -> {
//                    executeNotify(notify);
//                });
//            }
//        }
//    }


}
