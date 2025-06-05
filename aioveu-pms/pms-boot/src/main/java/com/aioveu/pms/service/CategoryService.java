package com.aioveu.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.common.web.model.Option;
import com.aioveu.pms.model.entity.PmsCategory;
import com.aioveu.pms.model.vo.CategoryVO;

import java.util.List;


/**
 * @Description: TODO 商品分类
 * @Author: 雒世松
 * @Date: 2025/6/5 18:34
 * @param
 * @return:
 **/

public interface CategoryService extends IService<PmsCategory> {


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

}
