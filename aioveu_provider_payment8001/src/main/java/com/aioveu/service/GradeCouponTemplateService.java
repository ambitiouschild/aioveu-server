package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.GradeCouponTemplate;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface GradeCouponTemplateService extends IService<GradeCouponTemplate> {

    /**
     * 查找班级绑定的优惠券模板id
     * @param gradeId
     * @return
     */
    List<GradeCouponTemplate> getCouponTemplateIdListByGradeId(Long gradeId);


}
