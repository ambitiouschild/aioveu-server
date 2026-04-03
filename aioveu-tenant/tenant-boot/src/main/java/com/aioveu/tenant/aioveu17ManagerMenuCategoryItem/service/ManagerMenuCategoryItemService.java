package com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.service;

import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.entity.ManagerMenuCategoryItem;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.form.ManagerMenuCategoryItemForm;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.query.ManagerMenuCategoryItemQuery;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.vo.ManagerMenuCategoryItemVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: ManagerMenuCategoryItemService
 * @Description TODO 管理系统工作台菜单项（多租户支持）服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/3 17:28
 * @Version 1.0
 **/

public interface ManagerMenuCategoryItemService extends IService<ManagerMenuCategoryItem> {

    /**
     *管理系统工作台菜单项（多租户支持）分页列表
     *
     * @return {@link IPage<ManagerMenuCategoryItemVo>} 管理系统工作台菜单项（多租户支持）分页列表
     */
    IPage<ManagerMenuCategoryItemVo> getManagerMenuCategoryItemPage(ManagerMenuCategoryItemQuery queryParams);

    /**
     * 获取管理系统工作台菜单项（多租户支持）表单数据
     *
     * @param id 管理系统工作台菜单项（多租户支持）ID
     * @return 管理系统工作台菜单项（多租户支持）表单数据
     */
    ManagerMenuCategoryItemForm getManagerMenuCategoryItemFormData(Long id);

    /**
     * 新增管理系统工作台菜单项（多租户支持）
     *
     * @param formData 管理系统工作台菜单项（多租户支持）表单对象
     * @return 是否新增成功
     */
    boolean saveManagerMenuCategoryItem(ManagerMenuCategoryItemForm formData);

    /**
     * 修改管理系统工作台菜单项（多租户支持）
     *
     * @param id   管理系统工作台菜单项（多租户支持）ID
     * @param formData 管理系统工作台菜单项（多租户支持）表单对象
     * @return 是否修改成功
     */
    boolean updateManagerMenuCategoryItem(Long id, ManagerMenuCategoryItemForm formData);

    /**
     * 删除管理系统工作台菜单项（多租户支持）
     *
     * @param ids 管理系统工作台菜单项（多租户支持）ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteManagerMenuCategoryItems(String ids);
}
