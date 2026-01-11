package com.aioveu.pms.aioveu01Brand.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO  商品品牌实体对象
 * @Date  2026/1/10 18:58
 * @Param
 * @return
 **/

@Data
@TableName("pms_brand")
public class PmsBrand extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(type= IdType.AUTO)
    private Long id;

    /**
     * 品牌名称
     */
    @NotBlank
    private String name;

    /**
     * LOGO图片
     */
    private String logoUrl;

    /**
     * 排序
     */
    private Integer sort;
}
