package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.BusinessArea;
import com.aioveu.entity.City;
import com.aioveu.entity.Province;
import com.aioveu.service.BusinessAreaService;
import com.aioveu.vo.BusinessAreaConditionVO;
import com.aioveu.vo.IdNameVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/businessArea")
public class BusinessAreaController {

    private final BusinessAreaService businessAreaService;

    public BusinessAreaController(BusinessAreaService businessAreaService) {
        this.businessAreaService = businessAreaService;
    }

    @GetMapping("/{regionId}")
    public List<IdNameVO> list(@PathVariable Long regionId) {
        return businessAreaService.getByRegionId(regionId);
    }

    @GetMapping("/condition")
    public IPage<BusinessAreaConditionVO> listByCondition(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                          @RequestParam(required = false, defaultValue = "10") Integer size,
                                                          @RequestParam(required = false) String name,
                                                          @RequestParam(required = false) Integer id,
                                                          @RequestParam(required = false) Integer parentId) {
        return businessAreaService.getBusinessAreaListByCondition(page, size, name, id, parentId);
    }

    @GetMapping("")
    public List<BusinessArea> getById(@RequestParam Long id) {
        return businessAreaService.getById(id);
    }


    @PostMapping("")
    public Integer addBusinessArea(@RequestBody BusinessArea dataDTO) {
        return businessAreaService.addBusinessArea(dataDTO);
    }

    @PutMapping("")
    public Integer modifyBusinessAreaMessage(@RequestBody BusinessArea dataDTO) {
        return businessAreaService.modifyBusinessAreaMessage(dataDTO);
    }

    @DeleteMapping("/{id}")
    public Integer deleteBusinessArea(@PathVariable long id) {
        return businessAreaService.deleteBusinessArea(id);
    }

}
