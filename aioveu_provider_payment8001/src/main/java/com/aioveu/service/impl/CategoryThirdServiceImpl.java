package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.CategoryDao;
import com.aioveu.dao.CategoryThirdDao;
import com.aioveu.entity.Category;
import com.aioveu.entity.CategoryThird;
import com.aioveu.enums.DataStatus;
import com.aioveu.exception.SportException;
import com.aioveu.service.CategoryService;
import com.aioveu.service.CategoryThirdService;
import com.aioveu.utils.FileUtil;
import com.aioveu.vo.CategoryBaseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class CategoryThirdServiceImpl extends ServiceImpl<CategoryThirdDao, CategoryThird> implements CategoryThirdService {


    @Autowired
    CategoryThirdDao categoryThirdDao;

    @Override
    public IPage<CategoryThird> getCategoryThirdByCondition(int page, int size, Integer parentId, Integer companyId, Integer storeId, String name) {
        IPage<CategoryThird> Ipage = new Page<>(page, size);
        LambdaQueryWrapper<CategoryThird> wrapper = Wrappers.lambdaQuery();
        if (parentId != null) {
            wrapper.eq(CategoryThird::getParentId, parentId);
        }
        if (name != null) {
            wrapper.eq(CategoryThird::getName, name);
        }
        if (companyId != null) {
            wrapper.eq(CategoryThird::getCompanyId, companyId);
        }
        if (storeId != null) {
            wrapper.eq(CategoryThird::getStoreId, storeId);
        }
        wrapper.eq(CategoryThird::getStatus, 1);
        return categoryThirdDao.selectPage(Ipage, wrapper);
    }

    @Override
    public Integer addCategory(CategoryThird category) {
        if (StringUtils.isEmpty(category.getCode())) {
            throw new SportException("编号不能为空");
        }
        if (StringUtils.isEmpty(category.getStoreId())) {
            throw new SportException("商店ID不能为空");
        }
        if (StringUtils.isEmpty(category.getCompanyId())) {
            throw new SportException("公司Id不能为空");
        }
        if (StringUtils.isEmpty(category.getCover())) {
            throw new SportException("背景图不能为空");
        }
        if (StringUtils.isEmpty(category.getName())) {
            throw new SportException("分类名称不能为空");
        }
        return categoryThirdDao.insert(category);
    }

    @Override
    public Integer modifyCategoryMessage(CategoryThird category) {
        LambdaUpdateWrapper<CategoryThird> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(CategoryThird::getName, category.getName())
                .set(CategoryThird::getCode, category.getCode())
                .set(CategoryThird::getCover, category.getCover())
                .eq(CategoryThird::getId, category.getId());
        int update = categoryThirdDao.update(category, wrapper);
        if (update <= 0) {
            throw new SportException("没有找到该城市");
        }
        return update;
    }

    @Override
    public Integer deleteCategory(long id) {
        LambdaQueryWrapper<CategoryThird> queryParent = Wrappers.lambdaQuery();
        queryParent.select(CategoryThird::getParentId).eq(CategoryThird::getId, id);
        CategoryThird category = categoryThirdDao.selectOne(queryParent);
        if (category.getParentId() == null) {
            LambdaQueryWrapper<CategoryThird> queryChild = Wrappers.lambdaQuery();
            queryParent.eq(CategoryThird::getParentId, id);
            List<CategoryThird> categories = categoryThirdDao.selectList(queryChild);
            if (categories.size() == 0) {
                return categoryThirdDao.delete(queryParent);
            }
            throw new SportException("该菜单下有子菜单无法删除");
        }
        return categoryThirdDao.delete(queryParent);
    }
}
