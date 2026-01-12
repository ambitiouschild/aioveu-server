package com.aioveu.sms.aioveu01Advert.service;

import com.aioveu.sms.aioveu01Advert.model.form.SmsAdvertForm;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.sms.aioveu01Advert.model.entity.SmsAdvert;
import com.aioveu.sms.aioveu01Advert.model.query.SmsAdvertQuery;
import com.aioveu.sms.aioveu01Advert.model.vo.SmsAdvertVO;
import com.aioveu.sms.aioveu01Advert.model.vo.BannerVO;

import java.util.List;

public interface SmsAdvertService extends IService<SmsAdvert> {

    /**
     * 广告分页列表
     *
     * @param queryParams
     * @return
     */
    Page<SmsAdvertVO> getAdvertPage(SmsAdvertQuery queryParams);

    List<BannerVO> getBannerList();

    /**
     *广告分页列表
     *
     * @return {@link IPage<SmsAdvertVO>} 广告分页列表
     */
    IPage<SmsAdvertVO> getSmsAdvertPage(SmsAdvertQuery queryParams);

    /**
     * 获取广告表单数据
     *
     * @param id 广告ID
     * @return 广告表单数据
     */
    SmsAdvertForm getSmsAdvertFormData(Long id);

    /**
     * 新增广告
     *
     * @param formData 广告表单对象
     * @return 是否新增成功
     */
    boolean saveSmsAdvert(SmsAdvertForm formData);

    /**
     * 修改广告
     *
     * @param id   广告ID
     * @param formData 广告表单对象
     * @return 是否修改成功
     */
    boolean updateSmsAdvert(Long id, SmsAdvertForm formData);

    /**
     * 删除广告
     *
     * @param ids 广告ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteSmsAdverts(String ids);
}
