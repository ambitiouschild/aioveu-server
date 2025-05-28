package com.aioveu.controller;

import com.aioveu.entity.CompanyStoreUser;
import com.aioveu.exception.SportException;
import com.aioveu.form.CompanyStoreUserForm;
import com.aioveu.service.CompanyStoreUserService;
import com.aioveu.utils.JacksonUtils;
import com.aioveu.vo.CompanyStoreUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 公司店铺用户
 * 授权用户店铺
 * @Author： yao
 * @Date： 2024/10/28 22:09
 * @Describe：
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/companyStoreUser")
public class CompanyStoreUserController {

    @Autowired
    private CompanyStoreUserService companyStoreUserService;

    @PostMapping("")
    public boolean create(@Valid @RequestBody CompanyStoreUser companyStoreUser) {
        return companyStoreUserService.create(companyStoreUser);
    }

    @PostMapping("/add")
    public boolean createOrUpdate(@Valid @RequestBody CompanyStoreUserForm user) {
        companyStoreUserService.delUserIdByCompanyId(user.getId(), user.getCompanyId());
        if (!companyStoreUserService.batchCreateByStoreId(user.getId(), user.getStoreIdList())) {
            log.error("用户授权创建失败:" + JacksonUtils.obj2Json(user));
            throw new SportException("用户保存失败!");
        }
        return true;
    }

    @GetMapping("/userAll")
    public List<CompanyStoreUser> getListByCompanyId(@RequestParam String id,
                                                 @RequestParam Long companyId) {
        return companyStoreUserService.getUserIdByCompanyId(id, companyId);
    }

    @GetMapping("/user/{userId}")
    public List<CompanyStoreUserVo> getListByUserId(@PathVariable String userId) {
        return companyStoreUserService.getListByUserId(userId);
    }

    @DeleteMapping("{id}")
    public boolean deleteById(@PathVariable Long id) {
        return companyStoreUserService.removeById(id);
    }
}
