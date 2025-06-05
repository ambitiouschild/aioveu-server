package com.aioveu.common.web.constraint;

import lombok.Data;

/**
 * @Description: TODO
 * @Author: 雒世松
 * @Date: 2025/6/5 16:25
 * @param
 * @return:
 **/

@Data
public class CityEntity {
    private String value;
    private String name;
    private String parent;
}
