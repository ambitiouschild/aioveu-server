package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.City;
import com.aioveu.entity.Province;
import com.aioveu.entity.Region;
import com.aioveu.service.RegionService;
import com.aioveu.vo.IdNameVO;
import com.aioveu.vo.RegionConditionVO;
import lombok.extern.slf4j.Slf4j;
import org.ini4j.Reg;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/region")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping("/{cityId}")
    public List<IdNameVO> list(@PathVariable Long cityId) {
        return regionService.getByCityId(cityId);
    }

    @GetMapping("/condition")
    public IPage<RegionConditionVO> listByCondition(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                    @RequestParam(required = false, defaultValue = "10") Integer size,
                                                    @RequestParam(required = false) String name,
                                                    @RequestParam(required = false) Integer id,
                                                    @RequestParam(required = false) Integer parentId) {
        return regionService.getRegionListByCondition(page, size, name, id, parentId);
    }

    @PostMapping("")
    public Integer addRegion(@RequestBody Region dataDTO) {
        return regionService.addRegion(dataDTO);
    }

    @PutMapping("")
    public Integer modifyRegionMessage(@RequestBody Region dataDTO) {
        return regionService.modifyRegionMessage(dataDTO);
    }

    @DeleteMapping("/{id}")
    public Integer deleteRegion(@PathVariable long id) {
        return regionService.deleteRegion(id);
    }


}
