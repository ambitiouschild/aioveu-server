package com.aioveu.tenant.aioveu16ManagerMenuCategory.service;

import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.entity.ManagerMenuCategory;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.form.ManagerMenuCategoryForm;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.query.ManagerMenuCategoryQuery;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.vo.ManagerMenuCategoryVo;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.vo.ManagerMenuCategoryWithItemsVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName: ManagerMenuCategoryService
 * @Description TODO 管理端菜单分类（多租户）服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/3 17:13
 * @Version 1.0
 **/

public interface ManagerMenuCategoryService extends IService<ManagerMenuCategory> {

    /**
     *管理端菜单分类（多租户）分页列表
     *
     * @return {@link IPage<ManagerMenuCategoryVo>} 管理端菜单分类（多租户）分页列表
     */
    IPage<ManagerMenuCategoryVo> getManagerMenuCategoryPage(ManagerMenuCategoryQuery queryParams);

    /**
     * 获取管理端菜单分类（多租户）表单数据
     *
     * @param id 管理端菜单分类（多租户）ID
     * @return 管理端菜单分类（多租户）表单数据
     */
    ManagerMenuCategoryForm getManagerMenuCategoryFormData(Long id);

    /**
     * 新增管理端菜单分类（多租户）
     *
     * @param formData 管理端菜单分类（多租户）表单对象
     * @return 是否新增成功
     */
    boolean saveManagerMenuCategory(ManagerMenuCategoryForm formData);

    /**
     * 修改管理端菜单分类（多租户）
     *
     * @param id   管理端菜单分类（多租户）ID
     * @param formData 管理端菜单分类（多租户）表单对象
     * @return 是否修改成功
     */
    boolean updateManagerMenuCategory(Long id, ManagerMenuCategoryForm formData);

    /**
     * 删除管理端菜单分类（多租户）
     *
     * @param ids 管理端菜单分类（多租户）ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteManagerMenuCategorys(String ids);


    /**
     * 获取用户的工作台菜单（包含分类和菜单项）
     */
    List<ManagerMenuCategoryWithItemsVO>  getManagerMenuCategoriesWithItems();
}
