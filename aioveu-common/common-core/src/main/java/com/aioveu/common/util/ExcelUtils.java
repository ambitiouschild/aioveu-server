package com.aioveu.common.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.event.AnalysisEventListener;

import java.io.InputStream;

/**
 * @ClassName: ExcelUtils
 * @Description TODO Excel 工具类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/21 16:22
 * @Version 1.0
 **/
public class ExcelUtils {

    public static <T> void importExcel(InputStream is, Class clazz, AnalysisEventListener<T> listener) {
        EasyExcel.read(is, clazz, listener).sheet().doRead();
    }
}
