package com.aioveu.utils;

/**
 * @description 金额转成中文
 * @author: 雒世松
 * @date: Created in 2025/4/27 11:41
 */
public class NumberToChineseMoneyUtil {
    private static final String[] CN_UNITS = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};
    private static final String[] CN_NUMBERS = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};

    /**
     * 金额转中文
     * @param money
     * @return
     */
    public static String convertToChinese(double money) {
        if (money == 0) {
            return "零";
        }
        long integerPart = (long) money;
        int decimalPart = (int) ((money - integerPart) * 100);

        StringBuilder result = new StringBuilder();
        if (integerPart == 0) {
            result.append(CN_NUMBERS[0]);
        } else {
            result.append(convertIntegerToChinese(integerPart));
        }
        result.append("元");

        if (decimalPart == 0) {
            result.append("整");
        } else {
            int jiao = decimalPart / 10;
            int fen = decimalPart % 10;
            if (jiao != 0) {
                result.append(CN_NUMBERS[jiao]).append("角");
            }
            if (fen != 0) {
                result.append(CN_NUMBERS[fen]).append("分");
            }
        }

        return result.toString();
    }

    private static String convertIntegerToChinese(long number) {
        if (number == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        int unitIndex = 0;
        boolean zero = false;
        while (number > 0) {
            int digit = (int) (number % 10);
            if (digit == 0) {
                if (!zero) {
                    result.insert(0, CN_NUMBERS[0]);
                    zero = true;
                }
            } else {
                if (zero) {
                    result.insert(0, CN_NUMBERS[0]);
                    zero = false;
                }
                result.insert(0, CN_UNITS[unitIndex]);
                result.insert(0, CN_NUMBERS[digit]);
            }
            number /= 10;
            unitIndex++;
        }
        // 去除开头的零
        if (result.length() > 0 && result.charAt(0) == '零') {
            result.deleteCharAt(0);
        }
        return result.toString();
    }

    public static void main(String[] args) {
        double money = 1234.56;
        String chineseMoney = convertToChinese(money);
        System.out.println(chineseMoney);
    }
}    