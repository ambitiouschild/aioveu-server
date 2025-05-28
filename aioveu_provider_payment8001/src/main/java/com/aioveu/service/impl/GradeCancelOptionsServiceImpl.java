package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.GradeCancelOptionsDao;
import com.aioveu.entity.GradeCancelOptions;
import com.aioveu.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GradeCancelOptionsServiceImpl extends ServiceImpl<GradeCancelOptionsDao, GradeCancelOptions>  implements GradeCancelOptionsService {
    @Autowired
    private CompanyService companyService;

    @Override
    public GradeCancelOptions getGradeCancelOptionsByName(Long companyId, String name) {
        if (companyId == null) return null;
        QueryWrapper<GradeCancelOptions> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GradeCancelOptions::getName, name);
        queryWrapper.and(wrapper -> {
            wrapper.lambda().isNull(GradeCancelOptions::getCompanyId)
                    .or().eq(GradeCancelOptions::getCompanyId, companyId);
        });
        return getOne(queryWrapper);
    }

    @Override
    public List<GradeCancelOptions> getGradeCancelOptionsByStatus(Long companyId, Integer status) {
        QueryWrapper<GradeCancelOptions> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GradeCancelOptions::getStatus, status);
        queryWrapper.and(wrapper -> {
            wrapper.lambda().isNull(GradeCancelOptions::getCompanyId)
                    .or().eq(GradeCancelOptions::getCompanyId, companyId);
        });
        return list(queryWrapper);
    }

    @Override
    public void saveGradeCancelOptions(GradeCancelOptions gradeCancelOptions) throws Exception {
        if(StringUtils.isEmpty(gradeCancelOptions.getName())) {
            throw new Exception("取消原因不能为空");
        }
        if(gradeCancelOptions.getCouponExtendDays() == null) {
            gradeCancelOptions.setCouponExtendDays(0);
        }
        GradeCancelOptions gradeCancelOptionsByName = getGradeCancelOptionsByName(gradeCancelOptions.getCompanyId(), gradeCancelOptions.getName());
        if (gradeCancelOptionsByName != null && (gradeCancelOptions.getId() == null || !gradeCancelOptionsByName.getId().equals(gradeCancelOptions.getId()))) {
            throw new Exception("取消原因已经存在");
        }
        if (gradeCancelOptions.getId() != null) {
            GradeCancelOptions gradeCancelOptionsById = getById(gradeCancelOptions.getId());
            gradeCancelOptionsById.setName(gradeCancelOptions.getName());
            gradeCancelOptionsById.setCouponExtendDays(gradeCancelOptions.getCouponExtendDays());
            this.updateById(gradeCancelOptionsById);
        } else {
            this.save(gradeCancelOptions);
        }
    }

    @Override
    public boolean delGradeCancelOptions(Long id) {
        return this.removeById(id);
    }

}
