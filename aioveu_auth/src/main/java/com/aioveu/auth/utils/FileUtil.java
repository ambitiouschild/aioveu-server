package com.aioveu.auth.utils;

import org.apache.commons.lang.StringUtils;

/**
 * @description
 * @author: 雒世松
 * @date: 2024/12/14 12:32
 */
public class FileUtil {

    /**
     * 图片前缀
     */
    public static String IMAGE_PREFIX = "https://image.aioveu.com/";

    /**
     * 检查图片地址，并添加前缀
     * @param url
     * @return
     */
    public static String getImageFullUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        if (url.startsWith("http")){
            return url;
        } else {
            return IMAGE_PREFIX + url;
        }
    }

}
