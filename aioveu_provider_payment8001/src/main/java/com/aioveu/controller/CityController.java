package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.City;
import com.aioveu.entity.Province;
import com.aioveu.entity.Region;
import com.aioveu.service.CityService;
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
@RequestMapping("/api/v1/city")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/{provinceId}")
    public List<IdNameVO> list(@PathVariable Long provinceId) {
        return cityService.getByProvinceId(provinceId);
    }

    @GetMapping("/condition")
    public IPage<City> listByCondition(@RequestParam(required = false, defaultValue = "1") Integer page,
                                       @RequestParam(required = false, defaultValue = "10") Integer size,
                                       @RequestParam(required = false) String name,
                                       @RequestParam(required = false) Integer id,
                                       @RequestParam(required = false) Integer parentId) {
        return cityService.getCityListByCondition(page, size, name, id, parentId);
    }

    @PostMapping("")
    public Integer addCity(@RequestBody City dataDTO) {
        return cityService.addCity(dataDTO);
    }

    @PutMapping("")
    public Integer modifyCityMessage(@RequestBody City dataDTO) {
        return cityService.modifyCityMessage(dataDTO);
    }

    @DeleteMapping("/{id}")
    public Integer deleteProvince(@PathVariable long id) {
        return cityService.deleteCity(id);
    }

}
