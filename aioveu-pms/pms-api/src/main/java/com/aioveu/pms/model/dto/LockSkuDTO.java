package com.aioveu.pms.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Description: TODO 锁定库存传输对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:22
 * @param
 * @return:
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LockSkuDTO {

    /**
     * 商品ID
     */
    private Long skuId;

    /**
     * 商品数量
     */
    private Integer quantity;


}
