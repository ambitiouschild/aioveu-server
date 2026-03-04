package com.aioveu.sms.aioveu08HomeAdvert.service;

import com.aioveu.sms.aioveu07HomeCategory.model.vo.SmsHomeCategoryVO;
import com.aioveu.sms.aioveu08HomeAdvert.model.entity.SmsHomeAdvert;
import com.aioveu.sms.aioveu08HomeAdvert.model.form.SmsHomeAdvertForm;
import com.aioveu.sms.aioveu08HomeAdvert.model.query.SmsHomeAdvertQuery;
import com.aioveu.sms.aioveu08HomeAdvert.model.vo.SmsHomeAdvertVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName: SmsHomeAdvertService
 * @Description TODO 首页广告配置（增加跳转路径）服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/4 12:37
 * @Version 1.0
 **/

public interface SmsHomeAdvertService extends IService<SmsHomeAdvert> {


    /**
     * 获取首页广告配置列表
     *
     * @param
     * @return {@link IPage< SmsHomeCategoryVO >} 首页广告配置列表
     */
    List<SmsHomeAdvertVO> getSmsHomeAdvertList();

    /**
     *首页广告配置（增加跳转路径）分页列表
     *
     * @return {@link IPage<SmsHomeAdvertVO>} 首页广告配置（增加跳转路径）分页列表
     */
    IPage<SmsHomeAdvertVO> getSmsHomeAdvertPage(SmsHomeAdvertQuery queryParams);

    /**
     * 获取首页广告配置（增加跳转路径）表单数据
     *
     * @param id 首页广告配置（增加跳转路径）ID
     * @return 首页广告配置（增加跳转路径）表单数据
     */
    SmsHomeAdvertForm getSmsHomeAdvertFormData(Long id);

    /**
     * 新增首页广告配置（增加跳转路径）
     *
     * @param formData 首页广告配置（增加跳转路径）表单对象
     * @return 是否新增成功
     */
    boolean saveSmsHomeAdvert(SmsHomeAdvertForm formData);

    /**
     * 修改首页广告配置（增加跳转路径）
     *
     * @param id   首页广告配置（增加跳转路径）ID
     * @param formData 首页广告配置（增加跳转路径）表单对象
     * @return 是否修改成功
     */
    boolean updateSmsHomeAdvert(Long id, SmsHomeAdvertForm formData);

    /**
     * 删除首页广告配置（增加跳转路径）
     *
     * @param ids 首页广告配置（增加跳转路径）ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteSmsHomeAdverts(String ids);
}
