package com.aioveu.refund.aioveu01RefundOrder.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.refund.aioveu01RefundOrder.converter.RefundOrderConverter;
import com.aioveu.refund.aioveu01RefundOrder.enums.RefundStatusEnum;
import com.aioveu.refund.aioveu01RefundOrder.mapper.RefundOrderMapper;
import com.aioveu.refund.aioveu01RefundOrder.model.entity.RefundOrder;
import com.aioveu.refund.aioveu01RefundOrder.model.form.RefundApplyFormDTO;
import com.aioveu.refund.aioveu01RefundOrder.model.form.RefundOrderForm;
import com.aioveu.refund.aioveu01RefundOrder.model.query.RefundOrderQuery;
import com.aioveu.refund.aioveu01RefundOrder.model.vo.RefundOrderVO;
import com.aioveu.refund.aioveu01RefundOrder.service.RefundOrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.aioveu.refund.aioveu01RefundOrder.utils.RefundNoGenerator.generateRefundNo;

/**
 * @ClassName: RefundOrderServiceImpl
 * @Description TODO 订单退款申请服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 16:31
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class RefundOrderServiceImpl extends ServiceImpl<RefundOrderMapper, RefundOrder> implements RefundOrderService {

    private final RefundOrderConverter refundOrderConverter;

    /**
     * 获取订单退款申请分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<RefundOrderVO>} 订单退款申请分页列表
     */
    @Override
    public IPage<RefundOrderVO> getRefundOrderPage(RefundOrderQuery queryParams) {
        Page<RefundOrderVO> pageVO = this.baseMapper.getRefundOrderPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取订单退款申请表单数据
     *
     * @param id 订单退款申请ID
     * @return 订单退款申请表单数据
     */
    @Override
    public RefundOrderForm getRefundOrderFormData(Long id) {
        RefundOrder entity = this.getById(id);
        return refundOrderConverter.toForm(entity);
    }

    /**
     * 获取订单退款申请表单数据
     *
     * @param refundSn 退款单号
     * @return 订单退款申请表单数据
     */
    @Override
    public RefundOrder getRefundOrderEntityByRefundSn(String refundSn) {
        RefundOrder refundOrder = this.getOne(
                new LambdaQueryWrapper<RefundOrder>()
                        .eq(RefundOrder::getRefundSn, refundSn)
        );

        log.info("获取订单退款申请表单数据:{}",refundOrder);

        return refundOrder;
    }


    /**
     * 新增订单退款申请
     *
     * @param formData 订单退款申请表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveRefundOrder(RefundOrderForm formData) {
        RefundOrder entity = refundOrderConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 新增订单退款申请
     *
     * @param formData 订单退款申请表单对象
     * @return 订单退款申请实体
     */
    @Override
    public RefundOrder createRefundOrder(RefundApplyFormDTO formData) {

        Long memberId =  SecurityUtils.getMemberId();

        RefundOrder order = new RefundOrder();
        order.setOrderSn(formData.getOrderSn());
        order.setRefundSn(generateRefundNo(memberId)); // 生成退款单号
        order.setUserId(formData.getUserId());
        order.setRefundType(formData.getRefundType()); // 退款类型：1-仅退款 2-退货退款
        order.setRefundReason(formData.getRefundReason());
        order.setRefundAmount(formData.getRefundAmount());
        order.setStatus(RefundStatusEnum.PENDING_AUDIT.getValue()); // 待审核
        order.setHandleNote(formData.getHandleNote());

        this.save(order);

        return order;
    }

    /**
     * 更新订单退款申请
     *
     * @param id   订单退款申请ID
     * @param formData 订单退款申请表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateRefundOrder(Long id,RefundOrderForm formData) {
        RefundOrder entity = refundOrderConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除订单退款申请
     *
     * @param ids 订单退款申请ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteRefundOrders(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的订单退款申请数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
