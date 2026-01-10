package com.aioveu.oms.aioveu06OrderSetting.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.oms.aioveu06OrderSetting.converter.OmsOrderSettingConverter;
import com.aioveu.oms.aioveu06OrderSetting.model.form.OmsOrderSettingForm;
import com.aioveu.oms.aioveu06OrderSetting.model.query.OmsOrderSettingQuery;
import com.aioveu.oms.aioveu06OrderSetting.model.vo.OmsOrderSettingVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.oms.aioveu06OrderSetting.mapper.OmsOrderSettingMapper;
import com.aioveu.oms.aioveu06OrderSetting.model.entity.OmsOrderSetting;
import com.aioveu.oms.aioveu06OrderSetting.service.OmsOrderSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO  订单配置信息服务实现类
 * @Date  2026/1/10 17:18
 * @Param
 * @return
 **/

@Service
@RequiredArgsConstructor
public class OmsOrderSettingServiceImpl extends ServiceImpl<OmsOrderSettingMapper, OmsOrderSetting> implements OmsOrderSettingService {

    private final OmsOrderSettingConverter omsOrderSettingConverter;

    /**
     * 获取订单配置信息分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<OmsOrderSettingVO>} 订单配置信息分页列表
     */
    @Override
    public IPage<OmsOrderSettingVO> getOmsOrderSettingPage(OmsOrderSettingQuery queryParams) {
        Page<OmsOrderSettingVO> pageVO = this.baseMapper.getOmsOrderSettingPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取订单配置信息表单数据
     *
     * @param id 订单配置信息ID
     * @return 订单配置信息表单数据
     */
    @Override
    public OmsOrderSettingForm getOmsOrderSettingFormData(Long id) {
        OmsOrderSetting entity = this.getById(id);
        return omsOrderSettingConverter.toForm(entity);
    }

    /**
     * 新增订单配置信息
     *
     * @param formData 订单配置信息表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveOmsOrderSetting(OmsOrderSettingForm formData) {
        OmsOrderSetting entity = omsOrderSettingConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新订单配置信息
     *
     * @param id   订单配置信息ID
     * @param formData 订单配置信息表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateOmsOrderSetting(Long id,OmsOrderSettingForm formData) {
        OmsOrderSetting entity = omsOrderSettingConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除订单配置信息
     *
     * @param ids 订单配置信息ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteOmsOrderSettings(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的订单配置信息数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
