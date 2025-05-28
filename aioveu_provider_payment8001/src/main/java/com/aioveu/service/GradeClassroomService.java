package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.GradeClassroom;
import com.aioveu.form.ActiveVenueFieldForm;
import com.aioveu.vo.AvailableIdNameVO;
import com.aioveu.vo.FieldDayPlanVO;
import com.aioveu.vo.IdNameVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface GradeClassroomService extends IService<GradeClassroom> {

    /**
     * 根据店铺id查找列表
     * @param storeId
     * @return
     */
    List<IdNameVO> getByStoreId(Long storeId);

    /**
     * 通过场馆查找教室
     * @param venueId 
     * @return
     */
    List<IdNameVO> getByVenueId(Long venueId);

    /**
     * 获取教室的课程计划
     * @param venueId
     * @param day
     * @return
     */
    FieldDayPlanVO fieldPlayByVenueId(Long venueId, String day);

    /**
     * 获取可用的班级列表
     * @param form
     * @return
     */
    List<AvailableIdNameVO> getActiveClassroomList(ActiveVenueFieldForm form);

}
