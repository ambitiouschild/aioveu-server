package com.aioveu.controller;

import com.aioveu.entity.GroupLuck;
import com.aioveu.service.GroupLuckService;
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
@RequestMapping("/api/v1/group-luck")
@RestController
public class GroupLuckController {

    @Autowired
    private GroupLuckService groupLuckService;

    @GetMapping("")
    public List<GroupLuck> list() {
        return groupLuckService.list();
    }

    @PostMapping("")
    public GroupLuck luck(@Valid @RequestBody GroupLuck groupLuck){
        return groupLuckService.luck(groupLuck.getUuid(), groupLuck.getName(), groupLuck.getUsername());
    }

}
