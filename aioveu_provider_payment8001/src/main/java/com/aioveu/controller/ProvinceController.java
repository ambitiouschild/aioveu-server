package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.Province;
import com.aioveu.service.ProvinceService;
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
@RequestMapping("/api/v1/province")
public class ProvinceController {

    private final ProvinceService provinceService;

    public ProvinceController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }

    @GetMapping("")
    public List<IdNameVO> list() {
        return provinceService.getAll();
    }

    @GetMapping("/condition")
    public IPage<Province> listByCondition(@RequestParam(required = false, defaultValue = "1") Integer page,
                                           @RequestParam(required = false, defaultValue = "10") Integer size,
                                           @RequestParam(required = false) String name,
                                           @RequestParam(required = false) Integer id) {
        return provinceService.getProvinceListByCondition(page, size, name, id);
    }

    @PostMapping("")
    public Integer addProvince(@RequestBody Province dataDTO) {
        return provinceService.addProvince(dataDTO);
    }

    @PutMapping("")
    public Integer modifyProvinceMessage(@RequestBody Province dataDTO) {
        return provinceService.modifyProvinceMessage(dataDTO);
    }

    @DeleteMapping("/{id}")
    public Integer deleteProvince(@PathVariable long id) {
        return provinceService.deleteProvince(id);
    }


}
