package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.ExerciseCountTemplate;
import com.aioveu.form.ExerciseCountTemplateForm;
import com.aioveu.vo.ExerciseCountPayVO;
import com.aioveu.vo.ExerciseCountTemplateVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface ExerciseCountTemplateService extends IService<ExerciseCountTemplate> {


    /**
     * 模板定期更新
     * @return
     */
    boolean periodicUpdate();

    /**
     * 模板创建
     * @param form
     * @param username
     * @return
     */
    String createTemplate(ExerciseCountTemplateForm form, String username);

    /**
     * 获取用户的活动模板
     * @param username
     * @param appId
     * @return
     */
    List<ExerciseCountTemplateVO> getUserTemplate(String username, String appId);

    /**
     * 删除
     * @param id
     * @return
     */
    boolean deleteById(String id);

    /**
     * 根据活动id获取模板id
     * @param exerciseId
     * @return
     */
    ExerciseCountTemplate getByExerciseId(Long exerciseId);

    /**
     * 获取按次活动模板支付信息
     * @param exerciseId
     * @return
     */
    ExerciseCountPayVO getPayInfo(Long exerciseId);

    /**
     * 查看详情
     * @param id
     * @return
     */
    ExerciseCountTemplateForm detail(String id);



}
