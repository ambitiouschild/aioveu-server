package com.aioveu.vo;

import lombok.Data;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/1 13:33
 */
@Data
public class RegionVO {

    private Long id;

    private String name;

    private List<BusinessAreaVO> childList;

}
