package com.aioveu.system.aioveu10Log.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName: LogPageQuery
 * @Description TODO  日志分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 16:55
 * @Version 1.0
 **/

@Schema(description = "日志分页查询对象")
@Getter
@Setter
public class LogPageQuery extends BasePageQuery {

    @Schema(description="关键字(日志内容/请求路径/请求方法/地区/浏览器/终端系统)")
    private String keywords;

    @Schema(description="操作时间范围")
    List<String> createTime;
}
