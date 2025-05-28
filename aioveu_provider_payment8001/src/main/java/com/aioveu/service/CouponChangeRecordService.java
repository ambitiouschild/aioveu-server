package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.CouponChangeRecord;
import com.aioveu.vo.CouponVerifyItemVO;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface CouponChangeRecordService extends IService<CouponChangeRecord> {

    /**
     * 创建记录
     * @param userId
     * @param userCouponId
     * @param remark
     * @param gradeId
     * @return
     */
    boolean create(String userId, Long userCouponId, String remark, Long gradeId);

    /**
     * 列表分页
     * @param page
     * @param size
     * @param date
     * @param phone
     * @param storeId
     * @return
     */
    IPage<CouponVerifyItemVO> pageList(Integer page, Integer size, String date, String phone, String storeId);

    /**
     * 获取课券对应的班级id
     * @param userId
     * @param userCouponId
     * @return
     */
    Long getGradeId(String userId, Long userCouponId);


}
