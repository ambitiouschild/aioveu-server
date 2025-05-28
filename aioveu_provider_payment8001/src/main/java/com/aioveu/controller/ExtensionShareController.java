package com.aioveu.controller;

import com.aioveu.entity.ExtensionShare;
import com.aioveu.service.ExtensionShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description
 * @author: xiaoyao
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/extension-share")
public class ExtensionShareController {

    @Autowired
    private ExtensionShareService extensionShareService;

    @PostMapping("recodeShare")
    public void recodeShare(@RequestBody ExtensionShare dataDTO) {
        extensionShareService.recodeShare(dataDTO);
    }

    @GetMapping("")
    public int countShareCondition(@RequestParam(required = false) String userId,
                                    @RequestParam(required = false) String themeId) {
        return extensionShareService.selCountShareCondition(userId, themeId);
    }

}
