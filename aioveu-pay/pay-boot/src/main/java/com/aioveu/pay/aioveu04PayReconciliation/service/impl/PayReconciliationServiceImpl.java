package com.aioveu.pay.aioveu04PayReconciliation.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.exception.BusinessException;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu04PayReconciliation.converter.PayReconciliationConverter;
import com.aioveu.pay.aioveu04PayReconciliation.enums.PayReconciliationStatusEnum;
import com.aioveu.pay.aioveu04PayReconciliation.mapper.PayReconciliationMapper;
import com.aioveu.pay.aioveu04PayReconciliation.model.entity.PayReconciliation;
import com.aioveu.pay.aioveu04PayReconciliation.model.form.PayReconciliationForm;
import com.aioveu.pay.aioveu04PayReconciliation.model.query.PayReconciliationQuery;
import com.aioveu.pay.aioveu04PayReconciliation.model.vo.PayReconciliationVO;
import com.aioveu.pay.aioveu04PayReconciliation.service.PayReconciliationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: PayReconciliationServiceImpl
 * @Description TODO 支付对账服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 20:41
 * @Version 1.0
 **/

@Service
@RequiredArgsConstructor
public class PayReconciliationServiceImpl extends ServiceImpl<PayReconciliationMapper, PayReconciliation> implements PayReconciliationService {


    private final PayReconciliationConverter payReconciliationConverter;

    /**
     * 获取支付对账分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PayReconciliationVO>} 支付对账分页列表
     */
    @Override
    public IPage<PayReconciliationVO> getPayReconciliationPage(PayReconciliationQuery queryParams) {
        Page<PayReconciliationVO> pageVO = this.baseMapper.getPayReconciliationPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取支付对账表单数据
     *
     * @param id 支付对账ID
     * @return 支付对账表单数据
     */
    @Override
    public PayReconciliationForm getPayReconciliationFormData(Long id) {
        PayReconciliation entity = this.getById(id);
        return payReconciliationConverter.toForm(entity);
    }

    /**
     * 新增支付对账
     *
     * @param formData 支付对账表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePayReconciliation(PayReconciliationForm formData) {
        PayReconciliation entity = payReconciliationConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新支付对账
     *
     * @param id   支付对账ID
     * @param formData 支付对账表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePayReconciliation(Long id,PayReconciliationForm formData) {
        PayReconciliation entity = payReconciliationConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除支付对账
     *
     * @param ids 支付对账ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePayReconciliations(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的支付对账数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

//    @Override
//    public PayReconciliation selectByNo(String reconciliationNo) {
//
//        PayReconciliation  entity = this.getById(reconciliationNo);
//
//        return entity;
//
//    }


    /**
     * 执行对账
     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Result<Void> executeReconciliation(String reconciliationNo) {
//        // 1. 查询对账记录
//        PayReconciliation reconciliation = this.selectByNo(reconciliationNo);
//        if (reconciliation == null) {
//            throw new BusinessException("对账单不存在");
//        }
//
//        if (reconciliation.getReconcileStatus() != PayReconciliationStatusEnum.NOT_RECONCILED.getValue()) {
//            throw new BusinessException("对账单状态不正确");
//        }
//
//        // 2. 更新为对账中
//        reconciliation.setReconcileStatus(PayReconciliationStatusEnum.RECONCILING.getValue());
//        this.updateById(reconciliation);
//
//        try {
//            // 3. 获取系统订单数据
//            List<SystemOrder> systemOrders = getSystemOrders(reconciliation);
//
//            // 4. 获取渠道账单数据
//            List<ChannelOrder> channelOrders = getChannelOrders(reconciliation);
//
//            // 5. 执行对账比对
//            ReconciliationResult result = compareOrders(systemOrders, channelOrders);
//
//            // 6. 保存对账结果
//            saveReconciliationResult(reconciliation, result);
//
//            // 7. 更新对账单状态
//            reconciliation.setReconcileStatus(ReconciliationStatus.COMPLETED.getCode());
//            reconciliation.setReconcileTime(new Date());
//            reconciliation.setDifferenceCount(result.getDifferenceCount());
//            reconciliationMapper.updateById(reconciliation);
//
//            // 8. 生成对账报告
//            generateReconciliationReport(reconciliation, result);

//            return Result.success();
//
//        } catch (Exception e) {
//            log.error("对账执行异常", e);
////            reconciliation.setReconcileStatus(ReconciliationStatus.EXCEPTION.getCode());
////            reconciliation.setErrorMessage(e.getMessage());
////            reconciliationMapper.updateById(reconciliation);
//            throw new BusinessException("对账执行失败");
//        }
//    }

//    /**
//     * 比对订单数据
//     */
//    private ReconciliationResult compareOrders(List<SystemOrder> systemOrders,
//                                               List<ChannelOrder> channelOrders) {
//        ReconciliationResult result = new ReconciliationResult();
//
//        // 系统订单转Map
//        Map<String, SystemOrder> systemMap = systemOrders.stream()
//                .collect(Collectors.toMap(
//                        SystemOrder::getThirdTransactionNo,
//                        order -> order,
//                        (o1, o2) -> o1
//                ));
//
//        // 渠道订单转Map
//        Map<String, ChannelOrder> channelMap = channelOrders.stream()
//                .collect(Collectors.toMap(
//                        ChannelOrder::getTransactionNo,
//                        order -> order,
//                        (o1, o2) -> o1
//                ));
//
//        // 比对逻辑
//        for (SystemOrder systemOrder : systemOrders) {
//            String key = systemOrder.getThirdTransactionNo();
//            ChannelOrder channelOrder = channelMap.get(key);
//
//            if (channelOrder == null) {
//                // 系统有，渠道无
//                handleSystemMore(systemOrder, result);
//            } else if (!matchOrder(systemOrder, channelOrder)) {
//                // 金额不一致
//                handleDifference(systemOrder, channelOrder, result);
//            } else {
//                // 匹配成功
//                handleMatch(systemOrder, channelOrder, result);
//            }
//
//            channelMap.remove(key);
//        }
//
//        // 渠道有，系统无
//        for (ChannelOrder channelOrder : channelMap.values()) {
//            handleChannelMore(channelOrder, result);
//        }
//
//        return result;
//    }




}
