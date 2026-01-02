package com.aioveu.system.aioveu03Role.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: RolePageVO
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 17:02
 * @Version 1.0
 **/

@Schema(description ="角色分页对象")
@Data
public class RolePageVO {

    @Schema(description="角色ID")
    private Long id;

    @Schema(description="角色名称")
    private String name;

    @Schema(description="角色编码")
    private String code;

    @Schema(description="角色状态")
    private Integer status;

    @Schema(description="排序")
    private Integer sort;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
