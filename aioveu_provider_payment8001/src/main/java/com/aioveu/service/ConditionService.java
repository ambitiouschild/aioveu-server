package com.aioveu.service;

import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/17 0017 18:26
 */
public interface ConditionService {

    /**
     * 获取普通活动菜单数据
     * @param city
     * @return
     */
    Map<String, Object> getExerciseMenu(String city);

    /**
     * 获取普通店铺菜单数据
     * @param city
     * @return
     */
    Map<String, Object> getStoreMenu(String city);
}
