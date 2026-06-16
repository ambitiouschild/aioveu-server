package com.aioveu.pay.aioveu06PayFlow.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu06PayFlow.converter.PayFlowConverter;
import com.aioveu.pay.aioveu06PayFlow.enums.FlowDirectionEnum;
import com.aioveu.pay.aioveu06PayFlow.enums.FlowStatusEnum;
import com.aioveu.pay.aioveu06PayFlow.enums.FlowTypeEnum;
import com.aioveu.pay.aioveu06PayFlow.mapper.PayFlowMapper;
import com.aioveu.pay.aioveu06PayFlow.model.entity.PayFlow;
import com.aioveu.pay.aioveu06PayFlow.model.form.PayFlowForm;
import com.aioveu.pay.aioveu06PayFlow.model.query.PayFlowQuery;
import com.aioveu.pay.aioveu06PayFlow.model.vo.PayFlowVO;
import com.aioveu.pay.aioveu06PayFlow.service.PayFlowService;
import com.aioveu.pay.aioveu06PayFlow.utils.PayFlowNoGenerator;
import com.aioveu.pay.model.aioveuPayment.PaymentCallbackDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: PayFlowServiceImpl
 * @Description TODO 支付流水服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/9 15:57
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class PayFlowServiceImpl extends ServiceImpl<PayFlowMapper, PayFlow> implements PayFlowService {

    private final PayFlowConverter payFlowConverter;

    /**
     * 获取支付流水分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PayFlowVO>} 支付流水分页列表
     */
    @Override
    public IPage<PayFlowVO> getPayFlowPage(PayFlowQuery queryParams) {
        Page<PayFlowVO> pageVO = this.baseMapper.getPayFlowPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取支付流水表单数据
     *
     * @param id 支付流水ID
     * @return 支付流水表单数据
     */
    @Override
    public PayFlowForm getPayFlowFormData(Long id) {
        PayFlow entity = this.getById(id);
        return payFlowConverter.toForm(entity);
    }

    /**
     * 新增支付流水
     *
     * @param formData 支付流水表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePayFlow(PayFlowForm formData) {
        PayFlow entity = payFlowConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新支付流水
     *
     * @param id   支付流水ID
     * @param formData 支付流水表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePayFlow(Long id,PayFlowForm formData) {
        PayFlow entity = payFlowConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除支付流水
     *
     * @param ids 支付流水ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePayFlows(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的支付流水数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }


    /**
     * 根据回调记录支付流水
     *
     * @param payOrder 支付流水ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public void recordPaymentFlow(PayOrder payOrder, PaymentCallbackDTO callback) {

        Long memberId = payOrder.getUserId();
        LocalDateTime now = LocalDateTime.now();


        //✅ 防止重复流水（兜底）
        boolean exists = this.existsByPaymentNoAndFlowType(
                payOrder.getPaymentNo(),
                FlowTypeEnum.PAYMENT.name()
        );

        if (exists) {
            log.warn("支付流水已存在，跳过: paymentNo:{}", payOrder.getPaymentNo());
            return;
        }

        PayFlow flow = PayFlow.builder()
                .flowNo(PayFlowNoGenerator.generatePayFlowNo(memberId))
                .paymentNo(payOrder.getPaymentNo())
                .orderNo(payOrder.getOrderNo())
                .userId(payOrder.getUserId())
                .flowType(FlowTypeEnum.PAYMENT)
                .flowDirection(FlowDirectionEnum.IN) // 支付是入金
                .amount(callback.getPaidAmount())
                .balanceBefore(null)   // ✅ 不使用
                .balanceAfter(null)    // ✅ 不使用
                .channelCode(callback.getChannel())
                .thirdFlowNo(callback.getThirdTransactionId())
                .flowStatus(FlowStatusEnum.SUCCESS)
                .tradeTime(callback.getPaidTime())
                .completeTime(now)
//                .callbackData(callback.getRawData())
                .errorCode(callback.getErrorCode())
                .errorMessage(callback.getErrorMessage())
                .remark("支付回调成功")
                .isDeleted(0)
                .createBy("system")
                .updateBy("system")
                .build();

        this.save(flow);
    }

    public boolean existsByPaymentNoAndFlowType(String paymentNo, String flowType) {
        return this.count(
                Wrappers.<PayFlow>lambdaQuery()
                        .eq(PayFlow::getPaymentNo, paymentNo)
                        .eq(PayFlow::getFlowType, flowType)
                        .eq(PayFlow::getIsDeleted, 0)
        ) > 0;
    }
    /**
     * 构建退款流水
     */
    private PayFlow buildRefundFlow(
            PayOrder payOrder,
            PaymentCallbackDTO callback,
            BigDecimal refundAmount
    ) {
        LocalDateTime now = LocalDateTime.now();

        return PayFlow.builder()
//                .tenantId(payOrder.getTenantId())
//                .flowNo(PayFlowNoGenerator.generateRefundFlowNo(payOrder.getUserId()))
                .paymentNo(payOrder.getPaymentNo())
//                .refundNo(callback.getRefundNo()) // ✅ 用你现有字段
                .orderNo(payOrder.getOrderNo())
                .userId(payOrder.getUserId())
                .flowType(FlowTypeEnum.REFUND)
                .flowDirection(FlowDirectionEnum.OUT) // 退款是出金
                .amount(refundAmount)
                .balanceBefore(null)
                .balanceAfter(null)
                .channelCode(callback.getChannel())
                .thirdFlowNo(callback.getThirdTransactionId())
                .flowStatus(FlowStatusEnum.FAILED)
                .tradeTime(callback.getPaidTime())
                .completeTime(now)
//                .callbackData(callback.getRawData())
                .remark("退款回调成功")
                .isDeleted(0)
                .createBy("system")
                .updateBy("system")
                .build();
    }

}
