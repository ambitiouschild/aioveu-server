package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.GradeCancelOptions;

import java.util.List;

public interface GradeCancelOptionsService extends IService<GradeCancelOptions> {
    GradeCancelOptions getGradeCancelOptionsByName(Long companyId, String name);

    List<GradeCancelOptions> getGradeCancelOptionsByStatus(Long companyId, Integer status);

    void saveGradeCancelOptions(GradeCancelOptions gradeCancelOptions) throws Exception;

    boolean delGradeCancelOptions(Long id);
}
