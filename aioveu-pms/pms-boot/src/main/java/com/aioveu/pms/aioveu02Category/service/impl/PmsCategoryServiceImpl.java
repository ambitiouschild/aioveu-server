package com.aioveu.pms.aioveu02Category.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pms.aioveu02Category.converter.PmsCategoryConverter;
import com.aioveu.pms.aioveu02Category.model.form.PmsCategoryForm;
import com.aioveu.pms.aioveu02Category.model.query.PmsCategoryQuery;
import com.aioveu.pms.aioveu02Category.model.vo.PmsCategoryVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.common.constant.GlobalConstants;
import com.aioveu.common.web.model.Option;
import com.aioveu.pms.aioveu02Category.mapper.PmsCategoryMapper;
import com.aioveu.pms.aioveu02Category.model.entity.PmsCategory;
import com.aioveu.pms.model.vo.CategoryVO;
import com.aioveu.pms.aioveu02Category.service.PmsCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Description: TODO 商品分类
 *                      核心功能概述
 *                          1.树形结构构建：将数据库中的平铺分类数据转换为前端需要的树形结构
 *                          2.级联选择器支持：为前端级联选择器组件提供特定格式的数据
 *                          3.缓存管理：通过注解管理分类数据的缓存，提高性能
 *                      关键技术点详解
 *                      1. 递归算法
 *                          recursionTree方法：深度优先遍历，构建完整的树形结构
 *                          recursionCascade方法：构建级联选择器特定的数据结构
 *                          使用Optional.ofNullable进行空安全处理，避免NPE异常
 *                      2. 数据转换
 *                          BeanUtil.copyProperties：使用Hutool工具类进行对象属性拷贝
 *                          VO/DTO模式：使用CategoryVO隔离实体对象和视图对象
 *                          Option对象：标准化前端级联选择器的数据格式
 *                      3. 查询优化
 *                          LambdaQueryWrapper：类型安全的查询条件构建
 *                          排序策略：树形列表降序，级联选项升序（根据业务需求）
 *                          状态过滤：只查询可见(STATUS_YES)的分类数据
 *                      4. 缓存策略
 *                          @CacheEvict：数据变更时自动清除缓存，保证数据一致性
 *                          缓存键设计：'categoryList'作为缓存键名
 *                          缓存分区：使用pms作为缓存命名空间
 *                      业务逻辑说明
 *                          1.树形结构应用场景：分类管理页面、商品分类导航等
 *                          2.级联选择器应用场景：商品发布时的分类选择、筛选条件等
 *                          3.缓存失效时机：任何分类的新增、修改操作都会清除缓存
 *                      性能考虑
 *                          1.递归方法的时间复杂度为O(n)，适合常规数量的分类数据
 *                          2.对于大量分类数据，可考虑在数据库层面进行优化或使用非递归算法
 *                          3.缓存机制显著减少数据库查询压力
 * @Author: 雒世松
 * @Date: 2025/6/5 18:33
 * @param
 * @return:
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class PmsCategoryServiceImpl extends ServiceImpl<PmsCategoryMapper, PmsCategory> implements PmsCategoryService {


    private final PmsCategoryConverter pmsCategoryConverter;

    /**
     *     TODO             获取分类列表（树形结构）
     *                  从指定父节点开始递归构建完整的树形分类结构
     *
     * @param parentId 父级分类ID，如果为null则从根节点(0L)开始构建
     * @return List<CategoryVO> 树形结构的分类列表
     *
     * 缓存说明：方法被@Cacheable注解注释，实际可根据需要开启缓存
     * value: 缓存名称(分区)；key：缓存键
     */
    // @Cacheable(value = "pms", key = "'categoryList'")
    @Override
    public List<CategoryVO> getCategoryList(Long parentId) {

        log.info("查询所有可见的分类数据，按排序字段降序排列");
        List<PmsCategory> categoryList = this.list(
                new LambdaQueryWrapper<PmsCategory>()
                        .eq(PmsCategory::getVisible, GlobalConstants.STATUS_YES)   // 只查询可见的分类
                        .orderByDesc(PmsCategory::getSort)    // 按排序字段降序排列
        );

        log.info("递归构建树形结构，如果parentId为null则从根节点(0L)开始");
        List<CategoryVO> list = recursionTree(parentId != null ? parentId : 0l, categoryList);
        return list;
    }


    /**
     *     TODO         递归构建树形分类结构
     *              私有方法，通过递归方式将平铺的分类列表转换为树形结构
     *
     * @param parentId 当前递归的父节点ID
     * @param categoryList 所有分类的平铺列表
     * @return List<CategoryVO> 当前父节点下的子树结构
     */
    private static List<CategoryVO> recursionTree(Long parentId, List<PmsCategory> categoryList) {
        List<CategoryVO> list = new ArrayList<>();

        log.info("使用Optional避免空指针，安全处理可能为空的分类列表");
        Optional.ofNullable(categoryList)
                .ifPresent(categories ->

                        // 过滤出当前父节点下的所有直接子分类
                        categories.stream().filter(category ->
                                category.getParentId().equals(parentId))   // 匹配父节点ID
                                .forEach(category -> {

                                    // 创建VO对象并复制属性
                                    CategoryVO categoryVO = new CategoryVO();
                                    BeanUtil.copyProperties(category, categoryVO);   // 使用BeanUtil进行属性拷贝

                                    // 递归获取当前分类的子分类（深度优先遍历）
                                    List<CategoryVO> children = recursionTree(category.getId(), categoryList);
                                    categoryVO.setChildren(children);   // 设置子节点列表

                                    list.add(categoryVO);  // 将当前节点添加到结果列表
                                }));
        return list;
    }


    /**
     *      TODO        获取分类列表（级联选择器格式）
     *              主要用于前端级联选择器组件的数据格式
     *
     * @return List<Option> 级联选择器格式的分类选项
     */
    @Override
    public List<Option> getCategoryOptions() {

        log.info("查询所有可见的分类数据，按排序字段升序排列（与树形列表排序方向不同）");
        List<PmsCategory> categoryList = this.list(
                new LambdaQueryWrapper<PmsCategory>()
                        .eq(PmsCategory::getVisible, GlobalConstants.STATUS_YES)   // 只查询可见的分类
                        .orderByAsc(PmsCategory::getSort)   // 按排序字段升序排列
        );

        log.info("递归构建级联选择器格式的数据结构");
        List<Option> list = recursionCascade(0l, categoryList);  // 从根节点(0L)开始构建
        return list;
    }


    /**
     *      TODO                递归构建级联选择器格式的分类数据
     *                      私有方法，构建符合前端级联选择器组件要求的数据结构
     *
     * @param parentId 当前递归的父节点ID
     * @param categoryList 所有分类的平铺列表
     * @return List<Option> 级联选择器格式的选项列表
     */
    private List<Option> recursionCascade(Long parentId, List<PmsCategory> categoryList) {
        List<Option> list = new ArrayList<>();

        log.info("使用Optional避免空指针，安全处理分类列表");
        Optional.ofNullable(categoryList)
                .ifPresent(categories ->


                        // 过滤出当前父节点下的所有直接子分类
                        categories.stream().filter(category ->   // 匹配父节点ID
                                category.getParentId().equals(parentId))


                                // 创建级联选择器选项对象
                                // Option构造函数参数：(value, label) - 值和使用名称
                                .forEach(category -> {
                                    Option categoryVO = new Option<>(category.getId(), category.getName());
                                    BeanUtil.copyProperties(category, categoryVO);

                                    // 递归获取当前分类的子分类
                                    List<Option> children = recursionCascade(category.getId(), categoryList);
                                    categoryVO.setChildren(children);  // 设置子选项列表


                                    list.add(categoryVO); // 将当前选项添加到结果列表
                                })
                );
        return list;
    }


    /**
     *   TODO       新增/修改分类
     *          保存分类信息，并清除相关的缓存数据
     *
     * @param category 分类实体对象
     * @return Long 保存后的分类ID
     *
     * 缓存说明：@CacheEvict在执行方法后清除指定缓存
     * value: 缓存名称；key：缓存键
     * 当分类数据发生变化时，清除树形分类列表的缓存，保证数据一致性
     */
    @CacheEvict(value = "pms", key = "'categoryList'")
    @Override
    public Long saveCategory(PmsCategory category) {

        log.info("保存或更新分类信息（MyBatis-Plus的saveOrUpdate方法会根据ID判断是新增还是更新）");
        this.saveOrUpdate(category);

        log.info("返回保存后的分类ID（新增时返回生成的ID，更新时返回原有ID）");
        return category.getId();
    }

    /**
     * 获取商品分类分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PmsCategoryVO>} 商品分类分页列表
     */
    @Override
    public IPage<PmsCategoryVO> getPmsCategoryPage(PmsCategoryQuery queryParams) {
        Page<PmsCategoryVO> pageVO = this.baseMapper.getPmsCategoryPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取商品分类表单数据
     *
     * @param id 商品分类ID
     * @return 商品分类表单数据
     */
    @Override
    public PmsCategoryForm getPmsCategoryFormData(Long id) {
        PmsCategory entity = this.getById(id);
        return pmsCategoryConverter.toForm(entity);
    }

    /**
     * 新增商品分类
     *
     * @param formData 商品分类表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePmsCategory(PmsCategoryForm formData) {
        PmsCategory entity = pmsCategoryConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新商品分类
     *
     * @param id   商品分类ID
     * @param formData 商品分类表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePmsCategory(Long id, PmsCategoryForm formData) {
        PmsCategory entity = pmsCategoryConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除商品分类
     *
     * @param ids 商品分类ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePmsCategorys(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的商品分类数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
