package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.CouponVerify;
import com.aioveu.vo.CouponVerifyItemVO;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface CouponVerifyDao extends BaseMapper<CouponVerify> {

    /**
     * 列表
     * @param page
     * @param storeId
     * @param date
     * @param phone
     * @return
     */
    IPage<CouponVerifyItemVO> getCouponVerifyList(IPage<CouponVerifyItemVO> page, Long storeId, String date, String phone);

}
