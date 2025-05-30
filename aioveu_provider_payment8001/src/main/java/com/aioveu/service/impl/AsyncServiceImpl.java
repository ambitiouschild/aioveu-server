package com.aioveu.service.impl;

import com.google.common.base.Stopwatch;
import com.aioveu.constant.CouponConstant;
import com.aioveu.dao.CouponTemplateDao;
import com.aioveu.entity.CouponTemplate;
import com.aioveu.service.IAsyncService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description <h1>异步服务接口实现</h1>
 * @author: 雒世松
 * @date: 2025/1/27 0027 16:45
 */
@Slf4j
@Service
public class AsyncServiceImpl implements IAsyncService {

    /** CouponTemplate Dao */
    private final CouponTemplateDao couponTemplateDao;

    /** 注入 Redis 模板类 */
    private final StringRedisTemplate stringRedisTemplate;

    public AsyncServiceImpl(CouponTemplateDao couponTemplateDao, StringRedisTemplate stringRedisTemplate) {
        this.couponTemplateDao = couponTemplateDao;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * <h2>根据模板异步的创建优惠券码</h2>
     * @param template {@link CouponTemplate} 优惠券模板实体
     */
    @Async("getAsyncExecutor")
    @Override
    public void asyncConstructCouponByTemplate(CouponTemplate template) {
        Stopwatch watch = Stopwatch.createStarted();
        Set<String> couponCodes = buildCouponCode(template);
        if (couponCodes.size()>0) {
            // fan_coupon_template_code_1
            String redisKey = String.format("%s%s",
                    CouponConstant.RedisPrefix.COUPON_TEMPLATE, template.getId().toString());
            log.info("Push CouponCode To Redis: {}",
                    stringRedisTemplate.opsForList().rightPushAll(redisKey, couponCodes));
        }

        template.setAvailable(true);
        couponTemplateDao.updateById(template);

        watch.stop();
        log.info("Construct CouponCode By Template Cost: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));

        // TODO 发送短信或者邮件通知优惠券模板已经可用
        log.info("CouponTemplate({}) Is Available!", template.getId());
    }

    /**
     * <h2>尝试从 Cache 中获取一个优惠券码</h2>
     * @param templateId 优惠券模板主键
     * @return 优惠券码
     */
    @Override
    public String tryToAcquireCouponCodeFromCache(Long templateId) {

        String redisKey = String.format("%s%s",
                CouponConstant.RedisPrefix.COUPON_TEMPLATE, templateId.toString());
        // 因为优惠券码不存在顺序关系, 左边 pop 或右边 pop, 没有影响
        String couponCode = stringRedisTemplate.opsForList().leftPop(redisKey);

        log.info("Acquire Coupon Code: {}, {}, {}",
                templateId, redisKey, couponCode);

        return couponCode;
    }

    @Override
    public List<String> getAllCodeByTemplateId(Long templateId) {
        String redisKey = String.format("%s%s",
                CouponConstant.RedisPrefix.COUPON_TEMPLATE, templateId.toString());
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();

        Long size = listOperations.size(redisKey);
        if (size == null || size == 0) {
            return null;
        }
        return listOperations.range(redisKey, 0, size);
    }

    @Override
    public boolean removeCouponCode(Long templateId, String code) {
        String redisKey = String.format("%s%s",
                CouponConstant.RedisPrefix.COUPON_TEMPLATE, templateId.toString());
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
        Long removeIndex = listOperations.remove(redisKey, 1, code);
        if (removeIndex != null) {
            return removeIndex > 0;
        }
        return  false;
    }

    /**
     * <h2>构造一个优惠券码</h2>
     * @param template {@link CouponTemplate} 优惠券模板实体
     * @return String 优惠券码
     */
    @Override
    public String buildOneCouponCode(CouponTemplate template) {
        // 前四位
        String prefix4 = template.getProductLine().getCode().toString()
                + template.getCategory().getCode();
        String date = new SimpleDateFormat("yyMMdd")
                .format(template.getCreateDate());
        return prefix4 + buildCouponCodeSuffix14(date);
    }

    /**
     * <h2>构造优惠券码</h2>
     * 优惠券码(对应于每一张优惠券, 18位)
     * 前四位: 产品线 + 类型
     * 中间六位: 日期随机(190101)
     * 后八位: 0 ~ 9 随机数构成
     * @param template {@link CouponTemplate} 优惠券模板实体
     * @return Set<String> 与 template.count 相同个数的优惠券码
     */
    private Set<String> buildCouponCode(CouponTemplate template) {
        if (template.getCouponCount()==-1) {
            log.info("The Coupon Template Code Is Not Limit: {}", template.getId());
            return Collections.EMPTY_SET;
        }
        Stopwatch watch = Stopwatch.createStarted();

        Set<String> result = new HashSet<>(template.getCouponCount());
        // 前四位
        String prefix4 = template.getProductLine().getCode().toString()
                + template.getCategory().getCode();
        String date = new SimpleDateFormat("yyMMdd")
                .format(template.getCreateDate());

        for (int i = 0; i != template.getCouponCount(); ++i) {
            result.add(prefix4 + buildCouponCodeSuffix14(date));
        }
        // 这里为啥先for  再 while， 因为for的效率比较高，result.size()比较耗性能
        while (result.size() < template.getCouponCount()) {
            result.add(prefix4 + buildCouponCodeSuffix14(date));
        }

        assert result.size() == template.getCouponCount();

        watch.stop();
        log.info("Build Coupon Code Cost: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));

        return result;
    }

    /**
     * <h2>构造优惠券码的后 14 位</h2>
     * @param date 创建优惠券的日期
     * @return 14 位优惠券码
     */
    private String buildCouponCodeSuffix14(String date) {
        char[] bases = new char[] {'1', '2', '3', '4', '5', '6', '7', '8', '9'};

        // 中间六位
        List<Character> chars = date.chars()
                .mapToObj(e -> (char)e).collect(Collectors.toList());
        // 随机排列
        Collections.shuffle(chars);
        String mid6 = chars.stream().map(Object::toString).collect(Collectors.joining());

        // 后八位
        String suffix8 = RandomStringUtils.random(1, bases)
                + RandomStringUtils.randomNumeric(7);

        return mid6 + suffix8;
    }
}
