package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.UserCoach;

import java.util.List;


public interface UserCoachService extends IService<UserCoach> {

    /**
     * 根据userId查询教师信息获取教师id
     * @param userId
     * @return
     */
    List<UserCoach> getByUserId(String userId);

    /**
     * 根据userId、门店id查询教师、销售信息
     * @param userId
     * @param storeId
     * @return
     */
    List<UserCoach> getByUserId(String userId, Long storeId);

    /**
     * 创建
     * @param userId
     * @param coachIdList
     * @param storeId
     * @param companyId
     * @return
     */
    boolean create(String userId, List<Long> coachIdList, Long storeId, Long companyId);


    /**
     * 根据userId删除数据
      * @param userId
     * @return
     */
    boolean delUserId(String userId);

    /**
     * 根据userId删除数据
     * @param userId
     * @param coachId
     * @return
     */
    boolean delUserId(String userId, List<Long> coachId);

    /**
     * 根据userId、storeId查询教练id
     * @param userId
     * @param storeId
     * @return
     */
    Long getStoreUserCoach(String userId, Long storeId);

    /**
     * 获取班级对应教练用户id
     * @param gradeId
     * @return
     */
    String getCoachUserIdByGradeId(Long gradeId);


}
