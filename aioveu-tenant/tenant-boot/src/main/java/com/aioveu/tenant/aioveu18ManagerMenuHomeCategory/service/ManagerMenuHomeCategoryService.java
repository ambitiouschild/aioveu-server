package com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.service;

import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.model.entity.ManagerMenuHomeCategory;
import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.model.form.ManagerMenuHomeCategoryForm;
import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.model.query.ManagerMenuHomeCategoryQuery;
import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.model.vo.ManagerMenuHomeCategoryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName: ManagerMenuHomeCategoryService
 * @Description TODO 管理端app首页分类配置服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/4 13:41
 * @Version 1.0
 **/

public interface ManagerMenuHomeCategoryService extends IService<ManagerMenuHomeCategory> {

    /**
     *管理端app首页分类配置分页列表
     *
     * @return {@link IPage<ManagerMenuHomeCategoryVo>} 管理端app首页分类配置分页列表
     */
    IPage<ManagerMenuHomeCategoryVo> getManagerMenuHomeCategoryPage(ManagerMenuHomeCategoryQuery queryParams);

    /**
     * 获取管理端app首页分类配置表单数据
     *
     * @param id 管理端app首页分类配置ID
     * @return 管理端app首页分类配置表单数据
     */
    ManagerMenuHomeCategoryForm getManagerMenuHomeCategoryFormData(Long id);

    /**
     * 新增管理端app首页分类配置
     *
     * @param formData 管理端app首页分类配置表单对象
     * @return 是否新增成功
     */
    boolean saveManagerMenuHomeCategory(ManagerMenuHomeCategoryForm formData);

    /**
     * 修改管理端app首页分类配置
     *
     * @param id   管理端app首页分类配置ID
     * @param formData 管理端app首页分类配置表单对象
     * @return 是否修改成功
     */
    boolean updateManagerMenuHomeCategory(Long id, ManagerMenuHomeCategoryForm formData);

    /**
     * 删除管理端app首页分类配置
     *
     * @param ids 管理端app首页分类配置ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteManagerMenuHomeCategorys(String ids);


    /*
    * 管理端app首页分类配置分页列表 ForApp
    * */
    List<ManagerMenuHomeCategoryVo> getManagerMenuHomeCategoryForApp();

}
