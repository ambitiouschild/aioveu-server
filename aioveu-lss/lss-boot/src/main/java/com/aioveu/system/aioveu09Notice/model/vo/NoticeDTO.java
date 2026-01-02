package com.aioveu.system.aioveu09Notice.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: NoticeDTO
 * @Description TODO  通知传送对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 19:55
 * @Version 1.0
 **/

@Data
public class NoticeDTO {

    @Schema(description = "通知ID")
    private Long id;

    @Schema(description = "通知类型")
    private Integer type;

    @Schema(description = "通知标题")
    private String title;

    @Schema(description = "通知时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime publishTime;
}
