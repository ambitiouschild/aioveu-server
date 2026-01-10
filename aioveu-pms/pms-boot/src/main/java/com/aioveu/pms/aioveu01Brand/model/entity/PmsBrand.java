package com.aioveu.pms.aioveu01Brand.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.aioveu.common.base.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class PmsBrand extends BaseEntity {

    @TableId(type= IdType.AUTO)
    private Long id;

    @NotBlank
    private String name;

    private String logoUrl;

    private Integer sort;
}
