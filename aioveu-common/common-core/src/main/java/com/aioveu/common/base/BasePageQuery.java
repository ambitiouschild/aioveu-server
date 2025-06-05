package com.aioveu.common.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: TODO 础分页请求对象
 * @Author: 雒世松
 * @Date: 2025/6/5 15:36
 * @param
 * @return:
 **/

@Data
@Schema 
public class BasePageQuery implements Serializable {

    @Schema(description = "页码", example = "1")
    private int pageNum = 1;

    @Schema(description = "每页记录数", example = "10")
    private int pageSize = 10;
}
