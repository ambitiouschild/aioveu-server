package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.TopicImageDao;
import com.aioveu.entity.TopicImage;
import com.aioveu.service.TopicImageService;
import com.aioveu.utils.FileUtil;
import com.aioveu.utils.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class TopicImageServiceImpl extends ServiceImpl<TopicImageDao, TopicImage> implements TopicImageService {


    @Override
    public boolean deleteImageByTopicId(Long topicId) {
        QueryWrapper<TopicImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TopicImage::getTopicId, topicId);
        List<TopicImage> topicImageList = list(queryWrapper);
        if (CollectionUtils.isNotEmpty(topicImageList)) {
            topicImageList.forEach(item -> {
                OssUtil.deleteFile(item.getUrl());
            });
        }
        return true;
    }

    @Override
    public List<TopicImage> getByTopicId(Long topicId) {
        QueryWrapper<TopicImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TopicImage::getTopicId, topicId)
                .orderByAsc(TopicImage::getPriority);
        List<TopicImage> list = list(queryWrapper);
        list.forEach(item -> {
            item.setUrl(FileUtil.getImageFullUrl(item.getUrl()));
        });
        return list;
    }

    @Override
    public boolean deleteById(Long id) {
        TopicImage topicImage = getById(id);
        if (topicImage != null) {
            OssUtil.deleteFile(topicImage.getUrl());
            return removeById(id);
        }
        return false;
    }
}
