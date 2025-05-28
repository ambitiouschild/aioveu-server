package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.CouponTemplateDao;
import com.aioveu.entity.CouponSharing;
import com.aioveu.entity.CouponTemplate;
import com.aioveu.entity.UserCoupon;
import com.aioveu.enums.CouponCategory;
import com.aioveu.enums.DataStatus;
import com.aioveu.enums.PeriodType;
import com.aioveu.exception.SportException;
import com.aioveu.service.CouponSharingService;
import com.aioveu.service.CouponTemplateService;
import com.aioveu.service.IAsyncService;
import com.aioveu.service.ITemplateBaseService;
import com.aioveu.vo.CouponTemplateVO;
import com.aioveu.vo.IdNameVO;
import com.aioveu.vo.TemplateRule;
import com.aioveu.vo.UserCouponVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class CouponTemplateServiceImpl extends ServiceImpl<CouponTemplateDao, CouponTemplate> implements CouponTemplateService {

    @Autowired
    private CouponSharingService couponSharingService;

    @Autowired
    private ITemplateBaseService iTemplateBaseService;

    @Autowired
    private IAsyncService iAsyncService;

    @Override
    public CouponTemplate findByName(String name) {
        return getOne(new QueryWrapper<CouponTemplate>().eq("name", name));
    }

    @Override
    public List<CouponTemplate> findAllByAvailableAndExpired(boolean available, boolean expired) {
        return list(new QueryWrapper<CouponTemplate>().eq("available", available).eq("expired", expired));
    }

    @Override
    public List<CouponTemplate> findAllByExpired(boolean expired) {
        return list(new QueryWrapper<CouponTemplate>().eq("expired", expired));
    }

    @Override
    public List<IdNameVO> getSimpleCouponByStoreId(Long storeId) {
        List<CouponTemplate> couponTemplateList = list(new QueryWrapper<CouponTemplate>()
                .lambda()
                .eq(CouponTemplate::getStoreId, storeId)
                .eq(CouponTemplate::getStatus, DataStatus.NORMAL.getCode())
                .orderByDesc(CouponTemplate::getCreateDate));
        if (CollectionUtils.isNotEmpty(couponTemplateList)) {
            return couponTemplateList.stream().map(item -> new IdNameVO(item.getId(), item.getName())).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<CouponTemplate> getCouponListByStoreId(Long storeId) {
        List<CouponTemplate> couponTemplateList = list(new QueryWrapper<CouponTemplate>()
                .lambda()
                .eq(CouponTemplate::getStoreId, storeId)
                .eq(CouponTemplate::getShowStore, 1)
                .eq(CouponTemplate::getStatus, DataStatus.NORMAL.getCode())
                .orderByDesc(CouponTemplate::getCreateDate));
        return couponTemplateList;
    }

    private List<CouponTemplateVO> transCouponTemplateVO(List<CouponTemplate> couponTemplateList) {
        List<CouponTemplateVO> list = new ArrayList<>();
        for (CouponTemplate couponTemplate : couponTemplateList) {
            CouponTemplateVO couponTemplateVO = new CouponTemplateVO();
            BeanUtils.copyProperties(couponTemplate, couponTemplateVO);
            list.add(couponTemplateVO);
        }
        return list;
    }

    private List<UserCouponVO> userCoupon2UserCouponVO(List<UserCoupon> userCouponList, boolean useExpire) {
        return userCouponList.stream().map(item -> {
            UserCouponVO userCouponVO = new UserCouponVO();
            userCouponVO.setId(item.getId());
            userCouponVO.setActivePrice(item.getActivePrice());
            userCouponVO.setName(item.getTemplateSDK().getName());
            userCouponVO.setTemplateId(item.getTemplateId());
            userCouponVO.setCategory(CouponCategory.of(item.getTemplateSDK().getCategory()).getDescription());
            userCouponVO.setStatus(item.getStatus().getCode());
            userCouponVO.setAmount(item.getAmount());

            Date start = item.getCreateDate();
            Date end = null;
            if (item.getTemplateSDK().getRule().getExpiration().getPeriod().equals(PeriodType.REGULAR.getCode())) {
                if (item.getTemplateSDK().getRule().getExpiration().getDeadline() != null) {
                    end = new Date(item.getTemplateSDK().getRule().getExpiration().getDeadline());
                }
            } else if (item.getTemplateSDK().getRule().getExpiration().getGap() != null) {
                end = DateUtils.addDays(start, item.getTemplateSDK().getRule().getExpiration().getGap());
            }
            if (end != null) {
                userCouponVO.setTime(DateFormatUtils.format(start, "yyyy-MM-dd") + " - " +
                        DateFormatUtils.format(end, "yyyy-MM-dd"));
            } else {
                userCouponVO.setTime("永久");
            }

            userCouponVO.setRemark("");
            if (!useExpire && userCouponVO.getCategory().equals(CouponCategory.DUIHUAN.getDescription())) {
                userCouponVO.setRemark("点击进行核销");
            }
            return userCouponVO;
        }).collect(Collectors.toList());
    }

    @Override
    public String getShareCouponCode(Long id, String sharingUserId) {
        CouponTemplate couponTemplate = getById(id);
        if (couponTemplate == null) {
            throw new SportException("优惠券不存在");
        }
        String couponCode = null;
        if (couponTemplate.getCouponCount() != -1) {
            List<String> couponCodeList = iTemplateBaseService.getCouponCodeList(id);
            if (CollectionUtils.isEmpty(couponCodeList)) {
                throw new SportException("无可用的优惠券!");
            }
            for (String item : couponCodeList) {
                CouponSharing couponSharing = couponSharingService.getByCode(item);
                if (couponSharing == null) {
                    couponCode = item;
                    break;
                }
            }
            if (couponCode == null) {
                throw new SportException("没有可以分享的优惠券!");
            }
        } else {
            couponCode = iAsyncService.buildOneCouponCode(couponTemplate);
            log.info("无限优惠券模板, 随机生成一个券码:{}", couponCode);
        }
        couponSharingService.create(sharingUserId, id, couponCode);
        return couponCode;
    }

    @Override
    public List<Long> getByStoreId(Long storeId) {
        QueryWrapper<CouponTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CouponTemplate::getStoreId, storeId);
        return list(queryWrapper).stream().map(CouponTemplate::getId).collect(Collectors.toList());
    }

    @Override
    public void offlineCouponTemplate() {
        List<CouponTemplate> templates = findAllByExpired(false);
        if (CollectionUtils.isEmpty(templates)) {
            log.info("Done To Expire CouponTemplate.");
            return;
        }

        Date cur = new Date();
        List<CouponTemplate> expiredTemplates = new ArrayList<>(templates.size());
        templates.forEach(t -> {
            // 根据优惠券模板规则中的 "过期规则" 校验模板是否过期
            TemplateRule rule = t.getRule();
            if (rule.getExpiration().getDeadline() != null && rule.getExpiration().getDeadline() < cur.getTime()) {
                t.setExpired(true);
                expiredTemplates.add(t);
            }
        });

        if (CollectionUtils.isNotEmpty(expiredTemplates)) {
            log.info("Expired CouponTemplate Num: {}", saveOrUpdateBatch(expiredTemplates));
        }
    }
}
