package com.aioveu.oms.aioveu01Order.model.vo;

import cn.hutool.core.date.DateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description: TODO 购物车商品项
 * @Author: 雒世松
 * @Date: 2025/6/5 18:09
 * @param
 * @return:
 **/

@Data
public class CartItemDto implements Serializable {

    /**
     * 商品库存ID
     */
    private Long skuId;

    /**
     * 商品名称
     */
    private String skuName;

    /**
     * 商品数量
     */
    private Integer count;

    /**
     * 是否选中
     */
    private Boolean checked;   // 是否选中

    /**
     * SKU 编号
     */
    private String skuSn;

    /**
     * 商品 图片地址
     */
    private String picUrl;
    /**
     * 商品 价格
     */
    private Long price;
    /**
     * 商品 库存数量
     */
    private Integer stock;  // ✅ 必须：库存数量  添加购物车时需要验证库存  数量选择器需要根据库存限制最大可购买数量

    /**
     * 商品 加入时间
     */
    // 添加 Jackson 注解
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addTime;

    // 使用 Long 存储时间戳
//    private Long addTime;  // 保存 System.currentTimeMillis()


}
