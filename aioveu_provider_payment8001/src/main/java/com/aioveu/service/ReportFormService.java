package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.ReportForm;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface ReportFormService extends IService<ReportForm> {

    /**
     * 创建
     * @param reportForm
     * @return
     */
    boolean create(ReportForm reportForm, String username);


    /**
     * 获取报单列表
     * @param page
     * @param size
     * @param storeId
     * @return
     */
    IPage<ReportForm> getList(int page, int size, Long storeId, String username);

    /**
     * 已读
     * @param ids
     * @return
     */
    boolean read(List<Long> ids);

    /**
     * 批量删除
     * @param ids
     * @param username
     * @return
     */
    boolean batchDelete(List<Long> ids, String username);


}
