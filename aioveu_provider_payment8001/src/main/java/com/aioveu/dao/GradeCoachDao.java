package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.GradeCoach;
import com.aioveu.entity.StoreCoach;
import com.aioveu.vo.BaseServiceItemVO;
import com.aioveu.vo.GradeVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface GradeCoachDao extends BaseMapper<GradeCoach> {

    /**
     * 通过用户id查找教练Id
     * @param userId
     * @return
     */
    List<Long> getByUserId(String userId);

    /**
     * 通过ID查找班级课程
     * @param page
     * @param coachList
     * @param type
     * @param experienceCategoryId
     * @param date
     * @return
     */
    IPage<GradeVO> getGradeByCoachId(IPage<BaseServiceItemVO> page, List<Long> coachList, Integer type, Long experienceCategoryId, String date);

    /**
     * 获取班级的老师
     * @param gradeId
     * @return
     */
    List<StoreCoach> getByGradeId(Long gradeId);

}
