package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.dto.UserAvailableCouponDTO;
import com.aioveu.dto.UserCouponDTO;
import com.aioveu.entity.UserCoupon;
import com.aioveu.vo.OrderCouponVO;
import com.aioveu.vo.UserCouponTotalVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface UserCouponDao extends BaseMapper<UserCoupon> {

    /**
     * 获取订单优惠券
     * @param phone
     * @return
     */
    List<OrderCouponVO> getOrderCoupon(String phone);


    /**
     * 获取优惠券列表汇总
     * @param page
     * @param phone
     * @param couponTemplateIdList
     * @return
     */
    IPage<UserCouponTotalVO> getTotalList(Page<UserCouponTotalVO> page, String phone, String userName, List<Long> couponTemplateIdList,Integer status, Integer countFrom,Integer countTo, Integer orderStatus,String orderStatusDesc,String storeId, String coachId);

    /**
     * 获取指定用户的优惠券列表
     * @param userIdList
     * @return
     */
    List<UserCouponDTO> getCouponByUserList(Long storeId, List<String> userIdList);

    List<UserCoupon> findUserCouponsByStatus(String userId, Long companyId, Integer status);

    /**
     * 获取用户课程券
     * @return
     */
    List<UserAvailableCouponDTO> getUserGradeCoupon();

    /**
     * 传入课券数阈值threshold，查询门店下所有课券数低于改阈值的人员信息，不包括课券数为0的客户
     * 课券数>0 and 课券数 <= threshold
     * @param storeId
     * @param threshold  阈值
     * @return
     */
    List<UserCouponTotalVO> queryCouponsBelowThreshold(Long storeId, Integer threshold);

    /**
     * 获取传入的用户id对应的销售、教练姓名信息
     * @param companyId
     * @param storeId
     * @param userIds
     * @return
     */
    List<UserCouponTotalVO> queryUserReceiveCallByUserIds(Long companyId,Long storeId,List<String> userIds);

}
