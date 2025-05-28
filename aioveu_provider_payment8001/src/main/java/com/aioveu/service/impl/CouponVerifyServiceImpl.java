package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.CouponVerifyDao;
import com.aioveu.entity.CouponVerify;
import com.aioveu.service.CouponVerifyService;
import com.aioveu.vo.CouponVerifyItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class CouponVerifyServiceImpl extends ServiceImpl<CouponVerifyDao, CouponVerify> implements CouponVerifyService {


    @Override
    public IPage<CouponVerifyItemVO> pageList(Integer page, Integer size, Long storeId, String date, String phone) {
        return getBaseMapper().getCouponVerifyList(new Page<>(page, size), storeId, date, phone);
    }

    @Override
    public boolean recordCouponVerify(String userId, Long storeId, Long userCouponId, String remark) {
        CouponVerify couponVerify = new CouponVerify();
        couponVerify.setUserId(userId);
        couponVerify.setStoreId(storeId);
        couponVerify.setUserCouponId(userCouponId);
        couponVerify.setRemark(remark);
        return save(couponVerify);
    }

    @Override
    public boolean recordCouponVerifyBatch(String userId, Long storeId, List<Long> userCouponIdList, String remark) {
        List<CouponVerify> couponVerifyList = userCouponIdList.stream().map(userCouponId -> {
            CouponVerify couponVerify = new CouponVerify();
            couponVerify.setUserId(userId);
            couponVerify.setStoreId(storeId);
            couponVerify.setUserCouponId(userCouponId);
            couponVerify.setRemark(remark);
            return couponVerify;
        }).collect(Collectors.toList());
        return saveBatch(couponVerifyList);
    }
}
