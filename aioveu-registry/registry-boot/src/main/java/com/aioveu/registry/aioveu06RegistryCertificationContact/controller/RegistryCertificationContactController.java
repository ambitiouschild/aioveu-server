package com.aioveu.registry.aioveu06RegistryCertificationContact.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.registry.aioveu06RegistryCertificationContact.model.form.RegistryCertificationContactForm;
import com.aioveu.registry.aioveu06RegistryCertificationContact.model.query.RegistryCertificationContactQuery;
import com.aioveu.registry.aioveu06RegistryCertificationContact.model.vo.RegistryCertificationContactVo;
import com.aioveu.registry.aioveu06RegistryCertificationContact.service.RegistryCertificationContactService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: RegistryCertificationContactController
 * @Description TODO 认证联系人前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:32
 * @Version 1.0
 **/
@Tag(name = "认证联系人接口")
@RestController
@RequestMapping("/api/v1/registry-certification-contact")
@RequiredArgsConstructor
public class RegistryCertificationContactController {

    private final RegistryCertificationContactService registryCertificationContactService;

    @Operation(summary = "认证联系人分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryCertificationContact:registry-certification-contact:list')")
    public PageResult<RegistryCertificationContactVo> getRegistryCertificationContactPage(RegistryCertificationContactQuery queryParams ) {
        IPage<RegistryCertificationContactVo> result = registryCertificationContactService.getRegistryCertificationContactPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增认证联系人")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryCertificationContact:registry-certification-contact:create')")
    public Result<Void> saveRegistryCertificationContact(@RequestBody @Valid RegistryCertificationContactForm formData ) {
        boolean result = registryCertificationContactService.saveRegistryCertificationContact(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取认证联系人表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryCertificationContact:registry-certification-contact:update')")
    public Result<RegistryCertificationContactForm> getRegistryCertificationContactForm(
            @Parameter(description = "认证联系人ID") @PathVariable Long id
    ) {
        RegistryCertificationContactForm formData = registryCertificationContactService.getRegistryCertificationContactFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改认证联系人")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryCertificationContact:registry-certification-contact:update')")
    public Result<Void> updateRegistryCertificationContact(
            @Parameter(description = "认证联系人ID") @PathVariable Long id,
            @RequestBody @Validated RegistryCertificationContactForm formData
    ) {
        boolean result = registryCertificationContactService.updateRegistryCertificationContact(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除认证联系人")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryCertificationContact:registry-certification-contact:delete')")
    public Result<Void> deleteRegistryCertificationContacts(
            @Parameter(description = "认证联系人ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = registryCertificationContactService.deleteRegistryCertificationContacts(ids);
        return Result.judge(result);
    }
}
