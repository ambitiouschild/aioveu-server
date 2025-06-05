package com.aioveu.oms.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: TODO
 * @Author: 雒世松
 * @Date: 2025/6/5 18:13
 * @param
 * @return:
 **/

@Data
@Accessors(chain = true)
public class WxPayResponseVO {
    private String code;

    private String message;
}
