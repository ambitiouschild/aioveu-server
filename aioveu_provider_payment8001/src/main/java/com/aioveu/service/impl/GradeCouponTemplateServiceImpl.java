package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.GradeCouponTemplateDao;
import com.aioveu.entity.GradeCouponTemplate;
import com.aioveu.enums.DataStatus;
import com.aioveu.service.GradeCouponTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class GradeCouponTemplateServiceImpl extends ServiceImpl<GradeCouponTemplateDao, GradeCouponTemplate> implements GradeCouponTemplateService {


    @Override
    public List<GradeCouponTemplate> getCouponTemplateIdListByGradeId(Long gradeId) {
        QueryWrapper<GradeCouponTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GradeCouponTemplate::getGradeId, gradeId)
                .eq(GradeCouponTemplate::getStatus, DataStatus.NORMAL.getCode());
        return list(queryWrapper);
    }
}
