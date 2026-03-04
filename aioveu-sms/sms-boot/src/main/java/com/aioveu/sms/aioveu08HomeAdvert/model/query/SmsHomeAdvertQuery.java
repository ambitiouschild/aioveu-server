package com.aioveu.sms.aioveu08HomeAdvert.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: SmsHomeAdvertQuery
 * @Description TODO 首页广告配置（增加跳转路径）分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/4 12:34
 * @Version 1.0
 **/
@Data
@Schema(description ="首页广告配置（增加跳转路径）查询对象")
public class SmsHomeAdvertQuery extends BasePageQuery {

    @Schema(description = "关联广告ID（sms_advert表）")
    private Long advertId;
    @Schema(description = "广告显示名称")
    private String homeAdvertName;
    @Schema(description = "图片模式")
    private String imageMode;
    @Schema(description = "跳转类型：navigateTo, redirectTo, switchTab")
    private String jumpType;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "状态：0-隐藏，1-显示")
    private Integer status;
    @Schema(description = "逻辑删除：0-正常 1-删除")
    private Integer deleted;
}
