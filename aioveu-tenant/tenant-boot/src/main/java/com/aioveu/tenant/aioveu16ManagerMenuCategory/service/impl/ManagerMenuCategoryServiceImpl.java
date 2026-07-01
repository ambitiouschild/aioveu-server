package com.aioveu.tenant.aioveu16ManagerMenuCategory.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.tenant.TenantContextHolder;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.converter.ManagerMenuCategoryConverter;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.mapper.ManagerMenuCategoryMapper;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.entity.ManagerMenuCategory;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.form.ManagerMenuCategoryForm;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.query.ManagerMenuCategoryQuery;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.vo.ManagerMenuCategoryVo;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.vo.ManagerMenuCategoryWithItemsVO;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.service.ManagerMenuCategoryService;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.entity.ManagerMenuCategoryItem;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.service.ManagerMenuCategoryItemService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: ManagerMenuCategoryServiceImpl
 * @Description TODO 管理端菜单分类（多租户）服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/3 17:14
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerMenuCategoryServiceImpl extends ServiceImpl<ManagerMenuCategoryMapper, ManagerMenuCategory> implements ManagerMenuCategoryService {

    private final ManagerMenuCategoryConverter managerMenuCategoryConverter;

    private final ManagerMenuCategoryItemService managerMenuCategoryItemService;

    /**
     * 获取管理端菜单分类（多租户）分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<ManagerMenuCategoryVo>} 管理端菜单分类（多租户）分页列表
     */
    @Override
    public IPage<ManagerMenuCategoryVo> getManagerMenuCategoryPage(ManagerMenuCategoryQuery queryParams) {
        Page<ManagerMenuCategoryVo> page = this.baseMapper.getManagerMenuCategoryPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取管理端菜单分类（多租户）表单数据
     *
     * @param id 管理端菜单分类（多租户）ID
     * @return 管理端菜单分类（多租户）表单数据
     */
    @Override
    public ManagerMenuCategoryForm getManagerMenuCategoryFormData(Long id) {
        ManagerMenuCategory entity = this.getById(id);
        return managerMenuCategoryConverter.toForm(entity);
    }

    /**
     * 新增管理端菜单分类（多租户）
     *
     * @param formData 管理端菜单分类（多租户）表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveManagerMenuCategory(ManagerMenuCategoryForm formData) {
        ManagerMenuCategory entity = managerMenuCategoryConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新管理端菜单分类（多租户）
     *
     * @param id   管理端菜单分类（多租户）ID
     * @param formData 管理端菜单分类（多租户）表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateManagerMenuCategory(Long id,ManagerMenuCategoryForm formData) {
        ManagerMenuCategory entity = managerMenuCategoryConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除管理端菜单分类（多租户）
     *
     * @param ids 管理端菜单分类（多租户）ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteManagerMenuCategorys(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的管理端菜单分类（多租户）数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }


    /**
     * 获取用户的工作台菜单（包含分类和菜单项）
     */
    @Override
    public List<ManagerMenuCategoryWithItemsVO>  getManagerMenuCategoriesWithItems() {

        Long tenantId = TenantContextHolder.getTenantId();
        Assert.notNull(tenantId, "租户ID不能为空");


        /**
         * 工作台菜单获取规则：
         * 1. 优先使用当前租户配置（由 MP 自动注入 tenantId）
         * 2. 若租户未配置，则使用平台默认（tenant_id = 0）
         * 3. 分类与菜单项均遵循此规则
         */

        // 1. 查询启用的分类  // 1. 查询租户自己的分类
        LambdaQueryWrapper<ManagerMenuCategory> categoryQuery = new LambdaQueryWrapper<>();
        categoryQuery.eq(ManagerMenuCategory::getStatus, 1)
                .eq(ManagerMenuCategory::getIsDeleted, 0)
                .orderByAsc(ManagerMenuCategory::getSort);

        List<ManagerMenuCategory> categories = this.list(categoryQuery);


        // 2. 租户没有，用平台默认
        if (categories.isEmpty()) {
            TenantContextHolder.setIgnoreTenant(true);
            try {
                categories = this.list(
                        new LambdaQueryWrapper<ManagerMenuCategory>()
                                .eq(ManagerMenuCategory::getTenantId, 0L)
                                .eq(ManagerMenuCategory::getStatus, 1)
                                .eq(ManagerMenuCategory::getIsDeleted, 0)
                );
            } finally {
                TenantContextHolder.clear();
            }
        }

        if (categories.isEmpty()) {
            return List.of();
        }

        log.info("【ManagerMenuCategory】查询启用的分类：{}",categories);


        // 2. 获取分类ID列表
        List<Long> categoryIds = categories.stream()
                .map(ManagerMenuCategory::getId)
                .toList();

        log.info("【ManagerMenuCategory】获取分类ID列表：{}",categoryIds);


        List<ManagerMenuCategoryItem> managerMenuCategoryItems =
                managerMenuCategoryItemService.getManagerMenuCategoryItemsWithCategoryIds(categoryIds);

        // 4. 按分类ID分组菜单项
        Map<Long, List<ManagerMenuCategoryItem>> itemsByCategory = managerMenuCategoryItems.stream()
                .collect(Collectors.groupingBy(ManagerMenuCategoryItem::getCategoryId));

        // 3. 查询菜单项（租户优先 + 平台兜底）
        List<ManagerMenuCategoryWithItemsVO>  managerMenuCategoryWithItems=   categories.stream()
                .map(category -> {
                    ManagerMenuCategoryWithItemsVO vo = new ManagerMenuCategoryWithItemsVO();
                    vo.setId(category.getId());
                    vo.setTitle(category.getTitle());
                    vo.setIcon(category.getIcon());
                    vo.setSort(category.getSort());
                    vo.setStatus(category.getStatus());

                    // 获取该分类的菜单项
                    List<ManagerMenuCategoryWithItemsVO.MenuItemVO> itemVOs = itemsByCategory
                            .getOrDefault(category.getId(), List.of())
                            .stream()
                            .map(managerMenuCategoryConverter::convertToMenuItemVO)
                            .collect(Collectors.toList());

                    vo.setChildren(itemVOs);
                    return vo;
                })
                .filter(category -> !category.getChildren().isEmpty()) // 只返回有菜单项的分类
                .collect(Collectors.toList());

        return managerMenuCategoryWithItems;
    }





    @Override
    public List<ManagerMenuCategory> listPlatformCategories() {
        TenantContextHolder.setIgnoreTenant(true);
        try {
            return this.list(
                    new LambdaQueryWrapper<ManagerMenuCategory>()
                            .eq(ManagerMenuCategory::getTenantId, 0L)
                            .eq(ManagerMenuCategory::getIsDeleted, 0)
                            .orderByAsc(ManagerMenuCategory::getSort)
            );
        } finally {
            TenantContextHolder.setIgnoreTenant(false);
        }
    }

}
