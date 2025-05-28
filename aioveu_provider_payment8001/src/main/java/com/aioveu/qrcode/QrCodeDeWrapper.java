package com.aioveu.qrcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.aioveu.qrcode.util.ImageUtil;
import lombok.extern.slf4j.Slf4j;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析二维码的工具类
 * <p>
 * Created by yihui on 2025/7/17.
 */
@Slf4j
public class QrCodeDeWrapper {


    /**
     * 读取二维码中的内容, 并返回
     *
     * @param qrcodeImg 二维码图片的地址
     * @return 返回二维码的内容
     * @throws IOException       读取二维码失败
     * @throws FormatException   二维码解析失败
     * @throws ChecksumException
     * @throws NotFoundException
     */
    public static String decode(String qrcodeImg) throws IOException, FormatException, ChecksumException, NotFoundException {
        BufferedImage image = ImageUtil.getImageByPath(qrcodeImg);
        return decode(image);
    }


    public static String decode(BufferedImage image) throws ChecksumException, NotFoundException, FormatException {
        if (image == null) {
            throw new IllegalStateException("can not load qrCode!");
        }
        LuminanceSource luminanceSource = new BufferedImageLuminanceSource(image);
        Binarizer binarizer = new HybridBinarizer(luminanceSource);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
        Map<DecodeHintType, Object> hints = new HashMap<>();
        // 解码设置编码方式为：utf-8，
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        //优化精度
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        Result result = new QRCodeReader().decode(binaryBitmap, hints);
        return result.getText();
    }

}
