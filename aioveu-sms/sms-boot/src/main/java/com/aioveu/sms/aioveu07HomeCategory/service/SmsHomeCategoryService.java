package com.aioveu.sms.aioveu07HomeCategory.service;

import com.aioveu.sms.aioveu07HomeCategory.model.entity.SmsHomeCategory;
import com.aioveu.sms.aioveu07HomeCategory.model.form.SmsHomeCategoryForm;
import com.aioveu.sms.aioveu07HomeCategory.model.query.SmsHomeCategoryQuery;
import com.aioveu.sms.aioveu07HomeCategory.model.vo.SmsHomeCategoryVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName: SmsHomeCategoryService
 * @Description TODO 首页分类配置服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/4 12:16
 * @Version 1.0
 **/

public interface SmsHomeCategoryService extends IService<SmsHomeCategory> {


    /**
     * 获取首页分类配置列表
     *
     * @param
     * @return {@link IPage<SmsHomeCategoryVO>} 首页分类配置列表
     */
    List<SmsHomeCategoryVO> getSmsHomeCategoryList();

    /**
     *首页分类配置分页列表
     *
     * @return {@link IPage<SmsHomeCategoryVO>} 首页分类配置分页列表
     */
    IPage<SmsHomeCategoryVO> getSmsHomeCategoryPage(SmsHomeCategoryQuery queryParams);

    /**
     * 获取首页分类配置表单数据
     *
     * @param id 首页分类配置ID
     * @return 首页分类配置表单数据
     */
    SmsHomeCategoryForm getSmsHomeCategoryFormData(Long id);

    /**
     * 新增首页分类配置
     *
     * @param formData 首页分类配置表单对象
     * @return 是否新增成功
     */
    boolean saveSmsHomeCategory(SmsHomeCategoryForm formData);

    /**
     * 修改首页分类配置
     *
     * @param id   首页分类配置ID
     * @param formData 首页分类配置表单对象
     * @return 是否修改成功
     */
    boolean updateSmsHomeCategory(Long id, SmsHomeCategoryForm formData);

    /**
     * 删除首页分类配置
     *
     * @param ids 首页分类配置ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteSmsHomeCategorys(String ids);
}
