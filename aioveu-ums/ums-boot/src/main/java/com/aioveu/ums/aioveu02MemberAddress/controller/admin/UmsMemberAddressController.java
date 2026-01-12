package com.aioveu.ums.aioveu02MemberAddress.controller.admin;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.ums.aioveu02MemberAddress.model.form.UmsMemberAddressForm;
import com.aioveu.ums.aioveu02MemberAddress.model.query.UmsMemberAddressQuery;
import com.aioveu.ums.aioveu02MemberAddress.model.vo.UmsMemberAddressVO;
import com.aioveu.ums.aioveu02MemberAddress.service.UmsMemberAddressService;
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
 * @ClassName: UmsMemberAddressController
 * @Description TODO 会员收货地址前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/12 15:39
 * @Version 1.0
 **/

@Tag(name = "会员收货地址接口")
@RestController
@RequestMapping("/api/v1/ums-member-address")
@RequiredArgsConstructor
public class UmsMemberAddressController {

    private final UmsMemberAddressService umsMemberAddressService;

    @Operation(summary = "会员收货地址分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallUmsMemberAddress:ums-member-address:query')")
    public PageResult<UmsMemberAddressVO> getUmsMemberAddressPage(UmsMemberAddressQuery queryParams ) {
        IPage<UmsMemberAddressVO> result = umsMemberAddressService.getUmsMemberAddressPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增会员收货地址")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallUmsMemberAddress:ums-member-address:add')")
    public Result<Void> saveUmsMemberAddress(@RequestBody @Valid UmsMemberAddressForm formData ) {
        boolean result = umsMemberAddressService.saveUmsMemberAddress(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取会员收货地址表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallUmsMemberAddress:ums-member-address:edit')")
    public Result<UmsMemberAddressForm> getUmsMemberAddressForm(
            @Parameter(description = "会员收货地址ID") @PathVariable Long id
    ) {
        UmsMemberAddressForm formData = umsMemberAddressService.getUmsMemberAddressFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改会员收货地址")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallUmsMemberAddress:ums-member-address:edit')")
    public Result<Void> updateUmsMemberAddress(
            @Parameter(description = "会员收货地址ID") @PathVariable Long id,
            @RequestBody @Validated UmsMemberAddressForm formData
    ) {
        boolean result = umsMemberAddressService.updateUmsMemberAddress(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除会员收货地址")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallUmsMemberAddress:ums-member-address:delete')")
    public Result<Void> deleteUmsMemberAddresss(
            @Parameter(description = "会员收货地址ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = umsMemberAddressService.deleteUmsMemberAddresss(ids);
        return Result.judge(result);
    }
}
