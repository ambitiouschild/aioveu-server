package com.aioveu.pms.aioveu02Category.service;

import com.aioveu.pms.aioveu02Category.model.form.PmsCategoryForm;
import com.aioveu.pms.aioveu02Category.model.query.PmsCategoryQuery;
import com.aioveu.pms.aioveu02Category.model.vo.PmsCategoryVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.common.web.model.Option;
import com.aioveu.pms.aioveu02Category.model.entity.PmsCategory;
import com.aioveu.pms.model.vo.CategoryVO;

import java.util.List;


/**
 * @Description: TODO 商品分类服务类
 * @Author: 雒世松
 * @Date: 2025/6/5 18:34
 * @param
 * @return:
 **/

public interface PmsCategoryService extends IService<PmsCategory> {


    /**
     * 分类列表（树形）
     *
     * @param parentId
     * @return
     */
    List<CategoryVO> getCategoryList(Long parentId);

    /**
     * 分类列表（级联）
     * @return
     */
    List<Option> getCategoryOptions();


    /**
     *
     * 保存（新增/修改）分类
     *
     *
     * @param category
     * @return
     */
    Long saveCategory(PmsCategory category);


    /**
     *商品分类分页列表
     *
     * @return {@link IPage<PmsCategoryVO>} 商品分类分页列表
     */
    IPage<PmsCategoryVO> getPmsCategoryPage(PmsCategoryQuery queryParams);

    /**
     * 获取商品分类表单数据
     *
     * @param id 商品分类ID
     * @return 商品分类表单数据
     */
    PmsCategoryForm getPmsCategoryFormData(Long id);

    /**
     * 新增商品分类
     *
     * @param formData 商品分类表单对象
     * @return 是否新增成功
     */
    boolean savePmsCategory(PmsCategoryForm formData);

    /**
     * 修改商品分类
     *
     * @param id   商品分类ID
     * @param formData 商品分类表单对象
     * @return 是否修改成功
     */
    boolean updatePmsCategory(Long id, PmsCategoryForm formData);

    /**
     * 删除商品分类
     *
     * @param ids 商品分类ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deletePmsCategorys(String ids);

}
