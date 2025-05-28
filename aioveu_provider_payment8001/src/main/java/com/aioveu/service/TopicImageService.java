package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.TopicImage;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface TopicImageService extends IService<TopicImage> {

    /**
     * 删除主题对应的图片
     * @param topicId
     * @return
     */
    boolean deleteImageByTopicId(Long topicId);

    /**
     * 查询主题的图片
     * @param topicId
     * @return
     */
    List<TopicImage> getByTopicId(Long topicId);

    /**
     * id删除
     * @param id
     * @return
     */
    boolean deleteById(Long id);

}
