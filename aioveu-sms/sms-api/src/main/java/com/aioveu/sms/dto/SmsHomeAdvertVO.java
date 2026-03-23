package com.aioveu.sms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: SmsHomeAdvertVO
 * @Description TODO 首页广告配置（增加跳转路径）视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/4 12:35
 * @Version 1.0
 **/
@Data
@Schema( description = "首页广告配置（增加跳转路径）视图对象")
public class SmsHomeAdvertVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "关联广告ID（sms_advert表）")
    private Long advertId;
    @Schema(description = "广告显示的图标URL")
    private String homeAdvertIcon;
    @Schema(description = "广告显示名称")
    private String homeAdvertName;
    @Schema(description = "高度（rpx/upx）")
    private Integer height;
    @Schema(description = "图片模式")
    private String imageMode;
    @Schema(description = "跳转路径")
    private String jumpPath;
    @Schema(description = "跳转类型：navigateTo, redirectTo, switchTab")
    private String jumpType;
    @Schema(description = "跳转参数（JSON格式）")
    private String jumpParams;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "状态：0-隐藏，1-显示")
    private Integer status;
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

}
