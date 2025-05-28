package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.dto.FieldBookUserDTO;
import com.aioveu.dto.FieldPlanDTO;
import com.aioveu.entity.FieldBookUser;
import com.aioveu.entity.FieldPlan;
import com.aioveu.vo.FieldPlanVO;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface FieldPlanDao extends BaseMapper<FieldPlan> {

    IPage<FieldPlanVO> listByCondition(IPage<FieldPlanVO> page, Long storeId, Long venueId, Long fieldId, String date, Integer status);

    /**
     * 根据订单id查找预订场地信息
     * @param orderId
     * @return
     */
    List<FieldBookUserDTO> getFieldBookUserDetail(String orderId);

    List<FieldPlanDTO> getByUser(String userId, Long companyId, Integer status);

    List<FieldBookUser> getFieldBookUserByNotUsed(String userId, Long venueId);

    List<FieldPlan> getFieldByGrade(String fieldIds, String day, String timeFrom, String timeTo);

    List<FieldPlan> getFieldByGradeId(Long gradeId);

    /**
     * 根据id获取场地计划、场地、场馆名称信息
     * @param id
     * @return
     */
    FieldPlanDTO getFiledNameById(Long id);

    /**
     * 获取指定天数 并且 非指定平台的锁场订场数据
     * @param storeId
     * @param start
     * @param end
     * @param platformCode
     * @return
     */
    List<FieldPlanDTO> getFieldLockedList4BookDayAndTime(@Param("storeId") Long storeId, @Param("start") Date start, @Param("end") Date end, @Param("platformCode") String platformCode, @Param("venueIdList")List<Long> venueIdList);
}
