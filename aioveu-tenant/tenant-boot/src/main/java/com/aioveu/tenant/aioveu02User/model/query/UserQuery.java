package com.aioveu.tenant.aioveu02User.model.query;

import com.aioveu.common.base.BasePageQuery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @ClassName: UserQuery
 * @Description TODO 用户分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:48
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "用户分页查询对象")
public class UserQuery extends BasePageQuery {

    @Schema(description = "关键字(用户名/昵称/手机号)")
    private String keywords;

    @Schema(description = "用户状态")
    private Integer status;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "角色ID")
    private List<Long> roleIds;

    @Schema(description = "创建时间范围")
    private List<String> createTime;

    /**
     * 是否超级管理员
     */
    @JsonIgnore
    @Schema(hidden = true)
    private Boolean isRoot;
}
