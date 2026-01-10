package com.aioveu.pms.aioveu06Spu.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description: TODO 【应用端】秒杀商品视图对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:32
 * @param
 * @return:
 **/

@Schema(description = "商品分页对象")
@Data
public class SeckillingSpuVO {

    @Schema(description="商品ID")
    private Long id;

    @Schema(description="商品名称")
    private String name;

    @Schema(description="商品价格(单位：分)")
    private Long price;

    @Schema(description="图片地址")
    private String picUrl;

}
