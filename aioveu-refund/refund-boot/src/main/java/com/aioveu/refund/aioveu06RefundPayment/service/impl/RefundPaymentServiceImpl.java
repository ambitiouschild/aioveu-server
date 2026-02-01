package com.aioveu.refund.aioveu06RefundPayment.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.refund.aioveu06RefundPayment.converter.RefundPaymentConverter;
import com.aioveu.refund.aioveu06RefundPayment.mapper.RefundPaymentMapper;
import com.aioveu.refund.aioveu06RefundPayment.model.entity.RefundPayment;
import com.aioveu.refund.aioveu06RefundPayment.model.form.RefundPaymentForm;
import com.aioveu.refund.aioveu06RefundPayment.model.query.RefundPaymentQuery;
import com.aioveu.refund.aioveu06RefundPayment.model.vo.RefundPaymentVO;
import com.aioveu.refund.aioveu06RefundPayment.service.RefundPaymentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: RefundPaymentServiceImpl
 * @Description TODO  退款支付记录服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:31
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class RefundPaymentServiceImpl extends ServiceImpl<RefundPaymentMapper, RefundPayment> implements RefundPaymentService {

    private final RefundPaymentConverter refundPaymentConverter;

    /**
     * 获取退款支付记录分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<RefundPaymentVO>} 退款支付记录分页列表
     */
    @Override
    public IPage<RefundPaymentVO> getRefundPaymentPage(RefundPaymentQuery queryParams) {
        Page<RefundPaymentVO> pageVO = this.baseMapper.getRefundPaymentPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取退款支付记录表单数据
     *
     * @param id 退款支付记录ID
     * @return 退款支付记录表单数据
     */
    @Override
    public RefundPaymentForm getRefundPaymentFormData(Long id) {
        RefundPayment entity = this.getById(id);
        return refundPaymentConverter.toForm(entity);
    }

    /**
     * 新增退款支付记录
     *
     * @param formData 退款支付记录表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveRefundPayment(RefundPaymentForm formData) {
        RefundPayment entity = refundPaymentConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新退款支付记录
     *
     * @param id   退款支付记录ID
     * @param formData 退款支付记录表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateRefundPayment(Long id,RefundPaymentForm formData) {
        RefundPayment entity = refundPaymentConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除退款支付记录
     *
     * @param ids 退款支付记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteRefundPayments(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的退款支付记录数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
