package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.Category;
import com.aioveu.entity.CategoryThird;
import com.aioveu.vo.CategoryBaseVO;

import java.util.List;

/**
 * @description
 * @author: xiaoyao
 * @date: 2025/09/28
 */
public interface CategoryThirdService extends IService<CategoryThird> {


    /**
     * 根据父ID条件查询分类
     * @return
     */
    IPage<CategoryThird> getCategoryThirdByCondition(int page,int size, Integer parentId,Integer companyId,Integer storeId,String name);

    /**
     * 新增分类信息
     * @param category
     * @return
     */
    Integer addCategory(CategoryThird category);

    /**
     * 修改分类信息
     * @param category
     * @return
     */
    Integer modifyCategoryMessage(CategoryThird category);

    /**
     * 删除分类
     * @param id
     * @return
     */
    Integer deleteCategory(long id);


}
