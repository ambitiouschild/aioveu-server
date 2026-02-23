package com.aioveu.tenant.aioveu10Log.model.form;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: LogQuery
 * @Description TODO 日志分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:45
 * @Version 1.0
 **/
@Schema(description = "日志分页查询对象")
@Data
public class LogQuery extends BasePageQuery {

    @Schema(description="关键字(日志内容/请求路径/请求方法/地区/浏览器/终端系统)")
    private String keywords;

    @Schema(description="操作时间范围")
    List<String> createTime;

}
