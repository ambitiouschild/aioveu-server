package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.WaterPoolSaleGroupUser;
import com.aioveu.service.WaterPoolSaleGroupUserService;
import com.aioveu.vo.WaterPoolSaleGroupUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequestMapping("/api/v1/water_pool_sale_group_user")
@RestController
public class WaterPoolSaleGroupUserController {

    @Autowired
    private WaterPoolSaleGroupUserService userGroupService;

    @GetMapping("")
    public IPage<WaterPoolSaleGroupUserVO> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                @RequestParam(required = false, defaultValue = "10") Integer size,
                                                @RequestParam Long saleGroupId) {
        return userGroupService.getGroupUserAll(page,size,saleGroupId);
    }



    @PostMapping("")
    public Boolean create(@Valid @RequestBody List<WaterPoolSaleGroupUser> userGroup) {
        return userGroupService.batchAdd(userGroup);
    }

    @PutMapping("")
    public boolean update(@Valid @RequestBody WaterPoolSaleGroupUser userGroup) {
        return userGroupService.updGroupById(userGroup);
    }

    @DeleteMapping("/{id}")
    public boolean groupDelete(@PathVariable Long id) {
        return userGroupService.deleteGroup(id);
    }

}
