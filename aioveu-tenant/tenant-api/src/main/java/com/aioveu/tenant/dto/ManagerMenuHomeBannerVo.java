package com.aioveu.tenant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: ManagerMenuHomeBannerVo
 * @Description TODO 管理端app首页滚播栏视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/4 15:39
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "管理端app首页滚播栏视图对象")
public class ManagerMenuHomeBannerVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @Schema(description = "滚播栏标题")
    private String title;
    @Schema(description = "滚播栏图片地址")
    private String imageUrl;
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    @Schema(description = "结束时间")
    private LocalDateTime endTime;
    @Schema(description = "状态(1:开启；0:关闭)")
    private Integer status;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "跳转链接")
    private String redirectUrl;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    @Schema(description = "逻辑删除：0-正常 1-删除")
    private Integer deleted;
    @Schema(description = "版本号（用于乐观锁）")
    private Integer version;
    @Schema(description = "租户ID")
    private Long tenantId;
}
