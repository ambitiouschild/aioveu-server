package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.CouponChangeRecordDao;
import com.aioveu.entity.CouponChangeRecord;
import com.aioveu.service.CouponChangeRecordService;
import com.aioveu.vo.CouponVerifyItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class CouponChangeRecordServiceImpl extends ServiceImpl<CouponChangeRecordDao, CouponChangeRecord> implements CouponChangeRecordService {


    @Override
    public boolean create(String userId, Long userCouponId, String remark, Long gradeId) {
        CouponChangeRecord cr = new CouponChangeRecord();
        cr.setUserId(userId);
        cr.setUserCouponId(userCouponId);
        cr.setGradeId(gradeId);
        cr.setRemark(remark);
        return save(cr);
    }

    @Override
    public IPage<CouponVerifyItemVO> pageList(Integer page, Integer size, String date, String phone, String storeId) {
        return getBaseMapper().getByStoreAndDate(new Page<>(page, size), date, phone, storeId);
    }

    @Override
    public Long getGradeId(String userId, Long userCouponId) {
        return getBaseMapper().getGradeId(userId, userCouponId);
    }
}
