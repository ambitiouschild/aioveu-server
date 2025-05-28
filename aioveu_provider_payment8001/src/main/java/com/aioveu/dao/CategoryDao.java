package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.Category;
import com.aioveu.vo.CategoryVo;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface CategoryDao extends BaseMapper<Category> {

    IPage<CategoryVo> selCondition(IPage<CategoryVo> page, Integer parentId, Integer id);

}
