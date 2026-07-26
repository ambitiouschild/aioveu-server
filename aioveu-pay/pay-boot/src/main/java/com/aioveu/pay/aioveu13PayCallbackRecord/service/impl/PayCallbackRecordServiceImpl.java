package com.aioveu.pay.aioveu13PayCallbackRecord.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.enums.pay.PaymentCallbackStatusEnum;
import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu01PayOrder.service.PayOrderService;
import com.aioveu.pay.aioveu13PayCallbackRecord.converter.PayCallbackRecordConverter;
import com.aioveu.pay.aioveu13PayCallbackRecord.mapper.PayCallbackRecordMapper;
import com.aioveu.pay.aioveu13PayCallbackRecord.model.entity.PayCallbackRecord;
import com.aioveu.pay.aioveu13PayCallbackRecord.model.form.PayCallbackRecordForm;
import com.aioveu.pay.aioveu13PayCallbackRecord.model.query.PayCallbackRecordQuery;
import com.aioveu.pay.aioveu13PayCallbackRecord.model.vo.PayCallbackRecordVo;
import com.aioveu.pay.aioveu13PayCallbackRecord.service.PayCallbackRecordService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: PayCallbackRecordServiceImpl
 * @Description TODO 支付回调记录服务实现类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/29 18:13
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class PayCallbackRecordServiceImpl extends ServiceImpl<PayCallbackRecordMapper, PayCallbackRecord> implements PayCallbackRecordService {

    private final PayCallbackRecordConverter payCallbackRecordConverter;

    private final PayOrderService payOrderService;

    /**
     * 获取支付回调记录分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PayCallbackRecordVo>} 支付回调记录分页列表
     */
    @Override
    public IPage<PayCallbackRecordVo> getPayCallbackRecordPage(PayCallbackRecordQuery queryParams) {
        Page<PayCallbackRecordVo> page = this.baseMapper.getPayCallbackRecordPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取支付回调记录表单数据
     *
     * @param id 支付回调记录ID
     * @return 支付回调记录表单数据
     */
    @Override
    public PayCallbackRecordForm getPayCallbackRecordFormData(Long id) {
        PayCallbackRecord entity = this.getById(id);
        return payCallbackRecordConverter.toForm(entity);
    }

    /**
     * 新增支付回调记录
     *
     * @param formData 支付回调记录表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePayCallbackRecord(PayCallbackRecordForm formData) {
        PayCallbackRecord entity = payCallbackRecordConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新支付回调记录
     *
     * @param id   支付回调记录ID
     * @param formData 支付回调记录表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePayCallbackRecord(Long id,PayCallbackRecordForm formData) {
        PayCallbackRecord entity = payCallbackRecordConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除支付回调记录
     *
     * @param ids 支付回调记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePayCallbackRecords(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的支付回调记录数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

    /**
     * 是否已处理过该回调（幂等判断）
     */
    @Override
    public boolean isConsumed(String transactionId) {
        if (StringUtils.isBlank(transactionId)) {
            return false;
        }

        Long count = this.baseMapper.countByTransactionId(transactionId);
        return count != null && count > 0;
    }

    /**
     * 记录回调（含幂等）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markConsumed(String transactionId, String paymentNo, String orderNo, Map<String, String> params) {

        PayCallbackRecord record = new PayCallbackRecord();
        // 2️核心字段
        record.setTransactionId(transactionId);
        record.setPaymentNo(paymentNo);
        record.setOrderNo(orderNo);
        record.setChannel("WECHAT");

        // 3️金额（✅ 必须从回调取）
        BigDecimal paidAmount = parsePaidAmount(params);
        record.setPaidAmount(paidAmount);

        // 4️商户 / 应用
        record.setMchId(params.get("mch_id"));
        record.setAppId(params.get("appid"));

        // 5️状态 & 次数
        record.setNotifyStatus(PaymentCallbackStatusEnum.SUCCESS);
//        record.setNotifyCount(1); // ❌ 删掉 第一次插入时默认就是 1（数据库默认值） 后续只用 incrNotifyCount
        record.setLastNotifyTime(LocalDateTime.now());

        // 6️原始数据（✅ 必须有）
        record.setRawData(JSON.toJSONString(params));

        // 7️租户（✅ 从上下文取）
//        record.setTenantId(TenantContext.getTenantId());

        save(record);

    }

    private BigDecimal parsePaidAmount(Map<String, String> params) {
        try {
            String totalFee = params.get("total_fee");
            if (totalFee == null) {
                return BigDecimal.ZERO;
            }
            return new BigDecimal(totalFee)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            log.error("解析回调金额失败", e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markFailed(
            String transactionId,
            String paymentNo,
            String orderNo,
            Map<String, String> params,
            String errorMsg) {

        PayCallbackRecord record = new PayCallbackRecord();

        record.setTransactionId(transactionId);
        record.setPaymentNo(paymentNo);
        record.setOrderNo(orderNo);
        record.setChannel("WECHAT");
        record.setPaidAmount(parsePaidAmount(params));
        record.setMchId(params.get("mch_id"));
        record.setAppId(params.get("appid"));
        record.setNotifyStatus(PaymentCallbackStatusEnum.FAILED);          // ✅ 失败
        record.setNotifyCount(1);
        record.setLastNotifyTime(LocalDateTime.now());
        record.setErrorMsg(errorMsg);        // ✅ 必须有
        record.setRawData(JSON.toJSONString(params));
//        record.setTenantId(TenantContext.getTenantId());

        save(record);
    }

    @Override
    public void incrNotifyCount(String transactionId) {
        lambdaUpdate()
                .eq(PayCallbackRecord::getTransactionId, transactionId)
                .setSql("notify_count = notify_count + 1")
                .update();
    }


    /*
     * 根据transactionId查找支付回调记录
     * */
    @Override
    public PayCallbackRecord getByTransactionId(String transactionId) {

        if (StringUtils.isBlank(transactionId)) {
            return null;
        }

        return lambdaQuery()
                .eq(PayCallbackRecord::getTransactionId, transactionId)
//                .eq(PayCallbackRecord::getTenantId, tenantId)
                .eq(PayCallbackRecord::getIsDeleted, 0)
                .last("LIMIT 1")
                .one();
    }


    /*
     * 保存回调记录
     * */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    //这样即使后面的流水/MQ 挂了，回调记录已经提交了，微信再来时 isConsumed能查到。
    public void saveCallbackRecord(String paymentNo, String transactionId, LocalDateTime paidTime, Object rawParams, String source) {

        PayOrder payOrder = payOrderService.getByPaymentNo(paymentNo);
        if (payOrder == null) return;

        // 幂等：如果已经有一条相同 transactionId + source 的记录，跳过
        if (StringUtils.isNotBlank(transactionId)
                && this.isConsumed(transactionId)) {
            log.info("【回调记录】已存在，跳过: transactionId={}", transactionId);
            this.incrNotifyCount(transactionId); // 累计次数
            return;
        }

        // 2. 不存在就插入（REQUIRES_NEW）
        PayCallbackRecord record = new PayCallbackRecord();
        record.setPaymentNo(paymentNo);
        record.setOrderNo(payOrder.getOrderNo());
        record.setTransactionId(transactionId);
        record.setChannel(source.contains("WECHAT") ? "WECHAT" : "POLLING");
        record.setNotifyStatus(PaymentCallbackStatusEnum.SUCCESS);
        record.setRawData(rawParams != null ? JSON.toJSONString(rawParams) : null);
        record.setLastNotifyTime(paidTime);
        record.setNotifyCount(1);


        this.save(record);
        log.info("【回调记录】已保存: paymentNo={}, source={}", paymentNo, source);
    }
}
