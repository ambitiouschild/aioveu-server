package com.aioveu.common.util;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @ClassName: DateTimeCalculator
 * @Description TODO 日期时间计算工具类
 *                      * 修复 LocalDateTime 计算问题
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 19:03
 * @Version 1.0
 **/

public class DateTimeCalculator {


    /**
     * 计算两个 LocalDateTime 之间的毫秒差
     */
    public static long getMillisBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0L;
        }
        Duration duration = Duration.between(start, end);
        return duration.toMillis();
    }

    /**
     * 计算两个 LocalDateTime 之间的秒差
     */
    public static long getSecondsBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0L;
        }
        Duration duration = Duration.between(start, end);
        return duration.getSeconds();
    }

    /**
     * 计算两个 LocalDateTime 之间的分钟差
     */
    public static long getMinutesBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0L;
        }
        Duration duration = Duration.between(start, end);
        return duration.toMinutes();
    }

    /**
     * 计算两个 LocalDateTime 之间的小时差
     */
    public static long getHoursBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0L;
        }
        Duration duration = Duration.between(start, end);
        return duration.toHours();
    }

    /**
     * 计算两个 LocalDateTime 之间的天数差
     */
    public static long getDaysBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0L;
        }
        Duration duration = Duration.between(start, end);
        return duration.toDays();
    }

    /**
     * 使用 ChronoUnit 计算时间差
     */
    public static long getTimeBetween(LocalDateTime start, LocalDateTime end, ChronoUnit unit) {
        if (start == null || end == null || unit == null) {
            return 0L;
        }
        return unit.between(start, end);
    }

    /**
     * 计算两个 Date 之间的毫秒差
     */
    public static long getMillisBetween(Date start, Date end) {
        if (start == null || end == null) {
            return 0L;
        }
        return end.getTime() - start.getTime();
    }

    /**
     * 计算耗时（人类可读格式）
     */
    public static String getHumanReadableDuration(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return "0ms";
        }

        Duration duration = Duration.between(start, end);
        return formatDuration(duration);
    }

    /**
     * 格式化持续时间
     */
    public static String formatDuration(Duration duration) {
        if (duration == null) {
            return "0ms";
        }

        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        long millis = duration.toMillis() % 1000;

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");
        if (seconds > 0) sb.append(seconds).append("s ");
        if (millis > 0 || sb.length() == 0) sb.append(millis).append("ms");

        return sb.toString().trim();
    }
}
