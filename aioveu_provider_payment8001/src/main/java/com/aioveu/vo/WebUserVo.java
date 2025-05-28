package com.aioveu.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/3 0003 17:16
 */
@Data
public class WebUserVo {

    private String id;

    private String name;

    private String username;

    private String phone;

    private String head;

    /**
     * 0 未知 1 男性 2 女性
     */
    private Integer gender;

    private String introduction;

    /**
     * 角色列表
     */
    private List<String> roles;

    /**
     * 菜单列表
     */
    private List<Map<String, Object>> routers;


}
