package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.aioveu.dto.OrderStatusVo;
import lombok.Data;

import java.util.List;

/**
 * @description 类别
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_category")
@Data
public class Category extends IdNameEntity {

    private Long parentId;

    private String cover;

    private Integer priority;

    private String code;

    /**
     * 树形
     */
    @TableField(exist = false)
    List<Category> treeList;


    /**
     *  订单状态列表
     */
    @TableField(exist = false)
    List<OrderStatusVo> orderStatusVos;

}
