package com.aioveu.oms.aioveu02OrderItem.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.oms.aioveu02OrderItem.converter.OmsOrderItemConverter;
import com.aioveu.oms.aioveu02OrderItem.model.form.OmsOrderItemForm;
import com.aioveu.oms.aioveu02OrderItem.model.query.OmsOrderItemQuery;
import com.aioveu.oms.aioveu02OrderItem.model.vo.OmsOrderItemVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.oms.aioveu02OrderItem.mapper.OmsOrderItemMapper;
import com.aioveu.oms.aioveu02OrderItem.model.entity.OmsOrderItem;
import com.aioveu.oms.aioveu02OrderItem.service.OmsOrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO  订单商品信息服务类
 * @Date  2026/1/8 19:39
 * @Param
 * @return
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl extends ServiceImpl<OmsOrderItemMapper, OmsOrderItem> implements OmsOrderItemService {

    private final OmsOrderItemConverter omsOrderItemConverter;

    /**
     * 获取订单商品信息分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<OmsOrderItemVO>} 订单商品信息分页列表
     */
    @Override
    public IPage<OmsOrderItemVO> getOmsOrderItemPage(OmsOrderItemQuery queryParams) {
        Page<OmsOrderItemVO> pageVO = this.baseMapper.getOmsOrderItemPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取订单商品信息表单数据
     *
     * @param id 订单商品信息ID
     * @return 订单商品信息表单数据
     */
    @Override
    public OmsOrderItemForm getOmsOrderItemFormData(Long id) {
        OmsOrderItem entity = this.getById(id);
        return omsOrderItemConverter.toForm(entity);
    }

    /**
     * 新增订单商品信息
     *
     * @param formData 订单商品信息表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveOmsOrderItem(OmsOrderItemForm formData) {
        OmsOrderItem entity = omsOrderItemConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新订单商品信息
     *
     * @param id   订单商品信息ID
     * @param formData 订单商品信息表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateOmsOrderItem(Long id,OmsOrderItemForm formData) {
        OmsOrderItem entity = omsOrderItemConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除订单商品信息
     *
     * @param ids 订单商品信息ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteOmsOrderItems(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的订单商品信息数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }


}
