package com.aioveu.tenant.aioveu02User.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: UserNoticePageVO
 * @Description TODO 用户公告Vo
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 20:01
 * @Version 1.0
 **/

@Data
@Schema(description = "用户公告Vo")
public class UserNoticePageVO {

    @Schema(description = "通知ID")
    private Long id;

    @Schema(description = "通知标题")
    private String title;

    @Schema(description = "通知类型")
    private Integer type;

    @Schema(description = "通知等级")
    private String level;

    @Schema(description = "发布人姓名")
    private String publisherName;

    @Schema(description = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime publishTime;

    @Schema(description = "是否已读")
    private Integer isRead;
}
