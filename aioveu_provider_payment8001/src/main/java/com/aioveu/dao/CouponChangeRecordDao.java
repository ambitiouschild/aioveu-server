package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.CouponChangeRecord;
import com.aioveu.vo.CouponVerifyItemVO;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface CouponChangeRecordDao extends BaseMapper<CouponChangeRecord> {

    /**
     * 列表,查询指定店铺的卡券变更记录
     * @param page
     * @param date
     * @param phone
     * @return
     */
    IPage<CouponVerifyItemVO> getByStoreAndDate(IPage<CouponVerifyItemVO> page, String date, String phone,String storeId);

    /**
     * 获取课券对应的班级id
     * @param userId
     * @param userCouponId
     * @return
     */
    Long getGradeId(String userId, Long userCouponId);


}
