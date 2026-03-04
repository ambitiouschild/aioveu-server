package com.aioveu.sms.aioveu07HomeCategory.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.enums.StatusEnum;
import com.aioveu.sms.aioveu07HomeCategory.converter.SmsHomeCategoryConverter;
import com.aioveu.sms.aioveu07HomeCategory.mapper.SmsHomeCategoryMapper;
import com.aioveu.sms.aioveu07HomeCategory.model.entity.SmsHomeCategory;
import com.aioveu.sms.aioveu07HomeCategory.model.form.SmsHomeCategoryForm;
import com.aioveu.sms.aioveu07HomeCategory.model.query.SmsHomeCategoryQuery;
import com.aioveu.sms.aioveu07HomeCategory.model.vo.SmsHomeCategoryVO;
import com.aioveu.sms.aioveu07HomeCategory.service.SmsHomeCategoryService;
import com.aioveu.sms.aioveu08HomeAdvert.model.entity.SmsHomeAdvert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: SmsHomeCategoryServiceImpl
 * @Description TODO 首页分类配置服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/4 12:17
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class SmsHomeCategoryServiceImpl extends ServiceImpl<SmsHomeCategoryMapper, SmsHomeCategory> implements SmsHomeCategoryService {

    private final SmsHomeCategoryConverter smsHomeCategoryConverter;



    /**
     * 获取首页分类配置列表
     *
     * @param
     * @return {@link IPage<SmsHomeCategoryVO>} 首页分类配置列表
     */
    @Override
    public List<SmsHomeCategoryVO> getSmsHomeCategoryList() {
        List<SmsHomeCategory> entities = this.list(new LambdaQueryWrapper<SmsHomeCategory>()
                .eq(SmsHomeCategory::getStatus, StatusEnum.ENABLE.getValue())
//                .orderByDesc(SmsHomeCategory::getSort)    // 按排序字段降序排列
                .orderByAsc(SmsHomeCategory::getSort)    // 按排序字段升序排列
        );
        return smsHomeCategoryConverter.toVO(entities);
    }

    /**
     * 获取首页分类配置分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<SmsHomeCategoryVO>} 首页分类配置分页列表
     */
    @Override
    public IPage<SmsHomeCategoryVO> getSmsHomeCategoryPage(SmsHomeCategoryQuery queryParams) {
        Page<SmsHomeCategoryVO> pageVO = this.baseMapper.getSmsHomeCategoryPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取首页分类配置表单数据
     *
     * @param id 首页分类配置ID
     * @return 首页分类配置表单数据
     */
    @Override
    public SmsHomeCategoryForm getSmsHomeCategoryFormData(Long id) {
        SmsHomeCategory entity = this.getById(id);
        return smsHomeCategoryConverter.toForm(entity);
    }

    /**
     * 新增首页分类配置
     *
     * @param formData 首页分类配置表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveSmsHomeCategory(SmsHomeCategoryForm formData) {
        SmsHomeCategory entity = smsHomeCategoryConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新首页分类配置
     *
     * @param id   首页分类配置ID
     * @param formData 首页分类配置表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateSmsHomeCategory(Long id,SmsHomeCategoryForm formData) {
        SmsHomeCategory entity = smsHomeCategoryConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除首页分类配置
     *
     * @param ids 首页分类配置ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteSmsHomeCategorys(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的首页分类配置数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
