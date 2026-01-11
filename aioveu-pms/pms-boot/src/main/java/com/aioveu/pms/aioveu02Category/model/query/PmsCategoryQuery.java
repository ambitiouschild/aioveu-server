package com.aioveu.pms.aioveu02Category.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: PmsCategoryQuery
 * @Description TODO  商品分类分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/11 17:31
 * @Version 1.0
 **/

@Schema(description ="商品分类查询对象")
@Getter
@Setter
public class PmsCategoryQuery extends BasePageQuery {

    @Schema(description = "商品分类名称")
    private String name;
    @Schema(description = "父级ID")
    private Long parentId;
    @Schema(description = "图标地址")
    private String iconUrl;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "显示状态:( 0:隐藏 1:显示)")
    private Integer visible;
}
