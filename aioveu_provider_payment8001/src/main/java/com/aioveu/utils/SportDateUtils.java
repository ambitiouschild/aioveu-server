package com.aioveu.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/9/20 10:47
 */
public class SportDateUtils {

    /**
     * 判断时间是否存在重叠
     * @param start1
     * @param end1
     * @param start2
     * @param end2
     * @return
     */
    public static boolean isOverlap(Date start1, Date end1, Date start2, Date end2) {
        if (end1.compareTo(start2) <= 0 || end2.compareTo(start1) <= 0) {
            return false;
        }
        return true;
    }

    /**
     * 获取7天内的所有日期
     * @return
     */
    public static List<String> getFuture7Day() {
        return getFutureDay(7);
    }
    /**
     * 获取未来
     * @return
     */
    public static List<String> getFutureDay(int days) {
        long millis = System.currentTimeMillis();
        List<String> dateList = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(millis);
            c.add(Calendar.DAY_OF_YEAR, i + 1);
            dateList.add(DateFormatUtils.format(new Date(c.getTimeInMillis()), "yyyy-MM-dd"));
        }
        return dateList;
    }
    public static List<String> getWeekDayFromString(List<String> strList) {
        return getWeekDayFromString(strList, 7);
    }
    public static List<String> getWeekDayFromString(List<String> strList, int days) {
        List<String> dateList = new ArrayList<>();

        Date now = new Date();
        for (int i = 0; i < days; i++) {
            Date date = DateUtils.addDays(now, i);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            int today = c.get(Calendar.DAY_OF_WEEK);
            for (String str : strList) {
                int weekDay = getWeekDayFromString(str);
                if(today != weekDay) {
                    continue;
                }
                dateList.add(DateFormatUtils.format(date, "yyyy-MM-dd"));
            }
        }
        return dateList;
    }

    public static List<String> getMonthDayFromString(List<String> strList) {
        return getMonthDayFromString(strList, 7);
    }

    public static List<String> getMonthDayFromString(List<String> strList, int days) {
        List<String> dateList = new ArrayList<>();
        String dayStr = DateFormatUtils.format(new Date(), "yyyy-MM");
        Date now = new Date();
//        Date now = null;
//        try {
//            now = DateUtils.parseDate("2022-03-28", "yyyy-MM-dd");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        Date end = DateUtils.addDays(now, days);
        for (String item : strList) {
            try {
                Date date = DateUtils.parseDate(dayStr + "-" + item, "yyyy-MM-dd");
                if (date.after(now) && date.before(end)) {
                    dateList.add(dayStr + "-" + item);
                } else {
                    Date future = DateUtils.addMonths(date, 1);
                    if (future.after(now) && future.before(end)) {
                        dateList.add(DateFormatUtils.format(future, "yyyy-MM-dd"));
                    } else {
                        dateList.add(dayStr + "-" + item);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return dateList;
    }

    public static String getDateByDayString(int day) {
        long millis = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
//        try {
//            c.setTime(DateUtils.parseDate("2022-09-24 14:54:21", "yyyy-MM-dd HH:mm:ss"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        c.add(Calendar.DAY_OF_YEAR, day);
        return DateFormatUtils.format(new Date(c.getTimeInMillis()), "yyyy-MM-dd");
    }

    public static List<Map<String, String>> get7Date() {
        List<Map<String, String>> dates = new ArrayList<>();
        long millis = System.currentTimeMillis();
        for (int i = 0; i < 7; i++) {

            Map<String, String> item = new HashMap<>();
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(millis);
            c.add(Calendar.DAY_OF_YEAR, i);
            Date date = new Date(c.getTimeInMillis());
            item.put("value", DateFormatUtils.format(date, "yyyy-MM-dd"));

            switch (i) {
                case 0:
                    item.put("name", "今天");
                    break;
                case 1:
                    item.put("name", "明天");
                    break;
                case 2:
                    item.put("name", "后天");
                    break;
                default:
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH) + 1;
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    item.put("name", getDayOfWeek(year + "-" + month + "-" + day));
                    break;
            }
            dates.add(item);
        }
        return dates;
    }

    /**
     * 获取日期对应的星期
     * @param day
     * @return
     */
    public static String getDayOfWeek(String day) {
        try {
            return getDayOfWeek(DateUtils.parseDate(day, "yyyy-MM-dd"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Integer getWeekDayFromString(String week) {
        if ("周日".equals(week)) {
            return 1;
        } else if ("周一".equals(week)) {
            return 2;
        } else if ("周二".equals(week)) {
            return 3;
        } else if ("周三".equals(week)) {
            return 4;
        } else if ("周四".equals(week)) {
            return 5;
        } else if ("周五".equals(week)) {
            return 6;
        } else if ("周六".equals(week)) {
            return 7;
        }
        return 0;
    }

    /**
     * 是否是周末
     * @param date
     * @return
     */
    public static boolean isWeekDay(Date date) {
        String week = getDayOfWeek(date);
        return "周六".equals(week) || "周日".equals(week);
    }

    /**
     * 根据当前日期获得是星期几
     *
     * @return
     */
    public static String getDayOfWeek(Date date) {
        String week = "";
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            week += "周日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            week += "周一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            week += "周二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            week += "周三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            week += "周四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            week += "周五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            week += "周六";
        }
        return week;
    }

    /**
     * 获取当然时间的下一周时间
     * @param date
     * @return
     */
    public static Date getFutureOneWeek(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 7);
        return calendar.getTime();
    }

    public static Date getFuture2Hour(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 2);
        return calendar.getTime();
    }

    public static String timeFormat(Time time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }

    /**
     * 格式化两个Time 并显示在一起 例如 10:00-12:00
     * @param start
     * @param end
     * @param format
     * @return
     */
    public static String format2Time(Time start, Time end, String format) {
        return timeFormat(start, format) + "-" + timeFormat(end, format);
    }

    /**
     * Time 转 Date
     * @param time
     * @return
     */
    public static Date time2Day(Time time) {
        return new Date(time.getTime());
    }

    /**
     * 合并两个日期
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getMergeTime(Date startTime, Date endTime){
        SimpleDateFormat dayFormat = new SimpleDateFormat("MM-dd HH:mm");
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return dayFormat.format(startTime)+ "-" + formatter.format(endTime);
    }

    /**
     * 合并两个日期 获取小时时间
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getMergeOnlyHourTime(Date startTime, Date endTime){
        SimpleDateFormat dayFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return dayFormat.format(startTime)+ "-" + formatter.format(endTime);
    }

    /**
     * 获取两个日期显示时间
     * @param startTime
     * @param endTime
     * @return
     */
    public static String get2Day(Date startTime, Date endTime) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startTime);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endTime);
        if (startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR)) {
            if (startCalendar.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH)) {
                if (startCalendar.get(Calendar.DAY_OF_MONTH) == endCalendar.get(Calendar.DAY_OF_MONTH)) {
                    // 使用 ~ 是因为微信服务号消息模板强制要求
                    return DateFormatUtils.format(startTime, "yyyy年MM月dd日 HH:mm") + "~" + DateFormatUtils.format(endTime, "HH:mm");
                } else {
                    return DateFormatUtils.format(startTime, "yyyy年MM月dd日 HH:mm") + "~" + DateFormatUtils.format(endTime, "dd日 HH:mm");
                }
            } else {
                return DateFormatUtils.format(startTime, "yyyy年MM月dd日 HH:mm") + "~" + DateFormatUtils.format(endTime, "MM月dd日 HH:mm");
            }
        } else {
            return DateFormatUtils.format(startTime, "yyyy年MM月dd日 HH:mm") + "~" + DateFormatUtils.format(endTime, "yyyy年MM-dd HH:mm");
        }
    }

    /**
     * 获取两个日期显示日期
     * @param startTime
     * @param endTime
     * @return
     */
    public static String get2Day2DayShow(Date startTime, Date endTime) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startTime);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endTime);
        String dayPattern = "yyyy年MM月dd日";
        if (startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR)) {
            if (startCalendar.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH)) {
                if (startCalendar.get(Calendar.DAY_OF_MONTH) == endCalendar.get(Calendar.DAY_OF_MONTH)) {
                    return DateFormatUtils.format(startTime, dayPattern);
                } else {
                    return DateFormatUtils.format(startTime, dayPattern) + "-" + DateFormatUtils.format(endTime, "dd日");
                }
            } else {
                return DateFormatUtils.format(startTime, dayPattern) + "-" + DateFormatUtils.format(endTime, "MM月dd日");
            }
        } else {
            return DateFormatUtils.format(startTime, dayPattern) + "-" + DateFormatUtils.format(endTime, "yyyy年MM月-dd日");
        }
    }

    public static String getNowFormatTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy-MM-dd HH:mm:ss");
    }

    // 定义可能的时间格式
    private static final String[] DATE_FORMATS = {
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd"
    };

    /**
     * 将时间字符串转换为Date对象
     *
     * @param dateString 时间字符串
     * @return Date对象
     * @throws ParseException 如果无法解析时间字符串
     */
    public static Date parseDate(String dateString) throws ParseException {
        for (String format : DATE_FORMATS) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                // 严格解析日期
                sdf.setLenient(false);
                return sdf.parse(dateString);
            } catch (ParseException e) {
                // 继续尝试下一个格式
            }
        }
        throw new ParseException("无法解析日期: " + dateString, 0);
    }

    /**
     * 获取两个日期之间的所有日期
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 日期字符串集合，格式为 "yyyy-MM-dd"
     */
    public static List<String> getDateRange(Date startDate, Date endDate) {
        List<String> dateRange = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // 使用Calendar进行日期操作
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        // 循环直到结束日期
        while (!calendar.getTime().after(endDate)) {
            dateRange.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH, 1); // 增加一天
        }

        return dateRange;
    }

    /**
     * 带时分秒的日期转成不带时分秒的日期
     * @param dateTime
     * @return
     * @throws ParseException
     */
    public static Date dayTime2Day(Date dateTime) throws ParseException {
        String dayStr = DateFormatUtils.format(dateTime, "yyyy-MM-dd");
        return DateUtils.parseDate(dayStr, "yyyy-MM-dd");
    }

    /**
     * 判断时间字符串是否是整点或半小时
     * @param timeStr 时间字符串，格式为 "HH:mm:ss"
     * @return true 如果是整点或半小时，否则返回 false
     * @throws DateTimeParseException 如果时间格式不正确
     */
    public static boolean isValidTimeFormat(String timeStr) throws DateTimeParseException {
        // 定义时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        // 解析时间字符串
        LocalTime time = LocalTime.parse(timeStr, formatter);

        // 获取分钟和秒
        int minute = time.getMinute();
        int second = time.getSecond();

        // 判断是否为整点或半小时，且秒数为0
        return (minute == 0 || minute == 30) && second == 0;
    }

    /**
     * 合并date和time到新的Date对象
     * @param date
     * @param time
     * @return
     */
    public static Date combineDateAndTime(Date date, Time time) {
        // 将 java.util.Date 转换为 LocalDate
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        // 将 java.sql.Time 转换为 LocalTime
        LocalTime localTime = time.toLocalTime();
        // 组合日期和时间
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        // 将 LocalDateTime 转换为 java.util.Date
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取开始和结束时间
     * @param param
     * @return
     * @throws Exception
     */
    public static Tuple2<Date, Date> getStartAndEndDate(Map<String, Object> param) throws Exception {
        Date start = DateUtils.parseDate(param.get("start") + "", "yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.parseDate(param.get("end") + "", "yyyy-MM-dd"));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date end = calendar.getTime();
        return Tuples.of(start, end);
    }

    /**
     * 获取时间查有多个指定分钟数
     * @param startDate
     * @param endDate
     * @param minute
     * @return
     */
    public static int getNumberOfMinute(Date startDate, Date endDate, int minute) {
        // 计算两个日期之间的毫秒数差值
        long diffInMillis = endDate.getTime() - startDate.getTime();

        // 将毫秒数差值转换为分钟数
        long diffInMinutes = diffInMillis / (1000 * 60);

        // 计算有多少个指定分钟数
        return (int) (diffInMinutes / minute);
    }

    /**
     * 获取天对应的日期，去除小时分钟和秒
     * @param amount
     * @return
     * @throws ParseException
     */
    public static Date getDayNoHour(int amount) throws ParseException  {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, amount);
        String day = DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd");
        return DateUtils.parseDate(day, "yyyy-MM-dd");
    }

    /**
     * 格式化日期时间
     * @param date
     * @param start
     * @param end
     * @return
     */
    public static String formatDateTime(Date date, Time start, Time end) {
        return DateFormatUtils.format(date, "yyyy-MM-dd") + " " +  SportDateUtils.format2Time(start, end, "HH:mm");
    }

    /**
     * 订场是否过期
     * @param fieldDay
     * @param time
     * @param now
     * @return
     */
    public static boolean isExpire(Date fieldDay, Time time, Date now) {
        try {
            Date fieldDateTime = DateUtils.parseDate(DateFormatUtils.format(fieldDay, "yyyy-MM-dd") + " " + time.toString(),
                    "yyyy-MM-dd HH:mm:ss");
            return fieldDateTime.before(now);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        // 示例日期
        Date date1 = DateUtils.parseDate("2025-04-26", "yyyy-MM-dd");
        Time time = Time.valueOf("22:00:00");
        boolean isExpire = isExpire(date1, time, new Date());
        System.out.println(isExpire);

    }
}
