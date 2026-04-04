package com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.service;

import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.model.entity.ManagerMenuHomeBanner;
import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.model.form.ManagerMenuHomeBannerForm;
import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.model.query.ManagerMenuHomeBannerQuery;
import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.model.vo.ManagerMenuHomeBannerVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: ManagerMenuHomeBannerService
 * @Description TODO 管理端app首页滚播栏服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/4 15:42
 * @Version 1.0
 **/

public interface ManagerMenuHomeBannerService extends IService<ManagerMenuHomeBanner> {

    /**
     *管理端app首页滚播栏分页列表
     *
     * @return {@link IPage<ManagerMenuHomeBannerVo>} 管理端app首页滚播栏分页列表
     */
    IPage<ManagerMenuHomeBannerVo> getManagerMenuHomeBannerPage(ManagerMenuHomeBannerQuery queryParams);

    /**
     * 获取管理端app首页滚播栏表单数据
     *
     * @param id 管理端app首页滚播栏ID
     * @return 管理端app首页滚播栏表单数据
     */
    ManagerMenuHomeBannerForm getManagerMenuHomeBannerFormData(Long id);

    /**
     * 新增管理端app首页滚播栏
     *
     * @param formData 管理端app首页滚播栏表单对象
     * @return 是否新增成功
     */
    boolean saveManagerMenuHomeBanner(ManagerMenuHomeBannerForm formData);

    /**
     * 修改管理端app首页滚播栏
     *
     * @param id   管理端app首页滚播栏ID
     * @param formData 管理端app首页滚播栏表单对象
     * @return 是否修改成功
     */
    boolean updateManagerMenuHomeBanner(Long id, ManagerMenuHomeBannerForm formData);

    /**
     * 删除管理端app首页滚播栏
     *
     * @param ids 管理端app首页滚播栏ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteManagerMenuHomeBanners(String ids);
}
