package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.entity.ExerciseCountTemplate;
import com.aioveu.vo.ExerciseCountPayVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface ExerciseCountTemplateDao extends BaseMapper<ExerciseCountTemplate> {

    /**
     * 保存活动和模板的id
     * @param exerciseId
     * @param templateId
     * @return
     */
    int saveExerciseCountTemplateId(Long exerciseId, String templateId);

    /**
     * 获取用户模板
     * @param userId
     * @param companyId
     * @return
     */
    List<ExerciseCountTemplate> getUserTemplate(String userId, Long companyId);

    /**
     * 通过活动id获取对应模板id
     * @param exerciseId
     * @return
     */
    String getExerciseCountTemplateIdByExerciseId(Long exerciseId);

    /**
     * 获取按次活动模板支付信息
     * @param exerciseId
     * @return
     */
    ExerciseCountPayVO getPayInfo(Long exerciseId);

}
