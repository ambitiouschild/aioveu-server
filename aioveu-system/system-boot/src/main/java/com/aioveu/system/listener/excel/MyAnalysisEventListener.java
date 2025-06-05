package com.aioveu.system.listener.excel;

import com.alibaba.excel.event.AnalysisEventListener;

/**
 * @Description: TODO 自定义解析结果监听器
 * @Author: 雒世松
 * @Date: 2025/6/5 17:14
 * @param
 * @return:
 **/

public abstract class MyAnalysisEventListener<T> extends AnalysisEventListener<T> {

    private String msg;
    public abstract String getMsg();
}
