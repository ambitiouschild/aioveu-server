package com.aioveu.system.aioveu09Notice.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @ClassName: NoticePageQuery
 * @Description TODO  通知公告分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 16:56
 * @Version 1.0
 **/

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description ="通知公告查询对象")
public class NoticePageQuery extends BasePageQuery {

    @Schema(description = "通知标题")
    private String title;

    @Schema(description = "发布状态(0-未发布 1已发布 -1已撤回)")
    private Integer publishStatus;

    @Schema(description = "发布时间(起止)")
    private List<String> publishTime;

    @Schema(description = "查询人ID")
    private Long userId;

    @Schema(description = "是否已读（0-未读 1-已读）")
    private Integer isRead;
}
