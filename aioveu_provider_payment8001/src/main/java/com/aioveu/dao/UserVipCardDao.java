package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.entity.UserVipCard;
import com.aioveu.vo.UserVipCardItemVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2023/01/17 10:42
 */
@Repository
public interface UserVipCardDao extends BaseMapper<UserVipCard> {

    /**
     * 获取我的会员卡列表
     * @param userId
     * @return
     */
    List<UserVipCardItemVO> getMyVipCardList(String userId);

    /**
     * 管理端会员卡列表
     * @param companyId
     * @param phone
     * @param vipCardNo
     * @return
     */
    List<UserVipCard> getManageList(Long companyId, String phone, String vipCardNo, String username);

}
