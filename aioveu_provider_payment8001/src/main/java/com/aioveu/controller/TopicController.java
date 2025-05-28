package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.form.TopicForm;
import com.aioveu.service.CodeService;
import com.aioveu.service.TopicService;
import com.aioveu.vo.TopicBaseVO;
import com.aioveu.vo.TopicDetailVO;
import com.aioveu.vo.TopicItemVO;
import com.aioveu.vo.TopicStoreItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/23 0023 16:00
 */
@Slf4j
@RequestMapping("/api/v1/topic")
@RestController
public class TopicController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private CodeService codeService;

    @GetMapping("/category")
    public IPage<TopicBaseVO> categoryList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                   @RequestParam(required = false, defaultValue = "10") Integer size,
                                   @RequestParam(required = false) Long categoryId,
                                   @RequestParam(required = false) String userId) {
        return topicService.getTopicListByCategoryId(page, size, categoryId, userId);
    }

    @GetMapping("/store-list")
    public IPage<TopicStoreItemVO> storeCategoryList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                    @RequestParam(required = false, defaultValue = "10") Integer size,
                                                    @RequestParam(required = false) Long categoryId) {
        return topicService.getStoreCategoryList(page, size, categoryId);
    }

    @GetMapping("")
    public IPage<TopicItemVO> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                   @RequestParam(required = false, defaultValue = "10") Integer size,
                                   @RequestParam(required = false) Long categoryId) {
        return topicService.getList(page, size, categoryId);
    }

    @PostMapping("")
    public boolean create(@Valid @RequestBody TopicForm form) {
        return topicService.create(form);
    }

    @PutMapping("")
    public boolean update(@Valid @RequestBody TopicForm form) {
        return topicService.updateTopic(form);
    }

    @PostMapping("/upAndLow")
    public boolean upAndLow(@RequestBody TopicForm form) {
        return topicService.upAndLow(form);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Long id) {
        return topicService.deleteById(id);
    }

    @GetMapping("{id}")
    public TopicForm getById(@PathVariable Long id) {
        return topicService.getDetailById(id);
    }

    @GetMapping("/recommend")
    public TopicBaseVO recommend() {
        return topicService.recommend(OauthUtils.getCurrentUsername());
    }

    @GetMapping("/mini-app/{id}")
    public TopicDetailVO miniAppDetail(@PathVariable Long id, @RequestParam(required = false) String pushUserId
            , @RequestParam(required = false) String userId) {
        return topicService.miniAppDetail(id, pushUserId, userId);
    }

    /**
     * 主题详情页二维码
     * @param id
     * @return
     */
    @GetMapping("/topic-code/{id}")
    public String topicCode(@PathVariable Long id) {
        return codeService.topicPageCode(id);
    }

}
