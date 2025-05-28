package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.CouponVerify;
import com.aioveu.vo.CouponVerifyItemVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface CouponVerifyService extends IService<CouponVerify> {

    /**
     * 列表分页
     * @param page
     * @param size
     * @param storeId
     * @param date
     * @param phone
     * @return
     */
    IPage<CouponVerifyItemVO> pageList(Integer page, Integer size, Long storeId, String date, String phone);

    /**
     * 优惠券核销记录
     * @param userId
     * @param storeId
     * @param userCouponId
     * @param remark
     * @return
     */
    boolean recordCouponVerify(String userId, Long storeId, Long userCouponId, String remark);

    /**
     * 优惠券批量核销记录
     * @param userId
     * @param storeId
     * @param userCouponIdList
     * @param remark
     * @return
     */
    boolean recordCouponVerifyBatch(String userId, Long storeId, List<Long> userCouponIdList, String remark);


}
