package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.UserCall;
import com.aioveu.entity.UserReceiveCall;
import com.aioveu.vo.NewUserInfoVO;
import com.aioveu.vo.UserCallSimpleVO;
import com.aioveu.vo.UserCallVO;
import com.aioveu.vo.UserInfoVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface UserCallDao extends BaseMapper<UserCall> {

    IPage<NewUserInfoVO> newUserInfoList(IPage<NewUserInfoVO> page, String createUserId, String userId, String timeFrom, String timeTo, Integer status, String keyword, List<Integer> statusList);

    /**
     * 根据用户查询拨打列表
     * @param page
     * @param userId
     * @param keyword
     * @return
     */
    IPage<UserCallVO> getByUserId(IPage<UserInfoVO> page, String userId, String keyword, Integer type, Long companyId);

    /**
     * 根据用户跟进列表
     * @param page
     * @param userInfoId
     * @return
     */
    IPage<UserCallSimpleVO> getSimpleByUserId(IPage<UserInfoVO> page, Long userInfoId);

    /**
     * 批量保存用户领取记录
     * @param receiveCallList
     */
    void batchUserReceiveCall(List<UserReceiveCall> receiveCallList);

    /**
     * 转移客资
     * @param newUserId
     * @param userId
     * @return
     */
    int updateUserReceiveCallToUser(String newUserId, String userId);
    
}
