package com.aioveu.utils;

import com.aioveu.constant.SportConstant;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/22 15:24
 */
public class FileUtil {

    /**
     * 回去文件的文件名
     * @param originalFilename
     * @return 文件名
     */
    public static String getFileType(String originalFilename) {
        if (StringUtils.isNotEmpty(originalFilename)) {
            int dotPosition = originalFilename.lastIndexOf(".");
            return (dotPosition == -1) ? null : originalFilename.substring(dotPosition).toLowerCase();
        }
        return null;
    }

    public static String getOssUrlFileName(String url){
        if (url.startsWith("https://image.highyundong.com")){
            url = url.substring(30, url.length());
        } else if (url.startsWith("http://image.highyundong.com")){
            url = url.substring(29, url.length());
        }
        if (url.startsWith("/")){
            url = url.substring(1, url.length());
        }
        return url;
    }

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
            return SportConstant.IMAGE_PREFIX + url;
        }
    }

    public static String getAgreementBase64(String url) {
        String fullUrl = getImageFullUrl(url);
        return Base64.getEncoder().encodeToString(fullUrl.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 检查路径，不存在就创建
     * @param path
     */
    public static void checkPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 图片地址变成byte数组
     * @param imageUrl
     * @return
     * @throws IOException
     */
    public static byte[] imageUrlToByteArray(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream in = url.openStream();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            return out.toByteArray();
        }
    }

    /**
     * 删除文件
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        file.deleteOnExit();
    }

}
