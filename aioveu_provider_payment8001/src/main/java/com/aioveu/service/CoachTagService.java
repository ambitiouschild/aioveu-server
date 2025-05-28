package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.CoachTag;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface CoachTagService extends IService<CoachTag> {

    /**
     * 创建或更新tag
     * @param tags
     * @param coachId
     * @return
     */
    boolean createOrUpdateTag(String tags, Long coachId);




}
