package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description 类别
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_third_category")
@Data
public class CategoryThird extends IdNameEntity {

    private Long parentId;

    private String name;

    private String cover;

    /**
     * 公司Id
     */
    private int companyId;

    /**
     * 商铺Id
     */
    private int storeId;

    private Integer priority;

    private String code;

    private Integer status;

}
