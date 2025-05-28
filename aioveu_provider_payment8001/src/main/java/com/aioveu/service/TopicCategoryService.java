package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.TopicCategory;
import com.aioveu.entity.TopicImage;
import com.aioveu.form.TopicCategoryForm;
import com.aioveu.vo.CategoryBaseVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface TopicCategoryService extends IService<TopicCategory> {

    /**
     * 查找主题下的分类
     * @param topicId
     * @return
     */
    List<CategoryBaseVO> getByTopicId(Long topicId);

    /**
     * 创建主题二级分类
     * @param form
     * @return
     */
    boolean create(TopicCategoryForm form);

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    boolean deleteById(Long id);

    /**
     * 根据id查找
     * @param id
     * @return
     */
    TopicCategoryForm getDetailById(Long id);

    /**
     * 更新主题二级分类
     * @param form
     * @return
     */
    boolean updateByForm(TopicCategoryForm form);



}
