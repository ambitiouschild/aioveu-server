package com.aioveu.pms.aioveu06Spu.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Description: TODO 【APP端】批量更新商品状态DTO
 * @Author: 雒世松
 * @Date: 2026/5/8 13:50
 * @param
 * @return:
 **/

@Data
@Accessors(chain = true)
@Schema( description = "【管理端】商品视图对象")
public class UpdateStatusDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "商品ID列表", required = true)
    @NotEmpty(message = "商品ID列表不能为空")
    private Long id;

    @Schema(description = "目标状态(0:下架, 1:上架)")
    @NotNull(message = "状态不能为空")
    private Integer status;


}
