package com.aioveu.refund.aioveu03RefundDelivery.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.refund.aioveu01RefundOrder.model.entity.RefundOrder;
import com.aioveu.refund.aioveu03RefundDelivery.converter.RefundDeliveryConverter;
import com.aioveu.refund.aioveu03RefundDelivery.enums.DeliveryTypeEnum;
import com.aioveu.refund.aioveu03RefundDelivery.mapper.RefundDeliveryMapper;
import com.aioveu.refund.aioveu03RefundDelivery.model.entity.RefundDelivery;
import com.aioveu.refund.aioveu03RefundDelivery.model.form.ConfirmReceiveFormDTO;
import com.aioveu.refund.aioveu03RefundDelivery.model.form.RefundDeliveryForm;
import com.aioveu.refund.aioveu03RefundDelivery.model.query.RefundDeliveryQuery;
import com.aioveu.refund.aioveu03RefundDelivery.model.vo.RefundDeliveryVO;
import com.aioveu.refund.aioveu03RefundDelivery.service.RefundDeliveryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.Now;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.time.LocalDateTime.now;

/**
 * @ClassName: RefundDeliveryServiceImpl
 * @Description TODO 退款物流信息（用于退货）服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 18:06
 * @Version 1.0
 **/

@Slf4j
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
     * 获取退款物流信息（用于退货）实体
     *
     * @param refundId 退款ID
     * @return 退款物流信息（用于退货）实体
     */
    @Override
    public RefundDelivery getRefundDeliveryEntityByRefundId(Long refundId) {
        RefundDelivery refundDelivery = this.getOne(
                new LambdaQueryWrapper<RefundDelivery>()
                        .eq(RefundDelivery::getRefundId, refundId)
        );

        log.info("获取退款物流信息（用于退货）实体:{}",refundDelivery);

        return refundDelivery;
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
     * 新增退款物流信息（用于退货）
     *
     * @param formData 退款物流信息（用于退货）表单对象
     * @return 是否新增成功
     */
    @Override
    public RefundDelivery fillRefundDelivery(RefundDeliveryForm formData) {

        Long refundId = formData.getRefundId();
        RefundDelivery delivery = this.getById(refundId);
        delivery.setDeliveryType(DeliveryTypeEnum.Buyer_ships.getValue()); //买家发货
        delivery.setDeliveryCompany(formData.getDeliveryCompany());
        delivery.setDeliverySn(formData.getDeliverySn());
        delivery.setReceiverName(formData.getReceiverName());
        delivery.setReceiverPhone(formData.getReceiverPhone());
        delivery.setReceiverAddress(formData.getReceiverAddress());
        delivery.setDeliveryTime(now());

        this.save(delivery);

        return delivery;
    }

    /**
     * 新增退款物流信息（用于退货）
     *
     * @param formData 退款物流信息（用于退货）表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean createRefundDelivery(RefundDeliveryForm formData,Long refundId) {

        RefundDelivery delivery = new RefundDelivery();
        delivery.setRefundId(refundId);
        delivery.setDeliveryType(DeliveryTypeEnum.Exchange_shipment.getValue());
//        delivery.setDeliverySn(generateDeliverySn());
        delivery.setDeliveryCompany(formData.getDeliveryCompany());



        return this.save(delivery);
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

    /**
     * 商家确认收货
     *
     * @param refundId 退款物流信息（用于退货）ID
     * @return 退款物流信息（用于退货）表单数据
     */
    @Override
    public boolean confirmReceive(Long refundId, ConfirmReceiveFormDTO formData) {

        RefundDelivery delivery = this.getById(refundId);
        delivery.setReceiveTime(now()); // 收货时间

        return this.updateById(delivery);
    }
}
