package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.constant.SportConstant;
import com.aioveu.entity.Company;
import com.aioveu.form.CompanySettingForm;
import com.aioveu.service.CompanyService;
import com.aioveu.service.CompanyStoreUserService;
import com.aioveu.vo.api.CompanyItemVO;
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
@RequestMapping("/api/v1/company")
@RestController
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    CompanyStoreUserService companyStoreUserService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("")
    public IPage<CompanyItemVO> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                     @RequestParam(required = false, defaultValue = "10") Integer size,
                                     @RequestParam(required = false, defaultValue = "1") Integer status,
                                     @RequestParam(required = false) String keyword) {
        return companyService.getAll(page, size, status, keyword);
    }

    @GetMapping("/{id}")
    public CompanySettingForm detail(@PathVariable Long id) {
        return companyService.getCompanySettingForm(id);
    }

    @PutMapping("")
    public boolean update(@Valid @RequestBody Company company) {
        return companyService.saveOrUpdate(company);
    }

    @PostMapping("")
    public boolean create(@Valid @RequestBody Company company){
        if (company.getMpAppId() == null) {
            company.setMpAppId(SportConstant.QU_SHU_WECHAT_MP);
        }
        return companyService.save(company);
    }

    @PutMapping("/status")
    public boolean status(@RequestParam Long id, @RequestParam Integer status) {
        return companyService.changeStatus(id, status);
    }

    @PutMapping("/update-company-setting")
    public boolean updateCompanySetting(@RequestBody CompanySettingForm company) {
        return companyService.updateCompanySetting(company);
    }

    /**
     * 根据username获取有效的、有店铺的公司信息
     * @param username
     * @return
     */
    @GetMapping("getUserValidCompanys/{username}")
    public List<CompanyItemVO> getUserValidCompanys(@PathVariable String username) {
        return companyService.getUserValidCompanys(username);
    }
}
