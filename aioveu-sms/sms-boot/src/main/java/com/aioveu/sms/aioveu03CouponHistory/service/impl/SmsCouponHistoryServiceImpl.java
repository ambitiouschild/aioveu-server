package com.aioveu.sms.aioveu03CouponHistory.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.sms.aioveu03CouponHistory.converter.SmsCouponHistoryConverter;
import com.aioveu.sms.aioveu03CouponHistory.model.form.SmsCouponHistoryForm;
import com.aioveu.sms.aioveu03CouponHistory.model.query.SmsCouponHistoryQuery;
import com.aioveu.sms.aioveu03CouponHistory.model.vo.SmsCouponHistoryVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.sms.aioveu03CouponHistory.mapper.SmsCouponHistoryMapper;
import com.aioveu.sms.aioveu03CouponHistory.model.entity.SmsCouponHistory;
import com.aioveu.sms.aioveu03CouponHistory.service.SmsCouponHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 优惠券领取/使用记录服务实现类
 * @Date  2026/1/12 12:00
 * @Param
 * @return
 **/
@Service
@RequiredArgsConstructor
public class SmsCouponHistoryServiceImpl extends ServiceImpl<SmsCouponHistoryMapper, SmsCouponHistory>
implements SmsCouponHistoryService{


    private final SmsCouponHistoryConverter smsCouponHistoryConverter;

    /**
     * 获取优惠券领取/使用记录分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<SmsCouponHistoryVO>} 优惠券领取/使用记录分页列表
     */
    @Override
    public IPage<SmsCouponHistoryVO> getSmsCouponHistoryPage(SmsCouponHistoryQuery queryParams) {
        Page<SmsCouponHistoryVO> pageVO = this.baseMapper.getSmsCouponHistoryPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取优惠券领取/使用记录表单数据
     *
     * @param id 优惠券领取/使用记录ID
     * @return 优惠券领取/使用记录表单数据
     */
    @Override
    public SmsCouponHistoryForm getSmsCouponHistoryFormData(Long id) {
        SmsCouponHistory entity = this.getById(id);
        return smsCouponHistoryConverter.toForm(entity);
    }

    /**
     * 新增优惠券领取/使用记录
     *
     * @param formData 优惠券领取/使用记录表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveSmsCouponHistory(SmsCouponHistoryForm formData) {
        SmsCouponHistory entity = smsCouponHistoryConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新优惠券领取/使用记录
     *
     * @param id   优惠券领取/使用记录ID
     * @param formData 优惠券领取/使用记录表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateSmsCouponHistory(Long id,SmsCouponHistoryForm formData) {
        SmsCouponHistory entity = smsCouponHistoryConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除优惠券领取/使用记录
     *
     * @param ids 优惠券领取/使用记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteSmsCouponHistorys(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的优惠券领取/使用记录数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}




