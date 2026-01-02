package com.aioveu.system.aioveu03Role.model.query;

import com.aioveu.common.base.BasePageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: RolePageQuery
 * @Description TODO  角色分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 16:57
 * @Version 1.0
 **/

@Schema(description = "角色分页查询对象")
@Getter
@Setter
public class RolePageQuery extends BasePageQuery {

    @Schema(description="关键字(角色名称/角色编码)")
    private String keywords;

    @Schema(description="开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDate;

    @Schema(description="结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endDate;
}
