package com.aioveu.refund.aioveu03RefundDelivery.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.refund.aioveu03RefundDelivery.converter.RefundDeliveryConverter;
import com.aioveu.refund.aioveu03RefundDelivery.mapper.RefundDeliveryMapper;
import com.aioveu.refund.aioveu03RefundDelivery.model.entity.RefundDelivery;
import com.aioveu.refund.aioveu03RefundDelivery.model.form.RefundDeliveryForm;
import com.aioveu.refund.aioveu03RefundDelivery.model.query.RefundDeliveryQuery;
import com.aioveu.refund.aioveu03RefundDelivery.model.vo.RefundDeliveryVO;
import com.aioveu.refund.aioveu03RefundDelivery.service.RefundDeliveryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: RefundDeliveryServiceImpl
 * @Description TODO 退款物流信息（用于退货）服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 18:06
 * @Version 1.0
 **/

@Service
@RequiredArgsConstructor
public class RefundDeliveryServiceImpl extends ServiceImpl<RefundDeliveryMapper, RefundDelivery> implements RefundDeliveryService {

    private final RefundDeliveryConverter refundDeliveryConverter;

    /**
     * 获取退款物流信息（用于退货）分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<RefundDeliveryVO>} 退款物流信息（用于退货）分页列表
     */
    @Override
    public IPage<RefundDeliveryVO> getRefundDeliveryPage(RefundDeliveryQuery queryParams) {
        Page<RefundDeliveryVO> pageVO = this.baseMapper.getRefundDeliveryPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取退款物流信息（用于退货）表单数据
     *
     * @param id 退款物流信息（用于退货）ID
     * @return 退款物流信息（用于退货）表单数据
     */
    @Override
    public RefundDeliveryForm getRefundDeliveryFormData(Long id) {
        RefundDelivery entity = this.getById(id);
        return refundDeliveryConverter.toForm(entity);
    }

    /**
     * 新增退款物流信息（用于退货）
     *
     * @param formData 退款物流信息（用于退货）表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveRefundDelivery(RefundDeliveryForm formData) {
        RefundDelivery entity = refundDeliveryConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新退款物流信息（用于退货）
     *
     * @param id   退款物流信息（用于退货）ID
     * @param formData 退款物流信息（用于退货）表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateRefundDelivery(Long id,RefundDeliveryForm formData) {
        RefundDelivery entity = refundDeliveryConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除退款物流信息（用于退货）
     *
     * @param ids 退款物流信息（用于退货）ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteRefundDeliverys(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的退款物流信息（用于退货）数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
