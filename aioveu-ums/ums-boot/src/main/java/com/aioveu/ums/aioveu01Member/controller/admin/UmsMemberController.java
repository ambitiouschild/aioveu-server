package com.aioveu.ums.aioveu01Member.controller.admin;

import com.aioveu.ums.aioveu01Member.model.form.UmsMemberForm;
import com.aioveu.ums.aioveu01Member.model.query.UmsMemberQuery;
import com.aioveu.ums.aioveu01Member.model.vo.UmsMemberVO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.common.constant.GlobalConstants;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.ums.aioveu01Member.model.entity.UmsMember;
import com.aioveu.ums.aioveu01Member.service.UmsMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


@Tag(name = "Admin-会员管理")
@RestController
//@RequestMapping("/api/v1/members")
@RequestMapping("/api/v1/ums-member")
@RequiredArgsConstructor
public class UmsMemberController {

    private final UmsMemberService umsMemberService;

//    @Operation(summary= "会员分页列表")
//    @GetMapping
//    public PageResult<UmsMember> getMemberPage(
//            @Parameter(name = "页码") Long pageNum,
//            @Parameter(name = "每页数量") Long pageSize,
//            @Parameter(name = "会员昵称") String nickName
//    ) {
//        IPage<UmsMember> result = umsMemberService.list(new Page<>(pageNum, pageSize), nickName);
//        return PageResult.success(result);
//    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallUmsMember:ums-member:query')")
    public PageResult<UmsMemberVO> getUmsMemberPage(UmsMemberQuery queryParams ) {
        IPage<UmsMemberVO> result = umsMemberService.getUmsMemberPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增会员")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallUmsMember:ums-member:add')")
    public Result<Void> saveUmsMember(@RequestBody @Valid UmsMemberForm formData ) {
        boolean result = umsMemberService.saveUmsMember(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取会员表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallUmsMember:ums-member:edit')")
    public Result<UmsMemberForm> getUmsMemberForm(
            @Parameter(description = "会员ID") @PathVariable Long id
    ) {
        UmsMemberForm formData = umsMemberService.getUmsMemberFormData(id);
        return Result.success(formData);
    }

//    @Operation(summary= "修改会员")
//    @PutMapping(value = "/{memberId}")
//    public <T> Result<T> update(
//            @Parameter(name = "会员ID") @PathVariable Long memberId,
//            @RequestBody UmsMember member
//    ) {
//        boolean status = umsMemberService.updateById(member);
//        return Result.judge(status);
//    }


    @Operation(summary = "修改会员")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallUmsMember:ums-member:edit')")
    public Result<Void> updateUmsMember(
            @Parameter(description = "会员ID") @PathVariable Long id,
            @RequestBody @Validated UmsMemberForm formData
    ) {
        boolean result = umsMemberService.updateUmsMember(id, formData);
        return Result.judge(result);
    }

    @Operation(summary= "修改会员状态")
    @PatchMapping("/{memberId}/status")
    public <T> Result<T> updateMemberStatus(
            @Parameter(name = "会员ID") @PathVariable Long memberId,
            @RequestBody UmsMember member
    ) {
        boolean status = umsMemberService.update(
                new LambdaUpdateWrapper<UmsMember>()
                        .eq(UmsMember::getId, memberId)
                        .set(UmsMember::getStatus, member.getStatus())
        );
        return Result.judge(status);
    }

//    @Operation(summary= "删除会员")
//    @DeleteMapping("/{ids}")
//    public <T> Result<T> delete(
//            @Parameter(name = "会员ID，多个以英文逗号(,)拼接") @PathVariable String ids
//    ) {
//        boolean status = umsMemberService.update(new LambdaUpdateWrapper<UmsMember>()
//                .in(UmsMember::getId, Arrays.asList(ids.split(",")))
//                .set(UmsMember::getDeleted, GlobalConstants.STATUS_YES));
//        return Result.judge(status);
//    }

    @Operation(summary = "删除会员,软删除")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallUmsMember:ums-member:delete')")
    public Result<Void> deleteUmsMembers(
            @Parameter(description = "会员ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean status = umsMemberService.update(new LambdaUpdateWrapper<UmsMember>()
                .in(UmsMember::getId, Arrays.asList(ids.split(",")))
                .set(UmsMember::getDeleted, GlobalConstants.STATUS_YES));
        return Result.judge(status);
    }


}
