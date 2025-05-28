package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.GradeUserCouponDao;
import com.aioveu.entity.GradeUserCoupon;
import com.aioveu.enums.DataStatus;
import com.aioveu.enums.UserCouponStatus;
import com.aioveu.service.GradeUserCouponService;
import com.aioveu.service.IUserCouponService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author： yao
 * @Date： 2024/12/2 11:32
 * @Describe：
 */
@Slf4j
@Service
public class GradeUserCouponServiceImpl extends ServiceImpl<GradeUserCouponDao, GradeUserCoupon> implements GradeUserCouponService {

    @Autowired
    private IUserCouponService iUserCouponService;

    @Override
    public boolean gradeUserCoupon2Use(Long gradeId) {
        QueryWrapper<GradeUserCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GradeUserCoupon::getGradeId, gradeId)
                .eq(GradeUserCoupon::getStatus, DataStatus.NORMAL.getCode());
        List<GradeUserCoupon> list = list(queryWrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            return iUserCouponService.change2Status(list.stream().map(GradeUserCoupon::getUserCouponId).collect(Collectors.toList()), UserCouponStatus.USED);
        }
        return false;
    }
}
