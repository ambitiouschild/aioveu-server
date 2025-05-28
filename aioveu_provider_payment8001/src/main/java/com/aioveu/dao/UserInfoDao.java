package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.entity.UserInfo;
import com.aioveu.vo.UserInfoOrderVO;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface UserInfoDao extends BaseMapper<UserInfo> {

    IPage<UserInfo> userInfoList(IPage<UserInfo> page, Integer type, Long companyId, String phone);

    /**
     * 订单列表
     * @param page
     * @param userId
     * @param categoryId
     * @return
     */
    IPage<UserInfoOrderVO> orderList(IPage<UserInfoOrderVO> page, String userId, Long categoryId);


    /**
     * 查看约课体验数据
     * @param objectPage
     * @param userId
     * @param categoryId
     * @return
     */
    IPage<UserInfoOrderVO> appointmentList(Page<Object> objectPage, String userId, Long categoryId);

    /**
     * id查找用户id
     * @param id
     * @return
     */
    String getUserId(Long id);

    /**
     * 获取公司对应的手机号用户的所属销售或者教练的用户id
     * @param phone
     * @param companyId
     * @return
     */
    String getUserIdByPhoneAndCompanyId(String phone, Long companyId, Integer type);



    /**
     * 根据用户id，公司id
     * 获取用户的归属教练或销售
     * @param userId
     * @param companyId
     * @return
     */
    String getUserIdByIdAndCompanyId(String userId, Long companyId, Integer type);
}
