package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.Agreement;
import com.aioveu.vo.AgreementDetailVO;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface AgreementService extends IService<Agreement> {

    /**
     * 获取列表
     * @param page
     * @param size
     * @param companyId
     * @return
     */
    IPage<AgreementDetailVO> getManagerAll(int page, int size, Long companyId);

    /**
     * 删除图片
     * @param id
     * @return
     */
    boolean deleteImage(Long id);

    /**
     * 管理平台活动详情
     * @param id
     * @return
     */
    AgreementDetailVO managerDetail(Long id);


}
