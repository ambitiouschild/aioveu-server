package com.aioveu.pms.aioveu04CategoryBrand.service;

import com.aioveu.pms.aioveu04CategoryBrand.model.form.PmsCategoryBrandForm;
import com.aioveu.pms.aioveu04CategoryBrand.model.query.PmsCategoryBrandQuery;
import com.aioveu.pms.aioveu04CategoryBrand.model.vo.PmsCategoryBrandVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.pms.aioveu04CategoryBrand.model.entity.PmsCategoryBrand;


/**
 * @Description: TODO 商品分类与品牌关联表服务类
 * @Author: 雒世松
 * @Date: 2026-01-11 20:01
 * @param
 * @return:
 **/

public interface PmsCategoryBrandService extends IService<PmsCategoryBrand> {

    /**
     *商品分类与品牌关联表分页列表
     *
     * @return {@link IPage<PmsCategoryBrandVO>} 商品分类与品牌关联表分页列表
     */
    IPage<PmsCategoryBrandVO> getPmsCategoryBrandPage(PmsCategoryBrandQuery queryParams);

    /**
     * 获取商品分类与品牌关联表表单数据
     *
     * @param id 商品分类与品牌关联表ID
     * @return 商品分类与品牌关联表表单数据
     */
    PmsCategoryBrandForm getPmsCategoryBrandFormData(Long id);

    /**
     * 新增商品分类与品牌关联表
     *
     * @param formData 商品分类与品牌关联表表单对象
     * @return 是否新增成功
     */
    boolean savePmsCategoryBrand(PmsCategoryBrandForm formData);

    /**
     * 修改商品分类与品牌关联表
     *
     * @param id   商品分类与品牌关联表ID
     * @param formData 商品分类与品牌关联表表单对象
     * @return 是否修改成功
     */
    boolean updatePmsCategoryBrand(Long id, PmsCategoryBrandForm formData);

    /**
     * 删除商品分类与品牌关联表
     *
     * @param ids 商品分类与品牌关联表ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deletePmsCategoryBrands(String ids);

}
