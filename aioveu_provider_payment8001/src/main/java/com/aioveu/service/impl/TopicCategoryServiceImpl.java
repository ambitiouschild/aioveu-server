package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.TopicCategoryDao;
import com.aioveu.entity.TopicCategory;
import com.aioveu.enums.TopicStatus;
import com.aioveu.form.TopicCategoryForm;
import com.aioveu.service.TopicCategoryService;
import com.aioveu.utils.FileUtil;
import com.aioveu.vo.CategoryBaseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class TopicCategoryServiceImpl extends ServiceImpl<TopicCategoryDao, TopicCategory> implements TopicCategoryService {


    @Override
    public List<CategoryBaseVO> getByTopicId(Long topicId) {
        QueryWrapper<TopicCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TopicCategory::getTopicId, topicId).eq(TopicCategory::getStatus, TopicStatus.REVIEWED.getCode());
        return list(queryWrapper).stream().map(item -> {
            CategoryBaseVO categoryBaseVO = new CategoryBaseVO();
            BeanUtils.copyProperties(item, categoryBaseVO);
            categoryBaseVO.setCover(FileUtil.getImageFullUrl(item.getIcon()));
            return categoryBaseVO;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean create(TopicCategoryForm form) {
        TopicCategory topicCategory = new TopicCategory();
        BeanUtils.copyProperties(form, topicCategory);
        return save(topicCategory);
    }

    @Override
    public boolean deleteById(Long id) {
        TopicCategory topicCategory = getById(id);
        if (topicCategory != null) {
            //OssUtil.deleteFile(topicCategory.getIcon());
            topicCategory.setStatus(TopicStatus.DELETE.getCode());
            getBaseMapper().updateById(topicCategory);
            return true;
        }
        return false;
    }

    @Override
    public TopicCategoryForm getDetailById(Long id) {
        TopicCategory topicCategory = getById(id);
        if (topicCategory != null) {
            TopicCategoryForm form = new TopicCategoryForm();
            BeanUtils.copyProperties(topicCategory, form);
            form.setIcon(FileUtil.getImageFullUrl(topicCategory.getIcon()));
            return form;
        }
        return null;
    }

    @Override
    public boolean updateByForm(TopicCategoryForm form) {
        TopicCategory topicCategory = new TopicCategory();
        BeanUtils.copyProperties(form, topicCategory);
        return updateById(topicCategory);
    }
}
