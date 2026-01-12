package com.aioveu.sms.aioveu01Advert.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;


/**
 * @Description: TODO 广告分页视图对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:47
 * @param
 * @return:
 **/

@Schema(description = "广告分页视图对象")
@Data
public class SmsAdvertVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    @Schema(description="广告ID")
    private Integer id;

    @Schema(description="广告标题")
    private String title;

    @Schema(description="广告图片")
    private String imageUrl;

    @Schema(description="开始时间")
    @JsonFormat( pattern = "yyyy-MM-dd")
    private Date startTime;

    @Schema(description="截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    @Schema(description="状态")
    private Integer status;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "跳转链接")
    private  String redirectUrl;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间(新增有值)")
    private LocalDateTime updateTime;

}
