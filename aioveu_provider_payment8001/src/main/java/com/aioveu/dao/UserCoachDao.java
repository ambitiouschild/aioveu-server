package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.entity.UserCoach;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCoachDao extends BaseMapper<UserCoach> {
    /**
     * 获取用户对应门店的绑定销售、教练数据
     * @param userId
     * @param storeId
     * @return
     */
    List<UserCoach> getByUserId(String userId, Long storeId);

    /**
     * 根据店铺和用户id查询教练id
     * @param userId
     * @param storeId
     * @return
     */
    Long getStoreUserCoach(String userId, Long storeId);

    /**
     * 获取课程对应教练用户id
     * @param gradeId
     * @return
     */
    String getCoachUserIdByGradeId(Long gradeId);

}
