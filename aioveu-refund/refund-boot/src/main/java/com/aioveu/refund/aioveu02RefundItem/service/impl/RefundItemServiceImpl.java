package com.aioveu.refund.aioveu02RefundItem.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.refund.aioveu02RefundItem.converter.RefundItemConverter;
import com.aioveu.refund.aioveu02RefundItem.mapper.RefundItemMapper;
import com.aioveu.refund.aioveu02RefundItem.model.entity.RefundItem;
import com.aioveu.refund.aioveu02RefundItem.model.form.RefundItemForm;
import com.aioveu.refund.aioveu02RefundItem.model.query.RefundItemQuery;
import com.aioveu.refund.aioveu02RefundItem.model.vo.RefundItemVO;
import com.aioveu.refund.aioveu02RefundItem.service.RefundItemService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: RefundItemServiceImpl
 * @Description TODO  退款商品明细服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 17:04
 * @Version 1.0
 **/

@Service
@RequiredArgsConstructor
public class RefundItemServiceImpl extends ServiceImpl<RefundItemMapper, RefundItem> implements RefundItemService {

    private final RefundItemConverter refundItemConverter;

    /**
     * 获取退款商品明细分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<RefundItemVO>} 退款商品明细分页列表
     */
    @Override
    public IPage<RefundItemVO> getRefundItemPage(RefundItemQuery queryParams) {
        Page<RefundItemVO> pageVO = this.baseMapper.getRefundItemPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取退款商品明细表单数据
     *
     * @param id 退款商品明细ID
     * @return 退款商品明细表单数据
     */
    @Override
    public RefundItemForm getRefundItemFormData(Long id) {
        RefundItem entity = this.getById(id);
        return refundItemConverter.toForm(entity);
    }

    /**
     * 新增退款商品明细
     *
     * @param formData 退款商品明细表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveRefundItem(RefundItemForm formData) {
        RefundItem entity = refundItemConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新退款商品明细
     *
     * @param id   退款商品明细ID
     * @param formData 退款商品明细表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateRefundItem(Long id,RefundItemForm formData) {
        RefundItem entity = refundItemConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除退款商品明细
     *
     * @param ids 退款商品明细ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteRefundItems(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的退款商品明细数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
