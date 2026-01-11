package com.aioveu.pms.aioveu04CategoryBrand.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pms.aioveu04CategoryBrand.converter.PmsCategoryBrandConverter;
import com.aioveu.pms.aioveu04CategoryBrand.model.form.PmsCategoryBrandForm;
import com.aioveu.pms.aioveu04CategoryBrand.model.query.PmsCategoryBrandQuery;
import com.aioveu.pms.aioveu04CategoryBrand.model.vo.PmsCategoryBrandVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.pms.aioveu04CategoryBrand.mapper.PmsCategoryBrandMapper;
import com.aioveu.pms.aioveu04CategoryBrand.model.entity.PmsCategoryBrand;
import com.aioveu.pms.aioveu04CategoryBrand.service.PmsCategoryBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 商品分类与品牌关联表服务实现类
 * @Date  2026/1/11 20:08
 * @Param
 * @return
 **/

@Service
@RequiredArgsConstructor
public class PmsCategoryBrandServiceImpl extends ServiceImpl<PmsCategoryBrandMapper, PmsCategoryBrand> implements PmsCategoryBrandService {


    private final PmsCategoryBrandConverter pmsCategoryBrandConverter;

    /**
     * 获取商品分类与品牌关联表分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PmsCategoryBrandVO>} 商品分类与品牌关联表分页列表
     */
    @Override
    public IPage<PmsCategoryBrandVO> getPmsCategoryBrandPage(PmsCategoryBrandQuery queryParams) {
        Page<PmsCategoryBrandVO> pageVO = this.baseMapper.getPmsCategoryBrandPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取商品分类与品牌关联表表单数据
     *
     * @param id 商品分类与品牌关联表ID
     * @return 商品分类与品牌关联表表单数据
     */
    @Override
    public PmsCategoryBrandForm getPmsCategoryBrandFormData(Long id) {
        PmsCategoryBrand entity = this.getById(id);
        return pmsCategoryBrandConverter.toForm(entity);
    }

    /**
     * 新增商品分类与品牌关联表
     *
     * @param formData 商品分类与品牌关联表表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePmsCategoryBrand(PmsCategoryBrandForm formData) {
        PmsCategoryBrand entity = pmsCategoryBrandConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新商品分类与品牌关联表
     *
     * @param id   商品分类与品牌关联表ID
     * @param formData 商品分类与品牌关联表表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePmsCategoryBrand(Long id,PmsCategoryBrandForm formData) {
        PmsCategoryBrand entity = pmsCategoryBrandConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除商品分类与品牌关联表
     *
     * @param ids 商品分类与品牌关联表ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePmsCategoryBrands(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的商品分类与品牌关联表数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }



}
