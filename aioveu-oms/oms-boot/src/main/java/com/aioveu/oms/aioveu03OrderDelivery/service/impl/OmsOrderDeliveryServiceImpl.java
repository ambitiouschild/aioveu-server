package com.aioveu.oms.aioveu03OrderDelivery.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.oms.aioveu03OrderDelivery.converter.OmsOrderDeliveryConverter;
import com.aioveu.oms.aioveu03OrderDelivery.model.form.OmsOrderDeliveryForm;
import com.aioveu.oms.aioveu03OrderDelivery.model.query.OmsOrderDeliveryQuery;
import com.aioveu.oms.aioveu03OrderDelivery.model.vo.OmsOrderDeliveryVO;
import com.aioveu.oms.aioveu03OrderDelivery.service.OmsOrderDeliveryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.oms.aioveu03OrderDelivery.mapper.OmsOrderDeliveryMapper;
import com.aioveu.oms.aioveu03OrderDelivery.model.entity.OmsOrderDelivery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO  订单物流记录服务实现类
 * @Date  2026/1/8 20:30
 * @Param
 * @return
 **/


@Service("orderDeliveryService")
@RequiredArgsConstructor
public class OmsOrderDeliveryServiceImpl extends ServiceImpl<OmsOrderDeliveryMapper, OmsOrderDelivery> implements OmsOrderDeliveryService {

    private final OmsOrderDeliveryConverter omsOrderDeliveryConverter;

    /**
     * 获取订单物流记录分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<OmsOrderDeliveryVO>} 订单物流记录分页列表
     */
    @Override
    public IPage<OmsOrderDeliveryVO> getOmsOrderDeliveryPage(OmsOrderDeliveryQuery queryParams) {
        Page<OmsOrderDeliveryVO> pageVO = this.baseMapper.getOmsOrderDeliveryPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取订单物流记录表单数据
     *
     * @param id 订单物流记录ID
     * @return 订单物流记录表单数据
     */
    @Override
    public OmsOrderDeliveryForm getOmsOrderDeliveryFormData(Long id) {
        OmsOrderDelivery entity = this.getById(id);
        return omsOrderDeliveryConverter.toForm(entity);
    }

    /**
     * 新增订单物流记录
     *
     * @param formData 订单物流记录表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveOmsOrderDelivery(OmsOrderDeliveryForm formData) {
        OmsOrderDelivery entity = omsOrderDeliveryConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新订单物流记录
     *
     * @param id   订单物流记录ID
     * @param formData 订单物流记录表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateOmsOrderDelivery(Long id,OmsOrderDeliveryForm formData) {
        OmsOrderDelivery entity = omsOrderDeliveryConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除订单物流记录
     *
     * @param ids 订单物流记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteOmsOrderDeliverys(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的订单物流记录数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
