package com.aioveu.vo;

import lombok.Data;

import java.util.List;

@Data
public class StoreMiniVO  extends IdNameVO{

    private String address;

    private Integer distance;

    private String introduce;

    private String logo;

    private String tags;

    private List<ProductSimpleVO> productList;

    private String appId;

    private String path;

    /**
     * 店铺分类id集合
     */
    private String categoryId;

}
