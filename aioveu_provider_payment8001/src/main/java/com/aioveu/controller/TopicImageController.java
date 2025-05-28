package com.aioveu.controller;

import com.aioveu.entity.TopicImage;
import com.aioveu.service.TopicImageService;
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
@RequestMapping("/api/v1/topic-image")
@RestController
public class TopicImageController {

    @Autowired
    private TopicImageService topicImageService;

    @GetMapping("/topic/{topicId}")
    public List<TopicImage> getByTopicId(@PathVariable Long topicId) {
        return topicImageService.getByTopicId(topicId);
    }

    @PostMapping("")
    public boolean create(@Valid @RequestBody TopicImage topicImage) {
        return topicImageService.save(topicImage);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Long id) {
        return topicImageService.deleteById(id);
    }
}
