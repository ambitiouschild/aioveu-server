package com.aioveu.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/1 12:58
 */
public class XmlUtil {

    public static String getWeChatXmlStr(Map<String, String> sortMap) {
        // 构建XML参数
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<xml>").append("\n");
        for (Map.Entry<String, String> item : sortMap.entrySet()) {
            xmlBuilder.append("<").append(item.getKey()).append(">");
            xmlBuilder.append("<![CDATA[");
            xmlBuilder.append(item.getValue());
            xmlBuilder.append("]]>");
            xmlBuilder.append("</").append(item.getKey()).append(">").append("\n");
        }
        xmlBuilder.append("</xml>").append("\n");
        return xmlBuilder.toString();
    }

    public static void main(String[] args) throws Exception {
//        Date f = DateUtils.parseDate("2023-04-30", "yyyy-MM-dd");
//        Date o = DateUtils.parseDate("2022-10-18", "yyyy-MM-dd");
//        System.out.println((f.getTime() - o.getTime()) / 1000 / 3600 / 24 );

        DecimalFormat df = new DecimalFormat ("#.00");
        String result = df.format(Double.parseDouble("1.36") / 215.52);

        BigDecimal b = BigDecimal.valueOf(Double.parseDouble("1.36") / 215.52).multiply(new BigDecimal(100));
        double f1 = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(f1);
        int ratio = (int) (f1 * 100);
        System.out.println(ratio);
    }

}
