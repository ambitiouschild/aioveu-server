package com.aioveu.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: EDZ
 * @Description: ${description}
 * @Date: 2019/5/9 13:05
 * @Version: 1.0
 */
public class RingTopic {

    /**
     * 图片设置成圆形
     * @param image
     * @return
     */
    public static byte[] getCircularImage(String image) {
        BufferedImage avatarImage;
        try {
            avatarImage = ImageIO.read(new URL(image));
            if (avatarImage == null) {
                throw new IllegalArgumentException("图片加载错误");
            }
            //若是长方形图片则按照边短的去裁剪成圆，如果没有这个就会变成椭圆
            int min;
            if(avatarImage.getWidth() > avatarImage.getHeight()){
                min = avatarImage.getHeight();
            }else{
                min = avatarImage.getWidth();
            }
            // 处理图片将其压缩成指定宽高正方形的小图
//            BufferedImage convertImage = scaleByPercentage(avatarImage,100,100);
            // 裁剪成圆形 （传入的图像必须是正方形的 才会 圆形 如果是长方形的比例则会变成椭圆的）
            BufferedImage convertImage = convertCircular(avatarImage, min);


//            OutputStream os = new FileOutputStream(outFile);
//            ImageIO.write(convertImage, "PNG", os);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(convertImage, "PNG", baos);
            return baos.toByteArray();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 传入的图像必须是正方形的 才会 圆形 如果是长方形的比例则会变成椭圆的
     *
     * @param bi1 用户头像地址
     * @return
     * @throws IOException
     */
    public static BufferedImage convertCircular(BufferedImage bi1, int min) throws IOException {
//      BufferedImage bi1 = ImageIO.read(new File(url));

        // 这种是黑色底的
//      BufferedImage bi2 = new BufferedImage(bi1.getWidth(), bi1.getHeight(), BufferedImage.TYPE_INT_RGB);

        // 透明底的图片
        BufferedImage bi2 = new BufferedImage(min, min, BufferedImage.TYPE_4BYTE_ABGR);
        Ellipse2D.Double shape = new Ellipse2D.Double(0, 0,min,min);
        Graphics2D g2 = bi2.createGraphics();
        g2.setClip(shape);
        // 使用 setRenderingHint 设置抗锯齿
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(bi1, 0, 0, null);
        // 设置颜色
        g2.setBackground(Color.green);
        g2.dispose();
        return bi2;
    }

    /**
     * 图片设置成光环
     * @param imageUrl
     * @param border
     * @param outFile
     * @param rgb
     * @return
     */
    public static BufferedImage ringImage(String imageUrl, int border, String outFile, String rgb) {
        BufferedImage avatarImage;
        try {
            if (imageUrl.startsWith("http")) {
                avatarImage = ImageIO.read(new URL(imageUrl));
            } else {
                avatarImage = ImageIO.read(new File(imageUrl));
            }
            if (avatarImage == null) {
                throw new IllegalArgumentException("图片加载错误");
            }
            avatarImage = scaleByPercentage(avatarImage, avatarImage.getWidth(),  avatarImage.getWidth());
            int width = avatarImage.getWidth() + border * 2;
            // 透明底的图片
            BufferedImage formatAvatarImage = new BufferedImage(width, width, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D graphics = formatAvatarImage.createGraphics();
            //把图片切成一个园
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //留一个像素的空白区域，这个很重要，画圆的时候把这个覆盖
            //图片是一个圆型
            Ellipse2D.Double shape = new Ellipse2D.Double(border, border, width - border * 2, width - border * 2);
            //需要保留的区域
            graphics.setClip(shape);
            graphics.drawImage(avatarImage, border, border, width - border * 2, width - border * 2, null);
            graphics.dispose();
            //在圆图外面再画一个圆
            //新创建一个graphics，这样画的圆不会有锯齿
            graphics = formatAvatarImage.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //画笔是4.5个像素，BasicStroke的使用可以查看下面的参考文档
            //使画笔时基本会像外延伸一定像素，具体可以自己使用的时候测试
            Stroke s = new BasicStroke(border * 1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            graphics.setStroke(s);
            String[] rgbArray = rgb.split(",");
            graphics.setColor(new Color(Integer.parseInt(rgbArray[0]), Integer.parseInt(rgbArray[1]), Integer.parseInt(rgbArray[2])));
            graphics.drawOval(border, border, width - border * 2, width - border * 2);
            graphics.dispose();

            OutputStream os = new FileOutputStream(outFile);
            ImageIO.write(formatAvatarImage, "PNG", os);
            return formatAvatarImage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 缩小Image，此方法返回源图像按给定宽度、高度限制下缩放后的图像
     *
     * @param inputImage
     *            ：压缩后宽度
     *            ：压缩后高度
     * @throws IOException
     *             return
     */
    public static BufferedImage scaleByPercentage(BufferedImage inputImage, int newWidth, int newHeight){
        // 获取原始图像透明度类型
        try {
            int type = inputImage.getColorModel().getTransparency();
            int width = inputImage.getWidth();
            int height = inputImage.getHeight();
            // 开启抗锯齿
            RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // 使用高质量压缩
            renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            BufferedImage img = new BufferedImage(newWidth, newHeight, type);
            Graphics2D graphics2d = img.createGraphics();
            graphics2d.setRenderingHints(renderingHints);
            graphics2d.drawImage(inputImage, 0, 0, newWidth, newHeight, 0, 0, width, height, null);
            graphics2d.dispose();
            return img;

        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 参数校验
     * @param r
     * @param g
     * @param b
     * @param a
     */
    public static void testColorValueRange(int r, int g, int b, int a) {
        boolean rangeError = false;
        String badComponentString = "";

        if ( a < 0 || a > 255) {
            rangeError = true;
            badComponentString = badComponentString + " Alpha";
        }
        if ( r < 0 || r > 255) {
            rangeError = true;
            badComponentString = badComponentString + " Red";
        }
        if ( g < 0 || g > 255) {
            rangeError = true;
            badComponentString = badComponentString + " Green";
        }
        if ( b < 0 || b > 255) {
            rangeError = true;
            badComponentString = badComponentString + " Blue";
        }
        if (rangeError) {
            throw new IllegalArgumentException("颜色参数超过有效范围:"
                    + badComponentString);
        }
    }

    public static void main(String[] args) throws Exception {
//        String out = "E:\\Test\\" + System.currentTimeMillis() + ".png";
//        String out = "C:\\Users\\Administrator\\Desktop\\img\\" + System.currentTimeMillis() + ".png";
//        ringImage("https://image.highyundong.com/store/logo/1638199478947_e8490dc4.png", 10, out, "132,112,255");
//        byte[] img = getCircularImage("https://image.highyundong.com/store/logo/1638199478947_e8490dc4.png");
//        OssUtil.uploadSingleImage("store/logo/topic/" + System.currentTimeMillis() + ".png", img);

        String logo = "https://image.highyundong.com/store/logo/1638199478947_e8490dc4.png?x-oss-process=image/resize,m_fixed,h_30,w_30";
//        ringImage(img, 2, out, "255,0,0");

        String lastFile = logo;
        List<String> colorList = Arrays.asList("255,0,0,", "0,0,255", "255,255,0");
        for (String rgb : colorList) {
            String out = "C:\\Users\\Administrator\\Desktop\\img\\" + System.currentTimeMillis() + ".png";
            RingTopic.ringImage(lastFile, 4, out, rgb);
            lastFile = out;
        }
    }
}