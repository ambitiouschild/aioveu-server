package com.aioveu.tenant.aioveu03Role.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

/**
 * @ClassName: RoleForm
 * @Description TODO 角色表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:36
 * @Version 1.0
 **/
@Schema(description = "角色表单对象")
@Data
public class RoleForm {

    @Schema(description="角色ID")
    private Long id;

    @Schema(description="角色名称")
    @NotBlank(message = "角色名称不能为空")
    private String name;

    @Schema(description="角色编码")
    @NotBlank(message = "角色编码不能为空")
    private String code;

    @Schema(description="排序")
    private Integer sort;

    @Schema(description="角色状态(1-正常；0-停用)")
    @Range(max = 1, min = 0, message = "角色状态不正确")
    private Integer status;

    @Schema(description="数据权限(1-所有数据 2-部门及子部门数据 3-本部门数据 4-本人数据 5-自定义部门数据)")
    private Integer dataScope;

    @Schema(description="自定义数据权限部门ID列表(当dataScope=5时有效)")
    private List<Long> deptIds;
}
