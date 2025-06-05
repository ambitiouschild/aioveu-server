package com.aioveu.pms.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.aioveu.common.result.Result;
import com.aioveu.pms.model.entity.PmsCategoryAttribute;
import com.aioveu.pms.model.form.PmsCategoryAttributeForm;
import com.aioveu.pms.service.AttributeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: TODO Admin-商品属性控制器
 * @Author: 雒世松
 * @Date: 2025/6/5 18:28
 * @param
 * @return:
 **/

@Tag(name = "Admin-商品属性")
@RestController
@RequestMapping("/api/v1/attributes")
@Slf4j
@AllArgsConstructor
public class PmsAttributeController {

    private AttributeService attributeService;

    @Operation(summary= "属性列表")
    @GetMapping
    public Result getAttributeList(
            @Parameter(name = "商品分类ID") Long categoryId,
            @Parameter(name = "类型（1：规格；2：属性）") Integer type
    ) {
        List<PmsCategoryAttribute> list = attributeService.list(new LambdaQueryWrapper<PmsCategoryAttribute>()
                .eq(categoryId != null, PmsCategoryAttribute::getCategoryId, categoryId)
                .eq(type != null, PmsCategoryAttribute::getType, type)
        );
        return Result.success(list);
    }

    @Operation(summary= "批量新增/修改")
    @PostMapping("/batch")
    public Result saveBatch(@RequestBody PmsCategoryAttributeForm pmsCategoryAttributeForm) {
        boolean result = attributeService.saveBatch(pmsCategoryAttributeForm);
        return Result.judge(result);
    }
}
