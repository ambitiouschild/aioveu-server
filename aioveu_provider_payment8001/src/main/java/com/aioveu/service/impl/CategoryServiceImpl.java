package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.CategoryDao;
import com.aioveu.dto.OrderStatusVo;
import com.aioveu.entity.Category;
import com.aioveu.enums.DataStatus;
import com.aioveu.exception.SportException;
import com.aioveu.service.CategoryService;
import com.aioveu.vo.CategoryBaseVO;
import com.aioveu.vo.CategoryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public IPage<CategoryVo> selCondition(int page, int size, Integer parentId, Integer id) {
        if (parentId == null && id != null){
            IPage<CategoryVo> categoryVoIPage = categoryDao.selCondition(new Page<>(page, size), parentId, id);

            Long parent = categoryVoIPage.getRecords().get(0).getParentId();
            if (parent != null){
                id = Math.toIntExact(parent);
                String parentName = categoryDao.selCondition(new Page<>(page, size), parentId, id).getRecords().get(0).getName();

                categoryVoIPage.getRecords().get(0).setParentName(parentName);
                categoryVoIPage.getRecords().get(0).setParentId(parent);
            }
            return categoryVoIPage;
        }
        return categoryDao.selCondition(new Page<>(page, size), parentId, id);
    }

    private Long getParentIdByCode(String categoryCode) {
        Category parent = getOneCategoryByCode(categoryCode);
        if (parent != null) {
            return parent.getId();
        }
        return null;
    }

    @Override
    public List<CategoryBaseVO> getCategoryListByCode(String categoryCode) {
        Long parentId = getParentIdByCode(categoryCode);
        if (parentId != null) {
            QueryWrapper<Category> queryListWrapper = new QueryWrapper<>();
            queryListWrapper.lambda().eq(Category::getParentId, parentId)
                    .eq(Category::getStatus, DataStatus.NORMAL.getCode());
            queryListWrapper.orderByDesc("priority");
            return list(queryListWrapper).stream().map(item -> {
                CategoryBaseVO idNameVO = new CategoryBaseVO();
                idNameVO.setId(item.getId());
                idNameVO.setName(item.getName());
                idNameVO.setCode(item.getCode());
                idNameVO.setCover(item.getCover());
                return idNameVO;
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Category getOneCategoryByCode(String code) {
        //TODO fanxiaole 后续可以做缓存
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Category::getCode, code);
        return getOne(queryWrapper);
    }

    @Override
    public Long getByCode(String code) {
        Category category = getOneCategoryByCode(code);
        if (category != null) {
            return category.getId();
        }
        return 0L;
    }

    @Override
    public Integer addCategory(Category category) {
        if (StringUtils.isEmpty(category.getCode())) {
            throw new SportException("编号不能为空");
        }
        if (StringUtils.isEmpty(category.getName())) {
            throw new SportException("分类名称不能为空");
        }
        if(count(new QueryWrapper<Category>().lambda().eq(Category::getCode, category.getCode())) == 0) {
            return categoryDao.insert(category);
        }
       throw new SportException("code值重复");
    }

    @Override
    public Integer modifyCategoryMessage(Category category) {
        LambdaUpdateWrapper<Category> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(Category::getName, category.getName())
                .set(Category::getCode, category.getCode())
                .set(Category::getCover, category.getCover())
                .eq(Category::getId, category.getId());
        int update = categoryDao.update(category, wrapper);
        if (update <= 0) {
            throw new SportException("没有找到该城市");
        }
        return update;
    }

    @Override
    public Integer deleteCategory(long id) {
        LambdaQueryWrapper<Category> queryParent = Wrappers.lambdaQuery();
        queryParent.eq(Category::getId, id);
        Category category = categoryDao.selectOne(queryParent);
        if (category == null) {
            throw new SportException("该对象未找到");
        }
        if (category.getParentId() == null) {
//            LambdaQueryWrapper<Category> queryChild = Wrappers.lambdaQuery();
//            queryParent.eq(Category::getParentId, id);
//            List<Category> categories = categoryDao.selectList(queryChild);
            Integer count = getBaseMapper().selectCount(new QueryWrapper<Category>().eq("parent_id", id));

//            if (!CollectionUtils.isNotEmpty(categories)) {
            if (count == 0) {
                return categoryDao.delete(queryParent);
            }
            throw new SportException("该菜单下有子菜单无法删除");
        }
        return categoryDao.delete(queryParent);
    }

    /**
     * 获取分类 返回tree树形
     *
     * @return tree
     */
    @Override
    public List<Category> getTreeCategory() {
        //获取全部的分类
        QueryWrapper queryWrapper =  new QueryWrapper();
        queryWrapper.eq("status","1");
        List<Category> list = baseMapper.selectList(queryWrapper);

        //订单列表
        for (Category category:list){
            List<OrderStatusVo> orderStatusVoList = new ArrayList<>();
            //获取订单
//            List<Order> orderListByCategoryId = orderService.getOrderListByCategoryId(category.getId().toString());
//            for (Order order:orderListByCategoryId){
//                OrderStatusVo orderStatusVo = new OrderStatusVo(order);
//                orderStatusVoList.add(orderStatusVo);
//            }
            category.setOrderStatusVos(orderStatusVoList);
        }

        //循环tree
        List<Category> treeList = new ArrayList<>();


        for (Category treeNode : list) {
            if (treeNode.getParentId() == 0) {
                treeList.add(treeNode);

            }
            for (Category tree : list) {
                if (tree.getParentId().equals(treeNode.getId())) {
                    if (treeNode.getTreeList() == null) {
                        treeNode.setTreeList(new ArrayList<>());
                        List<Category> lists = new ArrayList();
                        lists.add(tree);
                        treeNode.setTreeList(lists);
                    } else {
                        List<Category> lists = treeNode.getTreeList();
                        lists.add(tree);
                        treeNode.setTreeList(lists);
                    }
                }
            }

        }
        return treeList;
    }
}
