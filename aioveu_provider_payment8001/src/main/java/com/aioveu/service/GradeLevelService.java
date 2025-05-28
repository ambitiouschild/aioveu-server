package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.GradeLevel;
import com.aioveu.vo.IdNameVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface GradeLevelService extends IService<GradeLevel> {

    /**
     * 根据店铺id查找列表
     * @param storeId
     * @return
     */
    List<IdNameVO> getByStoreId(Long storeId);

}
