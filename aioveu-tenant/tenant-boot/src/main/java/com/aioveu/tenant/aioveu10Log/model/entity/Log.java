package com.aioveu.tenant.aioveu10Log.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.aioveu.common.enums.LogModuleEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName: Log
 * @Description TODO 系统日志 实体类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:18
 * @Version 1.0
 **/
@Data
@TableName("sys_log")
public class Log extends BaseEntity {

    /**
     * 日志模块
     */
    private LogModuleEnum module;

    /**
     * 请求方式
     */
    @TableField(value = "request_method")
    private String requestMethod;

    /**
     * 请求参数
     */
    @TableField(value = "request_params")
    private String requestParams;

    /**
     * 响应参数
     */
    @TableField(value = "response_content")
    private String responseContent;

    /**
     * 日志内容
     */
    private String content;

    /**
     * 请求路径
     */
    private String requestUri;

    /**
     * 请求方法
     */
    private String method;

    /**
     * IP 地址
     */
    private String ip;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 浏览器版本
     */
    private String browserVersion;

    /**
     * 终端系统
     */
    private String os;

    /**
     * 执行时间(毫秒)
     */
    private Long executionTime;

    /**
     * 创建人ID
     */
    private Long createBy;
}
