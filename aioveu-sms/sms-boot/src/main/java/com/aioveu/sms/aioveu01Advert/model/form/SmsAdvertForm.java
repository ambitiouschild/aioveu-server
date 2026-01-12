package com.aioveu.sms.aioveu01Advert.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: SmsAdvertForm
 * @Description TODO 广告表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/12 10:36
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "广告表单对象")
public class SmsAdvertForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "广告ID")
    private Long id;

    @Schema(description = "广告标题")
    @NotBlank(message = "广告标题不能为空")
    @Size(max=100, message="广告标题长度不能超过100个字符")
    private String title;

    @Schema(description = "图片地址")
    @NotBlank(message = "图片地址不能为空")
    @Size(max=255, message="图片地址长度不能超过255个字符")
    private String imageUrl;

    @Schema(description = "开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Schema(description = "状态(1:开启；0:关闭)")
    @NotNull(message = "状态(1:开启；0:关闭)不能为空")
    private Integer status;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "跳转链接")
    @Size(max=255, message="跳转链接长度不能超过255个字符")
    private String redirectUrl;

    @Schema(description = "备注")
    @Size(max=255, message="备注长度不能超过255个字符")
    private String remark;
}
