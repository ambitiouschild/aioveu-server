package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.Company;
import com.aioveu.form.CompanySettingForm;
import com.aioveu.vo.api.CompanyItemVO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface CompanyService extends IService<Company> {

    /**
     * 获取用户列表
     * @param page
     * @param size
     * @param status
     * @param keyword
     * @return
     */
    IPage<CompanyItemVO> getAll(int page, int size, Integer status, String keyword);

    /**
     * 更新状态
     * @param id
     * @param status
     * @return
     */
    boolean changeStatus(Long id, Integer status);

    /**
     * 公司设置
     * @param data
     * @return
     */
    boolean updateCompanySetting(CompanySettingForm data);

    /**
     * 获取微信小程序支付appId
     * @param companyId
     * @return
     */
    String getMiniAppPayIdById(Long companyId);

    /**
     * 通过小程序id查找对应公司
     * @param appId
     * @return
     */
    Company getCompanyByMiniAppId(String appId);

    /**
     * 通过appId获取公司id
     * @param appId
     * @return
     */
    Long getCompanyIdByAppId(String appId);
    /**
     * 根据小程序id获取公司id
     * @param miniAppId
     * @return
     */
    Long getIdByAppId(String miniAppId);

    /**
     * 根据username获取有效的、有店铺的公司信息
     * @param username
     * @return
     */
    List<CompanyItemVO> getUserValidCompanys(@PathVariable String username);

    /**
     * 根据门店id获取公司信息
     * @param storeId
     * @return
     */
    Company getOneByStoreId(Long storeId);

    /**
     * 获取公司设置
     * @param id
     * @return
     */
    CompanySettingForm getCompanySettingForm(Long id);

}
