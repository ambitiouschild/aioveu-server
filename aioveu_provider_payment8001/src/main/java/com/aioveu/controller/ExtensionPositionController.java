package com.aioveu.controller;

import com.aioveu.form.PushUserNearbyForm;
import com.aioveu.form.TopicCodeForm;
import com.aioveu.service.ExtensionPositionService;
import com.aioveu.vo.ExtensionPageCodeVo;
import com.aioveu.vo.ExtensionUserRangeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @description
 * @author: xiaoyao
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/extension-position")
public class ExtensionPositionController {

    @Autowired
    private ExtensionPositionService extensionPositionService;

    @PostMapping("getExtensionUser")
    public List<ExtensionUserRangeVo> getExtensionUserRangeSum(@RequestBody PushUserNearbyForm form) {
        return extensionPositionService.getExtensionUserRange(form);
    }

    @PostMapping("/code")
    public ExtensionPageCodeVo getTopicCode(@Valid @RequestBody TopicCodeForm form) {
        return extensionPositionService.getTopicCode(form);
    }

    @PostMapping("/record")
    public Boolean positionRecord(@Valid @RequestBody TopicCodeForm form) {
        return extensionPositionService.positionRecord(form, 1);
    }
}
