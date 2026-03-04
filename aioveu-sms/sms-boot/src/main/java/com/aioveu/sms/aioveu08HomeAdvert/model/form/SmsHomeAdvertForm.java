package com.aioveu.sms.aioveu08HomeAdvert.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: SmsHomeAdvertForm
 * @Description TODO 首页广告配置（增加跳转路径）表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/4 12:33
 * @Version 1.0
 **/
@Data
@Schema(description = "首页广告配置（增加跳转路径）表单对象")
public class SmsHomeAdvertForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "关联广告ID（sms_advert表）")
    private Long advertId;

    @Schema(description = "广告显示的图标URL")
    @NotBlank(message = "广告显示的图标URL不能为空")
    @Size(max=255, message="广告显示的图标URL长度不能超过255个字符")
    private String homeAdvertIcon;

    @Schema(description = "广告显示名称")
    @NotBlank(message = "广告显示名称不能为空")
    @Size(max=100, message="广告显示名称长度不能超过100个字符")
    private String homeAdvertName;

    @Schema(description = "高度（rpx/upx）")
    private Integer height;

    @Schema(description = "图片模式")
    @Size(max=20, message="图片模式长度不能超过20个字符")
    private String imageMode;

    @Schema(description = "跳转路径")
    @Size(max=255, message="跳转路径长度不能超过255个字符")
    private String jumpPath;

    @Schema(description = "跳转类型：navigateTo, redirectTo, switchTab")
    @Size(max=20, message="跳转类型：navigateTo, redirectTo, switchTab长度不能超过20个字符")
    private String jumpType;

    @Schema(description = "跳转参数（JSON格式）")
    @Size(max=500, message="跳转参数（JSON格式）长度不能超过500个字符")
    private String jumpParams;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态：0-隐藏，1-显示")
    @NotNull(message = "状态：0-隐藏，1-显示不能为空")
    private Integer status;

    @Schema(description = "备注")
    @Size(max=255, message="备注长度不能超过255个字符")
    private String remark;

    @Schema(description = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除：0-正常 1-删除")
    private Integer deleted;

    @Schema(description = "版本号（用于乐观锁）")
    private Integer version;
}
