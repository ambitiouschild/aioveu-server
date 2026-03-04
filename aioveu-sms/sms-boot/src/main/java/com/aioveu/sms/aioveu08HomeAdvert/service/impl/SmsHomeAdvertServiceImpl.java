package com.aioveu.sms.aioveu08HomeAdvert.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.enums.StatusEnum;
import com.aioveu.sms.aioveu07HomeCategory.model.entity.SmsHomeCategory;
import com.aioveu.sms.aioveu07HomeCategory.model.vo.SmsHomeCategoryVO;
import com.aioveu.sms.aioveu08HomeAdvert.converter.SmsHomeAdvertConverter;
import com.aioveu.sms.aioveu08HomeAdvert.mapper.SmsHomeAdvertMapper;
import com.aioveu.sms.aioveu08HomeAdvert.model.entity.SmsHomeAdvert;
import com.aioveu.sms.aioveu08HomeAdvert.model.form.SmsHomeAdvertForm;
import com.aioveu.sms.aioveu08HomeAdvert.model.query.SmsHomeAdvertQuery;
import com.aioveu.sms.aioveu08HomeAdvert.model.vo.SmsHomeAdvertVO;
import com.aioveu.sms.aioveu08HomeAdvert.service.SmsHomeAdvertService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: SmsHomeAdvertServiceImpl
 * @Description TODO 首页广告配置（增加跳转路径）服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/4 12:38
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class SmsHomeAdvertServiceImpl extends ServiceImpl<SmsHomeAdvertMapper, SmsHomeAdvert> implements SmsHomeAdvertService {

    private final SmsHomeAdvertConverter smsHomeAdvertConverter;

    /**
     * 获取首页广告配置列表
     *
     * @param
     * @return {@link IPage< SmsHomeCategoryVO >} 首页广告配置列表
     */
    @Override
    public List<SmsHomeAdvertVO> getSmsHomeAdvertList() {
        List<SmsHomeAdvert> entities = this.list(new LambdaQueryWrapper<SmsHomeAdvert>()
                .eq(SmsHomeAdvert::getStatus, StatusEnum.ENABLE.getValue())
//                .orderByDesc(SmsHomeAdvert::getSort)    // 按排序字段降序排列
                .orderByAsc(SmsHomeAdvert::getSort)    // 按排序字段升序排列
        );
        return smsHomeAdvertConverter.toVO(entities);
    }

    /**
     * 获取首页广告配置（增加跳转路径）分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<SmsHomeAdvertVO>} 首页广告配置（增加跳转路径）分页列表
     */
    @Override
    public IPage<SmsHomeAdvertVO> getSmsHomeAdvertPage(SmsHomeAdvertQuery queryParams) {
        Page<SmsHomeAdvertVO> pageVO = this.baseMapper.getSmsHomeAdvertPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取首页广告配置（增加跳转路径）表单数据
     *
     * @param id 首页广告配置（增加跳转路径）ID
     * @return 首页广告配置（增加跳转路径）表单数据
     */
    @Override
    public SmsHomeAdvertForm getSmsHomeAdvertFormData(Long id) {
        SmsHomeAdvert entity = this.getById(id);
        return smsHomeAdvertConverter.toForm(entity);
    }

    /**
     * 新增首页广告配置（增加跳转路径）
     *
     * @param formData 首页广告配置（增加跳转路径）表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveSmsHomeAdvert(SmsHomeAdvertForm formData) {
        SmsHomeAdvert entity = smsHomeAdvertConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新首页广告配置（增加跳转路径）
     *
     * @param id   首页广告配置（增加跳转路径）ID
     * @param formData 首页广告配置（增加跳转路径）表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateSmsHomeAdvert(Long id,SmsHomeAdvertForm formData) {
        SmsHomeAdvert entity = smsHomeAdvertConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除首页广告配置（增加跳转路径）
     *
     * @param ids 首页广告配置（增加跳转路径）ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteSmsHomeAdverts(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的首页广告配置（增加跳转路径）数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
