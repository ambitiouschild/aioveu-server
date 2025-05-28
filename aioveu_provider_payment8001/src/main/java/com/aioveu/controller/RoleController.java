package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.entity.Role;
import com.aioveu.enums.RoleTypeEnum;
import com.aioveu.exception.SportException;
import com.aioveu.service.RoleMenuService;
import com.aioveu.service.RoleService;
import com.aioveu.vo.IdNameCodeVO;
import com.aioveu.vo.RoleDetailVO;
import com.aioveu.vo.RoleMenuVO;
import com.aioveu.vo.WebRoleVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/role")
public class RoleController {


    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMenuService roleMenuService;

    @GetMapping("/web-list")
    public IPage<WebRoleVO> webList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                    @RequestParam(required = false, defaultValue = "10") Integer size,
                                    @RequestParam(required = false) Long storeId,
                                    @RequestParam(required = false) Long companyId,
                                    @RequestParam(required = false) Integer type) {
        return roleService.webList(page, size, storeId, companyId, type);
    }


    @GetMapping("")
    public List<IdNameCodeVO> list() {
        return roleService.getAll();
    }

    @GetMapping("/manage-list")
    public List<IdNameCodeVO> manageList(@RequestParam String storeId) {
        return roleService.manageList(storeId);
    }

    /**
     * 获取 商户端角色
     * @return
     */
    @GetMapping("business")
    public List<Role> businessRoleList(@RequestParam String storeId) {
        return roleService.getBusinessRoleList(storeId);
    }

    @GetMapping("/{id}")
    public RoleDetailVO getDetail(@PathVariable Long id) {
        return roleService.getDetail(id);
    }

    @PostMapping("")
    public boolean saveRole(@RequestBody RoleDetailVO roleDetailVO) {
        if (roleDetailVO.getCompanyId() == null || roleDetailVO.getStoreId() == null) {
            throw new SportException("店铺或公司id不能为空");
        }
        roleDetailVO.setType(RoleTypeEnum.THIRD_ROLE.getCode());
        if (StringUtils.isEmpty(roleDetailVO.getCode())) {
            roleDetailVO.setCode(UUID.randomUUID().toString().replace("-", ""));
        }
        return roleService.saveOrUpdateRole(roleDetailVO);
    }

    @PostMapping("/web")
    public boolean webSaveRole(@Valid @RequestBody RoleDetailVO roleDetailVO) {
        return roleService.saveOrUpdateRole(roleDetailVO);
    }

    @PutMapping("/web")
    public boolean webUpdateRole(@Valid @RequestBody RoleDetailVO roleDetailVO) {
        return roleService.saveOrUpdateRole(roleDetailVO);
    }

    @DeleteMapping("/web")
    public boolean deleteByCode(@RequestParam String code) {
        return roleService.deleteByCode(code);
    }

    @GetMapping("/menu")
    public Map<String, Object> roleMenu(@RequestParam String roleCode,
                                        @RequestParam Integer type) {
        return roleMenuService.getRoleMenusByRoleCodeAndType(roleCode, type);
    }

    @GetMapping("/mini-app-menu")
    public List<RoleMenuVO> roleMiniAppMenu(@RequestParam(required = false) String roleCode) {
        return roleMenuService.getMiniAppMenuByRoleCode(roleCode);
    }

}
