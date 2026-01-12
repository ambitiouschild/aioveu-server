package com.aioveu.sms.aioveu01Advert.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.sms.aioveu01Advert.converter.SmsAdvertConverter;
import com.aioveu.sms.aioveu01Advert.model.form.SmsAdvertForm;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.common.enums.StatusEnum;
import com.aioveu.sms.aioveu01Advert.mapper.SmsAdvertMapper;
import com.aioveu.sms.aioveu01Advert.model.entity.SmsAdvert;
import com.aioveu.sms.aioveu01Advert.model.query.SmsAdvertQuery;
import com.aioveu.sms.aioveu01Advert.model.vo.SmsAdvertVO;
import com.aioveu.sms.aioveu01Advert.model.vo.BannerVO;
import com.aioveu.sms.aioveu01Advert.service.SmsAdvertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: TODO 广告业务实现类
 * @Author: 雒世松
 * @Date: 2025/6/5 18:48
 * @param
 * @return:
 **/

@Service
@RequiredArgsConstructor
public class SmsAdvertServiceImpl extends ServiceImpl<SmsAdvertMapper, SmsAdvert> implements SmsAdvertService {

    private final SmsAdvertConverter smsAdvertConverter;

    /**
     * 广告分页列表
     *
     * @param queryParams 查询参数
     * @return 广告分页列表
     */
    @Override
    public Page<SmsAdvertVO> getAdvertPage(SmsAdvertQuery queryParams) {
        Page<SmsAdvert> page = this.baseMapper.getAdvertPage(new Page<>(queryParams.getPageNum(),
                        queryParams.getPageSize()),
                queryParams);
        return smsAdvertConverter.entity2PageVo(page);
    }

    /**
     * 获取广告横幅列表
     */
    @Override
    public List<BannerVO> getBannerList() {

        List<SmsAdvert> entities = this.list(new LambdaQueryWrapper<SmsAdvert>().
                eq(SmsAdvert::getStatus, StatusEnum.ENABLE.getValue())
                .select(SmsAdvert::getTitle, SmsAdvert::getImageUrl, SmsAdvert::getRedirectUrl)
        );
        return smsAdvertConverter.entity2BannerVo(entities);
    }

    /**
     * 获取广告分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<SmsAdvertVO>} 广告分页列表
     */
    @Override
    public IPage<SmsAdvertVO> getSmsAdvertPage(SmsAdvertQuery queryParams) {
        Page<SmsAdvertVO> pageVO = this.baseMapper.getSmsAdvertPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取广告表单数据
     *
     * @param id 广告ID
     * @return 广告表单数据
     */
    @Override
    public SmsAdvertForm getSmsAdvertFormData(Long id) {
        SmsAdvert entity = this.getById(id);
        return smsAdvertConverter.toForm(entity);
    }

    /**
     * 新增广告
     *
     * @param formData 广告表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveSmsAdvert(SmsAdvertForm formData) {
        SmsAdvert entity = smsAdvertConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新广告
     *
     * @param id   广告ID
     * @param formData 广告表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateSmsAdvert(Long id,SmsAdvertForm formData) {
        SmsAdvert entity = smsAdvertConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除广告
     *
     * @param ids 广告ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteSmsAdverts(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的广告数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
