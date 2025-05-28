package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.CouponSharingDao;
import com.aioveu.entity.CouponSharing;
import com.aioveu.service.CouponSharingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description 优惠券分享记录
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@Service
public class CouponSharingServiceImpl extends ServiceImpl<CouponSharingDao, CouponSharing> implements CouponSharingService {

    @Override
    public boolean create(String sharingUserId, Long couponTemplateId, String couponCode) {
        CouponSharing couponSharing = new CouponSharing();
        couponSharing.setSharingUserId(sharingUserId);
        couponSharing.setCouponCode(couponCode);
        couponSharing.setTemplateId(couponTemplateId);
        return save(couponSharing);
    }

    @Override
    public CouponSharing getByCode(String couponCode) {
        QueryWrapper<CouponSharing > queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CouponSharing::getCouponCode,couponCode);
        return getOne(queryWrapper);
    }

    @Override
    public boolean changeReceive(Long id, String receiveUserId) {
        CouponSharing couponSharing = new CouponSharing();
        couponSharing.setReceiveUserId(receiveUserId);
        couponSharing.setId(id);
        return updateById(couponSharing);
    }
}
