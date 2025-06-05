package com.aioveu.sms.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
public class AdvertPageVO {

    @Schema(description="广告ID")
    private Integer id;

    @Schema(description="广告标题")
    private String title;

    @Schema(description="广告图片")
    private String picUrl;

    @Schema(description="开始时间")
    @JsonFormat( pattern = "yyyy-MM-dd")
    private Date beginTime;

    @Schema(description="截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    @Schema(description="状态")
    private Integer status;

    private Integer sort;

    private  String redirectUrl;

    private String remark;

}
