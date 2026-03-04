package com.aioveu.sms.aioveu07HomeCategory.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: SmsHomeCategoryQuery
 * @Description TODO 首页分类配置分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/4 12:11
 * @Version 1.0
 **/
@Schema(description ="首页分类配置查询对象")
@Data
public class SmsHomeCategoryQuery extends BasePageQuery {

    @Schema(description = "关联商品分类ID（pms_category表）")
    private Long categoryId;
    @Schema(description = "首页显示名称")
    private String homeName;
    @Schema(description = "状态：0-隐藏，1-显示")
    private Integer status;
    @Schema(description = "逻辑删除：0-正常 1-删除")
    private Integer deleted;
}
