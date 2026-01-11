package com.aioveu.pms.aioveu02Category.model.vo;

import com.aioveu.pms.model.vo.CategoryVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: PmsCategoryVO
 * @Description TODO  商品分类视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/11 17:31
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "商品分类视图对象")
public class PmsCategoryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;
    @Schema(description = "商品分类名称")
    private String name;
    @Schema(description = "父级ID")
    private Long parentId;
    @Schema(description = "层级")
    private Integer level;
    @Schema(description = "图标地址")
    private String iconUrl;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "显示状态:( 0:隐藏 1:显示)")
    private Integer visible;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;


    private List<CategoryVO> children = new ArrayList<>();

}
