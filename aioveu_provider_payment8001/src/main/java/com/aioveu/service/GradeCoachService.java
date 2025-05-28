package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.GradeCoach;
import com.aioveu.entity.StoreCoach;
import com.aioveu.vo.GradeVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface GradeCoachService extends IService<GradeCoach> {


    /**
     * 通过用户id查找教练Id
     * @param userId
     * @return
     */
    List<Long> getByUserId(String userId);

    /**
     * 根据用户Id获取班级id
     * @param userId
     * @return
     */
    List<Long> getCoachIdByUserId(String userId);

    /**
     * 通过用户查找班级课程
     * @param page
     * @param size
     * @param userId
     * @param type
     * @param date
     * @return
     */
    IPage<GradeVO> getGradeByCoachId(int page, int size, String userId, Integer type, String date);

    /**
     * 删除更新班级教练
     * @param gradeId
     * @param coachList
     * @return
     */
    boolean deleteUpdateCoach(Long gradeId, List<Long> coachList);

    /**
     * 获取班级的教练
     * @param gradeId
     * @return
     */
    List<StoreCoach> getByGradeId(Long gradeId);


}
