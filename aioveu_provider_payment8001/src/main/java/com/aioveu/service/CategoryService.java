package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.Category;
import com.aioveu.vo.CategoryBaseVO;
import com.aioveu.vo.CategoryVo;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface CategoryService extends IService<Category> {


    /**
     * 根据父ID条件查询分类
     * @return
     */
    IPage<CategoryVo> selCondition(int page, int size, Integer parentId, Integer id);

    /**
     * 获取分类列表
     * @param categoryCode
     * @return
     */
    List<CategoryBaseVO> getCategoryListByCode(String categoryCode);

    Category getOneCategoryByCode(String code);

    /**
     * 通过code查找分类id
     * @param code
     * @return
     */
    Long getByCode(String code);

    /**
     * 新增分类信息
     * @param category
     * @return
     */
    Integer addCategory(Category category);

    /**
     * 修改分类信息
     * @param category
     * @return
     */
    Integer modifyCategoryMessage(Category category);

    /**
     * 删除分类
     * @param id
     * @return
     */
    Integer deleteCategory(long id);


    /**
     * 获取分类 返回tree树形
     * @return tree
     */
    List<Category> getTreeCategory();


}
