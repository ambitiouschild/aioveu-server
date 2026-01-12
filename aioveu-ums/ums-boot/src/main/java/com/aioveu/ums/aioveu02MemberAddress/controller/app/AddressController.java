package com.aioveu.ums.aioveu02MemberAddress.controller.app;

import com.aioveu.common.result.Result;
import com.aioveu.ums.aioveu02MemberAddress.model.entity.UmsMemberAddress;
import com.aioveu.ums.dto.MemberAddressDTO;
import com.aioveu.ums.aioveu02MemberAddress.model.form.UmsMemberAddressForm;
import com.aioveu.ums.aioveu02MemberAddress.service.UmsMemberAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Tag(name = "App-会员地址")
@RestController
@RequestMapping("/app-api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final UmsMemberAddressService addressService;

    @Operation(summary= "获取当前会员地址列表")
    @GetMapping
    public Result<List<MemberAddressDTO>> listCurrentMemberAddresses() {
        List<MemberAddressDTO> addressList = addressService.listCurrentMemberAddresses();
        return Result.success(addressList);
    }

    @Operation(summary= "获取地址详情")
    @GetMapping("/{addressId}")
    public Result<UmsMemberAddress> getAddressDetail(
            @Parameter(name = "地址ID") @PathVariable Long addressId
    ) {
        UmsMemberAddress umsMemberAddress = addressService.getById(addressId);
        return Result.success(umsMemberAddress);
    }

    @Operation(summary= "新增地址")
    @PostMapping
    public Result addAddress(
            @RequestBody @Validated UmsMemberAddressForm umsMemberAddressForm
    ) {
        boolean result = addressService.addAddress(umsMemberAddressForm);
        return Result.judge(result);
    }

    @Operation(summary= "修改地址")
    @PutMapping("/{addressId}")
    public Result updateAddress(
            @Parameter(name = "地址ID") @PathVariable Long addressId,
            @RequestBody @Validated UmsMemberAddressForm umsMemberAddressForm
    ) {
        boolean result = addressService.updateAddress(umsMemberAddressForm);
        return Result.judge(result);
    }

    @Operation(summary= "删除地址")
    @DeleteMapping("/{ids}")
    public Result deleteAddress(
            @Parameter(name = "地址ID，过个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean status = addressService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.judge(status);
    }

}
