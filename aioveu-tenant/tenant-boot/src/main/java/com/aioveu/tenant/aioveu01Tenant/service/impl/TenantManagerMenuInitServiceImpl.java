package com.aioveu.tenant.aioveu01Tenant.service.impl;


import com.aioveu.common.tenant.TenantContextHolder;
import com.aioveu.tenant.aioveu01Tenant.service.TenantManagerMenuInitService;
import com.aioveu.tenant.aioveu01Tenant.service.TenantMenuService;
import com.aioveu.tenant.aioveu01Tenant.service.TenantPlanMenuService;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.entity.ManagerMenuCategory;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.service.ManagerMenuCategoryService;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.entity.ManagerMenuCategoryItem;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.service.ManagerMenuCategoryItemService;
import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.model.entity.ManagerMenuHomeCategory;
import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.service.ManagerMenuHomeCategoryService;
import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.model.entity.ManagerMenuHomeBanner;
import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.service.ManagerMenuHomeBannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: aaa
 * @Description TODO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/1 22:22
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class TenantManagerMenuInitServiceImpl implements TenantManagerMenuInitService {



    private final ManagerMenuCategoryService managerMenuCategoryService;

    private final ManagerMenuCategoryItemService managerMenuCategoryItemService;

    private final ManagerMenuHomeCategoryService managerMenuHomeCategoryService;
    private final ManagerMenuHomeBannerService managerMenuHomeBannerService;

    /**
     * 初始化租户管理端菜单（从平台模板复制）
     */
    @Override
    public void initTenantManagerMenu(Long tenantId) {

        Long oldTenantId = TenantContextHolder.getTenantId();
        boolean oldIgnoreTenant = TenantContextHolder.isIgnoreTenant();

        try {
            TenantContextHolder.setIgnoreTenant(true);

            // 1️复制平台菜单分类
            List<ManagerMenuCategory> platformCategories =
                    managerMenuCategoryService.listPlatformCategories();

            Map<Long, Long> categoryIdMapping = new HashMap<>();

            for (ManagerMenuCategory platform : platformCategories) {
                ManagerMenuCategory tenantCategory = new ManagerMenuCategory();
                tenantCategory.setTenantId(tenantId);
                tenantCategory.setTitle(platform.getTitle());
                tenantCategory.setIcon(platform.getIcon());
                tenantCategory.setSort(platform.getSort());
                tenantCategory.setStatus(platform.getStatus());
                tenantCategory.setIsDeleted(0);

                managerMenuCategoryService.save(tenantCategory);
                categoryIdMapping.put(platform.getId(), tenantCategory.getId());
            }

            // 2️复制平台菜单项
            List<ManagerMenuCategoryItem> platformItems =
                    managerMenuCategoryItemService.listPlatformItems();

            for (ManagerMenuCategoryItem platformItem : platformItems) {
                Long newCategoryId = categoryIdMapping.get(platformItem.getCategoryId());
                if (newCategoryId == null) {
                    continue;
                }

                ManagerMenuCategoryItem tenantItem = new ManagerMenuCategoryItem();
                tenantItem.setTenantId(tenantId);
                tenantItem.setCategoryId(newCategoryId);
                tenantItem.setTitle(platformItem.getTitle());
                tenantItem.setIcon(platformItem.getIcon());
                tenantItem.setUrl(platformItem.getUrl());
                tenantItem.setPathName(platformItem.getPathName());
                tenantItem.setDescription(platformItem.getDescription());
                tenantItem.setSort(platformItem.getSort());
                tenantItem.setIsDeleted(0);

                managerMenuCategoryItemService.save(tenantItem);
            }


            // 4.复制平台首页分类
            List<ManagerMenuHomeCategory> platformHomeCategories =
                    managerMenuHomeCategoryService.listPlatformHomeCategories();

            for (ManagerMenuHomeCategory platform : platformHomeCategories) {

                ManagerMenuHomeCategory tenantCategory = new ManagerMenuHomeCategory();
                tenantCategory.setTenantId(tenantId);
                tenantCategory.setCategoryId(platform.getCategoryId()); // ✅ 你前面算好的
                tenantCategory.setHomeIcon(platform.getHomeIcon());
                tenantCategory.setHomeName(platform.getHomeName());
                tenantCategory.setJumpPath(platform.getJumpPath());
                tenantCategory.setJumpType(platform.getJumpType());
                tenantCategory.setSort(platform.getSort());
                tenantCategory.setDeleted(0);

                managerMenuHomeCategoryService.save(tenantCategory);
            }


            // 5.复制平台首页滚播栏
            List<ManagerMenuHomeBanner> platformHomeBanners =
                    managerMenuHomeBannerService.listPlatformHomeBanners();

            for (ManagerMenuHomeBanner platform : platformHomeBanners) {

                ManagerMenuHomeBanner tenantBanner = new ManagerMenuHomeBanner();
                tenantBanner.setTenantId(tenantId);
                tenantBanner.setTitle(platform.getTitle());
                tenantBanner.setImageUrl(platform.getImageUrl());
                tenantBanner.setStartTime(platform.getStartTime());
                tenantBanner.setEndTime(platform.getEndTime());
                tenantBanner.setStatus(platform.getStatus());
                tenantBanner.setSort(platform.getSort());
                tenantBanner.setRedirectUrl(platform.getRedirectUrl());
                tenantBanner.setRemark(platform.getRemark());
                tenantBanner.setDeleted(0);

                managerMenuHomeBannerService.save(tenantBanner);
            }




        } finally {
            TenantContextHolder.setIgnoreTenant(oldIgnoreTenant);
            if (oldTenantId != null) {
                TenantContextHolder.setTenantId(oldTenantId);
            }
        }
    }

}
