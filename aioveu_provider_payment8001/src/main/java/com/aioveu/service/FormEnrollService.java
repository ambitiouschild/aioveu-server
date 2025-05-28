package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.FormEnroll;
import com.aioveu.form.FormEnrollForm;
import com.aioveu.vo.FormEnrollManagerItemVO;
import com.aioveu.vo.FormEnrollVO;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface FormEnrollService extends IService<FormEnroll> {

    /**
     * 创建报名活动
     * @param formEnrollForm
     * @return
     */
    boolean create(FormEnrollForm formEnrollForm);

    /**
     * 获取报名表单详情
     * @param id
     * @return
     */
    FormEnrollVO getDetail(Long id);

    /**
     * 列表分页
     * @param page
     * @param size
     * @return
     */
    IPage<FormEnrollManagerItemVO> pageList(Integer page, Integer size);

    /**
     * 更新状态
     * @param id
     * @param status
     * @return
     */
    boolean changeStatus(Long id, Integer status);

}
