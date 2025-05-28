package com.aioveu.utils;

import com.aioveu.exception.SportException;
import com.aioveu.service.SpecialDayService;
import com.aioveu.vo.PriceRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PriceRuleUtils{
    private static PriceRuleUtils priceRuleUtils;

    @Autowired
    private SpecialDayService specialDayService;

    @PostConstruct
    public void init() {
        priceRuleUtils = this;
        priceRuleUtils.specialDayService = this.specialDayService;
    }

    public static PriceRule matchingPriceRule(List<PriceRule> priceRuleList, Date date) {
        return matchingPriceRule(priceRuleList, date, null, null);
    }

    public static PriceRule matchingPriceRule(List<PriceRule> priceRuleList, Date date, Long fieldId) {
        return matchingPriceRule(priceRuleList, date, fieldId, null);
    }

    public static PriceRule matchingPriceRule(List<PriceRule> priceRuleList, Date date, Long fieldId, Long venueId) {
        if (CollectionUtils.isEmpty(priceRuleList)) {
            return null;
        }
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            LocalDate localDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            int week = localDate.getDayOfWeek().getValue();
            String dateYmdStr = DateFormatUtils.format(date, "yyyy-MM-dd");
            Date dateYmd = DateUtils.parseDate(dateYmdStr, "yyyy-MM-dd");
            Map<Integer, List<PriceRule>> priceRuleMap = new HashMap<>();

            for (PriceRule priceRule : priceRuleList) {
                if (fieldId != null) {
                    if (!CollectionUtils.isEmpty(priceRule.getFieldIdList())) {
                        List<Long> fieldIdList = priceRule.getFieldIdList().stream().filter(t -> t != null).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(fieldIdList) && !fieldIdList.contains(fieldId)) {
                            continue;
                        }
                    }
                }
                if (venueId != null) {
                    if (!CollectionUtils.isEmpty(priceRule.getVenueIdList())) {
                        List<Long> venueIdList = priceRule.getVenueIdList().stream().filter(t -> t != null).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(venueIdList) && !venueIdList.contains(venueId)) {
                            continue;
                        }
                    }
                }
                //自由时间
                if (priceRule.getType().equals(0)) {
                    Date dateFrom = DateUtils.parseDate(priceRule.getDateFrom(), "yyyy-MM-dd");
                    Date dateTo = DateUtils.parseDate(priceRule.getDateTo(), "yyyy-MM-dd");
                    if (dateFrom.compareTo(dateYmd) <= 0 && dateTo.compareTo(dateYmd) >= 0) {
                        if (StringUtils.isEmpty(priceRule.getTimeFrom())) {
                            addMap(priceRuleMap, 40, priceRule);
                            continue;
                        }
                        Date timeFrom = DateUtils.parseDate(dateYmdStr + " " + priceRule.getTimeFrom(), "yyyy-MM-dd HH:mm");
                        Date timeTo = DateUtils.parseDate(dateYmdStr + " " + priceRule.getTimeTo(), "yyyy-MM-dd HH:mm");
                        if (timeFrom.compareTo(date) <= 0 && timeTo.compareTo(date) > 0) {
                            addMap(priceRuleMap, 45, priceRule);
                        }
                    }
                    //节假日
                } else if (priceRule.getType().equals(2)) {
                    Boolean specialDay = priceRuleUtils.specialDayService.isSpecialDayByCache(DateUtil.getDayByDate(date), 1);
                    if (specialDay && getBetweenDate(priceRule.getTimeFrom(), priceRule.getTimeTo(), dateYmdStr, date)){
                        addMap(priceRuleMap, 30, priceRule);
                    }
                    //节假调休补班
                } else if(priceRule.getType().equals(3)){
                    Boolean specialDay = priceRuleUtils.specialDayService.isSpecialDayByCache(DateUtil.getDayByDate(date), 0);
                    if (specialDay && getBetweenDate(priceRule.getTimeFrom(), priceRule.getTimeTo(), dateYmdStr, date)){
                        addMap(priceRuleMap, 20, priceRule);
                    }
                    //每星期
                }else if (priceRule.getType().equals(1)) {
                    if (priceRule.getWeekFrom() <= week && priceRule.getWeekTo() >= week) {
                        if (StringUtils.isEmpty(priceRule.getTimeFrom())) {
                            addMap(priceRuleMap, 10, priceRule);
                            continue;
                        }
                        Date timeFrom = DateUtils.parseDate(dateYmdStr + " " + priceRule.getTimeFrom(), "yyyy-MM-dd HH:mm");
                        Date timeTo = DateUtils.parseDate(dateYmdStr + " " + priceRule.getTimeTo(), "yyyy-MM-dd HH:mm");
                        if (timeFrom.compareTo(date) <= 0 && timeTo.compareTo(date) > 0) {
                            addMap(priceRuleMap, 12, priceRule);
                        }
                    }
                }
            }
            Set<Integer> keys = priceRuleMap.keySet();
            if (keys.size() == 0) {
                log.warn("未找到匹配的价格规则");
                return null;
            }
            Integer maxKey = keys.stream().max(Comparator.comparingInt(t -> t)).get();
            List<PriceRule> priceRules = priceRuleMap.get(maxKey);
            return priceRules.get(priceRules.size() - 1);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new SportException("匹配价格规则时出错");
        }
    }

    public static void addMap(Map<Integer, List<PriceRule>> priceRuleMap, Integer key, PriceRule priceRule) {
        if (!priceRuleMap.containsKey(key)) {
            priceRuleMap.put(key, new ArrayList<>());
        }
        priceRuleMap.get(key).add(priceRule);
    }

    /**
     * 比较date是否在timeFromStr与timeToStr之间
     * @param timeFromStr  08:00
     * @param timeToStr    20:00
     * @param dateYmdStr   2025-02-12
     * @param date        2025-02-12 09:00
     * @return
     */
    public static Boolean getBetweenDate(String timeFromStr, String timeToStr, String dateYmdStr, Date date){
        try {
            if (StringUtils.isEmpty(timeFromStr) && StringUtils.isEmpty(timeToStr) ){
               return true;
            }
            if (StringUtils.isEmpty(timeFromStr)){
                Date timeTo = DateUtils.parseDate(dateYmdStr + " " + timeToStr, "yyyy-MM-dd HH:mm");
                if (timeTo.compareTo(date) > 0){
                    return true;
                }
            }
            if (StringUtils.isEmpty(timeToStr)){
                Date timeFrom = DateUtils.parseDate(dateYmdStr + " " + timeFromStr, "yyyy-MM-dd HH:mm");
                if (timeFrom.compareTo(date) <= 0){
                    return true;
                }
            }
            Date timeFrom = DateUtils.parseDate(dateYmdStr + " " + timeFromStr, "yyyy-MM-dd HH:mm");
            Date timeTo = DateUtils.parseDate(dateYmdStr + " " + timeToStr, "yyyy-MM-dd HH:mm");
            if (timeFrom.compareTo(date) <= 0 && timeTo.compareTo(date) > 0) {
                return true;
            }
        }catch (Exception e){
            log.error("betweenDate error {}", e.getMessage());
        }
        return false;

    }
}
