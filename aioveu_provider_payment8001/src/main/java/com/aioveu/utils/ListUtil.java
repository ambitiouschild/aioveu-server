package com.aioveu.utils;

import com.aioveu.constant.SportConstant;
import com.aioveu.entity.UserCoupon;
import com.aioveu.enums.PeriodType;
import com.aioveu.vo.TemplateRule;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.*;

/**
 * @description
 * @author: 雒世松
 * @date: 2018/2/28 19:41
 */
public class ListUtil {

    public static void checkParam(Map<String, Object> param)throws Exception {
        checkParam(param, true);
    }

    public static void checkParam(Map<String, Object> param, boolean checkTime) throws Exception {
        if (param == null) {
            param = new HashMap<>();
        }
        if (param.get("longitude") == null || param.get("longitude").equals("null")) {
            param.put("longitude", SportConstant.CENTER_LONGITUDE);
        }
        if (param.get("categoryCode") == null) {
            param.put("categoryCode", "store_course");
        }
        if (param.get("latitude") == null || param.get("latitude").equals("null")) {
            param.put("latitude", SportConstant.CENTER_LATITUDE);
        }
        if (checkTime) {
            Date now = new Date();
            String dateStr = DateFormatUtils.format(now, "yyyy-MM-dd");
            if (param.get("startTime") == null || param.get("endTime") == null || StringUtils.equals(param.get("startTime").toString(), "null")
                    || StringUtils.equals(param.get("startTime").toString(), "undefined")) {
                Date startTime = DateUtils.addMinutes(now, 5);
                Date endTime = DateUtils.parseDate(dateStr + " 23:59", "yyyy-MM-dd HH:mm");
                param.put("startTime", startTime);
                param.put("endTime", endTime);
            }else {
                Date startTime = DateUtils.parseDate(param.get("startTime").toString(), "yyyy-MM-dd HH:mm");
                if (DateUtils.isSameDay(startTime, now)){
                    startTime = DateUtils.addMinutes(now, 5);
                    param.put("startTime", startTime);
                }
            }
        }
    }

    /**
     * 列表分组
     * @param source 待分组列表
     * @param eachGroupNumber 每组数量
     * @param <T>
     * @return
     */
    public static  <T> List<List<T>> group(List<T> source, int eachGroupNumber) {
        // 前面截取的数量
        int startSize = eachGroupNumber / 2;
        int startIndex = 0;
        // 后面截取的数量
        int endSize = eachGroupNumber - startSize;
        int endIndex = source.size();

        int groupNumber = source.size() / eachGroupNumber;

        List<List<T>> resultList = new ArrayList<>();
        for (int i=0; i<groupNumber; i++){
            List<T> list = new ArrayList<>(source);
            List<T> startList = list.subList(startIndex, startIndex + startSize);
            List<T> endList = list.subList(endIndex - endSize, endIndex);

            startIndex = startIndex + startSize;
            endIndex = endIndex - endSize;

            startList.addAll(endList);

            resultList.add(startList);
        }
        return resultList;
    }

    /**
     * 将一组数据平均分成n组
     *
     * @param source 要分组的数据源
     * @param n      平均分成n组
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remainder = source.size() % n;  //(先计算出余数)
        int number = source.size() / n;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remainder > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remainder--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

    /**
     * 获取优惠券的过期时间
     * @param uc
     * @return
     */
    public static Date getCouponExpireDate(UserCoupon uc) {
        TemplateRule rule1 = uc.getRule();
        if (rule1 == null) {
            rule1 = uc.getTemplateSDK().getRule();
        }
        if (rule1 != null) {
            TemplateRule.Expiration expiration1 = rule1.getExpiration();
            if (expiration1 != null) {
                // 固定过期时间
                if (PeriodType.REGULAR.equals(PeriodType.of(expiration1.getPeriod()))) {
                    return new Date(expiration1.getDeadline());
                } else if (PeriodType.SHIFT.equals(PeriodType.of(expiration1.getPeriod()))) {
                    // 根据领取时间计算
                    return DateUtils.addDays(uc.getCreateDate(), expiration1.getGap());
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 优惠券排序，快过期的在前面
     * @param userCouponList
     */
    public static void couponSort(List<UserCoupon> userCouponList) {
        // 对用户的优惠券进行排序 快过期的优惠券先使用
        userCouponList.sort((o1, o2) ->  {
            Date expire1 = ListUtil.getCouponExpireDate(o1);
            Date expire2 = ListUtil.getCouponExpireDate(o2);
            if (expire1 == null && expire2 == null) {
                return 0;
            } else if (expire1 == null) {
                return 1;
            } else if (expire2 == null) {
                return -1;
            } else {
                return expire1.before(expire2) ? -1 : 1;
            }
        });
    }

    public static void main(String[] args) {
        List<Long> userInfoIdList = Arrays.asList(100L, 11L, 23L);
//        List<List<Long>> partitionUserInfoIdList = Lists.partition(userInfoIdList, 2);
        List<List<Long>> partitionUserInfoIdList = averageAssign(userInfoIdList, 3);
        System.out.println("123");
    }


}
