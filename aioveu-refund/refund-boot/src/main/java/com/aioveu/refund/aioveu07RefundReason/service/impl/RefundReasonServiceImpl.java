package com.aioveu.refund.aioveu07RefundReason.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.refund.aioveu07RefundReason.converter.RefundReasonConverter;
import com.aioveu.refund.aioveu07RefundReason.mapper.RefundReasonMapper;
import com.aioveu.refund.aioveu07RefundReason.model.entity.RefundReason;
import com.aioveu.refund.aioveu07RefundReason.model.form.RefundReasonForm;
import com.aioveu.refund.aioveu07RefundReason.model.query.RefundReasonQuery;
import com.aioveu.refund.aioveu07RefundReason.model.vo.RefundReasonVO;
import com.aioveu.refund.aioveu07RefundReason.service.RefundReasonService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: RefundReasonServiceImpl
 * @Description TODO 退款原因分类服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:57
 * @Version 1.0
 **/

@Service
@RequiredArgsConstructor
public class RefundReasonServiceImpl extends ServiceImpl<RefundReasonMapper, RefundReason> implements RefundReasonService {

    private final RefundReasonConverter refundReasonConverter;

    /**
     * 获取退款原因分类分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<RefundReasonVO>} 退款原因分类分页列表
     */
    @Override
    public IPage<RefundReasonVO> getRefundReasonPage(RefundReasonQuery queryParams) {
        Page<RefundReasonVO> pageVO = this.baseMapper.getRefundReasonPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取退款原因分类表单数据
     *
     * @param id 退款原因分类ID
     * @return 退款原因分类表单数据
     */
    @Override
    public RefundReasonForm getRefundReasonFormData(Long id) {
        RefundReason entity = this.getById(id);
        return refundReasonConverter.toForm(entity);
    }

    /**
     * 新增退款原因分类
     *
     * @param formData 退款原因分类表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveRefundReason(RefundReasonForm formData) {
        RefundReason entity = refundReasonConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新退款原因分类
     *
     * @param id   退款原因分类ID
     * @param formData 退款原因分类表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateRefundReason(Long id,RefundReasonForm formData) {
        RefundReason entity = refundReasonConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除退款原因分类
     *
     * @param ids 退款原因分类ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteRefundReasons(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的退款原因分类数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
