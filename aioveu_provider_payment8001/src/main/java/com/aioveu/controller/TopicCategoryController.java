package com.aioveu.controller;

import com.aioveu.form.TopicCategoryForm;
import com.aioveu.service.TopicCategoryService;
import com.aioveu.vo.CategoryBaseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/23 0023 16:00
 */
@Slf4j
@RequestMapping("/api/v1/topic-category")
@RestController
public class TopicCategoryController {

    @Autowired
    private TopicCategoryService topicCategoryService;

    @GetMapping("/topic/{topicId}")
    public List<CategoryBaseVO> getByTopicId(@PathVariable Long topicId) {
        return topicCategoryService.getByTopicId(topicId);
    }

    @PostMapping("")
    public boolean create(@Valid @RequestBody TopicCategoryForm form) {
        return topicCategoryService.create(form);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Long id) {
        return topicCategoryService.deleteById(id);
    }

    @PutMapping("")
    public boolean update(@Valid @RequestBody TopicCategoryForm form) {
        return topicCategoryService.updateByForm(form);
    }

    @GetMapping("{id}")
    public TopicCategoryForm getById(@PathVariable Long id) {
        return topicCategoryService.getDetailById(id);
    }

}
